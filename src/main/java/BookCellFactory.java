import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class BookCellFactory implements Callback<ListView<AbstractBook>, ListCell<AbstractBook>> {

    BookshopManager manager;

    public BookCellFactory(BookshopManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public ListCell<AbstractBook> call(ListView<AbstractBook> param) {
        return new ListCell<>() {
            @Override
            protected void updateItem(AbstractBook item, boolean empty) {
                super.updateItem(item, empty);
                this.getStyleClass().add("list-cell");

                if (!empty && item != null) {

                    AbstractBook book = this.getItem();

                    HBox root = new HBox();
                    root.getStyleClass().addAll("box", "hbox", "book-cell-root");

                    // book icon (breaks over type)
                    FontIcon bookIcon;
                    if (book.getClass() == Paperback.class) {
                        bookIcon = new FontIcon("mdi2b-book");
                    } else if (book.getClass() == Ebook.class) {
                        bookIcon = new FontIcon("mdi2b-book-cog");
                    } else if (book.getClass() == Audiobook.class) {
                        bookIcon = new FontIcon("mdi2b-book-music");
                    } else {
                        bookIcon = new FontIcon("mdi2f-file-question");
                    }
                    bookIcon.getStyleClass().addAll("icon", "book-icon");

                    VBox textContainer = new VBox();
                    textContainer.getStyleClass().addAll("box", "vbox", "book-cell-text-container");
                    HBox.setHgrow(textContainer, Priority.ALWAYS);

                    Label titleLabel = new Label(book.title);
                    titleLabel.getStyleClass().addAll("text", "label", "title-label", "book-cell-text");
                    titleLabel.setFont(GUIConstants.montserrat20);

                    StringBuilder subtitleBuilder = new StringBuilder(book.barcode)
                            .append(", ")
                            .append(book.language.toString())
                            .append(", ")
                            .append(book.genre)
                            .append(", ")
                            .append(book.releaseDate)
                            .append(", ");

                    // add subtype-specific details
                    if (book.getClass() == Paperback.class) {
                        subtitleBuilder.insert(0, "Paperback, ")
                            .append(((Paperback) book).pages)
                            .append(" pages, ")
                            .append(((Paperback) book).condition);
                    } else if (book.getClass() == Ebook.class) {
                        subtitleBuilder.insert(0, "Ebook, ")
                            .append(((Ebook) book).pages)
                            .append(" pages, ")
                            .append(((Ebook) book).format);
                    } else if (book.getClass() == Audiobook.class) {
                        subtitleBuilder.insert(0, "Audiobook, ")
                            .append(((Audiobook) book).format)
                            .append(", ")
                            .append(((Audiobook) book).length)
                            .append(" hrs");
                    } else {
                        subtitleBuilder.insert(0, "Unknown, ");
                    }

                    Label subtitleLabel = new Label(subtitleBuilder.toString());
                    subtitleBuilder = null; // suggestively gc the builder
                    subtitleLabel.getStyleClass().addAll("text", "label", "subtitle-label", "book-cell-text");
                    subtitleLabel.setFont(GUIConstants.montserrat12);

                    textContainer.getChildren().addAll(titleLabel, subtitleLabel);

                    // TODO: add stock count (and bind it to the model)
                    // TODO: add retail price
                    // TODO: add "add-to-basket" button (not included for admins)

                    root.getChildren().addAll(bookIcon, textContainer);
                    this.setGraphic(root);
                } else {
                    setText("");
                }
            }
        };
    }
}
