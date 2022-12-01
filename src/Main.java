import java.io.*;
import java.net.InetAddress;

public class Main {
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

    public static void main(String[] args) {
        //check if an internet connection is established
        try {
            InetAddress.getByName("www.youtube.com").isReachable(3);
        } catch (IOException error) {
            //error message if there is no internet connection
            ErrorMessage noInternet = new ErrorMessage("Please establish an internet connection.");
            noInternet.close.setText("Ok");
            return;
        }

        if (!ffmpegInstalled()) {
            new ErrorMessage("ffmpeg could not be found.");
            return;
        }

        if (!ytdlpInstalled()) {
            new ErrorMessage("yt-dlp could not be found.");
            return;
        }

        GUI mainWindow = new GUI(500, 300, 10, 20, 25);
        Loader loader = new Loader(mainWindow, 1);

        loader.setLinkListener();

        loader.setArtistListeners();

        loader.setAlbumListeners();

        loader.setTitleListeners();

        loader.setPlaylistListeners();

        loader.initOutputDirectoryField();

        loader.setDownloadListener();
    }
}
