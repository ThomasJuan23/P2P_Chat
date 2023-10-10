package chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Users implements Serializable {
	ArrayList<User> users;

	public Users() {
		this.users = new ArrayList<User>();
	}

	public void add(User u) {
		users.add(u);
	}

	public void del(User u) {
		users.remove(u);
	}

	public int getCount() {
		return users.size();
	}

	public User find(String username) {
		Iterator<User> it = users.iterator();
		while (it.hasNext()) {
			User u = it.next();
			if (u.getUsername().equals(username)) {
				return u;
			}
		}
		return null;
	}

	public User find(int index) {
		return users.get(index);
	}

}
