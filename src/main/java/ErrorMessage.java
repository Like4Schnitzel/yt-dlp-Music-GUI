package main.java;
import javax.swing.*;

public class ErrorMessage {
    JFrame frame;
    JPanel panel;
    JLabel errorMessage;
    JButton close;

    private void createErrorMessage(String errorText, int frameWidth, int frameHeight) {
        frame = new JFrame();
        panel = new JPanel();

        frame.setResizable(false);
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Youtube Music Downloader");

        panel.setLayout(null);
        frame.add(panel);

        errorMessage = new JLabel(errorText);
        close = new JButton("Close");

        errorMessage.setBounds(0, 20, frameWidth, 20);
        errorMessage.setHorizontalAlignment(JLabel.CENTER);
        errorMessage.setVerticalAlignment(JLabel.CENTER);
        panel.add(errorMessage);

        close.setBounds(110, frame.getHeight() - 70, 80, 30);
        close.addActionListener(e -> frame.dispose());
        panel.add(close);

        frame.setVisible(true);
    }

    public ErrorMessage(String errorText) {
        createErrorMessage(errorText, 300, 140);
    }

    public ErrorMessage(String errorText, int frameWidth, int frameHeight) {
        createErrorMessage(errorText, frameWidth, frameHeight);
    }
}
