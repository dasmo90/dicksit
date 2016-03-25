package de.marmor.dicksit.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.marmor.dicksit.game.config.GameConfig;

public class GameImpl<T> implements Game<T> {

    private State mState;

    private int mStartCards;
    private int mMaxPlayers;
    private int mEndPoints;

    private Map<String, Player<T>> mPlayers = new LinkedHashMap<>();
    private Map<String, ChangeCallback<T>> mCallbacks = new LinkedHashMap<>();
    private Set<T> mPictures = new HashSet<>();
    private List<String> mOrdererPlayers = new ArrayList<>();
    private String mHostPlayer;
    private String mStartPlayer;

    private String mActivePlayer;

    private Set<T> mCollectedPictures = new HashSet<>();
    private T mSelectedPicture;
    private String mSelectedTitle;

    protected GameImpl(GameConfig<T> gameConfig) throws GameException {
        initialize(gameConfig);
    }

    private void initialize(GameConfig<T> gameConfig) throws GameException {

        if (gameConfig.getPictures().size() < gameConfig.getStartCards() * gameConfig.getMaxPlayers()) {
            throw new IllegalStateException("Invalid configuration: " +
                    "Choose more pictures or few start cards or maximum player count.");
        }
        mPictures.addAll(gameConfig.getPictures());
        mStartCards = gameConfig.getStartCards();
        mMaxPlayers = gameConfig.getMaxPlayers();
        mEndPoints = gameConfig.getEndPoints();
        mState = State.PLAYER_REGISTERING;
    }

    @Override
    public synchronized void register(ChangeCallback<T> changeCallback) throws GameException {
        if (changeCallback == null) {
            throw new IllegalStateException("Callback cannot be null");
        }
        if (mState != State.PLAYER_REGISTERING) {
            throw new GameException("Wrong phase");
        }
        if(mPlayers.size() >= mMaxPlayers) {
            throw new GameException("Too many players");
        }
        String uuid = GameUtils.generateId();
        // Set first player to host player
        if (mHostPlayer == null) {
            mHostPlayer = uuid;
        }
        Player<T> player = new Player<>();
        player.givePictures(getPictures(mStartCards));
        mPlayers.put(uuid, player);
        mCallbacks.put(uuid, changeCallback);
        setChanged();
    }

    private List<T> getPictures(int number) throws GameException {
        List<T> pictures = new LinkedList<>();
        Iterator<T> pictureIterator = mPictures.iterator();
        for (int i = 0; i < number; i++) {
            if (pictureIterator.hasNext()) {
                pictures.add(pictureIterator.next());
            }
        }
        return pictures;
    }

    @Override
    public synchronized void setStartPlayer(String uuid) throws GameException {
        if (mState != State.PLAYER_REGISTERING) {
            throw new GameException("Wrong phase");
        }
        mStartPlayer = uuid;
        setChanged();
    }

    @Override
    public synchronized void start() throws GameException {
        if (mState != State.PLAYER_REGISTERING) {
            throw new GameException("Wrong phase");
        }
        if (mPlayers.size() <= 1) {
            throw new GameException("Too few players");
        }
        if (mStartPlayer == null) {
            mActivePlayer = mPlayers.keySet().iterator().next();
        } else {
            mActivePlayer = mStartPlayer;
        }
        mOrdererPlayers.addAll(mPlayers.keySet());
        mState = State.PLAYER_SELECTING_TITLE;
        setChanged();
    }

    private Player<T> getPlayer(String uuid, boolean active) throws GameException {
        if(!mActivePlayer.equals(uuid) && active) {
            throw new GameException("Player not active");
        }
        if(mActivePlayer.equals(uuid) && !active) {
            throw new GameException("Player active");
        }
        Player<T> player = mPlayers.get(uuid);
        if(player == null) {
            throw new GameException("Player not registered");
        }
        return player;
    }

    @Override
    public synchronized void selectTitle(String uuid, T picture, String title) throws GameException {
        if(mState != State.PLAYER_SELECTING_TITLE) {
            throw new GameException("Wrong phase");
        }
        Player<T> player = getPlayer(uuid, true);
        if(!player.removePicture(picture)) {
            throw new GameException("Player doesn't own this picture");
        }
        mCollectedPictures.add(picture);
        mSelectedPicture = picture;
        mSelectedTitle = title;
        player.setPictureSelected(true);
        player.setPictureChosen(true);
        mState = State.PLAYER_SELECTING_PICTURE;
        setChanged();
    }

