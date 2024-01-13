package edu.effectuss.view.forms;

import javax.swing.*;
import java.awt.*;

public class InformationWindow extends JDialog {
    private final JTextArea informationLabel;

    public InformationWindow(JFrame frame) {
        super(frame, "Information", true);

        informationLabel = new JTextArea();
        informationLabel.setLineWrap(true);
        informationLabel.setWrapStyleWord(true);
        informationLabel.setEditable(false);
        informationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        informationLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JButton okButton = new JButton("Ok");

        setLayout(new BorderLayout());

        add(new JScrollPane(informationLabel), BorderLayout.CENTER);
        add(okButton, BorderLayout.SOUTH);

        okButton.addActionListener(e -> dispose());

        setSize(300, 200);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void showWindow(String message) {
        informationLabel.setText(message);
        setVisible(true);
    }
}