import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        BookshopManager manager = new BookshopManager(
                new HashMap<>(), // implicitly guarantees key uniqueness
                new HashSet<>()
        );

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

        for (AbstractBook key : manager.books.keySet()) {
            System.out.println(key.toString() + ", " + manager.books.get(key));
        }

        for (AbstractUser user : manager.users) {
            System.out.println(user);
        }

        System.out.println(System.getProperty("javafx.runtime.version"));

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
        // other stuff
        primaryStage.show();
    }
}
