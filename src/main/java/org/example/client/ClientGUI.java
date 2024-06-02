package org.example.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame implements ClientView{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private JTextArea log;
    private JTextField tfIPAddress, tfPort, tfLogin, tfMessage;
    private JPasswordField password;
    private JButton btnLogin, btnSend;
    private JPanel headerPanel;

    /**
     * Контроллер, описывающий реакцию на различные события.
     * Когда что-то происходит, например нажата какая-то кнопка на экране, то обращаемся
     * к контроллеру и вызываем нужный метод
     */
    private ClientController clientController;

    public ClientGUI() {
        setting();
        createPanel();

        setVisible(true);
    }

    /**
     * Сеттер
     * @param clientController объект контроллера, описывающий логику поведения
     */
    @Override
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * Настройка основных параметров GUI
     */
    private void setting() {
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat client");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    /**
     * Метод вывода текста на экран GUI. Вызывается из контроллера
     * @param msg текст, который требуется отобразить на экране
     */
    @Override
    public void showMessage(String msg) {
        log.append(msg);
    }

    /**
     * Метод, описывающий отключение клиента от сервера со стороны сервера
     */
    @Override
    public void disconnectedFromServer(){
        hideHeaderPanel(true);
    }

    /**
     * Метод, описывающий отключение клиента от сервера со стороны клиента
     */
    public void disconnectFromServer(){
        clientController.disconnectFromServer();
    }

    /**
     * Метод изменения видимости верхней панели экрана, на которой виджеты для авторизации (например кнопка логин)
     * @param visible true, если надо сделать панель видимой
     */
    public void hideHeaderPanel(boolean visible){
        headerPanel.setVisible(visible);
    }

    /**
     * Метод, срабатывающий при нажатии кнопки авторизации
     */
    public void login(){
        if (clientController.connectToServer(tfLogin.getText())){
            headerPanel.setVisible(false);
        }
    }

    /**
     * Метод для отправки сообщения. Используется при нажатии на кнопку send
     */
    private void message(){
        clientController.message(tfMessage.getText());
        tfMessage.setText("");
    }

    /**
     * Метод добавления виджетов на экран
     */
    private void createPanel() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLog());
        add(createFooter(), BorderLayout.SOUTH);
    }

    /**
     * Метод создания панели авторизации
     * @return возвращает созданную панель
     */
    private Component createHeaderPanel() {
        headerPanel = new JPanel(new GridLayout(2, 3));
        tfIPAddress = new JTextField("127.0.0.1");
        tfPort = new JTextField("8189");
        tfLogin = new JTextField("Ivan Ivanovich");
        password = new JPasswordField("123456");
        btnLogin = new JButton("login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        headerPanel.add(tfIPAddress);
        headerPanel.add(tfPort);
        headerPanel.add(new JPanel());
        headerPanel.add(tfLogin);
        headerPanel.add(password);
        headerPanel.add(btnLogin);

        return headerPanel;
    }

    /**
     * Метод создания центральной панели, на которой отображается история сообщений
     * @return возвращает созданную панель
     */
    private Component createLog() {
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }

    /**
     * Метод создания панели отправки сообщений
     * @return возвращает созданную панель
     */
    private Component createFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    message();
                }
            }
        });
        btnSend = new JButton("send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message();
            }
        });
        panel.add(tfMessage);
        panel.add(btnSend, BorderLayout.EAST);
        return panel;
    }

    /**
     * Метод срабатывающий при важных событиях связанных с графическим окном (например окно в фокусе)
     * @param e  the window event
     */
    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
            disconnectFromServer();
        }
    }

//    @Override
//    protected void processWindowEvent(WindowEvent e) {
//        super.processWindowEvent(e);
//        if (e.getID() == WindowEvent.WINDOW_CLOSING){
//            this.disconnectedFromServer();
//        }
//    }

