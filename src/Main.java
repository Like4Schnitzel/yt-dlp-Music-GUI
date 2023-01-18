import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DependencyChecker checker = new DependencyChecker();

        if (!checker.readConfig() || !checker.checkDependencies()) {
            return;
        }

        GUI mainWindow;
        Loader loader;
        try {
            mainWindow = new GUI(checker);
            loader = new Loader(mainWindow, checker, Integer.parseInt(checker.configValues.get("delay-seconds")));
        } catch (NumberFormatException e) {
            ErrorMessage confValues = new ErrorMessage("<html>Config values for window layout <br> must only be integers.", 300, 150);
            confValues.errorMessage.setBounds(confValues.errorMessage.getX(), confValues.errorMessage.getY(), confValues.errorMessage.getWidth(), 30);
            return;
        }

        loader.setKeyListener();
        loader.setLinkListener();
        loader.setArtistListeners();
        loader.setAlbumListeners();
        loader.setTitleListeners();
        loader.setPlaylistListeners();
        loader.setSectionListeners();
        loader.initOutputDirectoryField();
        loader.setDownloadListener();
    }
}
