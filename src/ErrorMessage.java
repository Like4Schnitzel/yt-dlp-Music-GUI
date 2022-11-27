import javax.swing.*;

public class ErrorMessage {
    JFrame frame;
    JPanel panel;
    JLabel errorMessage;
    JButton close;

    public ErrorMessage(String errorText) {
        frame = new JFrame();
        panel = new JPanel();

        frame.setResizable(false);
        frame.setSize(300, 120);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Youtube Music Downloader");

        panel.setLayout(null);
        frame.add(panel);

        errorMessage = new JLabel(errorText);
        close = new JButton("Close");

        errorMessage.setBounds(0, 20, 300, 20);
        errorMessage.setHorizontalAlignment(JLabel.CENTER);
        panel.add(errorMessage);

        close.setBounds(110, 50, 80, 30);
        close.addActionListener(e -> frame.dispose());
        panel.add(close);

        frame.setVisible(true);
    }
}
