package com.hyphenate.easeui.model;

import java.util.List;

public class UserAvatarBean {

	public List<UserAvatar> getList() {
		return list;
	}

	public void setList(List<UserAvatar> list) {
		this.list = list;
	}

	private List<UserAvatar> list;
	
	public class UserAvatar{
		private String avatarUrl;

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		private String nickName;

		public String getAvatarUrl() {
			return avatarUrl;
		}

		public void setAvatarUrl(String avatarUrl) {
			this.avatarUrl = avatarUrl;
		}

		public String getImAccount() {
			return imAccount;
		}

		public void setImAccount(String imAccount) {
			this.imAccount = imAccount;
		}

		private String imAccount;
	}
	
}
