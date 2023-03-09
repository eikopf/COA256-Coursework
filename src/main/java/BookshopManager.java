import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;

/**
 * Represents and controls the bookshop system, including permissions.
 * As an example, a user might pass a request to the manager.
 * Based on the type of the user and the state of the system,
 * the manager will either execute the request, or throw an error
 * to indicate that the request could not be fulfilled.
 */
public class BookshopManager {
    HashMap<AbstractBook, Integer> books;
    HashSet<AbstractUser> users;

    public BookshopManager(HashMap<AbstractBook, Integer> books, HashSet<AbstractUser> users) {
        this.books = books;
        this.users = users;
    }

    /*
     * Attempts to read user and book data from disk
     * @throws AbstractBook.MalformedBookCharacteristicException
     */
    void initialize(InputStream stockFileStream,
                    InputStream userAccountsFileStream) throws AbstractBook.MalformedBookCharacteristicException {
        try {
            // creates scanners over the datafiles
            Scanner stockScanner = new Scanner(stockFileStream);
            Scanner userScanner = new Scanner(userAccountsFileStream);

            while (stockScanner.hasNextLine()) {
                String[] components = stockScanner.nextLine().split(", ");

                String barcode = components[0];
                // the first component lists the type
                String title = components[2];
                AbstractBook.Language language = AbstractBook.Language.toLanguage(components[3]);
                AbstractBook.Genre genre = AbstractBook.Genre.toGenre(components[4]);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date releaseDate = formatter.parse(components[5]);
                int stockQuantity = Integer.parseInt(components[6]);
                double retailPrice = Double.parseDouble(components[7]);
                // the last two components list misc. traits

                switch (components[1]) {
                    case "paperback" -> addBooks(new Paperback(
                            barcode,
                            title,
                            language,
                            genre,
                            releaseDate,
                            retailPrice,
                            Integer.parseInt(components[8]),
                            Paperback.Condition.toCondition(components[9])
                    ), stockQuantity);

                    case "audiobook" -> addBooks(new Audiobook(
                          barcode,
                          title,
                          language,
                          genre,
                          releaseDate,
                          retailPrice,
                          Double.parseDouble(components[8]),
                          Audiobook.Format.toFormat(components[9])
                    ), stockQuantity);

                    case "ebook" -> addBooks(new Ebook(
                            barcode,
                            title,
                            language,
                            genre,
                            releaseDate,
                            retailPrice,
                            Integer.parseInt(components[8]),
                            Ebook.Format.toFormat(components[9])
                    ), stockQuantity);

                    default -> throw new AbstractBook.MalformedBookCharacteristicException(components[1]);
                }
            } stockScanner.close();

            while (userScanner.hasNextLine()) {
                String[] components = userScanner.nextLine().split(", ");

                String id = components[0];
                String username = components[1];
                String surname = components[2];
                AbstractUser.Address address = new AbstractUser.Address(
                        Integer.parseInt(components[3]),
                        components[4],
                        components[5]
                );
                // the last two components are misc. traits

                switch (components[7]) {
                    case "admin" -> addUser(new Admin(
                            id,
                            username,
                            surname,
                            address
                    ));

                    case "customer" -> addUser(new Customer(
                            id,
                            username,
                            surname,
                            address,
                            (int) Double.parseDouble(components[6]) * 100,
                            new HashMap<>()
                    ));
                }
            } userScanner.close();
        } catch (ParseException e) {
            System.out.println("Failed to initialize: ParseException");
        }
    }

    /*
     * Adds a book to the system. Guarantees that duplicates will not exist.
     * @param book an AbstractBook
     * @param count the number of books to be added
     */
    void addBooks(AbstractBook book, int count) {
        if (books.containsKey(book)) {
            int initialCount = books.get(book);
            books.replace(book, initialCount + count);
            return;
        }

        books.put(book, count);
    }

    /*
     * Adds a user to the system. Will silently fail if the user is already in the system
     * @param user an AbstractUser
     */
    void addUser(AbstractUser user) {
        users.add(user);
    }
}
