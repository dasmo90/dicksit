package de.marmor.dicksit.game;

import java.util.LinkedList;
import java.util.List;

class Player<T> {

    private List<T> pictures = new LinkedList<>();
    private T chosenPicture;
    private boolean pictureSelected;
    private boolean pictureChosen;
    private int mRoundPoints;
    private T selectedPicture;
    private boolean roundOkay;
    private int points;
    private boolean mFinished;

    void givePictures(List<T> randomPictures) {
        pictures.addAll(randomPictures);
    }

    boolean removePicture(T picture) {
        return pictures.remove(picture);
    }

    void setChosenPicture(T chosenPicture) {
        this.chosenPicture = chosenPicture;
    }

    T getChosenPicture() {
        return chosenPicture;
    }

    void setPictureSelected(boolean pictureSelected) {
        this.pictureSelected = pictureSelected;
    }

    boolean isPictureSelected() {
        return pictureSelected;
    }

    void setPictureChosen(boolean pictureChosen) {
        this.pictureChosen = pictureChosen;
    }

    boolean isPictureChosen() {
        return pictureChosen;
    }

    void addRoundPoints(int roundPoints) {
        mRoundPoints += roundPoints;
    }

    T getSelectedPicture() {
        return selectedPicture;
    }

    void setSelectedPicture(T selectedPicture) {
        this.selectedPicture = selectedPicture;
    }

    void roundOkay() {
        roundOkay = true;
    }

    boolean isRoundOkay() {
        return roundOkay;
    }

    int calcPoints() {
        points += mRoundPoints;
        mRoundPoints = 0;
        return points;
    }

    void finished() {
        mFinished = true;
    }

    boolean isFinished() {
        return mFinished;
    }

    List<T> getPictures() {
        return pictures;
    }

    PlayerState<T> getPlayerState(Game.State gameState, String uuid, boolean active, boolean me) {
        PlayerState<T> playerState = new PlayerState<>();
        playerState.setUuid(uuid);
        playerState.setPoints(points);
         if(gameState == Game.State.PLAYER_SELECTING_PICTURE) {
            playerState.setSelected(pictureSelected);
            if((active || me) && pictureSelected) {
                playerState.setSelectedPicture(selectedPicture);
            }
        } else if(gameState == Game.State.PLAYER_CHOSING_PICTURE) {
            playerState.setChosen(pictureChosen);
            if(me && pictureChosen) {
                playerState.setChosenPicture(chosenPicture);
            }
        } else if(gameState == Game.State.ROUND_ENDED) {
            playerState.setRoundPoints(mRoundPoints);
            playerState.setSelectedPicture(selectedPicture);
            playerState.setChosenPicture(chosenPicture);
        }
        return playerState;
    }
}
