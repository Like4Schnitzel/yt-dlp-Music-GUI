import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;

import static java.lang.Integer.parseInt;

public class Loader {
    private boolean waiting;
    private boolean downloadAsPlaylist;
    private int playlistCount;
    private LocalTime time;
    private String outputDirectory;
    private String title;
    private String album;
    private String artist;
    private String playlist;
    private GUI mainWindow;
    private int checkDelaySeconds;

    public Loader(GUI gui, int delaySeconds) {
        mainWindow = gui;
        waiting = false;
        downloadAsPlaylist = false;
        playlistCount = 0;
        time = LocalTime.now();
        checkDelaySeconds = delaySeconds;
    }

    public void setLinkListener() {
        mainWindow.linkText.getDocument().addDocumentListener(new DocumentListener() {
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
        });
    }

    public void setArtistListeners() {
        mainWindow.defaultArtist.addActionListener(e -> {
            mainWindow.artistText.setEnabled(false);
            mainWindow.artistText.setText(artist);
        });

        mainWindow.customArtist.addActionListener(e -> {
            mainWindow.artistText.setText("");
            mainWindow.artistText.setEnabled(true);
        });
    }

    public void setAlbumListeners() {
        mainWindow.defaultAlbum.addActionListener(e -> {
            mainWindow.albumText.setEnabled(false);
            mainWindow.albumText.setText(album);
        });

        mainWindow.singleAlbum.addActionListener(e -> {
            mainWindow.albumText.setEnabled(false);
            mainWindow.albumText.setText(mainWindow.titleText.getText());
        });

        mainWindow.customAlbum.addActionListener(e -> {
            mainWindow.albumText.setText("");
            mainWindow.albumText.setEnabled(true);
        });
    }

