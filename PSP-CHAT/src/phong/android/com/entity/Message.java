package phong.android.com.entity;

import java.io.Serializable;

public class Message implements Serializable{
	private static final long serialVersionUID = -8917906231213588072L;
	private String content;
	private String time;
	private People sender;
	private People receiver;
	private String type;

	public Message(People sender, People receiver, String time, String content) {
		this.sender = sender;
		this.receiver = receiver;
		this.time = time;
		this.content = content;
		this.type = "msg";
	}
	
	public Message(People sender, People receiver, String time, String content, String type){
		this.sender = sender;
		this.receiver = receiver;
		this.time = time;
		this.content = content;
		this.type = type;
	}

	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getContent() {
		return content;
	}

	public String getTime() {
		return time;
	}

	public People getSender() {
		return sender;
	}

	public People getReceiver() {
		return receiver;
	}
	
	
}
