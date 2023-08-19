package main.java;
import javax.swing.*;

public class GUI {
    JFrame frame;
    JPanel panel;
    JLabel linkLabel;
    JLabel artistLabel;
    JLabel albumLabel;
    JLabel titleLabel;
    JLabel playlistLabel;
    JLabel playlistTo;
    JLabel downloadSectionLabel;
    JLabel downloadTo;
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
    JRadioButton downloadFull;
    JRadioButton downloadChapter;
    JRadioButton downloadTimestamp;
    ButtonGroup downloadSections;
    JButton download;
    JProgressBar downloadProgress;
    JTextField linkText;
    JTextField artistText;
    JTextField albumText;
    JTextField titleText;
    JTextField playlistStart;
    JTextField playlistEnd;
    JTextField downloadChapterField;
    JTextField downloadStartStamp;
    JTextField downloadEndStamp;
    JFileChooser outputDirectoryChooser;
    JButton outputDirectoryChooserButton;
    JTextField outputDirectoryTextField;

    public void setAllEnabled(boolean enabled) {
        linkLabel.setEnabled(enabled);
        linkText.setEnabled(enabled);

        artistLabel.setEnabled(enabled);
        artistText.setEnabled(enabled);
        defaultArtist.setEnabled(enabled);
        customArtist.setEnabled(enabled);

        albumLabel.setEnabled(enabled);
        albumText.setEnabled(enabled);
        defaultAlbum.setEnabled(enabled);
        singleAlbum.setEnabled(enabled);
        customAlbum.setEnabled(enabled);

        titleLabel.setEnabled(enabled);
        titleText.setEnabled(enabled);
        defaultTitle.setEnabled(enabled);
        customTitle.setEnabled(enabled);

        playlistLabel.setEnabled(enabled);
        yesPlaylist.setEnabled(enabled);
        noPlaylist.setEnabled(enabled);
        customPlaylist.setEnabled(enabled);
        playlistStart.setEnabled(enabled);
        playlistTo.setEnabled(enabled);
        playlistEnd.setEnabled(enabled);

        downloadSectionLabel.setEnabled(enabled);
        downloadFull.setEnabled(enabled);
        downloadChapter.setEnabled(enabled);
        downloadTimestamp.setEnabled(enabled);
        downloadChapterField.setEnabled(enabled);
        downloadStartStamp.setEnabled(enabled);
        downloadTo.setEnabled(enabled);
        downloadEndStamp.setEnabled(enabled);

        outputDirectoryChooserButton.setEnabled(enabled);

        download.setEnabled(enabled);
    }

