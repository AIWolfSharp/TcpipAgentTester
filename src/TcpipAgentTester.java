import org.aiwolf.common.data.Player;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.net.TcpipClient;

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

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		int port = 10000;
		String clsName = null;

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

		for (int j = 0; j < 10; j++) {
			for (Role requestRole : Role.values()) {
				if (requestRole == Role.FREEMASON) {
					continue;
				}
				Thread th = new Thread(new MyServerStarter(port, 15));
				th.start();
				TcpipClient client = new TcpipClient("localhost", port, requestRole);
				client.connect((Player) Class.forName(clsName).newInstance());
				for (int i = 0; i < 14; i++) {
					client = new TcpipClient("localhost", port, null);
					client.connect(new RandomPlayer());
				}
				while (th.isAlive())
					;
			}
		}
	}
}
