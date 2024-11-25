package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ChatBotUI extends JFrame {
    private JTextArea chatArea;
    private JTextField userInputField;
    private JButton sendButton;
    private DialogflowService dialogflowService;

    public ChatBotUI() {
        try {
            dialogflowService = new DialogflowService("softwareengineer-bt9y", "C:\\Users\\J HARSHITH KUMAR\\IdeaProjects\\AI-chatbot\\src\\main\\java\\org\\example\\service=file.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle("AI Chatbot");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        userInputField = new JTextField();
        inputPanel.add(userInputField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userText = userInputField.getText();
                if (!userText.isEmpty()) {
                    chatArea.append("You: " + userText + "\n");
                    new Thread(() -> {
                        try {
                            String botResponse = dialogflowService.getBotResponse(userText);
                            SwingUtilities.invokeLater(() -> chatArea.append("Bot: " + botResponse + "\n"));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                    userInputField.setText("");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChatBotUI().setVisible(true);
            }
        });
    }
}
