package phong.android.com.entity;

import java.io.Serializable;

public class MutiUsersAdd implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserLogin me;
	private UserLogin friend;
	private String flag;

	public MutiUsersAdd() {

	}

	public MutiUsersAdd(UserLogin me, UserLogin friend, String flag) {
		super();
		this.me = me;
		this.friend = friend;
		this.flag = flag;
	}

	public UserLogin getMe() {
		return me;
	}

	public void setMe(UserLogin me) {
		this.me = me;
	}

	public UserLogin getFriend() {
		return friend;
	}

	public void setFriend(UserLogin friend) {
		this.friend = friend;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
