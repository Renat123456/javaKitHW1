package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class ServerWindow extends JFrame{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private JButton btnStart, btnStop;
    private JPanel panelWindow = new JPanel();
    private JScrollPane scrollPane = new JScrollPane();
    boolean serverIsOn = false;
    private String filePath = "messages.txt"; // Путь к файлу
    private File file = new File(filePath);
    private JLabel jLabelMessages = new JLabel();

    ArrayList<String> users = new ArrayList<>();
    ArrayList<ClientGUI> clientGUIS = new ArrayList<>();

    ServerWindow(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // программа заканчивается при закрытии этого окна
        setSize(WIDTH, HEIGHT); // установка размеров окна
        setLocationRelativeTo(null); // окно выводится по центру экрана
        setTitle("Server Window"); // название окна
        setResizable(false); // запрет на изменение размера экрана

        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");

        //Запускаем сервер
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        //Останавливаем сервер
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });

        JPanel panelBottom = new JPanel(new GridLayout(1, 2));
        panelBottom.add(btnStart);
        panelBottom.add(btnStop);
        add(panelBottom, BorderLayout.SOUTH);

        panelWindow = new JPanel(new FlowLayout(FlowLayout.LEFT)); // компоновка для левого выравнивания текста
        scrollPane.add(panelWindow);
        add(scrollPane);

        setVisible(true);

        try {
            // Попытка создать новый файл для записи сообщений, если его еще нет
            file.createNewFile();
        } catch (IOException e) {
            // Обработка возможных исключений
        }
    }

    private void startServer(){
        serverIsOn = true;
        updateMessages();
    };

    private void stopServer(){
        serverIsOn = false;

        panelWindow.removeAll();   // Удаление всех компонентов из панели
        JLabel jLabel = new JLabel("Сервер выключен");
        panelWindow.add(jLabel);
        panelWindow.revalidate();  // Пересчет компонента после изменения его содержимого
        panelWindow.repaint();     // Перерисовка компонента после изменения его содержимого

        for (ClientGUI client : clientGUIS) {
            client.disConnection();
        }

        users.clear();    // Удаление всех подключенных пользователей
        clientGUIS.clear(); // Удаление всех подключенных пользователей
    };

    public String ReadFromFile(){
        String lineFinal = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineFinal += line  + "<br>"; // чтение файла построчно
            }
        } catch (IOException e) {
            // Обработка возможных исключений
        }
        return lineFinal;
    };

    public void writeToFile(String s){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(s); // Запись текста в файл
            writer.newLine(); // Перевод строки
        } catch (IOException e) {
            // Обработка возможных исключений
        }

        for (ClientGUI client : clientGUIS) {
            client.updatePanelCenter();
        }
        updateMessages();
    };

    private void updateMessages(){
        remove(scrollPane);
        remove(panelWindow);
        panelWindow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String messages = "<html>Сервер запущен<br><br>" + ReadFromFile() + "</html>"; // чтение переписки из файла
        jLabelMessages.setText(messages);
        panelWindow.add(jLabelMessages); // добавление переписки в окно вывода
        scrollPane = new JScrollPane(panelWindow);
        add(scrollPane);
        revalidate();
        repaint();
    };

    void connection(String login, String pass, ClientGUI clientGUI){
        if(serverIsOn && !login.equals("") && !pass.equals("")){
            users.add(login);
            clientGUIS.add(clientGUI);
        }
    }
}
