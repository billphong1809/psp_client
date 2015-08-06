package phong.android.com.entity;

import java.util.ArrayList;

public class Recent {
	private UserLogin userLogin;
	private ArrayList<MessageWithIP> recentMessage;

	public Recent(UserLogin userLogin, ArrayList<MessageWithIP> arrMsg) {
		this.userLogin = userLogin;
		this.recentMessage = arrMsg;
	}

	public UserLogin getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(UserLogin userLogin) {
		this.userLogin = userLogin;
	}

	public ArrayList<MessageWithIP> getRecentMessage() {
		return recentMessage;
	}

	public MessageWithIP getLastMessage() {
		return recentMessage.get(recentMessage.size() - 1);
	}

}
