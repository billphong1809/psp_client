package phong.android.com.entity;

import java.io.Serializable;

public class MesageRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String requestToServer;

	public MesageRequest() {

	}

	public MesageRequest(String requestToServer) {
		super();
		this.requestToServer = requestToServer;

	}

	public String getRequestToServer() {
		return requestToServer;
	}

	public void setRequestToServer(String requestToServer) {
		this.requestToServer = requestToServer;
	}

}
