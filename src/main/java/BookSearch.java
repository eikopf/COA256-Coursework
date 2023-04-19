import java.util.Comparator;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * A JavaFX GUI element providing the ability to search through books in the
 * {@link BookshopManager}
 *
 */
public class BookSearch extends VBox {

    private static Comparator<AbstractBook> defaultSearchResultComparator = (a, b) -> {
        return b.barcode.compareTo(a.barcode);
    };

    private static Comparator<AbstractBook> levenshteinSearchResultComparator = (a, b) -> {
        return levenshtein(a.title, b.title);
    };

    /**
     * Computes the Levenshtein metric between two strings.
     */
    public static int levenshtein(String a, String b) {
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
    }

    public static BookSearch getBookSearch(BookshopManager manager) {
        BookSearch root = new BookSearch();
        root.setSpacing(12);
        VBox.setVgrow(root, Priority.ALWAYS);
        root.getChildren().addAll(constructSearchBar(), constructSearchResults(manager));
        return root;
    }

    private static HBox constructSearchBar() {
        HBox searchBar = new HBox();
        searchBar.setSpacing(10);
        searchBar.getStyleClass().addAll("hbox", "box");

        FontIcon searchIcon = new FontIcon("mdi2b-book-search");
        searchIcon.getStyleClass().addAll("icon", "search-icon", "searchbar-icon");

        TextField searchField = new TextField();
        searchField.setPromptText("Search for a book here!");
        searchField.getStyleClass().addAll("text-field", "search-field", "text");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        FontIcon optionsIcon = new FontIcon("mdi2d-dots-horizontal-circle");
        optionsIcon.getStyleClass().addAll("icon", "options-icon", "searchbar-icon");

        searchBar.getChildren().addAll(optionsIcon, searchField, searchIcon);
        return searchBar;
    }

    private static ListView<AbstractBook> constructSearchResults(BookshopManager manager) {
        ObservableList<AbstractBook> books = FXCollections.observableArrayList(manager.getBooks().keySet());
        SortedList<AbstractBook> sortedBooks = new SortedList<>(books, defaultSearchResultComparator);
        ListView<AbstractBook> searchResults = new ListView<>(sortedBooks);
        searchResults.getStyleClass().addAll("list-view", "search-results");
        VBox.setVgrow(searchResults, Priority.ALWAYS);

        return searchResults;
    }
}
