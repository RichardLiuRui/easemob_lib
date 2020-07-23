package com.hyphenate.easeui;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.model.UserAvatarBean;

/**
 * 公共类 方便moudle之间的传递
 */
public class CommonUtils {
	
	public static final int AUDIENCE = 1; //观众
	public static final int ANCHOR = 2; //主播
	//标记是主播还是观众
	public static Integer type = 1;
	
	//当前主播的im账号
	public static String anchorAccount = "wqx0816";
	
	public static void setAnchorAccount (String account) {
		anchorAccount = account;
	}

	public static void setRole(int role) {
		type = role;
	}
	//用户头像
	public static UserAvatarBean userAvatarBean;
	public static void setUserAvatarBean(UserAvatarBean bean){
		userAvatarBean = bean;
	}

	//聊天界面头像点击事件
	public static OnChatAvatarClickListener onChatAvatarClick;
	public static void setOnChatAvatarClickListener(CommonUtils.OnChatAvatarClickListener listener) {
		onChatAvatarClick = listener;
	}
	public interface OnChatAvatarClickListener{
		void onAvatarClick(String userName);
	}

	//会话列表点击
	public static OnConversationItemClickListener onConversationItemClickListener;
	public static void setConversationItemClickListener(CommonUtils.OnConversationItemClickListener listener) {
		onConversationItemClickListener = listener;
	}
	public interface OnConversationItemClickListener{
		void onConversationClick(String userName);
	}
	
	//会话列表左滑删除事件点击
	public static OnConversationDeleteListener onConversationDeleteListener;
	public static void setOnConversationDeleteListener(CommonUtils.OnConversationDeleteListener listener) {
		onConversationDeleteListener = listener;
	}
	public interface OnConversationDeleteListener{
		void onDeleteClick(EMConversation conversation);
	}
	
	
}
