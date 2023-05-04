import org.kordamp.ikonli.javafx.FontIcon;

import javafx.collections.transformation.SortedList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class BookCellFactory implements Callback<ListView<AbstractBook>, ListCell<AbstractBook>> {

    BookshopManager manager;
    AbstractUser user;
    SortedList<AbstractBook> items;

    public BookCellFactory(BookshopManager manager, SortedList<AbstractBook> items, AbstractUser user) {
        super();
        this.manager = manager;
        this.items = items;
        this.user = user;
    }

    private static String getMajorSubtitle(AbstractBook book) {
        StringBuilder builder = new StringBuilder();

        if (book.getClass() == Paperback.class) {
            builder.append("Paperback")
                    .append(", ")
                    .append(((Paperback) book).condition)
                    .append(", ")
                    .append(((Paperback) book).pages)
                    .append(" pages");
        } else if (book.getClass() == Ebook.class) {
            builder.append("Ebook")
                    .append(", ")
                    .append(((Ebook) book).format)
                    .append(", ")
                    .append(((Ebook) book).pages)
                    .append(" pages");
        } else if (book.getClass() == Audiobook.class) {
            builder.append("Audiobook")
                    .append(", ")
                    .append(((Audiobook) book).format)
                    .append(", ")
                    .append(((Audiobook) book).length)
                    .append(" hours");
        } else {
            builder.append("Unknown Type");
        }

        return builder.toString();
    }

    private static String getMinorSubtitle(AbstractBook book) {
        return new StringBuilder(book.language.toString())
                .append(", ")
                .append(book.genre)
                .append(", ")
                .append("Released ")
                .append(book.releaseDate)
                .toString();
    }

    private static String getTitle(AbstractBook book) {
        return new StringBuilder(book.title)
                .append(" (")
                .append(book.barcode)
                .append(")")
                .toString();
    }

    @Override
    public ListCell<AbstractBook> call(ListView<AbstractBook> param) {
        return new ListCell<>() {
            @Override
            protected void updateItem(AbstractBook item, boolean empty) {
                super.updateItem(item, empty);
                this.getStyleClass().add("list-cell");

                if (!empty && item != null && items.contains(item)) {

                    AbstractBook book = this.getItem();
                    int stock = manager.getBooks().get(book);

                    HBox root = new HBox();
                    root.getStyleClass().addAll("box", "hbox", "book-cell-root");
                    root.setSpacing(8);

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

                    Label titleLabel = new Label(getTitle(book));
                    titleLabel.getStyleClass().addAll("text", "label", "title-label", "book-cell-text");
                    titleLabel.setFont(GUIConstants.montserrat20);

                    Label majorSubtitleLabel = new Label(getMajorSubtitle(book));
                    majorSubtitleLabel.getStyleClass().addAll("text", "label", "subtitle-label", "book-cell-text");
                    majorSubtitleLabel.setFont(GUIConstants.montserrat12);

                    Label minorSubtitleLabel = new Label(getMinorSubtitle(book));
                    minorSubtitleLabel.getStyleClass().addAll("text", "label", "subtitle-label", "book-cell-text");
                    minorSubtitleLabel.setFont(GUIConstants.montserrat12);

                    textContainer.getChildren().addAll(titleLabel, majorSubtitleLabel, minorSubtitleLabel);

                    VBox valueContainer = new VBox();
                    valueContainer.getStyleClass().addAll("box", "vbox", "book-cell-value-container");

                    Label stockCountLabel = new Label("Remaining: " + Integer.toString(stock));
                    stockCountLabel.setFont(GUIConstants.montserrat12);
                    stockCountLabel.getStyleClass().addAll("text", "label", "stock-label", "book-cell-text");

                    Label retailPriceLabel = new Label("Price: " + GUIConstants.currencyFormat.format(book.retailPrice));
                    retailPriceLabel.setFont(GUIConstants.montserrat12);
                    retailPriceLabel.getStyleClass().addAll("text", "label", "price-label", "book-cell-text");

                    valueContainer.getChildren().addAll(retailPriceLabel, stockCountLabel);

                    if (user.getClass() == Customer.class) {
                        FontIcon addIcon = new FontIcon("mdi2b-basket-plus");
                        addIcon.getStyleClass().addAll("icon", "add-book-icon");

                        Button addButton = new Button();
                        addButton.getStyleClass().addAll("button", "add-button", "icon-button");
                        addButton.setGraphic(addIcon);

                        addButton.setOnMouseClicked((event) -> {
                                ((Customer) user).incrementCountInBasket(book);
                            });

                        root.getChildren().addAll(bookIcon, textContainer, valueContainer, addButton);
                    } else {
                        root.getChildren().addAll(bookIcon, textContainer, valueContainer);
                    }

                    this.setGraphic(root);

                } else {
                    setText(null);
                    setGraphic(null);
                }
            }
        };
    }
}
