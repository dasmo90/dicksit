package de.dasmo90;

public class ThreadReturn<T> {

    private volatile boolean waiting = false;
    private T returnValue;
    private Exception exception;

    public synchronized void startWait() {
        waiting = true;
        returnValue = null;
        exception = null;
    }

    public synchronized void nowReturn(T value) {
        returnValue = value;
        waiting = false;
    }

    public synchronized T waitTillReturn() throws Exception {
        while (waiting) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(returnValue != null || exception == null) {
            T value = returnValue;
            returnValue = null;
            exception = null;
            return value;
        } else {
            Exception exceptionTemp = exception;
            exception = null;
            throw exceptionTemp;
        }
    }

    public void nowThrow(Exception execption) {
        this.exception = execption;
        waiting = false;
    }

    public void nowReturn() {
        waiting = false;
    }
}
