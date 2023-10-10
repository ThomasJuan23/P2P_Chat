package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import chat.User;
import chat.Users;

public class ServerUI extends JFrame {

	JButton connect, stop;
	JTextArea history;
	JList<String> list;
	JLabel user, lHistory;
	JButton send, clear;
	JLabel command;
	JTextArea commandText;
	JLabel p2p;
	JLabel chat;
	ServerSocket serversocket;
	JScrollPane jp1;
	boolean isStop;
	ArrayList<ObjectOutputStream> outs = new ArrayList<ObjectOutputStream>();
	Socket socket;
	JButton kick, viewHistory;
	Users users;
	ObjectInputStream in;
	int y = 0;
	String Serverhistory;
	String Userhistory = "";
	int judge=0;
	DefaultListModel<String> model = new DefaultListModel<String>();;

	public static void main(String[] args) {
		ServerUI su = new ServerUI();
		JFrame j = su.serverWindow();
	}

	public JFrame serverWindow() {
		JFrame jf = new JFrame();
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int sWidth = screenSize.width / 2;
		int sHeight = screenSize.height / 2;

		jf.setSize(sWidth / 2, (sHeight * 2) / 3);
		jf.setTitle("Server");
		connect = new JButton("Connect");
		stop = new JButton("Exit");
		connect.setFont(new Font("Aril", 0, 12));
		stop.setFont(new Font("Aril", 0, 12));
		stop.setEnabled(false);
		kick = new JButton("Kick");
		kick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				kick();

			}
		});
		connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// history.append("Connecting...."+"\n");
				connect.setEnabled(false);
				stop.setEnabled(true);
				commandText.setEnabled(true);
				send.setEnabled(true);
				clear.setEnabled(true);

				startServer();
				// TODO Auto-generated method stub

			}
		});
		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				connect.setEnabled(true);
				stop.setEnabled(false);
				send.setEnabled(false);
				commandText.setEnabled(false);
				clear.setEnabled(false);
				kick.setEnabled(false);
				history.setText("");
				stopServer();
				// TODO Auto-generated method stub
			}
		});
		user = new JLabel("User list");
		// user.setSize(width*2/3, height*2/3);
		lHistory = new JLabel("Message history");
		lHistory.setFont(new Font("Aril", 0, 28));
		p2p = new JLabel("P2P");
		chat = new JLabel("Chat");
		p2p.setHorizontalAlignment(JLabel.CENTER);
		chat.setHorizontalAlignment(JLabel.LEFT);
		p2p.setFont(new Font("Aril", 2, 18));
		chat.setFont(new Font("Aril", 2, 18));
		command = new JLabel("Message: ");
		command.setFont(new Font("Aril", 0, 28));
		command.setHorizontalAlignment(JLabel.CENTER);
		command.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		commandText = new JTextArea();
		commandText.setBackground(Color.lightGray);
		commandText.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		commandText.setBackground(Color.white);
		user.setFont(new Font("Aril", 0, 28));
		user.setHorizontalAlignment(JLabel.CENTER);
		user.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		lHistory.setHorizontalAlignment(JLabel.CENTER);
		lHistory.setBorder(BorderFactory.createLineBorder(Color.black, 1));

		list = new JList<String>();
		// list.setSize(width*2/3, height*2/3);
		history = new JTextArea();
		jp1 = new JScrollPane(history);
		jp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		list.setBackground(Color.white);
		history.setBackground(Color.white);
		send = new JButton("Send");
		clear = new JButton("Clear");
		send.setEnabled(false);
		clear.setEnabled(false);
		send.setFont(new Font("Aril", 0, 12));
		clear.setFont(new Font("Aril", 0, 12));
		list.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		history.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				commandText.setText("");
				// TODO Auto-generated method stub

			}
		});
		history.setEnabled(false);
		history.setDisabledTextColor(Color.black);
		commandText.setEnabled(false);
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (commandText.getText().equalsIgnoreCase("") || commandText.getText() == null) {
					history.append("You input nothing");
				}
				String message = commandText.getText();
				sendSystemMessage(message);
				// TODO Auto-generated method stub

			}
		});
		kick.setEnabled(false);
		viewHistory = new JButton("History");
		viewHistory.setEnabled(false);
		viewHistory.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				view();

			}
		});
		JPanel panel1 = new JPanel();
		panel1.setPreferredSize(new Dimension(130, 350));
		panel1.setLayout(new BorderLayout());
		panel1.add(user, BorderLayout.NORTH);
		panel1.add(list, BorderLayout.CENTER);
		JPanel panel6 = new JPanel();
		panel6.setLayout(new GridLayout(1, 2));
		panel6.add(p2p);
		panel6.add(chat);
		JPanel panel7 = new JPanel();
		panel7.setLayout(new BorderLayout());
		panel7.add(panel6, BorderLayout.NORTH);
		panel7.add(panel1, BorderLayout.CENTER);
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(2, 7));
		panel2.add(kick);
		panel2.add(new JLabel(""));
		panel2.add(viewHistory);
		panel2.add(new JLabel(""));
		panel2.add(send);
		panel2.add(new JLabel(""));
		panel2.add(clear);
		panel2.add(new JLabel(""));
		panel2.add(new JLabel(""));
		panel2.add(new JLabel(""));
		panel2.add(new JLabel(""));
		panel2.add(new JLabel(""));
		panel2.add(new JLabel(""));
		panel2.add(new JLabel(""));
		JPanel panel3 = new JPanel();
		panel3.setLayout(new BorderLayout());
		panel3.add(lHistory, BorderLayout.NORTH);
		panel3.add(jp1, BorderLayout.CENTER);
		JPanel panel4 = new JPanel();

		JPanel panel9 = new JPanel();
		panel9.setLayout(new BorderLayout());
		panel9.add(command, BorderLayout.NORTH);
		panel9.add(commandText, BorderLayout.CENTER);
		JPanel panel10 = new JPanel();
		panel10.setLayout(new BorderLayout(10, 20));
		panel10.add(panel2, BorderLayout.SOUTH);
		panel10.add(panel9, BorderLayout.CENTER);
		JPanel panel8 = new JPanel();
		panel8.setLayout(new GridLayout(2, 1));
		panel8.add(panel3);
		panel8.add(panel10);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 5));
		panel.add(new JLabel(""));
		panel.add(connect);
		panel.add(new JLabel(""));
		panel.add(stop);
		panel.add(new JLabel(""));

		JPanel panel0 = new JPanel();
		panel0.setLayout(new BorderLayout());
		panel0.add(panel, BorderLayout.NORTH);
		panel0.add(panel8, BorderLayout.CENTER);
		JPanel panel5 = new JPanel();
		panel5.setLayout(new BorderLayout(10, 10));
		panel5.add(panel7, BorderLayout.WEST);
		panel5.add(panel0, BorderLayout.CENTER);
		panel4.setLayout(new BorderLayout(10, 20));
		panel4.add(panel2, BorderLayout.SOUTH);
		panel4.add(panel5, BorderLayout.CENTER);
		jf.setLayout(new BorderLayout());
		jf.add(panel4, BorderLayout.CENTER);
		jf.setVisible(true);
		jf.setLocation(sWidth - 300, sHeight - 250);
		jf.setDefaultCloseOperation(EXIT_ON_CLOSE);
		return jf;
	}

	public void setHistory(String his) {
		Userhistory = his;
	}

	public void view() {
		if (y == 0) {
			if (list.getComponentCount() == 0 && list.getSelectedValue().equals(null)) {
				history.append("There is no user to choose" + "\n");
			} else {
				try {
					int index = list.getSelectedIndex();
					ObjectOutputStream out = outs.get(index);
					viewHistory.setText("Back");
					y = 1;
					Serverhistory = history.getText();
					out.writeObject("{*^$>History");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} else {
			history.setText(Serverhistory);
			viewHistory.setText("History");
			y = 0;
		}
	}

	public void kick() {
		if (list.getComponentCount() == 0 && list.getSelectedValue().equals(null)) {
			history.append("There is no user to choose" + "\n");
		} else {
			String username = list.getSelectedValue();
			int index = list.getSelectedIndex();
			try {
				ObjectOutputStream out = outs.get(index);
				sendSystemMessage(username + " have been kicked");
			    model.removeAllElements();
				out.writeObject("{*^$>Stop");
				out.flush();
			    
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void sendSystemMessage(String message) {

		history.append("Server: " + "\n" + message + "\n");
		commandText.setText("");

		try {
			Iterator<ObjectOutputStream> it = outs.iterator();
			while (it.hasNext()) {
				ObjectOutputStream tout = it.next();
				tout.writeObject("{*^$>Server");
				tout.flush();
				tout.writeObject(message);
				tout.flush();
			}

		} catch (Exception e) {
			history.append("Fail to send  566");
		}
	}

	public void stopServer() {
		try {
			this.isStop = true;
			serversocket.close();
			socket.close();
			list.removeAll();
			System.exit(0);
		} catch (Exception e) {
			history.append("close");
		}
	}

	public void startServer() {
		try {
			serversocket = new ServerSocket(6666);
			history.append("Connection....." + "\n");
			this.isStop = false;
			users = new Users();
			ServerListenThread serverlistenthread = new ServerListenThread(serversocket, history, list, users);
			serverlistenthread.start();
		} catch (Exception e) {
			history.append("fail to connect 0");
		}
	}

	public class ServerListenThread extends Thread {
		ServerSocket serversocket;
		JTextArea history;
		JList<String> list;
		Users users;
		User user;
		ObjectInputStream in;
		ServerThread serverthread;

		public ServerListenThread(ServerSocket serversocket, JTextArea history, JList<String> list, Users users) {
			this.serversocket = serversocket;
			this.history = history;
			this.list = list;
			this.users = users;

		}

		public void run() {
			
			while (!isStop && !serversocket.isClosed()) {
				try {
//					if(judge==1) {
//						history.append("judge is right");
//						judge=0;
//						model = new DefaultListModel<String>();
//					}
				//    model=(DefaultListModel<String>) list.getModel();
					user = new User();
					socket = serversocket.accept();
					InetAddress ip = socket.getInetAddress().getByName(socket.getInetAddress().getHostAddress());
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					outs.add(out);
					in = new ObjectInputStream(socket.getInputStream());
					user.setUsername((String) in.readObject());
					InetAddress ips = (InetAddress) in.readObject();
					user.setip(ips);
					user.setPort(in.readInt());
					history.append("Connection successfully" + "\n");
					kick.setEnabled(true);
					viewHistory.setEnabled(true);
					model.addElement(user.getUsername());
					list.setModel(model);
					users.add(user);
					history.append("User " + user.getUsername() + " is online\n");
					serverthread = new ServerThread(socket, history, list, user, users, in, out, outs);
					serverthread.start();
				} catch (Exception e) {
					history.append("fail to connect 2  " + e.toString());
				}
			}
		}
	}

	public class ServerThread extends Thread {
		JTextArea history;
		JList<String> list;
		Users users;
		User u;
		Socket socket;
		ObjectOutputStream out;
		ObjectInputStream in;
		ArrayList<ObjectOutputStream> outs;
		boolean isStop;
	//	DefaultListModel<String> model;
		public ServerThread(Socket socket, JTextArea history, JList<String> list, User u, Users users,
				ObjectInputStream in, ObjectOutputStream out, ArrayList<ObjectOutputStream> outs) {
		//	this.model = new DefaultListModel<String>();
			this.socket = socket;
			this.history = history;
			this.list = list;
			this.u = u;
			this.users = users;
			this.in = in;
			this.out = out;
			this.isStop = false;
			this.outs = outs;
		}

		public void run() {
			sendUserList();
			while (true) {
				try {
					String request = (String) in.readObject();
					if (request.equalsIgnoreCase("{*^$>User is outLine")) {
						User user = users.find(u.getUsername());
						int j = searchIndex(users, u.getUsername());
						int x = users.getCount();
						users.del(user);
						outs.remove(j);
						String msg = "User " + u.getUsername() + " is outline\n";		
					//	history.append("x=" + x);
						list.removeAll();
						if (x == 1) {
							model = new DefaultListModel<String>();
							model.addElement("");
							list.setModel(model);
						}
						int count = users.getCount();
						history.append(msg);
						int i = 0;
						model.removeAllElements();
						while (i < count && x != 1) {
							user = users.find(i);
							if (user == null) {
								i++;
								continue;
							}
							model.addElement(user.getUsername());
							list.setModel(model);
							i++;
						}
						sendUserList();
						sendToAll(msg);
						break;
					} else if (request.equalsIgnoreCase("{*^$>Historyback")) {
						Userhistory = (String) in.readObject();
						if (!Userhistory.equals("")) {
							history.setText(Userhistory);
						} else {
							history.append("History is null" + "\n");
						}
					}
				} catch (Exception e) {
					history.append("Fail to exit" + "\n");
				}
			}
		}

		public void sendToAll(String msg) {
			try {
				Iterator<ObjectOutputStream> it = outs.iterator();
				while (it.hasNext()) {
					ObjectOutputStream tout = it.next();
					tout.writeObject("{*^$>Outline Information");
					tout.flush();
					tout.writeObject(msg);
					tout.flush();
				}

			} catch (Exception e) {
				history.append("Fail to broadcast" + "\n");
			}
		}

		public void sendUserList() {
			String userlist = "";
			int count = users.getCount();
			int i = 0;
			while (i < count) {
				User user = users.find(i);
				if (user == null) {
					i++;
					continue;
				}
				userlist += user.getUsername();
				userlist += " @Next line@ ";
				i++;
			}

			try {
				Iterator<ObjectOutputStream> it = outs.iterator();
				while (it.hasNext()) {
					ObjectOutputStream tout = it.next();
					tout.writeObject("{*^$>User List");
					tout.flush();
					tout.writeObject(userlist);
					tout.flush();
					tout.writeObject(" ");
					tout.flush();
					if (tout != out) {
						tout.writeObject(users.find(users.getCount() - 1));
						tout.flush();
					} else {
						tout.writeObject(users);
						tout.flush();
					}
				}

			} catch (Exception e) {
				history.append("Fail to broadcast" + "\n");
			}
		}

		public int searchIndex(Users uers, String name) {
			int count = uers.getCount();
			int i = 0;
			while (i < count) {
				User user = users.find(i);
				if (!name.equalsIgnoreCase(user.getUsername()))
					i++;
				else
					return i;
			}
			return i;
		}
	}

}
