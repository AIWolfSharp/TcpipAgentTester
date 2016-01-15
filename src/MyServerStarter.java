import java.io.IOException;
import java.util.Random;

import org.aiwolf.common.net.GameSetting;
import org.aiwolf.server.AIWolfGame;
import org.aiwolf.server.net.TcpipServer;

public class MyServerStarter implements Runnable {
	private int port;
	private int playerNum;

	public MyServerStarter(int port, int playerNum) {
		this.port = port;
		this.playerNum = playerNum;
	}

	@Override
	public void run() {
		System.out.printf("Start AIWolf Server port:%d playerNum:%d\n", port, playerNum);
		GameSetting gameSetting = GameSetting.getDefaultGame(playerNum);
		TcpipServer gameServer = new TcpipServer(port, playerNum, gameSetting);
		try {
			gameServer.waitForConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AIWolfGame game = new AIWolfGame(gameSetting, gameServer);
		game.setRand(new Random());
		game.start();
		gameServer.close();
	}
}
