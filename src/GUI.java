import javax.swing.*;
import java.io.File;
import java.time.LocalTime;

public class GUI {
    JFrame frame;
    JPanel panel;
    JLabel linkLabel;
    JLabel artistLabel;
    JLabel albumLabel;
    JLabel titleLabel;
    JLabel playlistLabel;
    JLabel playlistTo;
    JRadioButton defaultArtist;
    JRadioButton customArtist;
    ButtonGroup selectArtist;
    JRadioButton defaultAlbum;
    JRadioButton singleAlbum;
    JRadioButton customAlbum;
    ButtonGroup selectAlbum;
    JRadioButton defaultTitle;
    JRadioButton customTitle;
    ButtonGroup selectTitle;
    JRadioButton yesPlaylist;
    JRadioButton noPlaylist;
    JRadioButton customPlaylist;
    ButtonGroup downloadPlaylist;
    JButton download;
    JProgressBar downloadProgress;
    JTextField linkText;
    JTextField artistText;
    JTextField albumText;
    JTextField titleText;
    JTextField playlistStart;
    JTextField playlistEnd;
    JFileChooser outputDirectoryChooser;
    JButton outputDirectoryChooserButton;
    JTextField outputDirectoryTextField;

    public void disableAll() {
        artistLabel.setEnabled(false);
        artistText.setEnabled(false);
        defaultArtist.setEnabled(false);
        customArtist.setEnabled(false);

        albumLabel.setEnabled(false);
        albumText.setEnabled(false);
        defaultAlbum.setEnabled(false);
        singleAlbum.setEnabled(false);
        customAlbum.setEnabled(false);

        titleLabel.setEnabled(false);
        titleText.setEnabled(false);
        defaultTitle.setEnabled(false);
        customTitle.setEnabled(false);

        playlistLabel.setEnabled(false);
        yesPlaylist.setEnabled(false);
        noPlaylist.setEnabled(false);
        customPlaylist.setEnabled(false);
        playlistStart.setEnabled(false);
        playlistTo.setEnabled(false);
        playlistEnd.setEnabled(false);

        download.setEnabled(false);
    }

