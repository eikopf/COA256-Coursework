import java.util.HashMap;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        BookshopManager manager = new BookshopManager(
                new HashMap<>(),
                new HashSet<>()
        );

        try {
            manager.initialize();
        } catch (AbstractBook.MalformedBookCharacteristicException e) {
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
