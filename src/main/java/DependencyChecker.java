package main.java;
import java.io.*;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;

public class DependencyChecker {
    private String currentDirectory;
    public Dictionary<String, String> configValues;

    public DependencyChecker() {
        try {
            currentDirectory = filepathFormatter(URLDecoder.decode(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toString(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {/*can't happen*/}
        currentDirectory = currentDirectory.substring(0, currentDirectory.lastIndexOf('/'));
        System.out.println("Current directory is " + currentDirectory);
        configValues = new Hashtable<>();
    }

    private String filepathFormatter(String originalPath) {
        if (originalPath.charAt(1) == ':' && originalPath.charAt(2) == '\\') {
            return originalPath.substring(2).replace('\\', '/');
        }

        return originalPath;
    }

    public boolean checkDependencies() {
        if (!ffmpegInstalled()) {
            new ErrorMessage("ffmpeg could not be found.");
            return false;
        }

        if (!ytdlpInstalled()) {
            new ErrorMessage("yt-dlp could not be found.");
            return false;
        }

        if (!internetAvailable()) {
            ErrorMessage noInternet = new ErrorMessage("Please establish an internet connection.");
            noInternet.close.setText("Ok");
            return false;
        }

        return true;
    }

    public boolean readConfig() {
        try {
            configValues.put("ffmpeg-path", "ffmpeg");
            configValues.put("yt-dlp-path", "yt-dlp");
            configValues.put("delay-seconds", "1");
            configValues.put("replace-chars", "<>:\"/\\|?*");
            configValues.put("frame-width", "620");
            configValues.put("frame-height", "350");
            configValues.put("left-bound", "10");
            configValues.put("upper-bound", "20");
            configValues.put("line-distance", "25");
            Scanner configReader = new Scanner(new File(currentDirectory + "/config.txt"));
            String configString = "";
            for (int i = 0; configReader.hasNextLine(); i++) {
                configString = configReader.nextLine();
                if (configString.equals("")) {
                    continue;
                }
                String[] substrings = configString.split("=");
                if (substrings.length != 2) {
                    new ErrorMessage("config.txt syntax is incorrect.");
                    return false;
                }

                if (substrings[0].contains("path")) {
                    try {
                        if (substrings[1].charAt(0) == '.') {
                            configValues.put(substrings[0], filepathFormatter(currentDirectory + substrings[1].substring(1)));
                        } else {
                            configValues.put(substrings[0], filepathFormatter(substrings[1]));
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        configValues.put(substrings[0], substrings[1]);
                    }
                } else {
                    configValues.put(substrings[0], substrings[1]);
                }
            }
            configReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find config.txt, setting values to defaults.");
        }

        return true;
    }

    public boolean internetAvailable() {
        try {
            InetAddress.getByName("www.youtube.com").isReachable(3);
            return true;
        } catch (IOException error) {
            return false;
        }
    }

    public boolean ffmpegInstalled() {
        Runtime rt = Runtime.getRuntime();
        String[] command = {configValues.get("ffmpeg-path"), "-version"};
        try {
            rt.exec(command);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean ytdlpInstalled() {
        Runtime rt = Runtime.getRuntime();
        String[] command = {configValues.get("yt-dlp-path"), "--version"};
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
}
