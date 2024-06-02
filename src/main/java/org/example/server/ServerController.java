package org.example.server;

import org.example.client.ClientController;

import java.util.ArrayList;
import java.util.List;

public class ServerController {
    private boolean work;
    private ServerView serverView;
    private List<ClientController> clientControllerList;
    private ServerRepository<String> repository;

    public ServerController(ServerView serverView, ServerRepository<String> repository) {
        this.serverView = serverView;
        this.repository = repository;
        clientControllerList = new ArrayList<>();
        serverView.setServerController(this);
    }

    public void start(){
        if (work){
            showOnWindow("Сервер уже был запущен");
        } else {
            work = true;
            showOnWindow("Сервер запущен!");
        }
    }

    public void stop(){
        if (!work){
            showOnWindow("Сервер уже был остановлен");
        } else {
            work = false;
            while (!clientControllerList.isEmpty()){
                disconnectUser(clientControllerList.get(clientControllerList.size() - 1));
            }
            showOnWindow("Сервер остановлен!");
        }
    }

    public void disconnectUser(ClientController clientController){
        clientControllerList.remove(clientController);
        if (clientController != null){
            clientController.disconnectFromServer();
            showOnWindow(clientController.getName() + " отключился от беседы");
        }
    }

    public boolean connectUser(ClientController clientController){
        if (!work){
            return false;
        }
        clientControllerList.add(clientController);
        showOnWindow(clientController.getName() + " подключился к беседе");
        return true;
    }

    public void message(String text){
        if (!work){
            return;
        }
        showOnWindow(text);
        answerAll(text);
        saveInHistory(text);
    }

    public String getHistory() {
        return repository.load();
    }

    private void answerAll(String text){
        for (ClientController clientController : clientControllerList){
            clientController.answerFromServer(text);
        }
    }

    private void showOnWindow(String text){
        serverView.showMessage(text + "\n");
    }

    private void saveInHistory(String text){
        repository.save(text);
    }

    //    public static final int WIDTH = 400;
//    public static final int HEIGHT = 300;
//    public static final String LOG_PATH = "src/server/log.txt";
//
//    List<ClientGUI> clientGUIList;
//
//    JButton btnStart, btnStop;
//    JTextArea log;
//    boolean work;
//
//    public ServerWindow(){
//        clientGUIList = new ArrayList<>();
//
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setSize(WIDTH, HEIGHT);
//        setResizable(false);
//        setTitle("Chat server");
//        setLocationRelativeTo(null);
//
//        createPanel();
//
//        setVisible(true);
//    }
//
//    public boolean connectUser(ClientGUI clientGUI){
//        if (!work){
//            return false;
//        }
//        clientGUIList.add(clientGUI);
//        return true;
//    }
//
//    public String getLog() {
//        return readLog();
//    }
//
//    public void disconnectUser(ClientGUI clientGUI){
//        clientGUIList.remove(clientGUI);
//        if (clientGUI != null){
//            clientGUI.disconnectedFromServer();
//        }
//    }
//
//    public void message(String text){
//        if (!work){
//            return;
//        }
//        appendLog(text);
//        answerAll(text);
//        saveInLog(text);
//    }
//
//    private void answerAll(String text){
//        for (ClientGUI clientGUI: clientGUIList){
//            clientGUI.answer(text);
//        }
//    }
//
//    private void saveInLog(String text){
//        try (FileWriter writer = new FileWriter(LOG_PATH, true)){
//            writer.write(text);
//            writer.write("\n");
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private String readLog(){
//        StringBuilder stringBuilder = new StringBuilder();
//        try (FileReader reader = new FileReader(LOG_PATH)){
//            int c;
//            while ((c = reader.read()) != -1){
//                stringBuilder.append((char) c);
//            }
//            stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
//            return stringBuilder.toString();
//        } catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private void appendLog(String text){
//        log.append(text + "\n");
//    }
//
//    private void createPanel() {
//        log = new JTextArea();
//        add(log);
//        add(createButtons(), BorderLayout.SOUTH);
//    }
//
//    private Component createButtons() {
//        JPanel panel = new JPanel(new GridLayout(1, 2));
//        btnStart = new JButton("Start");
//        btnStop = new JButton("Stop");
//
//        btnStart.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (work){
//                    appendLog("Сервер уже был запущен");
//                } else {
//                    work = true;
//                    appendLog("Сервер запущен!");
//                }
//            }
//        });
//
//        btnStop.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (!work){
//                    appendLog("Сервер уже был остановлен");
//                } else {
//                    work = false;
//                    while (!clientGUIList.isEmpty()){
//                        disconnectUser(clientGUIList.get(clientGUIList.size()-1));
//                    }
//                    appendLog("Сервер остановлен!");
//                }
//            }
//        });
//
//        panel.add(btnStart);
//        panel.add(btnStop);
//        return panel;
//    }
}
