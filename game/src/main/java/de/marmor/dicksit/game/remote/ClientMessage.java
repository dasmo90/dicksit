package de.marmor.dicksit.game.remote;

class ClientMessage<T> {

    public static final String INITIALIZE = "initialize";
    public static final String REGISTER = "register";
    public static final String START = "start";
    public static final String SELECT_TITLE = "selectTitle";
    public static final String SELECT_PICTURE = "selectPicture";
    public static final String CHOSE_PICTURE = "chosePicture";
    public static final String ROUND_OKAY = "roundOkay";

    private String uuid;
    private T picture;
    private String method;
    private String title;
    private String pw;

    String getUuid() {
        return uuid;
    }

    String getMethod() {
        return method;
    }

    void setMethod(String method) {
        this.method = method;
    }

    T getPicture() {
        return picture;
    }

    void setPicture(T picture) {
        this.picture = picture;
    }

    void setUuid(String uuid) {
        this.uuid = uuid;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getPw() {
        return pw;
    }

    void setPw(String pw) {
        this.pw = pw;
    }
}