    public GUI(DependencyChecker checker) {
        int extraRightBoundOnWindows = System.getProperty("os.name").toLowerCase().contains("win") ? 20 : 0;
        int frameWidth = Integer.parseInt(checker.configValues.get("frame-width"));
        int frameHeight = Integer.parseInt(checker.configValues.get("frame-height"));
        int leftBound = Integer.parseInt(checker.configValues.get("left-bound"));
        int upperBound = Integer.parseInt(checker.configValues.get("upper-bound"));
        int lineDistance = Integer.parseInt(checker.configValues.get("line-distance"));

        frame = new JFrame();
        panel = new JPanel();
        frame.setResizable(false);

        linkLabel = new JLabel("Link: ");
        artistLabel = new JLabel("Artist name: ");
        albumLabel = new JLabel("Album name: ");
        titleLabel = new JLabel("Title name: ");
        playlistLabel = new JLabel("Download playlist?");
        playlistTo = new JLabel(" to");
        downloadSectionLabel = new JLabel("Download ");
        downloadTo = new JLabel(" to");
        linkText = new JTextField();
        artistText = new JTextField();
        albumText = new JTextField();
        titleText = new JTextField();
        playlistStart = new JTextField();
        playlistEnd = new JTextField();
        downloadChapterField = new JTextField();
        downloadStartStamp = new JTextField();
        downloadEndStamp = new JTextField();
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
        downloadFull = new JRadioButton("Full video");
        downloadChapter = new JRadioButton("Chapter");
        downloadTimestamp = new JRadioButton("From");
        downloadSections = new ButtonGroup();
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

        linkText.setBounds(linkLabel.getX() + linkLabel.getWidth(), upperBound, frameWidth - extraRightBoundOnWindows - leftBound - linkLabel.getX() - linkLabel.getWidth(), lineDistance);
        panel.add(linkText);

        artistLabel.setBounds(leftBound, upperBound + lineDistance, 100,  lineDistance);
        artistLabel.setEnabled(false);
        panel.add(artistLabel);

        defaultArtist.setBounds(leftBound + artistLabel.getWidth(), artistLabel.getY(), 80, lineDistance);
        defaultArtist.setSelected(true);
        defaultArtist.setEnabled(false);

        customArtist.setBounds(defaultArtist.getX() + defaultArtist.getWidth(), artistLabel.getY(), 20, lineDistance);
        customArtist.setEnabled(false);

        artistText.setBounds(customArtist.getX() + customArtist.getWidth(), artistLabel.getY(), frameWidth - extraRightBoundOnWindows - leftBound - customArtist.getX() - customArtist.getWidth(), lineDistance);
        artistText.setEnabled(false);
        panel.add(artistText);

        selectArtist.add(defaultArtist);
        selectArtist.add(customArtist);
        panel.add(defaultArtist);
        panel.add(customArtist);

        albumLabel.setBounds(leftBound, artistLabel.getY() + lineDistance, 100, lineDistance);
        albumLabel.setEnabled(false);
        panel.add(albumLabel);

        defaultAlbum.setBounds(albumLabel.getX() + albumLabel.getWidth(), albumLabel.getY(), 80, lineDistance);
        defaultAlbum.setSelected(true);
        defaultAlbum.setEnabled(false);

        singleAlbum.setBounds(defaultAlbum.getX() + defaultAlbum.getWidth(), albumLabel.getY(), 80, lineDistance);
        singleAlbum.setEnabled(false);

        customAlbum.setBounds(singleAlbum.getX() + singleAlbum.getWidth(), albumLabel.getY(), 20, lineDistance);
        customAlbum.setEnabled(false);

        albumText.setBounds(customAlbum.getX() + customAlbum.getWidth(), albumLabel.getY(), frameWidth- extraRightBoundOnWindows - leftBound - customAlbum.getX() - customAlbum.getWidth(), lineDistance);
        albumText.setEnabled(false);
        panel.add(albumText);

        selectAlbum.add(defaultAlbum);
        selectAlbum.add(singleAlbum);
        selectAlbum.add(customAlbum);
        panel.add(defaultAlbum);
        panel.add(singleAlbum);
        panel.add(customAlbum);

        titleLabel.setBounds(leftBound, albumLabel.getY() + lineDistance, 100, lineDistance);
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

        playlistLabel.setBounds(leftBound, titleLabel.getY() + lineDistance, 140, lineDistance);
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

        downloadSectionLabel.setBounds(leftBound, playlistLabel.getY() + lineDistance, 75, lineDistance);
        downloadSectionLabel.setEnabled(false);
        panel.add(downloadSectionLabel);

        downloadFull.setBounds(downloadSectionLabel.getX() + downloadSectionLabel.getWidth(), downloadSectionLabel.getY(), 100, lineDistance);
        downloadFull.setSelected(true);
        downloadFull.setEnabled(false);

        downloadChapter.setBounds(downloadFull.getX() + downloadFull.getWidth(), downloadSectionLabel.getY(), 82, lineDistance);
        downloadChapter.setEnabled(false);

        downloadChapterField.setBounds(downloadChapter.getX() + downloadChapter.getWidth(), downloadSectionLabel.getY(), 100, lineDistance);
        downloadChapterField.setEnabled(false);
        panel.add(downloadChapterField);

        downloadTimestamp.setBounds(downloadChapterField.getX() + downloadChapterField.getWidth(), downloadSectionLabel.getY(), 60, lineDistance);
        downloadTimestamp.setEnabled(false);

        downloadStartStamp.setBounds(downloadTimestamp.getX() + downloadTimestamp.getWidth(), downloadSectionLabel.getY(), 70, lineDistance);
        downloadStartStamp.setEnabled(false);
        downloadStartStamp.setText("hh:mm:ss");
        downloadStartStamp.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(downloadStartStamp);

        downloadTo.setBounds(downloadStartStamp.getX() + downloadStartStamp.getWidth(), downloadSectionLabel.getY(), 23, lineDistance);
        downloadTo.setEnabled(false);
        panel.add(downloadTo);

        downloadEndStamp.setBounds(downloadTo.getX() + downloadTo.getWidth(), downloadSectionLabel.getY(), 70, lineDistance);
        downloadEndStamp.setEnabled(false);
        downloadEndStamp.setText("hh:mm:ss");
        downloadEndStamp.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(downloadEndStamp);

        downloadSections.add(downloadFull);
        downloadSections.add(downloadChapter);
        downloadSections.add(downloadTimestamp);
        panel.add(downloadFull);
        panel.add(downloadChapter);
        panel.add(downloadTimestamp);

        outputDirectoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        outputDirectoryTextField.setBounds(frameWidth/2 - 20 - 200/2, downloadSectionLabel.getY() + downloadSectionLabel.getHeight() + lineDistance, 200, 25);
        outputDirectoryTextField.setEnabled(false);
        panel.add(outputDirectoryTextField);

        outputDirectoryChooserButton.setBounds(outputDirectoryTextField.getX() + outputDirectoryTextField.getWidth(), outputDirectoryTextField.getY(), 20, 25);
        panel.add(outputDirectoryChooserButton);

        download.setBounds(frameWidth/2 - 150/2, outputDirectoryTextField.getY() + outputDirectoryTextField.getHeight() + 10, 150, 25);

        download.setEnabled(false);
        panel.add(download);

        downloadProgress.setBounds(50, download.getY() + download.getHeight() + 10, frameWidth - 2*50, 25);
        downloadProgress.setVisible(false);
        downloadProgress.setStringPainted(true);
        panel.add(downloadProgress);

        frame.setVisible(true);
    }
}
