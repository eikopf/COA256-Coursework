import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

/**
 * Represents and controls the bookshop system, including permissions.
 * As an example, a user might pass a request to the manager.
 * Based on the type of the user and the state of the system,
 * the manager will either execute the request, or throw an error
 * to indicate that the request could not be fulfilled.
 */
public class BookshopManager {
    private SimpleMapProperty<AbstractBook, Integer> books;
    private SimpleSetProperty<AbstractUser> users;

    public BookshopManager(HashMap<AbstractBook, Integer> books, HashSet<AbstractUser> users) {
        this.books = new SimpleMapProperty<>(FXCollections.observableHashMap());
        this.books.putAll(books);
        this.users = new SimpleSetProperty<>(FXCollections.observableSet(users));
    }

    public BookshopManager() {
        this.books = new SimpleMapProperty<>(FXCollections.observableHashMap());
        this.users = new SimpleSetProperty<>(FXCollections.observableSet());
    }

    /**
     * Attempts to read user and book data from disk
     * @throws AbstractBook.MalformedBookCharacteristicException
     */
    BookshopManager initialize(InputStream stockFileStream,
                    InputStream userAccountsFileStream) throws AbstractBook.MalformedBookCharacteristicException {
        // creates scanners over the datafiles
        Scanner stockScanner = new Scanner(stockFileStream);
        Scanner userScanner = new Scanner(userAccountsFileStream);

        while (stockScanner.hasNextLine()) {
            String[] components = stockScanner.nextLine().split(", ");

            String barcode = components[0];
            // the component at index 1 lists the type
            String title = components[2];
            AbstractBook.Language language = AbstractBook.Language.toLanguage(components[3]);
            AbstractBook.Genre genre = AbstractBook.Genre.toGenre(components[4]);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate releaseDate = LocalDate.parse(components[5], formatter);
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

        return this;
    }

    /**
     * Adds a book to the system. Guarantees that duplicates will not exist.
     * @param book an AbstractBook
     * @param count the number of books to be added
     */
    void addBooks(AbstractBook book, int count) {
        if (books.get().containsKey(book)) {
            int initialCount = books.get().get(book);
            books.get().replace(book, initialCount + count);
            return;
        }

        books.getValue().put(book, count);
    }

    /**
     * Adds a user to the system. Will silently fail if the user is already in the system
     * @param user an AbstractUser
     */
    void addUser(AbstractUser user) {
        users.add(user);
    }

    public ObservableMap<AbstractBook, Integer> getBooks() {
        return books;
    }

    public ObservableSet<AbstractUser> getUsers() {
        return users;
    }
}
