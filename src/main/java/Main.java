import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javafx.application.Application;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        BookshopManager manager = new BookshopManager();

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
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        LoginScene scene = new LoginScene(new VBox());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("stopping...");
    }
}
