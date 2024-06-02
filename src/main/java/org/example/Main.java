package org.example;

import org.example.client.ClientController;
import org.example.client.ClientGUI;
import org.example.server.*;

public class Main {
    public static void main(String[] args) {

        ServerController serverController = new ServerController(new ServerGUI(), new ServerFileRepository());

        new ClientController(new ClientGUI(), serverController);
        new ClientController(new ClientGUI(), serverController);


//        ServerWindow serverWindow = new ServerWindow();
//        new ClientGUI(serverWindow);
//        new ClientGUI(serverWindow);
    }
}