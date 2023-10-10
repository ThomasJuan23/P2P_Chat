package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import server.ServerUI;

public class ChatUI extends JFrame {
	JButton send, clear;
	JTextArea history;
	JList<String> list;
	JLabel user;
	User u;
	Socket socket;
	JLabel lHistory;
	JLabel userName, IP;
	JTextField use, IPaddress;
	JButton login, exit;
	JLabel p2p, chat;
	JLabel command;
	JTextArea commandText;
	String username;
	InetAddress ip;
	DefaultListModel<String> model;
	JScrollPane jp1;
	int selectedPort;
	int clientListenPort;
	ObjectOutputStream out;
	ObjectInputStream in;
	Users users;
	ClientReceiveThread clientreceivethread;

	public static void main(String[] args) {
		ChatUI cu = new ChatUI();
		JFrame j = cu.chatWindow();
	}

	public JFrame chatWindow() {
		JFrame jf = new JFrame();
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int sWidth = screenSize.width / 2;
		int sHeight = screenSize.height / 2;
		jf.setSize(sWidth / 2, (sHeight * 2) / 3);
		jf.setTitle("Chat");
		send = new JButton("Send");
		clear = new JButton("Clear");
		userName = new JLabel("User name: ");
		IP = new JLabel("Server IP: ");
		use = new JTextField();
		use.setBackground(Color.white);
		IPaddress = new JTextField();
		IPaddress.setBackground(Color.WHITE);
		IPaddress.setText("127.0.0.1");
		login = new JButton("Login");
		exit = new JButton("Exit");
		p2p = new JLabel("P2P");
		p2p.setFont(new Font("Aril", 2, 18));
		chat = new JLabel("Chat");
		chat.setFont(new Font("Aril", 2, 18));
		send.setFont(new Font("Aril", 0, 12));
		clear.setFont(new Font("Aril", 0, 12));
		login.setFont(new Font("Aril", 0, 12));
		exit.setFont(new Font("Aril", 0, 12));
		user = new JLabel("User list");
		lHistory = new JLabel("Message history");
		lHistory.setFont(new Font("Aril", 0, 28));
		user.setFont(new Font("Aril", 0, 28));
		p2p.setHorizontalAlignment(JLabel.CENTER);
		chat.setHorizontalAlignment(JLabel.CENTER);
		user.setHorizontalAlignment(JLabel.CENTER);
		user.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		lHistory.setHorizontalAlignment(JLabel.CENTER);
		lHistory.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		command = new JLabel("Message: ");
		command.setFont(new Font("Aril", 0, 28));
		command.setHorizontalAlignment(JLabel.CENTER);
		command.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		commandText = new JTextArea();
		commandText.setBackground(Color.lightGray);
		commandText.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		list = new JList<String>();
		history = new JTextArea();
		list.setBackground(Color.white);
		history.setBackground(Color.white);
		list.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		commandText.setBackground(Color.white);
		history.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		clear.setEnabled(false);
		send.setEnabled(false);
		commandText.setEnabled(false);
		exit.setEnabled(false);
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (commandText.getText().equalsIgnoreCase("") || commandText.getText() == null) {
					history.append("You input nothing");
				}
				sendUserMessage();

			}
		});

		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				commandText.setText("");
				// TODO Auto-generated method stub

			}
		});
		login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				// s.startServer();

				username = use.getText();
				try {
					ip = InetAddress.getByName(IPaddress.getText());
					username = use.getText();
					history.append("Logging in....." + "\n");
					login.setEnabled(false);
					clear.setEnabled(true);
					send.setEnabled(true);
					commandText.setEnabled(true);
					exit.setEnabled(true);
					LoginServer log = new LoginServer();
					new Thread(log).start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					history.append("The IP address is wrong");
				}

				// TODO Auto-generated method stub

			}
		});
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				login.setEnabled(true);
				clear.setEnabled(false);
				send.setEnabled(false);
				commandText.setEnabled(false);
				exit.setEnabled(false);
				exit();
				history.setText("");

				// TODO Auto-generated method stub

			}
		});
		history.setEditable(false);
		history.setDisabledTextColor(Color.black);
		jp1 = new JScrollPane(history);
		jp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel panel1 = new JPanel();
		panel1.setPreferredSize(new Dimension(130, 350));
		panel1.setLayout(new BorderLayout());
		panel1.add(user, BorderLayout.NORTH);
		panel1.add(list, BorderLayout.CENTER);
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(2, 5));
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
		JPanel panel3 = new JPanel();
		panel3.setLayout(new BorderLayout());
		panel3.add(lHistory, BorderLayout.NORTH);
		panel3.add(jp1, BorderLayout.CENTER);
		JPanel panel9 = new JPanel();
		panel9.setLayout(new BorderLayout());
		panel9.add(command, BorderLayout.NORTH);
		panel9.add(commandText, BorderLayout.CENTER);
		JPanel panel10 = new JPanel();
		panel10.setLayout(new BorderLayout(10, 2));
		panel10.add(panel2, BorderLayout.SOUTH);
		panel10.add(panel9, BorderLayout.CENTER);
		JPanel panel4 = new JPanel();
		panel4.setLayout(new GridLayout(2, 1));
		panel4.add(panel3);
		panel4.add(panel10);
		JPanel panel5 = new JPanel();
		panel5.setLayout(new BorderLayout(10, 10));
		panel5.add(panel1, BorderLayout.WEST);
		panel5.add(panel4, BorderLayout.CENTER);
		JPanel panel6 = new JPanel();
		panel6.setLayout(new GridLayout(1, 7));
		panel6.add(p2p);
		panel6.add(new JLabel(""));
		panel6.add(userName);
		panel6.add(use);
		panel6.add(new JLabel(""));
		panel6.add(login);
		panel6.add(new JLabel(""));
		JPanel panel7 = new JPanel();
		panel7.setLayout(new GridLayout(1, 7));
		panel7.add(chat);
		panel7.add(new JLabel(""));
		panel7.add(IP);
		panel7.add(IPaddress);
		panel7.add(new JLabel(""));
		panel7.add(exit);
		panel7.add(new JLabel(""));
		JPanel panel8 = new JPanel();
		panel8.setLayout(new GridLayout(2, 1));
		panel8.add(panel6);
		panel8.add(panel7);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(panel8, BorderLayout.NORTH);
		panel.add(panel5, BorderLayout.CENTER);
		jf.setLayout(new BorderLayout());
		jf.add(panel, BorderLayout.CENTER);
		jf.setVisible(true);
		jf.setLocation(sWidth - 300, sHeight - 250);
		jf.setDefaultCloseOperation(EXIT_ON_CLOSE);
		return jf;
	}

	public void sendUserMessage() {
		try {
			if (list.getSelectedIndex() == 0) {
				String name = use.getText();
				String msgg = commandText.getText();
				for (int j = 0; j < users.getCount(); j++) {
					InetAddress ip1 = users.find(j).ip;
					int port1 = users.find(j).port;
					new ClientSendThread(ip1, port1, msgg, name).start();
				}
				commandText.setText("");

			} else {
				String toSomebody = list.getSelectedValue();
				history.append(use.getText() + " to " + toSomebody + ":" + "\n");
				history.append(" " + commandText.getText() + "\n");
				String name = use.getText();
				String msg = commandText.getText();
				InetAddress ip = users.find(toSomebody).ip;
				int port = users.find(toSomebody).port;
				String ip1 = ip.getHostAddress();
				Socket clientsocket = new Socket(ip1, port);
				ObjectOutputStream out = new ObjectOutputStream(clientsocket.getOutputStream());
				out.writeObject("{*^$>Message");
				out.flush();
				out.writeObject(name);
				out.flush();
				out.writeObject(msg);
				out.flush();
				commandText.setText("");
			}

		} catch (Exception ec) {
			history.append("Fail to send to someone" + ec.toString());
		}
	}

	public void exit() {

		if (!socket.isClosed()) {
			try {
				String s = "{*^$>User is outline";
				out.writeObject(s);
				out.flush();
				in.close();
				out.close();
				System.exit(0);
			} catch (Exception e) {
				System.exit(0);
			}
		}
	}

	public class LoginServer implements Runnable {

		public void run() {

			try {
				u = new User();
				socket = new Socket(ip, 6666);
				ip = socket.getLocalAddress();
				history.append(use.getText() + " login successfully" + "\n");
				Random rand = new Random();
				//clientListenPort = getPort.getAvailablePort();
				clientListenPort=rand.nextInt(6665);
				out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(use.getText());
				out.flush();
				out.writeObject(ip);
				out.flush();
				out.writeInt(clientListenPort);
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());
				clientreceivethread = new ClientReceiveThread(u, socket, in, out, list, history, commandText, ip,
						clientListenPort, selectedPort);
				clientreceivethread.start();
				while (true) {
					try {
						String type = (String) in.readObject();
						if (type.equalsIgnoreCase("{*^$>User list")) {
							String userlist = (String) in.readObject();
							String username[] = userlist.split(" @Next line@ ");
							list.removeAll();
							int i = 0;
							model = new DefaultListModel<String>();
							model.addElement("all");
							list.setModel(model);
							while (i < username.length) {
								model.addElement(username[i]);
								list.setModel(model);
								i++;
							}
							String msg = (String) in.readObject();
							commandText.setText(msg);

							Object o = in.readObject();
							if (o instanceof Users) {
								users = (Users) o;
							} else {
								users.add((User) o);
							}
						} else if (type.equalsIgnoreCase("{*^$>Server")) {
							String b = (String) in.readObject();
							history.append("Server:" + "\n" + b + "\n");
						} else if (type.equalsIgnoreCase("{*^$>Outline information")) {
							String msg = (String) in.readObject();
							history.append("Outline:"+"\n" + msg + "\n");
						} else if (type.equalsIgnoreCase("{*^$>Stop")) {
							// out.writeObject(obj);
							exit();
							break;
						} else if (type.equalsIgnoreCase("{*^$>History")) {
							String his = history.getText();
							out.writeObject("{*^$>Historyback");
							out.writeObject(his);
						}

					} catch (Exception e) {
						history.append("Fail to connect" + e.toString() + "\n");
					}
				}
			} catch (Exception e) {
				history.append("Fail to connect" + e.toString() + "\n");
			}
		}

	}

	public class ClientReceiveThread extends Thread {
		Socket socket;
		JList<String> list;
		JTextArea history;
		ObjectInputStream in;
		ObjectOutputStream out;
		User user;
		InetAddress ip;
		int port;
		SocketAddress ip1;
		ServerSocket serversocket;
		int selectedPort;
		JTextArea commandText;

		public ClientReceiveThread(User user, Socket socket, ObjectInputStream in, ObjectOutputStream out,
				JList<String> list, JTextArea history, JTextArea commandText, InetAddress ip, int port,
				int selectedPort) {
			this.user = user;
			this.socket = socket;
			this.in = in;
			this.out = out;
			this.list = list;
			this.history = history;
			this.commandText = commandText;
			this.ip = ip;
			this.port = port;
			this.selectedPort = selectedPort;
		}

		public void run() {

			try {
				serversocket = new ServerSocket(port);
				while (true) {
					Socket clientsocket = serversocket.accept();
					ObjectOutputStream out = new ObjectOutputStream(clientsocket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(clientsocket.getInputStream());
					String type = (String) in.readObject();
					if (type.equalsIgnoreCase("{*^$>Message")) {
						String name = (String) in.readObject();
						String mess = (String) in.readObject();
						history.append(name + " to you:" + "\n");
						history.append(" " + mess + "\n");
					}

				}

			} catch (Exception e) {
				history.append("Fail to send"+"\n");
			}
		}

	}

	public class ClientSendThread extends Thread {
		InetAddress address;
		int port;
		String message;
		String name;

		public ClientSendThread(InetAddress address, int port, String message, String name) {
			this.address = address;
			this.port = port;
			this.message = message;
			this.name = name;
		}

		public void run() {
			try {
				Socket socket = new Socket(address, port);
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject("{*^$>Message");
				out.writeObject(name);
				out.writeObject(message);
				out.flush();
			} catch (Exception e) {
				history.append("Fail to send"+"\n");
			}
		}
	}

}
