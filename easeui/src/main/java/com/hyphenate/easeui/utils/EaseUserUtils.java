package com.hyphenate.easeui.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.easeui.CommonUtils;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.UserAvatarBean;

import java.util.List;

public class EaseUserUtils {

	static EaseUserProfileProvider userProvider;

	static {
		userProvider = EaseUI.getInstance().getUserProfileProvider();
	}

	/**
	 * get EaseUser according username
	 *
	 * @param username
	 * @return
	 */
	public static EaseUser getUserInfo(String username) {
		if (userProvider != null)
			return userProvider.getUser(username);

		return null;
	}

	/**
	 * set user avatar
	 *
	 * @param username
	 */
	public static void setUserAvatar(Context context, String username, ImageView imageView) {
		//todo 设置用户头像
		CommonUtils.OnSetAvatarListener listener = CommonUtils.onSetAvatarListener;
		if (listener != null) {
			listener.onSetAvatar(context,username,imageView);
		}
//		if (CommonUtils.userAvatarBean != null && CommonUtils.userAvatarBean.getList() != null && CommonUtils.userAvatarBean.getList().size() > 0) {
//			List<UserAvatarBean.UserAvatar> list = CommonUtils.userAvatarBean.getList();
//			for (int i = 0; i < list.size(); i++) {
//				if (list.get(i).getImAccount().equals(username)) {
//					Glide.with(context).load(list.get(i).getAvatarUrl()).into(imageView);
//				}
//			}
//		} else {
//			Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
//		}
//    	EaseUser user = getUserInfo(username);
//        if(user != null && user.getAvatar() != null){
//            try {
//                int avatarResId = Integer.parseInt(user.getAvatar());
//                Glide.with(context).load(avatarResId).into(imageView);
//            } catch (Exception e) {
//                //use default avatar
//                Glide.with(context).load(user.getAvatar())
//                        .apply(RequestOptions.placeholderOf(R.drawable.ease_default_avatar)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL))
//                        .into(imageView);
//            }
//        }else{
//            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
//        }
	}

	/**
	 * set user's nickname
	 */
	public static void setUserNick(String username, View textView) {
		//todo 设置用户名
		CommonUtils.OnSetUserNameListener listener = CommonUtils.onSetUserNameListener;
		if (listener != null) {
			listener.onSetUserName(username,textView);
		}
//		if (CommonUtils.userAvatarBean != null && CommonUtils.userAvatarBean.getList() != null && CommonUtils.userAvatarBean.getList().size() > 0) {
//			List<UserAvatarBean.UserAvatar> list = CommonUtils.userAvatarBean.getList();
//			for (int i = 0; i < list.size(); i++) {
//				if (list.get(i).getImAccount().equals(username)) {
//					textView.setText(list.get(i).getNickName());
//				}
//			}
//		} else {
//			if (textView != null) {
//				EaseUser user = getUserInfo(username);
//				if (user != null && user.getNickname() != null) {
//					textView.setText(user.getNickname());
//				} else {
//					textView.setText(username);
//				}
//			}
//		}

	}

}
