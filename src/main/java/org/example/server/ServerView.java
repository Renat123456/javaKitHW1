package org.example.server;

public interface ServerView {
    void showMessage(String message);
    void setServerController(ServerController serverController);
}
