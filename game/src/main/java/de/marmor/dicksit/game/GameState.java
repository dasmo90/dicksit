package de.marmor.dicksit.game;

import java.util.LinkedList;
import java.util.List;

public class GameState<T> {

    private long timeStamp;
    private String state;
    private String activePlayer;
    private List<T> playerPictures;
    private String title;
    private List<T> collectedPictures;
    private T correctPicture;
    private List<PlayerState<T>> players = new LinkedList<>();
    private PlayerState<T> playerState;

    protected GameState() {
        timeStamp = System.currentTimeMillis();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getState() {
        return state;
    }

    protected void setState(String state) {
        this.state = state;
    }

    public String getActivePlayer() {
        return activePlayer;
    }

    protected void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    public List<T> getPlayerPictures() {
        return playerPictures;
    }

    protected void setPlayerPictures(List<T> playerPictures) {
        this.playerPictures = playerPictures;
    }

    public String getTitle() {
        return title;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    public List<T> getCollectedPictures() {
        return collectedPictures;
    }

    protected void setCollectedPictures(List<T> collectedPictures) {
        this.collectedPictures = collectedPictures;
    }

    public T getCorrectPicture() {
        return correctPicture;
    }

    protected void setCorrectPicture(T correctPicture) {
        this.correctPicture = correctPicture;
    }

    public List<PlayerState<T>> getPlayerStates() {
        return new LinkedList<>(players);
    }

    protected void addPlayerState(PlayerState<T> playerState) {
        players.add(playerState);
    }

    public PlayerState<T> getPlayerState() {
        return playerState;
    }

    protected void setPlayerState(PlayerState<T> playerState) {
        this.playerState = playerState;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "timeStamp=" + timeStamp +
                ", state='" + state + '\'' +
                ", activePlayer='" + activePlayer + '\'' +
                ", playerPictures=" + playerPictures +
                ", title='" + title + '\'' +
                ", collectedPictures=" + collectedPictures +
                ", correctPicture=" + correctPicture +
                ", players=" + players +
                ", playerState=" + playerState +
                '}';
    }
}
