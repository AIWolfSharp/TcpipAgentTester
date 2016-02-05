import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.aiwolf.common.data.Player;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.net.GameSetting;
import org.aiwolf.common.net.TcpipClient;
import org.aiwolf.server.AIWolfGame;
import org.aiwolf.server.net.TcpipServer;

/**
 * New agent tester.<br>
 * 
 * The server and clients communicate via TCP/IP.
 * 
 * @author Takashi OTSUKI
 *
 */
public class TcpipAgentTester {

	public static void usage() {
		System.err.println("Usage:" + TcpipAgentTester.class + " [-c clientClass] [-h host] [-p port] [-g gameNum]");
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException {
		int port = 10000;
		String hostName = null;
		String clsName = null;
		int playerNum = 15;
		int gameNum = 10;

		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-")) {
				if (args[i].equals("-p")) {
					i++;
					port = Integer.parseInt(args[i]);
				} else if (args[i].equals("-c")) {
					i++;
					clsName = args[i];
				} else if (args[i].equals("-h")) {
					i++;
					hostName = args[i];
				} else if (args[i].equals("-g")) {
					i++;
					gameNum = Integer.parseInt(args[i]);
				}
			}
		}
		if (port < 0) {
			usage();
			System.exit(0);
		}

		if (hostName == null) { // Standalone mode

			System.out.printf("Start AIWolf Server port:%d playerNum:%d\n", port, playerNum);
			GameSetting gameSetting = GameSetting.getDefaultGame(playerNum);
			TcpipServer gameServer = new TcpipServer(port, playerNum, gameSetting);

			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						gameServer.waitForConnection();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};

			Thread t = new Thread(r);
			t.start();

			ArrayList<TcpipClient> randomPlayerList = new ArrayList<TcpipClient>();
			for (int i = 0; i < 14; i++) {
				randomPlayerList.add(new TcpipClient("localhost", port, null));
				randomPlayerList.get(i).connect(new RandomPlayer());
			}

			// Wait a connection from outside when class name is not specified.
			if (clsName != null) {
				TcpipClient client = new TcpipClient("localhost", port, null);
				client.connect((Player) Class.forName(clsName).newInstance());
			}

			t.join();

			for (Role requestRole : Role.values()) {
				if (requestRole == Role.FREEMASON) {
					continue;
				}

				// Fill with RandomPlayers so that only the requestRole is left.
				Iterator<TcpipClient> it = randomPlayerList.iterator();
				for (Role role : Role.values()) {
					if (role == Role.FREEMASON) {
						continue;
					}
					int i = 0;
					if (role == requestRole) {
						i = 1;
					}
					while (i < gameSetting.getRoleNumMap().get(role)) {
						if (it.hasNext()) {
							it.next().setRequestRole(role);
						}
						i++;
					}
				}

				for (int i = 0; i < gameNum; i++) {
					new AIWolfGame(gameSetting, gameServer).start();
				}
			}
			gameServer.close();
		} else { // Client mode
			if (clsName == null) {
				usage();
				System.err.println("Player class must be specified in client mode.");
				System.exit(0);
			}
			TcpipClient client = new TcpipClient(hostName, port, null);
			client.connect((Player) Class.forName(clsName).newInstance());
			while (true) {
				Thread.sleep(1000);
				if (!client.isConnecting()) {
					break;
				}
			}
		}

		System.exit(0);
	}
}
