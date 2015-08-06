package phong.android.com.utilities;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketConnectToServerSingeton {
	private String hostName = "192.168.1.67";
	private int port = 1111;
	private static SocketConnectToServerSingeton instances;
	private Socket socket;

	private SocketConnectToServerSingeton() {
		try {
			socket = new Socket(hostName, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized static SocketConnectToServerSingeton getInstances() {
		if (instances == null) {
			instances = new SocketConnectToServerSingeton();

		}
		return instances;

	}

	public synchronized Socket getSocket() {
		return socket;
	}

}
