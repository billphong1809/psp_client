package phong.android.com.entity;

import java.io.Serializable;
import java.util.Vector;

public class ListPeople implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Vector<UserLogin> vtUsersLogin;

	public ListPeople() {

	}

	public ListPeople(Vector<UserLogin> vtUsersLogin) {
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
