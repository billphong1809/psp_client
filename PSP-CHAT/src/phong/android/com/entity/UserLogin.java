package phong.android.com.entity;

import java.io.Serializable;

public class UserLogin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String usersName;
	private String passWord;
	private String flag;
	private String avatar;
	private String status;

	public UserLogin() {
		super();
	}

	public UserLogin(String usersName, String passWord, String flag,
			String avatar, String status) {
		super();

		this.usersName = usersName;
		this.passWord = passWord;
		this.flag = flag;
		this.avatar = avatar;
		this.status = status;

	}

	public String getUsersName() {
		return usersName;
	}

	public void setUsersName(String usersName) {
		this.usersName = usersName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
