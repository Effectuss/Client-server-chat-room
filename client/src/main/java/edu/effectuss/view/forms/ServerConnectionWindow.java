package edu.effectuss.view.forms;

import edu.effectuss.view.listeners.ServerConnectionEventListener;

import javax.swing.*;
import java.awt.*;

public class ServerConnectionWindow extends JFrame {
    private static final String PROMPT_MESSAGE = "Please enter the host and port";
    private final GridBagLayout mainLayout;
    private final Container contentPane;
    private JTextField hostField;
    private JTextField portField;
    private JLabel informationLabel;
    private final transient ServerConnectionEventListener serverConnectionEventListener;

    public ServerConnectionWindow(ServerConnectionEventListener serverConnectionEventListener) {
        setTitle("Server Connection");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.serverConnectionEventListener = serverConnectionEventListener;

        contentPane = getContentPane();
        mainLayout = new GridBagLayout();
        contentPane.setLayout(mainLayout);
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(null);

        addConnectButton();
        addHostField();
        addPortField();
        addInformationLabel();
        informationLabel.setText(PROMPT_MESSAGE);
    }

    private void addHostField() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;

        JLabel hostLabel = new JLabel("Host:");
        contentPane.add(hostLabel, gbc);

        gbc.gridy = 2;
        gbc.ipady = 5;
        hostField = new JTextField();

        mainLayout.setConstraints(hostField, gbc);
        contentPane.add(hostField);
    }

    private void addPortField() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;

        JLabel portLabel = new JLabel("Port:");
        contentPane.add(portLabel, gbc);

        gbc.gridy = 4;
        gbc.ipady = 5;
        portField = new JTextField();

        mainLayout.setConstraints(portField, gbc);
        contentPane.add(portField);
    }

    private void addConnectButton() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_END;

        JButton connectButton = new JButton("Connect");

        connectButton.addActionListener(event -> {
                    if (!hostField.getText().isEmpty() && !portField.getText().isEmpty()) {
                        serverConnectionEventListener.onServerConnectClick(
                                hostField.getText(), portField.getText());
                    }
                }
        );

        mainLayout.setConstraints(connectButton, gbc);
        contentPane.add(connectButton);
    }

    private void addInformationLabel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;

        informationLabel = new JLabel();
        informationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        informationLabel.setVerticalAlignment(SwingConstants.CENTER);
        mainLayout.setConstraints(informationLabel, gbc);
        contentPane.add(informationLabel);
    }
}
