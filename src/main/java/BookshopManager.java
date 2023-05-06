import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

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
     *
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
                        Paperback.Condition.toCondition(components[9])), stockQuantity);

                case "audiobook" -> addBooks(new Audiobook(
                        barcode,
                        title,
                        language,
                        genre,
                        releaseDate,
                        retailPrice,
                        Double.parseDouble(components[8]),
                        Audiobook.Format.toFormat(components[9])), stockQuantity);

                case "ebook" -> addBooks(new Ebook(
                        barcode,
                        title,
                        language,
                        genre,
                        releaseDate,
                        retailPrice,
                        Integer.parseInt(components[8]),
                        Ebook.Format.toFormat(components[9])), stockQuantity);

                default -> throw new AbstractBook.MalformedBookCharacteristicException(components[1]);
            }
        }
        stockScanner.close();

        while (userScanner.hasNextLine()) {
            String[] components = userScanner.nextLine().split(", ");

            String id = components[0];
            String username = components[1];
            String surname = components[2];
            AbstractUser.Address address = new AbstractUser.Address(
                    Integer.parseInt(components[3]),
                    components[4],
                    components[5]);
            // the last two components are misc. traits

            switch (components[7]) {
                case "admin" -> addUser(new Admin(
                        id,
                        username,
                        surname,
                        address));

                case "customer" -> addUser(new Customer(
                        id,
                        username,
                        surname,
                        address,
                        (int) Double.parseDouble(components[6]) * 100,
                        new HashMap<>()));
            }
        }
        userScanner.close();

        return this;
    }

    public void processBookAddition(AbstractBook book, int addCount) throws IOException, URISyntaxException {
        // fetch resource
        URL stockFileURL = Main.class.getResource("/datafiles/Stock.txt");

        // get read-handle to file & init builder
        BufferedReader reader = Files.newBufferedReader(Paths.get(stockFileURL.toURI()));
        StringBuilder builder = new StringBuilder();

        // iterate over reader and append to builder
        // make changes by matching barcodes
        boolean alteredBufferFlag = false;
        for (String line : reader.lines().toList()) {
            if (!book.barcode.equals(line.substring(0, 8))) {
                builder.append(line);
            } else {
                String[] fields = line.split(", ");
                fields[6] = Integer.toString(addCount + Integer.parseInt(fields[6]));
                this.setCount(book, Integer.parseInt(fields[6]));

                alteredBufferFlag = true;
            }

            builder.append("\n");
        }

        // if no change was registered, append a new book to the stock file
        if (alteredBufferFlag) {
            builder.delete(builder.length() - 1, builder.length());
        } else {
            builder.append(book.toDataString(addCount));
        }

        // stop reading file & initialize writer in WRITE mode
        reader.close();
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(stockFileURL.toURI()),
                                                        StandardOpenOption.WRITE);

        writer.write(builder.toString());
        writer.close(); // implicit flush
    }

    public void processPurchase(Customer customer, Map<AbstractBook, Integer> basket)
            throws IOException, URISyntaxException {
        // fetch resource
        URL stockFileURL = Main.class.getResource("/datafiles/Stock.txt");

        // get read-handle to file & init builder
        BufferedReader reader = Files.newBufferedReader(Paths.get(stockFileURL.toURI()));
        StringBuilder builder = new StringBuilder();

        // build barcode-book map
        HashMap<String, AbstractBook> barcodeMap = new HashMap<>();
        basket.forEach((book, count) -> {
            barcodeMap.put(book.barcode, book);
        });

        // iterate over reader and append to builder
        // make changes by matching barcode
        reader.lines().forEachOrdered((line) -> {
            if (!barcodeMap.containsKey(line.substring(0, 8))) {
                builder.append(line);
            } else {
                String[] fields = line.split(", ");
                AbstractBook book = barcodeMap.get(fields[0]);
                int updatedCount = books.get(book) - basket.get(book);
                fields[6] = Integer.toString(updatedCount);
                this.setCount(book, updatedCount);

                // remove book from file if count <= 0
                if (Integer.parseInt(fields[6]) <= 0)
                    return;

                StringBuilder resultBuilder = new StringBuilder();
                Arrays.stream(fields).forEachOrdered((field) -> {
                    resultBuilder.append(field + ", ");
                });

                builder.append(resultBuilder.substring(0, resultBuilder.length() - 2));
            }

            builder.append("\n");
        });
        reader.close();
        builder.delete(builder.length() - 1, builder.length());

        // init buffwriter in WRITE mode
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(stockFileURL.toURI()),
                StandardOpenOption.WRITE);

        // write builder with buffwriter and close (implicitly flush) buffwriter
        writer.write(builder.toString());
        writer.close();

        // update user data
        setUserBalance(customer, customer.getBalance() - customer.getBasketPrice());
    }

    public void setUserBalance(Customer customer, double newBalance) throws IOException, URISyntaxException {
        // fetch resource
        URL userFileURL = Main.class.getResource("/datafiles/UserAccounts.txt");

        // get read-handle to file & init builder
        BufferedReader reader = Files.newBufferedReader(Paths.get(userFileURL.toURI()));
        StringBuilder builder = new StringBuilder();

        // write file contents + changes to builder
        reader.lines().forEachOrdered((line) -> {
            if (!line.substring(0, 3).equals(customer.id) || // ignore other ids
                    line.charAt(line.length() - 1) == 'n') { // ignore admins
                builder.append(line);
            } else {
                String[] fields = line.split(", ");

                fields[6] = Double.toString(newBalance);
                StringBuilder resultBuilder = new StringBuilder();
                Arrays.stream(fields).forEachOrdered((field) -> {
                    resultBuilder.append(field + ", ");
                });

                // append result without the final extraneous ", "
                builder.append(resultBuilder.substring(0, resultBuilder.length() - 2));
            }

            builder.append("\n"); // append new line
        });
        reader.close();
        builder.delete(builder.length() - 1, builder.length());

        // construct writer & overwrite file
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(userFileURL.toURI()),
                StandardOpenOption.WRITE);
        // write builder to file & close stream
        writer.write(builder.toString());
        writer.close(); // implicitly flushes the stream
    }

    /**
     * Adds a book to the system. Guarantees that duplicates will not exist.
     *
     * @param book  an AbstractBook
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
     * Adds a user to the system. Will silently fail if the user is already in the
     * system
     *
     * @param user an AbstractUser
     */
    void addUser(AbstractUser user) {
        users.add(user);
    }

    void setCount(AbstractBook book, int count) {
        if (count <= 1)
            this.books.remove(book);

        this.books.put(book, count);
    }

    public ObservableMap<AbstractBook, Integer> getBooks() {
        return books;
    }

    public ObservableSet<AbstractUser> getUsers() {
        return users;
    }
}
