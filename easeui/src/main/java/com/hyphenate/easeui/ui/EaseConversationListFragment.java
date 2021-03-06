package com.hyphenate.easeui.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCustomMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.CommonUtils;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.EaseConversationList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * conversation list fragment
 */
public class EaseConversationListFragment extends EaseBaseFragment {
	private final static int MSG_REFRESH = 2;
	// protected EditText query;
	protected ImageButton clearSearch;
	protected boolean hidden;
	protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
	public EaseConversationList conversationListView;
	protected FrameLayout errorItemContainer;

	public LinearLayout llNull;
	protected boolean isConflict;

	public TextView ignoreUnRead;
	protected EMConversationListener convListener = new EMConversationListener() {

		@Override
		public void onCoversationUpdate() {
			refresh();
		}

	};
	public View view;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
			return;
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	protected void initView() {
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		conversationListView = view.findViewById(R.id.list);
		llNull = view.findViewById(R.id.ll_null);
//		getView().findViewById(R.id.tv_ignore_unread).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				for (int i = 0; i < loadConversationList().size(); i++) {
//					EMConversation conversation = EMClient.getInstance().chatManager().getConversation(loadConversationList().get(i).conversationId());
//					//指定会话消息未读数清零
//					conversation.markAllMessagesAsRead();
//					refresh();
//				}
//
//			}
//		});
		//   query = (EditText) getView().findViewById(R.id.query);
		// button to clear content in search bar
		//     clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
		errorItemContainer = view.findViewById(R.id.fl_error_item);
	}

	@Override
	protected void setUpView() {
		conversationList.addAll(fristLoadConversationList());

		conversationListView.init(conversationList);

		if (listItemClickListener != null) {
			conversationListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					EMConversation conversation = conversationListView.getItem(position);
					listItemClickListener.onListItemClicked(conversation);
				}
			});
		}

		EMClient.getInstance().addConnectionListener(connectionListener);

//        query.addTextChangedListener(new TextWatcher() {
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                conversationListView.filter(s);
//                if (s.length() > 0) {
//                    clearSearch.setVisibility(View.VISIBLE);
//                } else {
//                    clearSearch.setVisibility(View.INVISIBLE);
//                }
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void afterTextChanged(Editable s) {
//            }
//        });
//        clearSearch.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                query.getText().clear();
//                hideSoftKeyboard();
//            }
//        });
//        
		conversationListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideSoftKeyboard();
				return false;
			}
		});
	}


	protected EMConnectionListener connectionListener = new EMConnectionListener() {

		@Override
		public void onDisconnected(int error) {
			if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED
					|| error == EMError.USER_KICKED_BY_CHANGE_PASSWORD || error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
				isConflict = true;
			} else {
				handler.sendEmptyMessage(0);
			}
		}

		@Override
		public void onConnected() {
			handler.sendEmptyMessage(1);
		}
	};
	private EaseConversationListItemClickListener listItemClickListener;

	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					onConnectionDisconnected();
					break;
				case 1:
					onConnectionConnected();
					break;

				case MSG_REFRESH: {
					conversationList.clear();
					conversationList.addAll(loadConversationList());
					conversationListView.refresh();
					break;
				}
				default:
					break;
			}
		}
	};

	/**
	 * connected to server
	 */
	protected void onConnectionConnected() {
		errorItemContainer.setVisibility(View.GONE);
	}

	/**
	 * disconnected with server
	 */
	protected void onConnectionDisconnected() {
		errorItemContainer.setVisibility(View.VISIBLE);
	}


	/**
	 * refresh ui
	 */
	public void refresh() {
		if (!handler.hasMessages(MSG_REFRESH)) {
			handler.sendEmptyMessage(MSG_REFRESH);
		}
	}

	/**
	 * load conversation list
	 *
	 * @return +
	 */
	protected List<EMConversation> loadConversationList() {
		// get all conversations
		Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		/**
		 * lastMsgTime will change if there is new message during sorting
		 * so use synchronized to make sure timestamp of last message won't change.
		 */
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		//todo 当前为观众时添加当前主播为第一条
		//	CommonUtils.setRole(CommonUtils.AUDIENCE);
		if (CommonUtils.privateType == CommonUtils.LIVE_PRIVATE_LETTER && CommonUtils.type == CommonUtils.AUDIENCE) {
			//判断会话列表是否包含当前主播
//			boolean contains = false;
//			for (int i = 0; i < list.size(); i++) {
//				if (list.get(i).conversationId().equals(CommonUtils.anchorAccount)) {
//					contains = true;
//					break;
//				}
//			}
//			//不包含  发送一条空消息
//			if (!contains) {
//				Log.e("111", "不包含");
//				EMMessage customMessage = EMMessage.createSendMessage(EMMessage.Type.CUSTOM);
//				EMCustomMessageBody customBody = new EMCustomMessageBody("top");
//				customMessage.addBody(customBody);
//				customMessage.setTo(CommonUtils.anchorAccount);
//				EMClient.getInstance().chatManager().sendMessage(customMessage);
//			}
			//将当前主播显示在第一条
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).conversationId().equals(CommonUtils.anchorAccount)) {
					EMConversation emConversation = list.get(i);
					list.remove(i);
					list.add(0, emConversation);
				}
			}
		}

		//外循环是循环的次数  去重
		for (int i = 0; i < list.size(); i++) {
			for (int j = list.size() - 1; j > i; j--) {
				//内循环是 外循环一次比较的次数
				if (list.get(i).conversationId().startsWith(list.get(j).conversationId())) {
					if (!list.get(j).conversationId().equals(CommonUtils.currentUserName))
					list.remove(j);
				}
			}
		}
		
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).conversationId().equals(CommonUtils.anchorAccount)) {
				if (list.get(i).getAllMessages().size() == 0) {
					EMClient.getInstance().chatManager().deleteConversation(list.get(i).conversationId(), false);
					refresh();
				}
			}
		}
		//todo 返回列表为0 展示缺省页
		if (list.size() > 0) {
			llNull.setVisibility(View.GONE);
			conversationListView.setVisibility(View.VISIBLE);
		} else {
			llNull.setVisibility(View.VISIBLE);
			conversationListView.setVisibility(View.GONE);
		}
		return list;
	}

	/**
	 * load conversation list
	 * 首次加载
	 *
	 * @return +
	 */
	protected List<EMConversation> fristLoadConversationList() {
		// get all conversations
		Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		/**
		 * lastMsgTime will change if there is new message during sorting
		 * so use synchronized to make sure timestamp of last message won't change.
		 */
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		//todo 当前为观众时添加当前主播为第一条
		//	CommonUtils.setRole(CommonUtils.AUDIENCE);
		if (CommonUtils.privateType == CommonUtils.LIVE_PRIVATE_LETTER && CommonUtils.type == CommonUtils.AUDIENCE) {
			//判断会话列表是否包含当前主播
			boolean contains = false;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).conversationId().equals(CommonUtils.anchorAccount)) {
					contains = true;
					break;
				}
			}
			//不包含  发送一条空消息
			if (!contains) {
				Log.e("111", "不包含");
				EMMessage customMessage = EMMessage.createSendMessage(EMMessage.Type.CUSTOM);
				EMCustomMessageBody customBody = new EMCustomMessageBody("top");
				customMessage.addBody(customBody);
				customMessage.setTo(CommonUtils.anchorAccount);
				EMClient.getInstance().chatManager().sendMessage(customMessage);
			}
			//将当前主播显示在第一条
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).conversationId().equals(CommonUtils.anchorAccount)) {
					EMConversation emConversation = list.get(i);
					list.remove(i);
					list.add(0, emConversation);
				}

			}
		}
		//todo 去重多端登录
