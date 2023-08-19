package main.java;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OptionMessage {
    JFrame frame;
    JPanel panel;
    JLabel windowMessage;
    JButton opt1;
    JButton opt2;
    boolean result;

    public OptionMessage(String windowText, String optOneText, String optTwoText, int frameWidth, int frameHeight) {
        frame = new JFrame();
        panel = new JPanel();

        frame.setResizable(false);
        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Youtube Music Downloader");

        panel.setLayout(null);
        frame.add(panel);

        windowMessage = new JLabel(windowText);
        opt1 = new JButton(optOneText);
        opt2 = new JButton(optTwoText);

        windowMessage.setBounds(0, 0, frameWidth, frameHeight/2);
        windowMessage.setHorizontalAlignment(JLabel.CENTER);
        windowMessage.setVerticalAlignment(JLabel.CENTER);
        panel.add(windowMessage);

        opt1.setBounds(frameWidth/5, frame.getHeight() - frameHeight/5*3, frameWidth/15*4, frameHeight/7);
        panel.add(opt1);

        opt2.setBounds(opt1.getX() + opt1.getWidth() + frameWidth/15, opt1.getY(), opt1.getWidth(), opt1.getHeight());
        panel.add(opt2);

        frame.setVisible(true);
    }
}
