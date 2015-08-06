package phong.android.com.entity;

import java.io.Serializable;

public class MessageWithIP implements Serializable {
	private String message;
	private UserLogin sender;
	private UserLogin received;
	private String flag;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageWithIP() {

	}

	public MessageWithIP(String message, UserLogin sender, UserLogin received,
			String flag) {
		super();
		this.message = message;
		this.sender = sender;
		this.received = received;
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserLogin getSender() {
		return sender;
	}

	public void setSender(UserLogin sender) {
		this.sender = sender;
	}

	public UserLogin getReceived() {
		return received;
	}

	public void setReceived(UserLogin received) {
		this.received = received;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}



}
