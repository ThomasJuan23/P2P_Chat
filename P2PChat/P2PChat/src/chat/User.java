package chat;

import java.io.Serializable;
import java.net.InetAddress;

public class User implements Serializable {
	String username;
	InetAddress ip;
	int port;

	public User() {

	}

	User next = null;

	public InetAddress getAddress() {
		return ip;
	}

	public String getUsername() {
		return username;
	}

	public int getPort() {
		return port;
	}

	public void setip(InetAddress i) {
		ip = i;
	}

	public void setUsername(String u) {
		username = u;
	}

	public void setPort(int p) {
		port = p;
	}

}
