import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Comparator;

/**
 * A JavaFX GUI element providing the ability to search through books in the
 * {@link BookshopManager}
 *
 */
public class BookSearch<T extends AbstractUser> extends VBox {

    private StringProperty currentQuery, previousQuery;

    private static final Comparator<AbstractBook> defaultSearchResultComparator = Comparator.comparing(a -> a.barcode);

    private final Comparator<AbstractBook> levenshteinSearchResultComparator = (a, b) -> {
        String query = this.currentQuery.get();
        return levenshtein(query, a.title.toLowerCase()) - levenshtein(query, b.title.toLowerCase());
    };

    /**
     * Computes the Levenshtein metric between two strings.
     */
    private static int levenshtein(String a, String b) {
        if (b.length() == 0) {
            return a.length();
        } else if (a.length() == 0) {
            return b.length();
        } else if (a.charAt(0) == b.charAt(0)) {
            return levenshtein(a.substring(1), b.substring(1));
        } else {
            int[] branches = new int[3];
            branches[0] = levenshtein(a.substring(1), b);
            branches[1] = levenshtein(a, b.substring(1));
            branches[2] = levenshtein(a.substring(1), b.substring(1));

            int min = branches[0];
            for (int i = 0; i < 3; i++) {
                if (branches[i] < min) {
                    min = branches[i];
                }
            }

            return min + 1;
        }
    }

    private BookSearch() {
        super();
        this.currentQuery = new SimpleStringProperty();
        this.previousQuery = new SimpleStringProperty();
    }

    public static <T extends AbstractUser> BookSearch<T> getBookSearch(BookshopManager manager, T user) {
        BookSearch<T> root = new BookSearch<>();
        root.setSpacing(12);
        VBox.setVgrow(root, Priority.ALWAYS);
        root.constructSearchBar();
        root.constructSearchResults(manager, user);
        return root;
    }

    private void constructSearchBar() {
        HBox searchBar = new HBox();
        searchBar.setSpacing(10);
        searchBar.getStyleClass().addAll("hbox", "box");

        FontIcon searchIcon = new FontIcon("mdi2b-book-search");
        searchIcon.getStyleClass().addAll("icon", "search-icon", "searchbar-icon");

        TextField searchField = new TextField();
        searchField.setPromptText("Search for a book here!");
        searchField.getStyleClass().addAll("text-field", "search-field", "text");
        searchField.textProperty().addListener((observable, oldVal, newVal) -> {
            this.currentQuery.setValue(newVal);
            this.previousQuery.setValue(oldVal);
        });
        HBox.setHgrow(searchField, Priority.ALWAYS);

        FontIcon optionsIcon = new FontIcon("mdi2d-dots-horizontal-circle");
        optionsIcon.getStyleClass().addAll("icon", "options-icon", "searchbar-icon");

        searchBar.getChildren().addAll(optionsIcon, searchField, searchIcon);
        this.getChildren().add(searchBar);
    }

    private void constructSearchResults(BookshopManager manager, T user) {
        // construct data
        ObservableList<AbstractBook> books = FXCollections.observableArrayList(manager.getBooks().keySet());

        // filter data by search term
        FilteredList<AbstractBook> filteredBooks = new FilteredList<>(books, p -> true);

        // bind changes in results to changes in query
        this.currentQuery.addListener((observable, oldVal, newVal) -> filteredBooks.setPredicate(book -> book.title.toLowerCase().contains(currentQuery.get().toLowerCase())));

        // sort data by search term
        SortedList<AbstractBook> sortedBooks = new SortedList<>(filteredBooks, defaultSearchResultComparator);

        // bind changes in results to changes in query
        this.currentQuery.addListener((observable, oldVal, newVal) -> {
            if (!newVal.isEmpty()) {
                sortedBooks.setComparator(levenshteinSearchResultComparator);
            } else {
                sortedBooks.setComparator(defaultSearchResultComparator);
            }
        });

        // construct results GUI elemnet
        ListView<AbstractBook> searchResults = new ListView<>(sortedBooks);
        searchResults.getStyleClass().addAll("list-view", "search-results");
        searchResults.setCellFactory(new BookCellFactory(manager, sortedBooks, user));
        VBox.setVgrow(searchResults, Priority.ALWAYS);

        this.getChildren().add(searchResults);
    }
}
