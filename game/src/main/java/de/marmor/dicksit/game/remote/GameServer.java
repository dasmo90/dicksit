package de.marmor.dicksit.game.remote;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.dasmo90.NetworkUtils;
import de.dasmo90.ObjectSerializer;
import de.marmor.dicksit.game.ChangeCallback;
import de.marmor.dicksit.game.Game;
import de.marmor.dicksit.game.GameException;
import de.marmor.dicksit.game.GameProvider;
import de.marmor.dicksit.game.GameState;
import de.marmor.dicksit.game.config.GameConfig;

public class GameServer<T> extends GameRemote<T> {

    private final Game<T> mGame;
    private String serverPw;
    private Map<String, String> clientsAndIps = new HashMap<>();

    protected GameServer(GameConfig<T> gameConfig, int serverPort, int clientPort) throws IOException, GameException {
        super(serverPort, clientPort);
        mGame = GameProvider.instance(gameConfig);
        serverPw = ServerPw.retrieveServerPw(getServerIp());
    }

    public String getServerPw() throws GameException {
        return serverPw;
    }

    @Override
    public void register(ChangeCallback<T> changeCallback) throws GameException {
        mGame.register(changeCallback);
    }

    @Override
    public void setStartPlayer(String uuid) throws GameException {
        mGame.setStartPlayer(uuid);
    }

    @Override
    public void start() throws GameException {
        mGame.start();
    }

    @Override
    public void selectTitle(String uuid, T picture, String title) throws GameException {
        mGame.selectTitle(uuid, picture, title);
    }

    @Override
    public void selectPicture(String uuid, T picture) throws GameException {
        mGame.selectPicture(uuid, picture);
    }

    @Override
    public void chosePicture(String uuid, T picture) throws GameException {
        mGame.chosePicture(uuid, picture);
    }

    @Override
    public void roundOkay(String uuid) throws GameException {
        mGame.roundOkay(uuid);
    }

    protected String handle(String input, String ip) {
        try {
            ClientMessage<T> clientMessage = ObjectSerializer.deserialize(input);
            clientsAndIps.put(clientMessage.getUuid(), ip);
            try {
                call(clientMessage, ip);
                return ObjectSerializer.serialize(Answer.success());
            } catch (GameException e) {
                return ObjectSerializer.serialize(Answer.failure(e.getMessage()));
            }
        } catch (IOException | ClassNotFoundException exception) {
            return ObjectSerializer.serializeHard(Answer.failure(exception.getMessage()));
        }
    }

    private void call(ClientMessage<T> clientMessage, final String ip) throws GameException {

        switch (clientMessage.getMethod()) {
            case ClientMessage.INITIALIZE:
                throw new GameException("Cannot call remotely 'initialize'");
            case ClientMessage.REGISTER:
                if(!getServerPw().equals(clientMessage.getPw())) {
                    throw new GameException("Wrong server password");
                }
                mGame.register(new ChangeCallback<T>() {
                    @Override
                    public void changed(final String uuid, final GameState<T> gameState) {
                        String client = clientsAndIps.get(uuid);
                        cast(client == null ? ip : client, gameState);
                    }
                });
                break;
            case ClientMessage.START:
                mGame.start();
                break;
            case ClientMessage.SELECT_TITLE:
                mGame.selectTitle(clientMessage.getUuid(), clientMessage.getPicture(), clientMessage.getTitle());
                break;
            case ClientMessage.SELECT_PICTURE:
                mGame.selectPicture(clientMessage.getUuid(), clientMessage.getPicture());
                break;
            case ClientMessage.CHOSE_PICTURE:
                mGame.chosePicture(clientMessage.getUuid(), clientMessage.getPicture());
                break;
            case ClientMessage.ROUND_OKAY:
                mGame.roundOkay(clientMessage.getUuid());
        }
        throw new GameException("Method not supported.");
    }
}
