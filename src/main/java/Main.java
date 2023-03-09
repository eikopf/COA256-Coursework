import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        BookshopManager manager = new BookshopManager(
                new HashMap<>(),
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
    }
}
