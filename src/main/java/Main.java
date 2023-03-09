import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        BookshopManager manager = new BookshopManager(
                new HashMap<>(),
                new HashSet<>()
        );

        // "src/main/resources/datafiles/Stock.txt"
        // "src/main/resources/datafiles/UserAccounts.txt"



        try {
            // construct input streams over the files
            InputStream stockFileStream = Main.class.getResource("/datafiles/Stock.txt").openStream();
            InputStream userAccountsFileStream = Main.class.getResource("/datafiles/UserAccounts.txt").openStream();

            manager.initialize(stockFileStream, userAccountsFileStream);
        }
        catch (AbstractBook.MalformedBookCharacteristicException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
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
