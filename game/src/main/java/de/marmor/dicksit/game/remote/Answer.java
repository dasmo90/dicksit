package de.marmor.dicksit.game.remote;

class Answer {

    private Answer.Type type;
    private String message;

    private Answer() {

    }

    Type getType() {
        return type;
    }

    String getMessage() {
        return message;
    }

    static Answer success() {
        Answer answer = new Answer();
        answer.type = Type.SUCCESS;
        return answer;
    }

    static Answer failure(String message) {
        Answer answer = new Answer();
        answer.type = Type.FAILURE;
        answer.message = message;
        return answer;
    }

    enum Type {
        SUCCESS, FAILURE
    }
}