//		for (int i = 0; i < list.size(); i++) {
//			if (list.get(i).conversationId().startsWith(CommonUtils.currentUserName)) {
//				if (!list.get(i).conversationId().equals(CommonUtils.currentUserName))
//					list.remove(i);
//			}
//		}

		//外循环是循环的次数  去重
		for (int i = 0; i < list.size(); i++) {
			for (int j = list.size() - 1; j > i; j--) {
				//内循环是 外循环一次比较的次数
				if (list.get(i).conversationId().startsWith(list.get(j).conversationId())) {
					if (!list.get(j).conversationId().equals(CommonUtils.currentUserName))
					list.remove(j);
				}
			}
		}
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).conversationId().equals(CommonUtils.anchorAccount)) {
				if (list.get(i).getAllMessages().size() == 0) {
					EMClient.getInstance().chatManager().deleteConversation(list.get(i).conversationId(), false);
					refresh();
				}
			}
		}
		//todo 返回列表为0 展示缺省页
		if (list.size() > 0) {
			llNull.setVisibility(View.GONE);
			conversationListView.setVisibility(View.VISIBLE);
		} else {
			llNull.setVisibility(View.VISIBLE);
			conversationListView.setVisibility(View.GONE);
		}

		return list;
	}

	/**
	 * sort conversations according time stamp of last message
	 *
	 * @param conversationList
	 */
	private void sortConversationByLastChatTime
	(List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
			@Override
			public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

				if (con1.first.equals(con2.first)) {
					return 0;
				} else if (con2.first.longValue() > con1.first.longValue()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	protected void hideSoftKeyboard() {
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden && !isConflict) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EMClient.getInstance().removeConnectionListener(connectionListener);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (isConflict) {
			outState.putBoolean("isConflict", true);
		}
	}

	public interface EaseConversationListItemClickListener {
		/**
		 * click event for conversation list
		 *
		 * @param conversation -- clicked item
		 */
		void onListItemClicked(EMConversation conversation);
	}

	/**
	 * set conversation list item click listener
	 *
	 * @param listItemClickListener
	 */
	public void setConversationListItemClickListener(EaseConversationListItemClickListener
			                                                 listItemClickListener) {
		this.listItemClickListener = listItemClickListener;
	}

}
