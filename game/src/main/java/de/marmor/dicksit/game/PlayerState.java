package de.marmor.dicksit.game;

public class PlayerState<T> {

    private String uuid;
    private int points;
    private int roundPoints;
    private boolean selected;
    private boolean chosen;
    private T selectedPicture;
    private T chosenPicture;

    protected PlayerState() {

    }

    public String getUuid() {
        return uuid;
    }

    protected void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getPoints() {
        return points;
    }

    protected void setPoints(int points) {
        this.points = points;
    }

    public int getRoundPoints() {
        return roundPoints;
    }

    protected void setRoundPoints(int roundPoints) {
        this.roundPoints = roundPoints;
    }

    public boolean isSelected() {
        return selected;
    }

    protected void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isChosen() {
        return chosen;
    }

    protected void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public T getSelectedPicture() {
        return selectedPicture;
    }

    protected void setSelectedPicture(T selectedPicture) {
        this.selectedPicture = selectedPicture;
    }

    public T getChosenPicture() {
        return chosenPicture;
    }

    protected void setChosenPicture(T chosenPicture) {
        this.chosenPicture = chosenPicture;
    }

    @Override
    public String toString() {
        return "PlayerState{" +
                "uuid='" + uuid + '\'' +
                ", points=" + points +
                ", roundPoints=" + roundPoints +
                ", selected=" + selected +
                ", chosen=" + chosen +
                ", selectedPicture=" + selectedPicture +
                ", chosenPicture=" + chosenPicture +
                '}';
    }
}
