package phong.android.com.entity;

public class People {
	private int idPeople;
	private String thumnaidAvataFriendl;
	private String usernameFriend;
	private String timeSend;
	private String messageRecent;
	private String statusPeople;

	public People() {
		super();
	}

	public People(int idPeople, String thumnaidAvataFriendl,
			String usernameFriend, String timeSend, String messageRecent,
			String statusPeople) {
		super();
		this.idPeople = idPeople;
		this.thumnaidAvataFriendl = thumnaidAvataFriendl;
		this.usernameFriend = usernameFriend;
		this.timeSend = timeSend;
		this.messageRecent = messageRecent;
		this.statusPeople = statusPeople;
	}

	public int getIdPeople() {
		return idPeople;
	}

	public void setIdPeople(int idPeople) {
		this.idPeople = idPeople;
	}

	public String getThumnaidAvataFriendl() {
		return thumnaidAvataFriendl;
	}

	public void setThumnaidAvataFriendl(String thumnaidAvataFriendl) {
		this.thumnaidAvataFriendl = thumnaidAvataFriendl;
	}

	public String getUsernameFriend() {
		return usernameFriend;
	}

	public void setUsernameFriend(String usernameFriend) {
		this.usernameFriend = usernameFriend;
	}

	public String getTimeSend() {
		return timeSend;
	}

	public void setTimeSend(String timeSend) {
		this.timeSend = timeSend;
	}

	public String getMessageRecent() {
		return messageRecent;
	}

	public void setMessageRecent(String messageRecent) {
		this.messageRecent = messageRecent;
	}

	public String getStatusPeople() {
		return statusPeople;
	}

	public void setStatusPeople(String statusPeople) {
		this.statusPeople = statusPeople;
	}

}