    public GUI(int frameWidth, int frameHeight, int leftBound, int upperBound, int lineDistance) {
        frame = new JFrame();
        panel = new JPanel();
        frame.setResizable(false);

        linkLabel = new JLabel("Link: ");
        artistLabel = new JLabel("Artist name: ");
        albumLabel = new JLabel("Album name: ");
        titleLabel = new JLabel("Title name: ");
        playlistLabel = new JLabel("Download playlist?");
        playlistTo = new JLabel(" to");
        linkText = new JTextField();
        artistText = new JTextField();
        albumText = new JTextField();
        titleText = new JTextField();
        playlistStart = new JTextField();
        playlistEnd = new JTextField();
        defaultArtist = new JRadioButton("detect");
        customArtist = new JRadioButton();
        selectArtist = new ButtonGroup();
        defaultAlbum = new JRadioButton("detect");
        singleAlbum = new JRadioButton("Single");
        customAlbum = new JRadioButton();
        selectAlbum = new ButtonGroup();
        defaultTitle = new JRadioButton("detect");
        customTitle = new JRadioButton("detect");
        selectTitle = new ButtonGroup();
        yesPlaylist = new JRadioButton("Yes");
        noPlaylist = new JRadioButton("No");
        customPlaylist = new JRadioButton("From index");
        downloadPlaylist = new ButtonGroup();
        download = new JButton("Download");
        downloadProgress = new JProgressBar();
        outputDirectoryChooser = new JFileChooser();
        outputDirectoryTextField = new JTextField();
        outputDirectoryChooserButton = new JButton("...");

        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Youtube Music Downloader");
        frame.add(panel);

        panel.setLayout(null);

        linkLabel.setBounds(leftBound, upperBound, 50, lineDistance);
        panel.add(linkLabel);

        linkText.setBounds(linkLabel.getX() + linkLabel.getWidth(), upperBound, frameWidth - leftBound - linkLabel.getX() - linkLabel.getWidth(), lineDistance);
        panel.add(linkText);

        artistLabel.setBounds(leftBound, upperBound + lineDistance, 100,  lineDistance);
        artistLabel.setEnabled(false);
        panel.add(artistLabel);

        defaultArtist.setBounds(leftBound + artistLabel.getWidth(), artistLabel.getY(), 80, lineDistance);
        defaultArtist.setSelected(true);
        defaultArtist.setEnabled(false);

        customArtist.setBounds(defaultArtist.getX() + defaultArtist.getWidth(), artistLabel.getY(), 20, lineDistance);
        customArtist.setEnabled(false);

        artistText.setBounds(customArtist.getX() + customArtist.getWidth(), artistLabel.getY(), frameWidth - leftBound - customArtist.getX() - customArtist.getWidth(), lineDistance);
        artistText.setEnabled(false);
        panel.add(artistText);

        selectArtist.add(defaultArtist);
        selectArtist.add(customArtist);
        panel.add(defaultArtist);
        panel.add(customArtist);

        albumLabel.setBounds(leftBound, upperBound + 2*lineDistance, 100, lineDistance);
        albumLabel.setEnabled(false);
        panel.add(albumLabel);

        defaultAlbum.setBounds(albumLabel.getX() + albumLabel.getWidth(), albumLabel.getY(), 80, lineDistance);
        defaultAlbum.setSelected(true);
        defaultAlbum.setEnabled(false);

        singleAlbum.setBounds(defaultAlbum.getX() + defaultAlbum.getWidth(), albumLabel.getY(), 80, lineDistance);
        singleAlbum.setEnabled(false);

        customAlbum.setBounds(singleAlbum.getX() + singleAlbum.getWidth(), albumLabel.getY(), 20, lineDistance);
        customAlbum.setEnabled(false);

        albumText.setBounds(customAlbum.getX() + customAlbum.getWidth(), albumLabel.getY(), frameWidth - leftBound - customAlbum.getX() - customAlbum.getWidth(), lineDistance);
        albumText.setEnabled(false);
        panel.add(albumText);

        selectAlbum.add(defaultAlbum);
        selectAlbum.add(singleAlbum);
        selectAlbum.add(customAlbum);
        panel.add(defaultAlbum);
        panel.add(singleAlbum);
        panel.add(customAlbum);

        titleLabel.setBounds(leftBound, upperBound + 3*lineDistance, 100, lineDistance);
        titleLabel.setEnabled(false);
        panel.add(titleLabel);

        defaultTitle.setBounds(titleLabel.getX() + titleLabel.getWidth(), titleLabel.getY(), 80, lineDistance);
        defaultTitle.setSelected(true);
        defaultTitle.setEnabled(false);

        customTitle.setBounds(defaultTitle.getX() + defaultTitle.getWidth(), titleLabel.getY(), 20, lineDistance);
        customTitle.setEnabled(false);

        titleText.setBounds(customTitle.getX() + customTitle.getWidth(), titleLabel.getY(), frameWidth - leftBound - customTitle.getX() - customTitle.getWidth(), lineDistance);
        titleText.setEnabled(false);

        panel.add(titleText);

        selectTitle.add(defaultTitle);
        selectTitle.add(customTitle);
        panel.add(defaultTitle);
        panel.add(customTitle);

        playlistLabel.setBounds(leftBound, upperBound + 4*lineDistance, 140, lineDistance);
        playlistLabel.setEnabled(false);
        panel.add(playlistLabel);

        yesPlaylist.setBounds(playlistLabel.getX() + playlistLabel.getWidth(), playlistLabel.getY(), 60, lineDistance);
        yesPlaylist.setEnabled(false);

        noPlaylist.setBounds(yesPlaylist.getX() + yesPlaylist.getWidth(), playlistLabel.getY(), 50, lineDistance);
        noPlaylist.setEnabled(false);
        noPlaylist.setSelected(true);

        customPlaylist.setBounds(noPlaylist.getX() + noPlaylist.getWidth(), playlistLabel.getY(), 103, lineDistance);
        customPlaylist.setEnabled(false);

        playlistStart.setBounds(customPlaylist.getX() + customPlaylist.getWidth(), playlistLabel.getY(), 30, lineDistance);
        playlistStart.setEnabled(false);
        playlistStart.setText("NA");
        playlistStart.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(playlistStart);

        playlistTo.setBounds(playlistStart.getX() + playlistStart.getWidth(), playlistLabel.getY(), 23, lineDistance);
        playlistTo.setEnabled(false);
        panel.add(playlistTo);

        playlistEnd.setBounds(playlistTo.getX() + playlistTo.getWidth(), playlistLabel.getY(), 30, lineDistance);
        playlistEnd.setEnabled(false);
        playlistEnd.setText("NA");
        playlistEnd.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(playlistEnd);

        downloadPlaylist.add(yesPlaylist);
        downloadPlaylist.add(noPlaylist);
        downloadPlaylist.add(customPlaylist);
        panel.add(yesPlaylist);
        panel.add(noPlaylist);
        panel.add(customPlaylist);


        outputDirectoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        outputDirectoryTextField.setBounds(frameWidth/2 - 20 - 200/2, frameHeight - 140, 200, 25);
        outputDirectoryTextField.setEnabled(false);
        panel.add(outputDirectoryTextField);

        outputDirectoryChooserButton.setBounds(outputDirectoryTextField.getX() + outputDirectoryTextField.getWidth(), outputDirectoryTextField.getY(), 20, 25);
        panel.add(outputDirectoryChooserButton);

        download.setBounds(frameWidth/2 - 150/2, frameHeight - 100, 150, 25);

        download.setEnabled(false);
        panel.add(download);

        downloadProgress.setBounds(50, download.getY() + download.getHeight() + 10, frameWidth - 2*50, 25);
        downloadProgress.setVisible(false);
        downloadProgress.setStringPainted(true);
        panel.add(downloadProgress);

        frame.setVisible(true);
    }
}
