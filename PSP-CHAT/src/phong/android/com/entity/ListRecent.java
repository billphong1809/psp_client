package phong.android.com.entity;

import java.io.Serializable;
import java.util.Vector;

public class ListRecent implements Serializable {
	private Vector<UserLogin> vtUsersLogin;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListRecent() {

	}

	public ListRecent(Vector<UserLogin> vtUsersLogin) {
		super();
		this.vtUsersLogin = vtUsersLogin;
	}

	public Vector<UserLogin> getVtUsersLogin() {
		return vtUsersLogin;
	}

	public void setVtUsersLogin(Vector<UserLogin> vtUsersLogin) {
		this.vtUsersLogin = vtUsersLogin;
	}

}
