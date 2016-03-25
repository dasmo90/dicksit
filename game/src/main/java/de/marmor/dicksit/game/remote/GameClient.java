package de.marmor.dicksit.game.remote;

import java.io.IOException;

import de.dasmo90.ObjectSerializer;
import de.marmor.dicksit.game.ChangeCallback;
import de.marmor.dicksit.game.GameException;
import de.marmor.dicksit.game.GameState;
import de.marmor.dicksit.game.config.GameConfig;

public class GameClient<T> extends GameRemote<T> {

    private final String mServerIp;

    private ChangeCallback<T> mChangeCallback;

    protected GameClient(String serverPw, int serverPort, int clientPort) throws IOException {
        super(serverPort, clientPort);
        String clientIp = getServerIp();
        mServerIp = ServerPw.retrieveServerIp(clientIp, serverPw);
    }

    @Override
    protected String handle(String input, String ip) {
        try {
            GameState<T> gameState = ObjectSerializer.deserialize(input);
            if(mChangeCallback != null) {
                mChangeCallback.changed(gameState.getPlayerState().getUuid(), gameState);
                return ObjectSerializer.serialize(Answer.success());
            } else {
                return ObjectSerializer.serialize(Answer.failure("Change callback not set."));
            }
        } catch (IOException | ClassNotFoundException exception) {
            return ObjectSerializer.serializeHard(Answer.failure(exception.getMessage()));
        }
    }

    @Override
    public void register(ChangeCallback<T> changeCallback) throws GameException {
        ClientMessage<T> clientMessage = new ClientMessage<>();
        clientMessage.setMethod(ClientMessage.REGISTER);
        mChangeCallback = changeCallback;
        send(mServerIp, clientMessage);
    }

    @Override
    public void setStartPlayer(String uuid) throws GameException {
        throw new UnsupportedOperationException(
                "Not yet implemented: GameClient#setStartPlayer(String)");
    }

    @Override
    public void start() throws GameException {
        ClientMessage<T> clientMessage = new ClientMessage<>();
        clientMessage.setMethod(ClientMessage.START);
        send(mServerIp, clientMessage);
    }

    @Override
    public void selectTitle(String uuid, T picture, String title) throws GameException {
        ClientMessage<T> clientMessage = new ClientMessage<>();
        clientMessage.setMethod(ClientMessage.SELECT_TITLE);
        clientMessage.setUuid(uuid);
        clientMessage.setPicture(picture);
        clientMessage.setTitle(title);
        send(mServerIp, clientMessage);
    }

    @Override
    public void selectPicture(String uuid, T picture) throws GameException {
        ClientMessage<T> clientMessage = new ClientMessage<>();
        clientMessage.setMethod(ClientMessage.SELECT_PICTURE);
        clientMessage.setUuid(uuid);
        clientMessage.setPicture(picture);
        send(mServerIp, clientMessage);
    }

    @Override
    public void chosePicture(String uuid, T picture) throws GameException {
        ClientMessage<T> clientMessage = new ClientMessage<>();
        clientMessage.setMethod(ClientMessage.CHOSE_PICTURE);
        clientMessage.setUuid(uuid);
        clientMessage.setPicture(picture);
        send(mServerIp, clientMessage);
    }

    @Override
    public void roundOkay(String uuid) throws GameException {
        ClientMessage<T> clientMessage = new ClientMessage<>();
        clientMessage.setMethod(ClientMessage.ROUND_OKAY);
        clientMessage.setUuid(uuid);
        send(mServerIp, clientMessage);
    }
}
