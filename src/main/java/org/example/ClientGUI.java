package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.ServerSocket;

public class ClientGUI extends JFrame{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    InetAddress ip;
    int port;
    private ServerWindow serverWindow;
    private String login;
    private String pass;
    private JButton btnSend;
    private JButton btnLogin;
    private JTextField messageField;
    private JTextField loginField;
    private JPasswordField passField;
    private JTextField ipField;
    private JTextField portField;
    private JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private JPanel panelBottom;
    private JPanel panelCenter = new JPanel(new FlowLayout(FlowLayout.LEFT));;
    private boolean userConnection = false;
    JScrollPane scrollPane = new JScrollPane();

    public ClientGUI(ServerWindow serverWindow){
        this.serverWindow = serverWindow;

        setSize(WIDTH, HEIGHT); // установка размеров окна
        setLocationRelativeTo(null); // окно выводится по центру экрана
        setTitle("Client Window"); // название окна
        setResizable(false); // запрет на изменение размера экрана

        addPanelTop();

        panelBottom = new JPanel(new GridLayout(1, 2));
        btnSend = new JButton("Send");
        messageField = new JTextField(20);
        panelBottom.add(messageField);
        panelBottom.add(btnSend);
        add(panelBottom, BorderLayout.SOUTH);

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                if(!message.equals("") && userConnection && serverWindow.serverIsOn){
                    serverWindow.writeToFile(login + ": " + message);
                    updatePanelCenter();
                }
            }
        });

        setVisible(true);
    }

    private void getInfoIpPort() {
        try {
            // Получение IP-адреса
            ip = InetAddress.getLocalHost();
            ipField.setText(ip.getHostAddress());

            // Получение свободного порта
            try (ServerSocket socket = new ServerSocket(0)) {
                int port = socket.getLocalPort();
                portField.setText(Integer.toString(port));
            }
        } catch (Exception ex) {
            // Обработка возможных исключений
        }
    }

    private void connection(){
        if(serverWindow.users.contains(login)){
            userConnection = true;
            remove(panelTop);
            updatePanelCenter();
        }
    }

    public void disConnection(){
        if(serverWindow.users.contains(login)){
            userConnection = false;
            remove(panelCenter);
            addPanelTop();
            revalidate();
            repaint();
        }
    }

    public void updatePanelCenter(){
        if(serverWindow.users.contains(login)){
            remove(scrollPane);

            panelCenter = new JPanel(new FlowLayout(FlowLayout.LEFT));
            String messages = "<html>Сервер подключен<br><br>" + serverWindow.ReadFromFile() + "</html>"; // чтение переписки из файла;
            JLabel jLabel = new JLabel(messages);
            panelCenter.add(jLabel);
            scrollPane = new JScrollPane(panelCenter);

            add(scrollPane, BorderLayout.CENTER);

            // Обновление внешнего вида окна
            revalidate();
            repaint();
        }
    }

    public void addPanelTop(){
            remove(panelCenter);
            remove(panelTop);

            panelTop = new JPanel(new GridLayout(2, 3));

            btnLogin = new JButton("Login");
            loginField = new JTextField(20);
            passField = new JPasswordField(20);
            ipField = new JTextField(20);
            portField = new JTextField(20);
            getInfoIpPort();
            panelTop.add(ipField);
            panelTop.add(portField);
            panelTop.add(new JPanel());
            panelTop.add(loginField);
            panelTop.add(passField);
            panelTop.add(btnLogin);
            add(panelTop, BorderLayout.NORTH);

            btnLogin.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    login = loginField.getText();
                    pass = passField.getText();
                    serverWindow.connection(login, pass, ClientGUI.this);
                    connection();
                }
            });
    }
}