    public void setTitleListeners() {
        mainWindow.defaultTitle.addActionListener(e -> {
            mainWindow.titleText.setEnabled(false);
            if(mainWindow.yesPlaylist.isSelected()) {
                mainWindow.titleText.setText("detect");
            } else {
                mainWindow.titleText.setText(title);
            }
        });

        mainWindow.customTitle.addActionListener(e -> {
            mainWindow.titleText.setText("");
            mainWindow.titleText.setEnabled(true);
        });

        mainWindow.titleText.getDocument().addDocumentListener(new DocumentListener() {
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
                if(mainWindow.singleAlbum.isSelected()) {
                    mainWindow.albumText.setText(mainWindow.titleText.getText());
                }
            }
        });
    }

    public void setPlaylistListeners() {
        mainWindow.yesPlaylist.addActionListener(e -> {
            mainWindow.defaultTitle.setSelected(true);
            mainWindow.customTitle.setEnabled(false);
            mainWindow.titleText.setEnabled(false);
            mainWindow.titleText.setText("detect");
            mainWindow.playlistStart.setEnabled(false);
            mainWindow.playlistStart.setText("1");
            mainWindow.playlistTo.setEnabled(false);
            mainWindow.playlistEnd.setEnabled(false);
            mainWindow.playlistEnd.setText(Integer.toString(playlistCount));
            downloadAsPlaylist = true;
        });

        mainWindow.noPlaylist.addActionListener(e -> {
            mainWindow.customTitle.setEnabled(true);
            mainWindow.titleText.setText(title);
            mainWindow.playlistStart.setEnabled(false);
            mainWindow.playlistStart.setText("NA");
            mainWindow.playlistTo.setEnabled(false);
            mainWindow.playlistEnd.setEnabled(false);
            mainWindow.playlistEnd.setText("NA");
            downloadAsPlaylist = false;
        });

        mainWindow.customPlaylist.addActionListener(e -> {
            mainWindow.defaultTitle.setSelected(true);
            mainWindow.customTitle.setEnabled(false);
            mainWindow.titleText.setEnabled(false);
            mainWindow.titleText.setText("detect");
            mainWindow.playlistStart.setEnabled(true);
            mainWindow.playlistStart.setText("1");
            mainWindow.playlistTo.setEnabled(true);
            mainWindow.playlistEnd.setEnabled(true);
            mainWindow.playlistEnd.setText(Integer.toString(playlistCount));
            downloadAsPlaylist = true;
        });

        mainWindow.playlistStart.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                mainWindow.download.setEnabled(true);

                try {
                    if (Integer.parseInt(mainWindow.playlistStart.getText()) < 1) {
                        mainWindow.playlistStart.setText("1");
                    } else if (Integer.parseInt(mainWindow.playlistStart.getText()) > Integer.parseInt(mainWindow.playlistEnd.getText())) {
                        mainWindow.download.setEnabled(false);
                    }
                } catch (NumberFormatException ex) {
                    mainWindow.playlistStart.setText("1");
                }
            }
        });
        mainWindow.playlistStart.getDocument().addDocumentListener(new DocumentListener() {
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
                        if (Integer.parseInt(mainWindow.playlistStart.getText()) < 1 || Integer.parseInt(mainWindow.playlistStart.getText()) > Integer.parseInt(mainWindow.playlistEnd.getText())) {
                            mainWindow.download.setEnabled(false);
                        } else {
                            mainWindow.download.setEnabled(true);
                        }
                    } catch (NumberFormatException ex) {
                        mainWindow.download.setEnabled(false);
                    }

                    if (mainWindow.noPlaylist.isSelected()) {
                        mainWindow.download.setEnabled(true);
                    }
                }
            };
        });

        mainWindow.playlistEnd.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                mainWindow.download.setEnabled(true);

                try {
                    if (Integer.parseInt(mainWindow.playlistEnd.getText()) > playlistCount) {
                        mainWindow.playlistEnd.setText(Integer.toString(playlistCount));
                    } else if (Integer.parseInt(mainWindow.playlistStart.getText()) > Integer.parseInt(mainWindow.playlistEnd.getText())) {
                        mainWindow.download.setEnabled(false);
                    }
                } catch (NumberFormatException ex) {
                    mainWindow.playlistEnd.setText(Integer.toString(playlistCount));
                }
            }
        });
        mainWindow.playlistEnd.getDocument().addDocumentListener(new DocumentListener() {
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
                        mainWindow.download.setEnabled(Integer.parseInt(mainWindow.playlistEnd.getText()) <= playlistCount && Integer.parseInt(mainWindow.playlistStart.getText()) <= Integer.parseInt(mainWindow.playlistEnd.getText()));
                    } catch (NumberFormatException ex) {
                        mainWindow.download.setEnabled(false);
                    }

                    if (mainWindow.noPlaylist.isSelected()) {
                        mainWindow.download.setEnabled(true);
                    }
                }
            };
        });
    }

    public void initOutputDirectoryField() {
        outputDirectory = System.getProperty("user.home");
        if (System.getProperty("os.name").startsWith("Windows")) {
            outputDirectory += "\\";
        } else {
            outputDirectory += "/";
        }
        outputDirectory += "Music";
        mainWindow.outputDirectoryTextField.setText(outputDirectory);
        mainWindow.outputDirectoryChooser.setSelectedFile(new File(outputDirectory));
        mainWindow.outputDirectoryChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mainWindow.outputDirectoryChooser.showOpenDialog(mainWindow.frame) == JFileChooser.APPROVE_OPTION) {
                    outputDirectory = mainWindow.outputDirectoryChooser.getSelectedFile().toString();
                    mainWindow.outputDirectoryTextField.setText(outputDirectory);
                }
            }
        });
    }

    public void setDownloadListener() {
        mainWindow.download.addActionListener(e -> {
            boolean albumDetectEnabled = mainWindow.defaultAlbum.isEnabled();
            boolean customTitleEnabled = mainWindow.customTitle.isEnabled();
            boolean playlistEnabled = mainWindow.playlistLabel.isEnabled();

            mainWindow.linkLabel.setEnabled(false);
            mainWindow.linkText.setEnabled(false);
            mainWindow.disableAll();

            mainWindow.downloadProgress.setVisible(true);
            mainWindow.downloadProgress.setString("Preparing download...");

            new Thread ( () -> {
                if (downloadAsPlaylist) {
                    mainWindow.downloadProgress.setMinimum(parseInt(mainWindow.playlistStart.getText()) * 1000);
                    mainWindow.downloadProgress.setMaximum(parseInt(mainWindow.playlistEnd.getText()) * 1000 + 1000);
                    mainWindow.downloadProgress.setValue(mainWindow.downloadProgress.getMinimum());

                    Runtime downloadRuntime = Runtime.getRuntime();
                    String[] downloader = {"yt-dlp", "--playlist-start", "set later [2]", "--playlist-end", "set later [4]", "--youtube-skip-dash-manifest", "--add-metadata", "--embed-thumbnail", "--format", "m4a", "-o", "set later [11]", "--ppa", "set later [13]", "--no-mtime", mainWindow.linkText.getText()};
                    String[] playlistTrackNames = {"yt-dlp", "--skip-download", "--print", "title", "--print", "track", "--playlist-start", mainWindow.playlistStart.getText(), "--playlist-end", mainWindow.playlistEnd.getText(), mainWindow.linkText.getText()};
                    try {
                        Runtime nameRuntime = Runtime.getRuntime();
                        Process getNames = nameRuntime.exec(playlistTrackNames);
                        BufferedReader stdInput = new BufferedReader(new InputStreamReader(getNames.getInputStream()));

                        String s;
                        String currentArtist = mainWindow.artistText.getText();
                        String currentAlbum;
                        String currentTitle = "";
                        for (int i = 0; (s = stdInput.readLine()) != null; i++) {
                            if (i % 2 == 0) {
                                currentTitle = s;
                            } else {
                                if (mainWindow.singleAlbum.isSelected()) {
                                    currentAlbum = currentTitle;
                                } else {
                                    currentAlbum = mainWindow.albumText.getText();
                                }

                                i = i / 2 + 1;
                                mainWindow.downloadProgress.setValue((i + parseInt(mainWindow.playlistStart.getText()) - 1) * 1000);
                                mainWindow.downloadProgress.setString("Preparing download for video " + i + " of " + (parseInt(mainWindow.playlistEnd.getText()) - parseInt(mainWindow.playlistStart.getText()) + 1) + "...");
                                downloader[2] = Integer.toString(i + parseInt(mainWindow.playlistStart.getText()) - 1);
                                downloader[4] = downloader[2];
                                if (!s.equals("NA")) {
                                    //download with track
                                    currentTitle = s;
                                    if (mainWindow.singleAlbum.isSelected()) {
                                        currentAlbum = s;
                                    }
                                }

                                downloader[11] = outputDirectory + "/" + formatForFilename(currentArtist) + "/" + formatForFilename(currentAlbum) + "/" + formatForFilename(currentTitle) + ".%(ext)s";
                                downloader[13] = "ffmpeg:-metadata artist=" + formatForPPA(currentArtist) + " -metadata album=" + formatForPPA(currentAlbum) + " -metadata title=" + formatForPPA(currentTitle);

                                try {
                                    Process downloading = downloadRuntime.exec(downloader);
                                    BufferedReader stdInput1 = new BufferedReader(new InputStreamReader(downloading.getInputStream()));

                                    String s1;
                                    while ((s1 = stdInput1.readLine()) != null) {
                                        if (s1.length() > 15 && s1.startsWith("[download]") && s1.charAt(16) == '%') {
                                            mainWindow.downloadProgress.setValue((int) (Double.parseDouble(s1.substring(11, 16)) * 10) + (i + parseInt(mainWindow.playlistStart.getText()) - 1) * 1000);
                                            mainWindow.downloadProgress.setString("Downloading video " + i + " of " + (parseInt(mainWindow.playlistEnd.getText()) - parseInt(mainWindow.playlistStart.getText()) + 1) + "...");
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
                    mainWindow.downloadProgress.setMinimum(0);
                    mainWindow.downloadProgress.setMaximum(1000);
                    mainWindow.downloadProgress.setValue(0);

                    Runtime downloadRuntime = Runtime.getRuntime();
                    String[] downloader = {"yt-dlp", "--no-playlist", "--youtube-skip-dash-manifest", "--add-metadata", "--embed-thumbnail", "--format", "m4a", "-o", outputDirectory + "/" + formatForFilename(mainWindow.artistText.getText()) + "/" + formatForFilename(mainWindow.albumText.getText()) + "/" + formatForFilename(mainWindow.titleText.getText()) + ".%(ext)s", "--ppa", "ffmpeg:-metadata artist=" + formatForPPA(mainWindow.artistText.getText()) + " -metadata album=" + formatForPPA(mainWindow.albumText.getText()) + " -metadata title=" + formatForPPA(mainWindow.titleText.getText()), "--no-mtime", mainWindow.linkText.getText()};
                    try {
                        Process proc = downloadRuntime.exec(downloader);
                        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                        String s;
                        while ((s = stdInput.readLine()) != null) {
                            if(s.length() > 15 && s.startsWith("[download]") && s.charAt(16) == '%') {
                                mainWindow.downloadProgress.setValue((int) (Double.parseDouble(s.substring(11, 16)) * 10));
                                mainWindow.downloadProgress.setString("Downloading video at " + mainWindow.downloadProgress.getValue()/10. + "%...");
                            }
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                mainWindow.downloadProgress.setValue(mainWindow.downloadProgress.getMaximum());
                mainWindow.downloadProgress.setString("Finished downloading successfully!");

                //re-enabling everything
                mainWindow.linkLabel.setEnabled(true);
                mainWindow.linkText.setEnabled(true);
                mainWindow.artistLabel.setEnabled(true);
                mainWindow.defaultArtist.setEnabled(true);
                mainWindow.customArtist.setEnabled(true);
                mainWindow.artistText.setEnabled(mainWindow.customArtist.isSelected());
                mainWindow.albumLabel.setEnabled(true);
                mainWindow.defaultAlbum.setEnabled(albumDetectEnabled);
                mainWindow.singleAlbum.setEnabled(true);
                mainWindow.customAlbum.setEnabled(true);
                mainWindow.albumText.setEnabled(mainWindow.customAlbum.isSelected());
                mainWindow.titleLabel.setEnabled(true);
                mainWindow.defaultTitle.setEnabled(true);
                mainWindow.customTitle.setEnabled(customTitleEnabled);
                mainWindow.titleText.setEnabled(mainWindow.customTitle.isSelected());
                if (playlistEnabled) {
                    mainWindow.playlistLabel.setEnabled(true);
                    mainWindow.yesPlaylist.setEnabled(true);
                    mainWindow.noPlaylist.setEnabled(true);
                    mainWindow.customPlaylist.setEnabled(true);
                    if (mainWindow.customPlaylist.isSelected()) {
                        mainWindow.playlistStart.setEnabled(true);
                        mainWindow.playlistTo.setEnabled(true);
                        mainWindow.playlistEnd.setEnabled(true);
                    }
                }
                mainWindow.download.setEnabled(true);
            }).start();
        });
    }

    public String getPlaylistCount(String link) throws IOException {
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

    public String getPlaylist(String link) throws IOException {
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

    public String[] getElements(String link) throws IOException {
        String[] output = new String[5];
        Runtime rt = Runtime.getRuntime();
        String[] command = {"yt-dlp", "--no-playlist", "--skip-download", "--print", "track", "--print", "title", "--print", "album", "--print", "artist", "--print", "channel", link};
        Process run = rt.exec(command);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(run.getInputStream()));

        String s;
        try {
            for (int i = 0; (s = stdInput.readLine()) != null; i++) {
                output[i] = s;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return new String[]{"", "", "", "", ""};
        }

        return output;
    }

    public void checkValidity() {
        mainWindow.disableAll();

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

            mainWindow.linkText.setEnabled(false);
            time = LocalTime.now();

            title = "";
            album = "";
            artist = "";
            boolean isList = mainWindow.linkText.getText().contains("list=");

            String[] elements;
            try {
                elements = getElements(mainWindow.linkText.getText());
                if (isList) {
                    playlistCount = parseInt(getPlaylistCount(mainWindow.linkText.getText()));
                    playlist = getPlaylist(mainWindow.linkText.getText());
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
                mainWindow.artistLabel.setEnabled(true);
                mainWindow.artistText.setText(artist);
                mainWindow.defaultArtist.setEnabled(true);
                mainWindow.defaultArtist.setSelected(true);
                mainWindow.customArtist.setEnabled(true);

                mainWindow.titleLabel.setEnabled(true);
                if (isList) {
                    mainWindow.titleText.setText("detect");
                } else {
                    mainWindow.titleText.setText(title);
                }
                mainWindow.defaultTitle.setEnabled(true);
                mainWindow.defaultTitle.setSelected(true);
                mainWindow.customTitle.setEnabled(true);

                mainWindow.albumLabel.setEnabled(true);
                mainWindow.singleAlbum.setEnabled(true);
                mainWindow.customAlbum.setEnabled(true);

                mainWindow.download.setEnabled(true);

                if (!album.equals("NA")) {
                    mainWindow.albumText.setText(album);
                    mainWindow.defaultAlbum.setEnabled(true);
                    mainWindow.defaultAlbum.setSelected(true);
                } else {
                    mainWindow.defaultAlbum.setEnabled(false);
                    mainWindow.singleAlbum.setSelected(true);
                    mainWindow.albumText.setText(title);
                }

                mainWindow.customTitle.setEnabled(!isList);
                mainWindow.playlistLabel.setEnabled(isList);
                mainWindow.yesPlaylist.setEnabled(isList);
                mainWindow.yesPlaylist.setSelected(isList);
                mainWindow.noPlaylist.setEnabled(isList);
                mainWindow.noPlaylist.setSelected(!isList);
                mainWindow.customPlaylist.setEnabled(isList);
                downloadAsPlaylist = isList;

                if (isList) {
                    mainWindow.playlistStart.setText("1");
                    mainWindow.playlistEnd.setText(Integer.toString(playlistCount));
                } else {
                    mainWindow.playlistStart.setText("NA");
                    mainWindow.playlistEnd.setText("NA");
                }
            } else {
                mainWindow.disableAll();
            }
            waiting = false;
            mainWindow.linkText.setEnabled(true);
        }).start();
    }

    public String formatForPPA(String str) {
        char[] checkChars = {' ', '\'', '\"'};
        String returnString = str;
        int length = returnString.length();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < checkChars.length; j++) {
                if (returnString.charAt(i) == checkChars[j]) {
                    returnString = returnString.substring(0, i) + '\\' + checkChars[j] + returnString.substring(i + 1, length);
                    length++;
                    i++;
                    break;
                }
            }
        }

        return returnString;
    }

    public String formatForFilename(String str) {
        //characters to replace with underscores
        char[] checkChars = {':', '/', '?'};
        String returnString = "";

        for (int i = 0; i < str.length(); i++) {
            if (charArrayContainsChar(checkChars, str.charAt(i))) {
                returnString += '_';
            } else {
                returnString += str.charAt(i);
            }
        }

        return returnString;
    }

    private boolean charArrayContainsChar(char[] arr, char c) {
        for (int i = 0; i < arr.length; i++) {
            if (c == arr[i]) {
                return true;
            }
        }

        return false;
    }
}
