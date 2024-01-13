package edu.effectuss.view.forms;

import edu.effectuss.view.listeners.MessageEventListener;
import edu.effectuss.view.listeners.ShutdownEventListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatWindow extends JFrame {
    private final GridBagLayout mainLayout;
    private final Container contentPane;
    private JLabel userNicknameLabel;
    private JTextArea messagePanel;
    private JTextArea inputPanel;
    private DefaultListModel<String> userListModel;
    private final transient MessageEventListener messageEventListener;
    private final transient ShutdownEventListener shutdownEventListener;

    public ChatWindow(MessageEventListener messageEventListener,
                      ShutdownEventListener shutdownEventListener) {
        super("Chat room");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.messageEventListener = messageEventListener;
        this.shutdownEventListener = shutdownEventListener;

        contentPane = getContentPane();
        mainLayout = new GridBagLayout();
        contentPane.setLayout(mainLayout);
        setSize(900, 500);
        setResizable(false);
        setLocationRelativeTo(null);

        addUsernameLabel();
        addMessagePanel();
        addParticipantsPanel();
        addInputPanel();
        addSendButton();
        setExitOnClose();
    }

    public synchronized void changeUserNickname(String nickname) {
        userNicknameLabel.setText("You nickname: " + nickname);
    }

    public synchronized void addMessage(String message) {
        messagePanel.append(message + System.lineSeparator());
    }

    public synchronized void activeUsersInitialized(List<String> activeUsers) {
        activeUsers.forEach(activeUser -> userListModel.addElement(activeUser));
    }

    public synchronized void activeUserDeleted(String userNickname) {
        if (userListModel.contains(userNickname)) {
            userListModel.removeElement(userNickname);
        }
    }

    public synchronized void activeUserAdded(String userNickname) {
        if (!userListModel.contains(userNickname)) {
            userListModel.addElement(userNickname);
        }
    }

    private void addUsernameLabel() {
        userNicknameLabel = new JLabel();
        GridBagConstraints usernameLabelConstraints = new GridBagConstraints();
        usernameLabelConstraints.gridx = 0;
        usernameLabelConstraints.gridy = 0;
        usernameLabelConstraints.gridwidth = 2;
        usernameLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        usernameLabelConstraints.anchor = GridBagConstraints.NORTH;
        mainLayout.setConstraints(userNicknameLabel, usernameLabelConstraints);
        contentPane.add(userNicknameLabel);
    }

    private void addMessagePanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        messagePanel = new JTextArea();
        messagePanel.setEditable(false);
        messagePanel.setLineWrap(true);
        messagePanel.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messagePanel);

        TitledBorder titledBorder = BorderFactory.createTitledBorder("Messages");
        titledBorder.setTitleJustification(TitledBorder.CENTER);

        scrollPane.setBorder(titledBorder);

        mainLayout.setConstraints(scrollPane, gbc);
        contentPane.add(scrollPane);
    }

    private void addParticipantsPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        userListModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userListModel);
        JScrollPane scrollPane = new JScrollPane(userList);

        TitledBorder titledBorder = BorderFactory.createTitledBorder("Active Users");
        titledBorder.setTitleJustification(TitledBorder.CENTER);

        scrollPane.setBorder(titledBorder);

        scrollPane.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
        scrollPane.setPreferredSize(new Dimension(200, 0));
        mainLayout.setConstraints(scrollPane, gbc);
        contentPane.add(scrollPane);
    }

    private void addInputPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.PAGE_END;

        inputPanel = new JTextArea();
        inputPanel.setLineWrap(true);
        inputPanel.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(inputPanel);

        mainLayout.setConstraints(scrollPane, gbc);
        contentPane.add(scrollPane);
    }

    private void addSendButton() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.PAGE_START;

        JButton sendButton = new JButton("Send");
        sendButton.setSize(new Dimension(50, 50));

        sendButton.addActionListener(event -> {
                    if (!inputPanel.getText().isEmpty()) {
                        messageEventListener.onMessageSendClick(inputPanel.getText());
                    }
                    if(!inputPanel.getText().isEmpty() && inputPanel.getText().length() < 1024) {
                        inputPanel.setText("");
                    }
                }
        );

        mainLayout.setConstraints(sendButton, gbc);
        contentPane.add(sendButton);
    }

    private void setExitOnClose() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                shutdownEventListener.onShutdown();
                dispose();
            }
        });
    }
}