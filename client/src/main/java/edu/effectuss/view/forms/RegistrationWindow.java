package edu.effectuss.view.forms;

import edu.effectuss.view.listeners.RegistrationEventListeners;
import edu.effectuss.view.listeners.ShutdownEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegistrationWindow extends JFrame {
    private static final String PROMPT_MESSAGE = "Please enter your nickname";
    private final GridBagLayout mainLayout;
    private final Container contentPane;
    private JLabel informationLabel;
    private JTextField nicknameField;
    private final transient RegistrationEventListeners registrationEventListeners;
    private final transient ShutdownEventListener shutdownEventListener;

    public RegistrationWindow(RegistrationEventListeners registrationEventListeners,
                              ShutdownEventListener shutdownEventListener) {
        super("Registration");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.registrationEventListeners = registrationEventListeners;
        this.shutdownEventListener = shutdownEventListener;

        contentPane = getContentPane();
        mainLayout = new GridBagLayout();
        contentPane.setLayout(mainLayout);
        setSize(300, 150);
        setResizable(false);
        setLocationRelativeTo(null);

        addRegisterButton();
        addNicknameField();
        addInformationLabel();
        informationLabel.setText(PROMPT_MESSAGE);
        setExitOnClose();
    }

    private void addRegisterButton() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_END;

        JButton registerButton = new JButton("Register");

        registerButton.addActionListener(e -> {
                    if (!nicknameField.getText().isEmpty()) {
                        registrationEventListeners.onRegisterClick(nicknameField.getText());
                    }
                }
        );

        mainLayout.setConstraints(registerButton, gbc);
        contentPane.add(registerButton);
    }

    private void addNicknameField() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;

        JLabel nicknameLabel = new JLabel("Nickname:");
        contentPane.add(nicknameLabel, gbc);

        gbc.gridy = 2;
        gbc.ipady = 5;
        nicknameField = new JTextField();

        mainLayout.setConstraints(nicknameField, gbc);
        contentPane.add(nicknameField);
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