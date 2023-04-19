import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Random;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    // putting this object here runs the risk that it could be null when accessed
    private static BookshopManager manager;
    private static Stage primaryStage;

    public static void main(String[] args) {
        manager = new BookshopManager();

        try {
            // solution found here (https://stackoverflow.com/a/70864141)
            // construct input streams over the files
            InputStream stockFileStream = Objects.requireNonNull(
                    Main.class.getResource("/datafiles/Stock.txt")
            ).openStream();
            InputStream userAccountsFileStream = Objects.requireNonNull(
                    Main.class.getResource("/datafiles/UserAccounts.txt")
            ).openStream();

            manager.initialize(stockFileStream, userAccountsFileStream);
        } catch (AbstractBook.MalformedBookCharacteristicException | IOException | NullPointerException e) {
            throw new RuntimeException(e);
        }

        Application.launch(args);
    }

    /**
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("F214180 Coursework - Bookshop Management Tool");
        primaryStage.setWidth(GUIConstants.WIDTH);
        primaryStage.setHeight(GUIConstants.HEIGHT);
        LoginScene scene = LoginScene.getLoginScene(manager);
        primaryStage.setScene(scene);
        Main.primaryStage = primaryStage;
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("stopping...");
    }

    public static Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    public static BookshopManager getBookshopManager() {
        return Main.manager;
    }
}
