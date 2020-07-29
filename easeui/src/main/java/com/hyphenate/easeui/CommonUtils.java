package com.hyphenate.easeui;

import android.content.Context;
import android.view.View;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.model.UserAvatarBean;

/**
 * 公共类 方便moudle之间的传递
 */
public class CommonUtils {
	
	public static final int AUDIENCE = 1; //观众
	public static final int ANCHOR = 2; //主播

	public static final int LIVE_PRIVATE_LETTER = 1; //直播间私信
	public static final int MY_PRIVATE_LETTER = 2; //我的私信
	
	
	//标记是主播还是观众
	public static Integer type ;
	//当前主播的im账号
	public static String anchorAccount = "";
	//当前用户
	public static  String currentUserName = "";
	//私信类型
	public static int privateType;
	//需要跳转的activity
	public static Class<?> intentClass;
	//扣钻值
	public static long diamondNum = 0;
	
	public static void setPrivateLetterType (int type) {
		privateType = type;
	}
	public static void setCurrentUserName (String name) {
		currentUserName = name;
	}


	
	public static void setIntentClass (Class<?> cls) {
		intentClass = cls;
	}
	public static void setAnchorAccount (String account) {
		anchorAccount = account;
	}

	public static void setRole(int role) {
		type = role;
	}
	public static void setDiamondNum(int num) {
		diamondNum = num;
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
	
	//发送文本消息
	public static OnSendTxtMessageListener onSendTxtMessageListener;
	public static void setOnSendTxtMessageListener(OnSendTxtMessageListener listener) {
		onSendTxtMessageListener = listener;
	}
	public interface OnSendTxtMessageListener{
		void onSendMessage(Context context,String userName,String content);
	}
	//直播--聊天界面返回事件
	public static OnLiveChatBackListener onLiveChatBackListener;
	public static void setOnLiveChatBackListener(OnLiveChatBackListener listener) {
		onLiveChatBackListener = listener;
	}
	public interface OnLiveChatBackListener{
		void onliveChatBack();
	}

	//设置头像
	public static OnSetAvatarListener onSetAvatarListener;
	public static void setOnSetAvatarListener(OnSetAvatarListener listener) {
		onSetAvatarListener = listener;
	}
	public interface OnSetAvatarListener{
		void onSetAvatar(Context context, String userName, View view);
	}

	//设置用户名
	public static OnSetUserNameListener onSetUserNameListener;
	public static void setOnSetUserNameListener(OnSetUserNameListener listener) {
		onSetUserNameListener = listener;
	}
	public interface OnSetUserNameListener{
		void onSetUserName(String userName, View view);
	}

	//接口返回成功后发送文本消息
	public static OnSendMessageListener onSendMessageListener;
	public static void setOnSendMessageListener(OnSendMessageListener listener) {
		onSendMessageListener = listener;
	}
	public interface OnSendMessageListener{
		void onSendMessage(String content);
	}
}