//    private static final int WIDTH = 400;
//    private static final int HEIGHT = 400;
//    InetAddress ip;
//    int port;
//    private ServerWindow serverWindow;
//    private String login;
//    private String pass;
//    private JButton btnSend;
//    private JButton btnLogin;
//    private JTextField messageField;
//    private JTextField loginField;
//    private JPasswordField passField;
//    private JTextField ipField;
//    private JTextField portField;
//    private JPanel panelTop = new JPanel(new GridLayout(2, 3));
//    private JPanel panelBottom;
//    private JPanel panelCenter = new JPanel(new FlowLayout(FlowLayout.LEFT));;
//    private boolean userConnection = false;
//    JScrollPane scrollPane = new JScrollPane();
//
//    public ClientGUI(ServerWindow serverWindow){
//        this.serverWindow = serverWindow;
//
//        setSize(WIDTH, HEIGHT); // установка размеров окна
//        setLocationRelativeTo(null); // окно выводится по центру экрана
//        setTitle("Client Window"); // название окна
//        setResizable(false); // запрет на изменение размера экрана
//
//        addPanelTop();
//
//        panelBottom = new JPanel(new GridLayout(1, 2));
//        btnSend = new JButton("Send");
//        messageField = new JTextField(20);
//        panelBottom.add(messageField);
//        panelBottom.add(btnSend);
//        add(panelBottom, BorderLayout.SOUTH);
//
//        btnSend.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String message = messageField.getText();
//                if(!message.equals("") && userConnection && serverWindow.serverIsOn){
//                    serverWindow.writeToFile(login + ": " + message);
//                    updatePanelCenter();
//                }
//            }
//        });
//
//        setVisible(true);
//    }
//
//    private void getInfoIpPort() {
//        try {
//            // Получение IP-адреса
//            ip = InetAddress.getLocalHost();
//            ipField.setText(ip.getHostAddress());
//
//            // Получение свободного порта
//            try (ServerSocket socket = new ServerSocket(0)) {
//                int port = socket.getLocalPort();
//                portField.setText(Integer.toString(port));
//            }
//        } catch (Exception ex) {
//            // Обработка возможных исключений
//        }
//    }
//
//    private void connection(){
//        if(serverWindow.users.contains(login)){
//            userConnection = true;
//            remove(panelTop);
//            updatePanelCenter();
//        }
//    }
//
//    public void disConnection(){
//        if(serverWindow.users.contains(login)){
//            userConnection = false;
//            remove(panelCenter);
//            addPanelTop();
//            revalidate();
//            repaint();
//        }
//    }
//
//    public void updatePanelCenter(){
//        if(serverWindow.users.contains(login)){
//            remove(scrollPane);
//
//            panelCenter = new JPanel(new FlowLayout(FlowLayout.LEFT));
//            String messages = "<html>Сервер подключен<br><br>" + serverWindow.ReadFromFile() + "</html>"; // чтение переписки из файла;
//            JLabel jLabel = new JLabel(messages);
//            panelCenter.add(jLabel);
//            scrollPane = new JScrollPane(panelCenter);
//
//            add(scrollPane, BorderLayout.CENTER);
//
//            // Обновление внешнего вида окна
//            revalidate();
//            repaint();
//        }
//    }
//
//    public void addPanelTop(){
//            remove(panelCenter);
//            remove(panelTop);
//
//            panelTop = new JPanel(new GridLayout(2, 3));
//
//            btnLogin = new JButton("Login");
//            loginField = new JTextField(20);
//            passField = new JPasswordField(20);
//            ipField = new JTextField(20);
//            portField = new JTextField(20);
//            getInfoIpPort();
//            panelTop.add(ipField);
//            panelTop.add(portField);
//            panelTop.add(new JPanel());
//            panelTop.add(loginField);
//            panelTop.add(passField);
//            panelTop.add(btnLogin);
//            add(panelTop, BorderLayout.NORTH);
//
//            btnLogin.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    login = loginField.getText();
//                    pass = passField.getText();
//                    serverWindow.connection(login, pass, ClientGUI.this);
//                    connection();
//                }
//            });
//    }
}
