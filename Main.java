import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;
import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Main {
    private static String title;
    private static String album;
    private static String artist;
    private static String playlist;
    private static LocalTime time = LocalTime.now();
    private static boolean waiting = false;
    private static JFrame frame = new JFrame();
    private static JPanel panel = new JPanel();
    private static JLabel linkLabel;
    private static JLabel artistLabel;
    private static JLabel albumLabel;
    private static JLabel titleLabel;
    private static JLabel playlistLabel;
    private static JLabel playlistTo;
    private static JLabel errorMessage;
    private static JRadioButton defaultArtist;
    private static JRadioButton customArtist;
    private static ButtonGroup selectArtist;
    private static JRadioButton defaultAlbum;
    private static JRadioButton singleAlbum;
    private static JRadioButton customAlbum;
    private static ButtonGroup selectAlbum;
    private static JRadioButton defaultTitle;
    private static JRadioButton customTitle;
    private static ButtonGroup selectTitle;
    private static JRadioButton yesPlaylist;
    private static JRadioButton noPlaylist;
    private static JRadioButton customPlaylist;
    private static ButtonGroup downloadPlaylist;
    private static JButton download;
    private static JButton close;
    private static JProgressBar downloadProgress;
    private static JTextField linkText;
    private static JTextField artistText;
    private static JTextField albumText;
    private static JTextField titleText;
    private static JTextField playlistStart;
    private static JTextField playlistEnd;
    private static boolean downloadAsPlaylist = false;
    private static int playlistCount = 0;

    public static String getPlaylistCount(String link) throws IOException {
        String count = "0";
        Runtime rt = Runtime.getRuntime();
        String[] command = {"yt-dlp", "--playlist-end", "1", "--print", "playlist_count", link};
        Process run = rt.exec(command);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(run.getInputStream()));

        String s;
        while ((s = stdInput.readLine()) != null) {
            count = s;
        }

        return count;
    }

    public static String getPlaylist(String link) throws IOException {
        String output = "";
        Runtime rt = Runtime.getRuntime();
        String[] command = {"yt-dlp", "--playlist-end", "1", "--print", "playlist", link};
        Process run = rt.exec(command);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(run.getInputStream()));

        String s;
        while ((s = stdInput.readLine()) != null) {
            output += s;
        }

        return output;
    }

    public static String[] getElements(String link) throws IOException {
        String[] output = new String[5];
        Runtime rt = Runtime.getRuntime();
        String[] command = {"yt-dlp", "--no-playlist", "--skip-download", "--print", "track", "--print", "title", "--print", "album", "--print", "artist", "--print", "channel", link};
        Process run = rt.exec(command);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(run.getInputStream()));

        String s;
        for (int i = 0; (s = stdInput.readLine()) != null; i++) {
            output[i] = s;
        }

        return output;
    }

    public static boolean ffmpegInstalled() {
        Runtime rt = Runtime.getRuntime();
        String[] command = {"ffmpeg", "-version"};
        try {
            rt.exec(command);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean ytdlpInstalled() {
        Runtime rt = Runtime.getRuntime();
        String[] command = {"yt-dlp", "--version"};
        try {
            Process run = rt.exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(run.getInputStream()));
            String s = stdInput.readLine();
            Integer.parseInt(s.substring(0, 4));
            Integer.parseInt(s.substring(5, 7));
            Integer.parseInt(s.substring(8, 10));
            if (!(s.charAt(4) == '.' && s.charAt(7) == '.')) {
                throw new IOException();
            }
            return true;
        } catch (IOException | NullPointerException | NumberFormatException e) {
            return false;
        }
    }

    public static void disableAll() {
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

    public static void initErrorMessage(String errorText) {
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

    public static void main(String[] args) {
        frame = new JFrame();
        panel = new JPanel();
        frame.setResizable(false);
        //check if an internet connection is established
        try {
            InetAddress.getByName("www.youtube.com").isReachable(3);
        } catch (IOException error) {
            //error message if there is no internet connection
            initErrorMessage("Please establish an internet connection.");
            close.setText("Ok");
            return;
        }

        if (!ffmpegInstalled()) {
            initErrorMessage("ffmpeg could not be found");
            return;
        }

        if (!ytdlpInstalled()) {
            initErrorMessage("yt-dlp could not be found.");
            return;
        }

        int checkDelaySeconds = 1;
        int leftBound = 10;
        int upperBound = 20;
        int lineDistance = 25;
        int frameWidth = 500;
        int frameHeight = 300;

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

        frame.setSize(frameWidth, frameHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Youtube Music Downloader");
        frame.add(panel);

        panel.setLayout(null);

        linkLabel.setBounds(leftBound, upperBound, 50, lineDistance);
        panel.add(linkLabel);

        linkText.setBounds(linkLabel.getX() + linkLabel.getWidth(), upperBound, frameWidth - leftBound - linkLabel.getX() - linkLabel.getWidth(), lineDistance);
        linkText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkValidity();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkValidity();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkValidity();
            }

            public void checkValidity() {
                disableAll();

                new Thread( () -> {
                    time = LocalTime.now();
                    if (waiting) {
                        return;
                    }

                    if (Duration.between(time, LocalTime.now()).getSeconds() < checkDelaySeconds){
                        waiting = true;
                        while(Duration.between(time, LocalTime.now()).getSeconds() < checkDelaySeconds) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    linkText.setEnabled(false);
                    time = LocalTime.now();

                    title = "";
                    album = "";
                    artist = "";
                    boolean isList = linkText.getText().contains("list=");

                    String[] elements;
                    try {
                        elements = getElements(linkText.getText());
                        if (isList) {
                            playlistCount = parseInt(getPlaylistCount(linkText.getText()));
                            playlist = getPlaylist(linkText.getText());
                        }
                    } catch (IOException | NumberFormatException e) {
                        elements = new String[]{"", "", "", "", ""};
                    }
                    for (int i = 0; i < 5; i++) {
                        if(elements[i] == null) {
                            elements[i] = "";
                        }
                    }

                    title = elements[0];
                    if (title.equals("NA")) {
                        title = elements[1];
                    }

                    album = elements[2];
                    if (album.equals("NA") && isList) {
                        album = playlist;
                    }

                    artist = elements[3];
                    if (artist.equals("NA")) {
                        artist = elements[4];
                    }

                    if (!title.equals("")) {
                        artistLabel.setEnabled(true);
                        artistText.setText(artist);
                        defaultArtist.setEnabled(true);
                        defaultArtist.setSelected(true);
                        customArtist.setEnabled(true);

                        titleLabel.setEnabled(true);
                        if (isList) {
                            titleText.setText("detect");
                        } else {
                            titleText.setText(title);
                        }
                        defaultTitle.setEnabled(true);
                        defaultTitle.setSelected(true);
                        customTitle.setEnabled(true);

                        albumLabel.setEnabled(true);
                        singleAlbum.setEnabled(true);
                        customAlbum.setEnabled(true);

                        download.setEnabled(true);

                        if (!album.equals("NA")) {
                            albumText.setText(album);
                            defaultAlbum.setEnabled(true);
                            defaultAlbum.setSelected(true);
                        } else {
                            defaultAlbum.setEnabled(false);
                            singleAlbum.setSelected(true);
                            albumText.setText(title);
                        }

                        customTitle.setEnabled(!isList);
                        playlistLabel.setEnabled(isList);
                        yesPlaylist.setEnabled(isList);
                        yesPlaylist.setSelected(isList);
                        noPlaylist.setEnabled(isList);
                        noPlaylist.setSelected(!isList);
                        customPlaylist.setEnabled(isList);
                        downloadAsPlaylist = isList;

                        if (isList) {
                            playlistStart.setText("1");
                            playlistEnd.setText(Integer.toString(playlistCount));
                        } else {
                            playlistStart.setText("NA");
                            playlistEnd.setText("NA");
                        }
                    } else {
                        disableAll();
                    }
                    waiting = false;
                    linkText.setEnabled(true);
                }).start();
            }
        });
        panel.add(linkText);

        artistLabel.setBounds(leftBound, upperBound + lineDistance, 100,  lineDistance);
        artistLabel.setEnabled(false);
        panel.add(artistLabel);

        defaultArtist.setBounds(leftBound + artistLabel.getWidth(), artistLabel.getY(), 80, lineDistance);
        defaultArtist.setSelected(true);
        defaultArtist.setEnabled(false);
        defaultArtist.addActionListener(e -> {
            artistText.setEnabled(false);
            artistText.setText(artist);
        });

        customArtist.setBounds(defaultArtist.getX() + defaultArtist.getWidth(), artistLabel.getY(), 20, lineDistance);
        customArtist.setEnabled(false);
        customArtist.addActionListener(e -> {
            artistText.setText("");
            artistText.setEnabled(true);
        });

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
        defaultAlbum.addActionListener(e -> {
            albumText.setEnabled(false);
            albumText.setText(album);
        });

        singleAlbum.setBounds(defaultAlbum.getX() + defaultAlbum.getWidth(), albumLabel.getY(), 80, lineDistance);
        singleAlbum.setEnabled(false);
        singleAlbum.addActionListener(e -> {
            albumText.setEnabled(false);
            albumText.setText(titleText.getText());
        });

        customAlbum.setBounds(singleAlbum.getX() + singleAlbum.getWidth(), albumLabel.getY(), 20, lineDistance);
        customAlbum.setEnabled(false);
        customAlbum.addActionListener(e -> {
            albumText.setText("");
            albumText.setEnabled(true);
        });

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
        defaultTitle.addActionListener(e -> {
            titleText.setEnabled(false);
            if(yesPlaylist.isSelected()) {
                titleText.setText("detect");
            } else {
                titleText.setText(title);
            }
        });

        customTitle.setBounds(defaultTitle.getX() + defaultTitle.getWidth(), titleLabel.getY(), 20, lineDistance);
        customTitle.setEnabled(false);
        customTitle.addActionListener(e -> {
            titleText.setText("");
            titleText.setEnabled(true);
        });

        titleText.setBounds(customTitle.getX() + customTitle.getWidth(), titleLabel.getY(), frameWidth - leftBound - customTitle.getX() - customTitle.getWidth(), lineDistance);
        titleText.setEnabled(false);
        titleText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSingle();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSingle();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSingle();
            }

            public void updateSingle() {
                if(singleAlbum.isSelected()) {
                    albumText.setText(titleText.getText());
                }
            }
        });
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
        yesPlaylist.addActionListener(e -> {
            defaultTitle.setSelected(true);
            customTitle.setEnabled(false);
            titleText.setEnabled(false);
            titleText.setText("detect");
            playlistStart.setEnabled(false);
            playlistStart.setText("1");
            playlistTo.setEnabled(false);
            playlistEnd.setEnabled(false);
            playlistEnd.setText(Integer.toString(playlistCount));
            downloadAsPlaylist = true;
        });

        noPlaylist.setBounds(yesPlaylist.getX() + yesPlaylist.getWidth(), playlistLabel.getY(), 50, lineDistance);
        noPlaylist.setEnabled(false);
        noPlaylist.setSelected(true);
        noPlaylist.addActionListener(e -> {
            customTitle.setEnabled(true);
            titleText.setText(title);
            playlistStart.setEnabled(false);
            playlistStart.setText("NA");
            playlistTo.setEnabled(false);
            playlistEnd.setEnabled(false);
            playlistEnd.setText("NA");
            downloadAsPlaylist = false;
        });

        customPlaylist.setBounds(noPlaylist.getX() + noPlaylist.getWidth(), playlistLabel.getY(), 103, lineDistance);
        customPlaylist.setEnabled(false);
        customPlaylist.addActionListener(e -> {
            defaultTitle.setSelected(true);
            customTitle.setEnabled(false);
            titleText.setEnabled(false);
            titleText.setText("detect");
            playlistStart.setEnabled(true);
            playlistStart.setText("1");
            playlistTo.setEnabled(true);
            playlistEnd.setEnabled(true);
            playlistEnd.setText(Integer.toString(playlistCount));
            downloadAsPlaylist = true;
        });

        playlistStart.setBounds(customPlaylist.getX() + customPlaylist.getWidth(), playlistLabel.getY(), 30, lineDistance);
        playlistStart.setEnabled(false);
        playlistStart.setText("NA");
        playlistStart.setHorizontalAlignment(JTextField.RIGHT);
        playlistStart.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                try {
                    if (Integer.parseInt(playlistStart.getText()) < 1) {
                        playlistStart.setText("1");
                    } else if (Integer.parseInt(playlistStart.getText()) > Integer.parseInt(playlistEnd.getText())) {
                        playlistStart.setText(playlistEnd.getText());
                    }
                } catch (NumberFormatException ex) {
                    playlistStart.setText("1");
                }

                download.setEnabled(true);
            }
        });
        playlistStart.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(checkLimits);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(checkLimits);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(checkLimits);
            }

            final Runnable checkLimits = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (Integer.parseInt(playlistStart.getText()) < 1 || Integer.parseInt(playlistStart.getText()) > Integer.parseInt(playlistEnd.getText())) {
                            download.setEnabled(false);
                        } else {
                            download.setEnabled(true);
                        }
                    } catch (NumberFormatException ex) {
                        download.setEnabled(false);
                    }

                    if (noPlaylist.isSelected()) {
                        download.setEnabled(true);
                    }
                }
            };
        });
        panel.add(playlistStart);

        playlistTo.setBounds(playlistStart.getX() + playlistStart.getWidth(), playlistLabel.getY(), 23, lineDistance);
        playlistTo.setEnabled(false);
        panel.add(playlistTo);

        playlistEnd.setBounds(playlistTo.getX() + playlistTo.getWidth(), playlistLabel.getY(), 30, lineDistance);
        playlistEnd.setEnabled(false);
        playlistEnd.setText("NA");
        playlistEnd.setHorizontalAlignment(JTextField.RIGHT);
        playlistEnd.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                try {
                    if (Integer.parseInt(playlistEnd.getText()) > playlistCount) {
                        playlistStart.setText(Integer.toString(playlistCount));
                    } else if (Integer.parseInt(playlistStart.getText()) > Integer.parseInt(playlistEnd.getText())) {
                        playlistEnd.setText(playlistStart.getText());
                    }
                } catch (NumberFormatException ex) {
                    playlistStart.setText(Integer.toString(playlistCount));
                }

                download.setEnabled(true);
            }
        });
        playlistEnd.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(checkLimits);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(checkLimits);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(checkLimits);
            }

            final Runnable checkLimits = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (Integer.parseInt(playlistEnd.getText()) > playlistCount || Integer.parseInt(playlistStart.getText()) > Integer.parseInt(playlistEnd.getText())) {
                            download.setEnabled(false);
                        } else {
                            download.setEnabled(true);
                        }
                    } catch (NumberFormatException ex) {
                        download.setEnabled(false);
                    }

                    if (noPlaylist.isSelected()) {
                        download.setEnabled(true);
                    }
                }
            };
        });
        panel.add(playlistEnd);

        downloadPlaylist.add(yesPlaylist);
        downloadPlaylist.add(noPlaylist);
        downloadPlaylist.add(customPlaylist);
        panel.add(yesPlaylist);
        panel.add(noPlaylist);
        panel.add(customPlaylist);

        download.setBounds(frameWidth/2 - 150/2, frameHeight - 100, 150, 25);
        download.addActionListener(e -> {
            boolean albumDetectEnabled = defaultAlbum.isEnabled();
            boolean customTitleEnabled = customTitle.isEnabled();
            boolean playlistEnabled = playlistLabel.isEnabled();

            linkLabel.setEnabled(false);
            linkText.setEnabled(false);
            disableAll();

            downloadProgress.setVisible(true);
            downloadProgress.setString("Preparing download...");

            new Thread ( () -> {
                if (downloadAsPlaylist) {
                    downloadProgress.setMinimum(Integer.parseInt(playlistStart.getText()) * 1000);
                    downloadProgress.setMaximum(Integer.parseInt(playlistEnd.getText()) * 1000 + 1000);
                    downloadProgress.setValue(downloadProgress.getMinimum());

                    Runtime downloadRuntime = Runtime.getRuntime();
                    String[] downloader = {"yt-dlp", "--playlist-start", "set later [2]", "--playlist-end", "set later [4]", "--youtube-skip-dash-manifest", "--add-metadata", "--embed-thumbnail", "--format", "m4a", "-o", "set later [11]", "--ppa", "set later [13]", linkText.getText()};
                    String[] playlistTrackNames = {"yt-dlp", "--skip-download", "--print", "title", "--print", "track", "--playlist-start", playlistStart.getText(), "--playlist-end", playlistEnd.getText(), linkText.getText()};
                    try {
                        Runtime nameRuntime = Runtime.getRuntime();
                        Process getNames = nameRuntime.exec(playlistTrackNames);
                        BufferedReader stdInput = new BufferedReader(new InputStreamReader(getNames.getInputStream()));

                        String s;
                        String currentAlbum = "";
                        String currentTitle = "";
                        for (int i = 0; (s = stdInput.readLine()) != null; i++) {
                            if (i % 2 == 0) {
                                currentTitle = s;
                            } else {
                                if (singleAlbum.isSelected()) {
                                    currentAlbum = currentTitle;
                                } else {
                                    currentAlbum = albumText.getText();
                                }

                                i = i / 2 + 1;
                                downloadProgress.setValue((i + Integer.parseInt(playlistStart.getText()) - 1) * 1000);
                                downloadProgress.setString("Preparing download for video " + i + " of " + (Integer.parseInt(playlistEnd.getText()) - Integer.parseInt(playlistStart.getText()) + 1) + "...");
                                downloader[2] = Integer.toString(i + Integer.parseInt(playlistStart.getText()) - 1);
                                downloader[4] = downloader[2];
                                if (s.equals("NA")) {
                                    //download with title
                                    if (singleAlbum.isSelected()) {
                                        downloader[11] = System.getProperty("user.home") + "/Music/" + artistText.getText() + "/" + currentTitle + "/" + currentTitle + ".%(ext)s";
                                    } else {
                                        downloader[11] = System.getProperty("user.home") + "/Music/" + artistText.getText() + "/" + albumText.getText() + "/" + currentTitle + ".%(ext)s";
                                    }
                                } else {
                                    //download with track
                                    if (singleAlbum.isSelected()) {
                                        downloader[11] = System.getProperty("user.home") + "/Music/" + artistText.getText() + "/" + s + "/" + s + ".%(ext)s";
                                    } else {
                                        downloader[11] = System.getProperty("user.home") + "/Music/" + artistText.getText() + "/" + albumText.getText() + "/" + s + ".%(ext)s";
                                    }
                                    currentTitle = s;
                                }
                                downloader[13] = "-metadata artist='" + artistText.getText() + "' -metadata album='" + currentAlbum + "' -metadata title='" + currentTitle + "'";

                                try {
                                    Process downloading = downloadRuntime.exec(downloader);
                                    BufferedReader stdInput1 = new BufferedReader(new InputStreamReader(downloading.getInputStream()));

                                    String s1;
                                    while ((s1 = stdInput1.readLine()) != null) {
                                        if (s1.length() > 15 && s1.substring(0, 10).equals("[download]") && s1.charAt(16) == '%') {
                                            downloadProgress.setValue((int) (Double.parseDouble(s1.substring(11, 16)) * 10) + (i + Integer.parseInt(playlistStart.getText()) - 1) * 1000);
                                            downloadProgress.setString("Downloading video " + i + " of " + (Integer.parseInt(playlistEnd.getText()) - Integer.parseInt(playlistStart.getText()) + 1) + "...");
                                        }
                                    }
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }

                                i = i * 2 - 1;
                            }
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    downloadProgress.setMinimum(0);
                    downloadProgress.setMaximum(1000);
                    downloadProgress.setValue(0);

                    Runtime downloadRuntime = Runtime.getRuntime();
                    String[] downloader = {"yt-dlp", "--no-playlist", "--youtube-skip-dash-manifest", "--add-metadata", "--embed-thumbnail", "--format", "m4a", "-o", System.getProperty("user.home") + "/Music/" + artistText.getText() + "/" + albumText.getText() + "/" + titleText.getText() + ".%(ext)s", "--ppa", "-metadata artist='" + artistText.getText() + "' -metadata album='" + albumText.getText() + "' -metadata title='" + titleText.getText() + "'", linkText.getText()};
                    try {
                        Process proc = downloadRuntime.exec(downloader);
                        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                        String s;
                        while ((s = stdInput.readLine()) != null) {
                            if(s.length() > 15 && s.substring(0, 10).equals("[download]") && s.charAt(16) == '%') {
                                downloadProgress.setValue((int) (Double.parseDouble(s.substring(11, 16)) * 10));
                                downloadProgress.setString("Downloading video at " + downloadProgress.getValue()/10. + "%...");
                            }
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                downloadProgress.setValue(downloadProgress.getMaximum());
                downloadProgress.setString("Finished downloading successfully!");

                //re-enabling everything
                linkLabel.setEnabled(true);
                linkText.setEnabled(true);
                artistLabel.setEnabled(true);
                defaultArtist.setEnabled(true);
                customArtist.setEnabled(true);
                artistText.setEnabled(customArtist.isSelected());
                albumLabel.setEnabled(true);
                defaultAlbum.setEnabled(albumDetectEnabled);
                singleAlbum.setEnabled(true);
                customAlbum.setEnabled(true);
                albumText.setEnabled(customAlbum.isSelected());
                titleLabel.setEnabled(true);
                defaultTitle.setEnabled(true);
                customTitle.setEnabled(customTitleEnabled);
                titleText.setEnabled(customTitle.isSelected());
                if (playlistEnabled) {
                    playlistLabel.setEnabled(true);
                    yesPlaylist.setEnabled(true);
                    noPlaylist.setEnabled(true);
                    customPlaylist.setEnabled(true);
                    if (customPlaylist.isSelected()) {
                        playlistStart.setEnabled(true);
                        playlistTo.setEnabled(true);
                        playlistEnd.setEnabled(true);
                    }
                }
                download.setEnabled(true);
            }).start();
        });
        download.setEnabled(false);
        panel.add(download);

        downloadProgress.setBounds(50, download.getY() + download.getHeight() + 10, frameWidth - 2*50, 25);
        downloadProgress.setVisible(false);
        downloadProgress.setStringPainted(true);
        panel.add(downloadProgress);

        frame.setVisible(true);
    }
}
