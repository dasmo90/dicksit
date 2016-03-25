package de.marmor.dicksit.game.remote;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import de.dasmo90.NetworkUtils;
import de.dasmo90.ObjectSerializer;
import de.dasmo90.ThreadReturn;
import de.marmor.dicksit.game.Game;
import de.marmor.dicksit.game.GameException;

public abstract class GameRemote<T> implements Game<T> {

    private static final String TAG = GameRemote.class.getSimpleName();

    private final int serverPort;
    private final int clientPort;
    private boolean running = true;

    protected GameRemote(int serverPort, int clientPort) throws IOException{
        this.serverPort = serverPort;
        this.clientPort = clientPort;
        this.startServer();
    }

    protected String getServerIp() throws IOException {
        return NetworkUtils.getIPAddress(true);
    }

    private void startServer() throws IOException {

        final ThreadReturn<Void> threadReturn = new ThreadReturn<>();
        threadReturn.startWait();
        new Thread(new Runnable() {
            @Override
            public void run() {

                final ServerSocket listener;
                try {
                    listener = new ServerSocket(GameRemote.this.serverPort);
                } catch (IOException exception) {
                    Log.e(TAG, "Error creating server socket", exception);
                    threadReturn.nowThrow(exception);
                    return;
                }
                Log.d(TAG, "Game server is running now.");
                threadReturn.nowReturn();
                while (running) {
                    try {
                        handleAccept(listener.accept());
                    } catch (IOException exception) {
                        Log.e(TAG, "Error accepting client", exception);
                    }
                }
                try {
                    listener.close();
                } catch (IOException exception) {
                    Log.e(TAG, "Error closing server", exception);
                }
            }
        }).start();

        try {
            threadReturn.waitTillReturn();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private void handleAccept(final Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    String inStr = "";
                    String line;
                    while ((line = in.readLine()) != null) {
                        inStr += line;
                    }
                    String answer = handle(inStr, socket.getInetAddress().getHostAddress());
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(answer);
                } catch (IOException exception) {
                    Log.e(TAG, "Error during client handling", exception);
                } finally {
                    try {
                        socket.close();
                    } catch (IOException exception) {
                        Log.e(TAG, "Error during closing client", exception);
                    }
                }

            }
        }).start();
    }

    protected void send(final String ip, final Object object) throws GameException {
        _send(ip,object,false);
    }

    protected void cast(final String ip, final Object object) {
        try {
            _send(ip, object, true);
        } catch (GameException e) {
            e.printStackTrace();
        }
    }

    private void _send(final String ip, final Object object, boolean async) throws GameException {
        final ThreadReturn<Void> threadReturn = new ThreadReturn<>();
        if(!async) {
            threadReturn.startWait();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Socket socket = new Socket(ip, clientPort);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    String serializedString = ObjectSerializer.serialize(object);
                    out.write(serializedString);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    String inStr = "";
                    String line;
                    while ((line = in.readLine()) != null) {
                        inStr += line;
                    }
                    socket.close();
                    Answer answer = ObjectSerializer.deserialize(inStr);

                    if(answer.getType() == Answer.Type.SUCCESS) {
                        threadReturn.nowReturn();
                    } else if(answer.getType() == Answer.Type.FAILURE) {
                        threadReturn.nowThrow(new GameException(answer.getMessage()));
                    }

                } catch(IOException | ClassNotFoundException exception) {
                    Log.e(TAG, "Something went wrong in communication...");
                    Log.w(TAG, "Still not stable!");
                    threadReturn.nowThrow(exception);
                }
            }
        }).start();
        try {
            threadReturn.waitTillReturn();
        } catch (Exception e) {
            throw new GameException(e);
        }
    }

    protected abstract String handle(String input, String ip);
}