    private boolean allPlayerSelected() {
        for(Player player : mPlayers.values()) {
            if(!player.isPictureSelected()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public synchronized void selectPicture(String uuid, T picture) throws GameException {
        if(mState != State.PLAYER_SELECTING_PICTURE) {
            throw new GameException("Wrong phase");
        }
        Player<T> player = getPlayer(uuid, true);
        if(player.isPictureSelected()) {
            throw new GameException("Player already chose a picture");
        }
        if(!player.removePicture(picture)) {
            throw new GameException("Player doesn't own this picture");
        }
        player.setSelectedPicture(picture);
        player.setPictureSelected(true);
        mCollectedPictures.add(picture);
        if(allPlayerSelected()) {
            mState = State.PLAYER_CHOSING_PICTURE;
        }
        setChanged();
    }

    private boolean allPlayerChosen() {
        for(Player player : mPlayers.values()) {
            if(!player.isPictureChosen()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public synchronized void chosePicture(String uuid, T picture) throws GameException {
        if(mState != State.PLAYER_CHOSING_PICTURE) {
            throw new GameException("Wrong phase");
        }
        if(!mCollectedPictures.contains(picture)) {
            throw new GameException("Picture was not collected");
        }
        Player<T> player = getPlayer(uuid, true);
        if(player.isPictureChosen()) {
            throw new GameException("Player already chose a picture");
        }
        player.setChosenPicture(picture);
        player.setPictureChosen(true);
        if(allPlayerChosen()) {
            endRound();
        }
        setChanged();
    }

    private void endRound() throws GameException {

        int playersRight = playersRight();
        if(playersRight == 0 || playersRight == mPlayers.size() - 1) {
            for(Map.Entry<String,Player<T>> entry: mPlayers.entrySet()) {
                Player<T> player = entry.getValue();
                if(entry.getKey().equals(mActivePlayer)) {
                    continue;
                }
                player.addRoundPoints(2);
            }
        } else {
            mPlayers.get(mActivePlayer).addRoundPoints(3);
            for(Map.Entry<String,Player<T>> entry: mPlayers.entrySet()) {
                Player<T> player = entry.getValue();
                if(entry.getKey().equals(mActivePlayer)) {
                    continue;
                }
                if(mSelectedPicture.equals(player.getChosenPicture())) {
                    player.addRoundPoints(3);
                } else {
                    playerThatSelectedPicture(player.getChosenPicture()).addRoundPoints(1);
                }
            }
        }

        mState = State.ROUND_ENDED;
    }

    private Player playerThatSelectedPicture(T picture) {
        for(Map.Entry<String,Player<T>> entry: mPlayers.entrySet()) {
            Player<T> player = entry.getValue();
            if(picture.equals(player.getSelectedPicture())) {
                return player;
            }
        }
        throw new IllegalStateException("No player selected picture.");
    }

    private int playersRight() {
        int playersRight = 0;
        for(Map.Entry<String,Player<T>> entry: mPlayers.entrySet()) {
            Player<T> player = entry.getValue();
            if(entry.getKey().equals(mActivePlayer)) {
                continue;
            }
            if(mSelectedPicture.equals(player.getChosenPicture())) {
                playersRight++;
            }
        }
        return playersRight;
    }

    private void changeActivePlayer() {
        int index = mOrdererPlayers.indexOf(mActivePlayer);
        index++;
        if(index == mOrdererPlayers.size()) {
            index = 0;
        }
        mActivePlayer = mOrdererPlayers.get(index);
    }

    @Override
    public void roundOkay(String uuid) throws GameException {
        if(mState != State.ROUND_ENDED) {
            throw new GameException("Wrong phase");
        }
        Player player = mPlayers.get(uuid);
        if(player == null) {
            throw new GameException("Player not registered");
        }
        player.roundOkay();
        if(allPlayerOkay()) {
            startNewRound();
        }
        setChanged();
    }

    private void startNewRound() throws GameException {

        mSelectedTitle = null;
        mSelectedPicture = null;
        mCollectedPictures.clear();

        for(Player<T> player : mPlayers.values()) {
            player.setPictureChosen(false);
            player.setPictureSelected(false);
            int points = player.calcPoints();
            if(points >= mEndPoints) {
                player.finished();
            }
            player.givePictures(getPictures(1));
        }
        if(playerFinished()) {
            mState = State.GAME_ENDED;
        } else {
            changeActivePlayer();
            mState = State.PLAYER_SELECTING_TITLE;
        }
    }

    private boolean playerFinished() {
        for(Player player : mPlayers.values()) {
            if(player.isFinished()) {
                return true;
            }
        }
        return false;
    }

    private boolean allPlayerOkay() {
        for(Player player : mPlayers.values()) {
            if(!player.isRoundOkay()) {
                return false;
            }
        }
        return true;
    }

    private void setChanged() throws GameException {
        for(Map.Entry<String, ChangeCallback<T>> entry : mCallbacks.entrySet()) {
            String uuid = entry.getKey();
            entry.getValue().changed(uuid, generateGameForPlayer(uuid));
        }
    }

    private GameState<T> generateGameForPlayer(String uuid) throws GameException {

        boolean active = uuid.equals(mActivePlayer);
        GameState<T> gameState = new GameState<>();
        gameState.setState(mState.toString());
        gameState.setActivePlayer(mActivePlayer);
        gameState.setPlayerState(mPlayers.get(uuid).getPlayerState(mState, uuid, active, false));
        gameState.setPlayerPictures(mPlayers.get(uuid).getPictures());
        for(Map.Entry<String, Player<T>> playerEntry: mPlayers.entrySet()) {
            if(!playerEntry.getKey().equals(uuid)) {
                gameState.addPlayerState(playerEntry.getValue().getPlayerState(mState,
                        playerEntry.getKey(), active, true));
            }
        }
        // switch with fall through
        switch (mState) {
            case ROUND_ENDED:
                gameState.setCorrectPicture(mSelectedPicture);
            case PLAYER_CHOSING_PICTURE:
                gameState.setCollectedPictures(new LinkedList<>(mCollectedPictures));
            case PLAYER_SELECTING_PICTURE:
                gameState.setTitle(mSelectedTitle);
        }
        return gameState;
    }
}
