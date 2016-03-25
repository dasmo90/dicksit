package de.marmor.dicksit.game.remote;

import java.io.IOException;

import de.marmor.dicksit.game.Game;
import de.marmor.dicksit.game.GameException;
import de.marmor.dicksit.game.config.GameConfig;

public class GameRemoteProvider<T> {

    private String mServerPw;
    private int mServerPort = 31251;
    private int mClientPort = 31252;

    public void setServerPort(int serverPort) {
        mServerPort = serverPort;
    }

    public void setClientPort(int clientPort) {
        mClientPort = clientPort;
    }

    public Game<T> server(GameConfig<T> gameConfig) throws GameException, IOException {
        GameServer<T> gameServer = new GameServer<T>(gameConfig, mServerPort, mClientPort);
        mServerPw = gameServer.getServerPw();
        return gameServer;
    }

    public String serverPassword() throws GameException {
        if(mServerPw == null) {
            throw new GameException("No server instance generated.");
        }
        return mServerPw;
    }

    public Game<T> client(String serverPw) throws IOException {
        return new GameClient<>(serverPw, mClientPort, mServerPort);
    }
}
