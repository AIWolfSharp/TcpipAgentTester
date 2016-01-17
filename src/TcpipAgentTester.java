import java.io.IOException;
import java.util.Random;

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
		System.err.println("Usage:" + TcpipAgentTester.class + " -c clientClass (-p port)");
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException {
		int port = 10000;
		String clsName = null;
		int playerNum = 15;

		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-")) {
				if (args[i].equals("-p")) {
					i++;
					port = Integer.parseInt(args[i]);
				} else if (args[i].equals("-c")) {
					i++;
					clsName = args[i];
				}
			}
		}
		if (port < 0 || clsName == null) {
			usage();
			System.exit(-1);
		}

		System.out.printf("Start AIWolf Server port:%d playerNum:%d\n", port, playerNum);
		GameSetting gameSetting = GameSetting.getDefaultGame(playerNum);
		TcpipServer gameServer = new TcpipServer(port, playerNum, gameSetting);

		for (Role requestRole : Role.values()) {
			if (requestRole == Role.FREEMASON) {
				continue;
			}

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

			TcpipClient client = new TcpipClient("localhost", port, requestRole);
			client.connect((Player) Class.forName(clsName).newInstance());
			for (int i = 0; i < playerNum - 1; i++) {
				client = new TcpipClient("localhost", port, null);
				client.connect(new RandomPlayer());
			}

			t.join();

			for (int i = 0; i < 10; i++) {
				AIWolfGame game = new AIWolfGame(gameSetting, gameServer);
				game.setRand(new Random());
				game.start();
			}

			gameServer.close();
		}
	}
}
