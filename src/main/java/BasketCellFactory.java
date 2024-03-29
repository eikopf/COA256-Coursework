import org.kordamp.ikonli.javafx.FontIcon;

import javafx.collections.ObservableMap;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class BasketCellFactory implements Callback<ListView<AbstractBook>, ListCell<AbstractBook>> {

    private final ObservableMap<AbstractBook, Integer> basket;

    public BasketCellFactory(Customer customer) {
        this.basket = customer.getBasket();
    }

    private static String getMajorText(AbstractBook book) {
        StringBuilder builder = new StringBuilder();

        if (book.getClass() == Paperback.class) {
            builder.append("Paperback")
                    .append(", ")
                    .append(((Paperback) book).condition);
        } else if (book.getClass() == Ebook.class) {
            builder.append("Ebook")
                    .append(", ")
                    .append(((Ebook) book).format);
        } else if (book.getClass() == Audiobook.class) {
            builder.append("Audiobook")
                    .append(", ")
                    .append(((Audiobook) book).format);
        } else {
            builder.append("Unknown Type");
        }

        return builder.toString();
    }

    @Override
    public ListCell<AbstractBook> call(ListView<AbstractBook> param) {
        return new ListCell<>() {
            @Override
            protected void updateItem(AbstractBook item, boolean empty) {
                super.updateItem(item, empty);
                this.getStyleClass().add("list-cell");

                if (!empty && item != null) {

                    // get data for construction
                    AbstractBook book = this.getItem();
                    int quantity = BasketCellFactory.this.basket.get(book);
                    double unitPrice = book.retailPrice;
                    double totalPrice = unitPrice * quantity;

                    HBox root = new HBox();
                    root.setSpacing(12);
                    root.getStyleClass().addAll("box", "hbox", "basket-root");

                    Label quantityLabel = new Label("x" + quantity);
                    quantityLabel.getStyleClass().addAll("text", "quantity-text");
                    quantityLabel.setFont(GUIConstants.montserrat25Bold);
                    quantityLabel.setPrefWidth(60);

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
                    textContainer.getStyleClass().addAll("box", "vbox", "basket-cell-text-container");
                    HBox.setHgrow(textContainer, Priority.ALWAYS);

                    Label majorLabel = new Label(book.title);
                    majorLabel.getStyleClass().addAll("text", "label", "basket-major-label");
                    majorLabel.setFont(GUIConstants.montserrat20);

                    Label minorLabel = new Label(getMajorText(book));
                    minorLabel.getStyleClass().addAll("text", "label", "basket-minor-label");
                    minorLabel.setFont(GUIConstants.montserrat12);

                    textContainer.getChildren().addAll(majorLabel, minorLabel);

                    VBox priceContainer = new VBox();
                    priceContainer.getStyleClass().addAll("box", "vbox", "basket-cell-price-container");
                    priceContainer.setPrefWidth(80);

                    Label totalPriceLabel = new Label(GUIConstants.currencyFormat.format(totalPrice));
                    totalPriceLabel.getStyleClass().addAll("text", "price-label", "label");
                    totalPriceLabel.setFont(GUIConstants.montserrat20);

                    Label unitPriceLabel = new Label(quantity +
                                                     " x " +
                                                     GUIConstants.currencyFormat.format(unitPrice));
                    unitPriceLabel.getStyleClass().addAll("text", "price-label", "label");
                    unitPriceLabel.setFont(GUIConstants.montserrat12Italic);

                    priceContainer.getChildren().addAll(totalPriceLabel, unitPriceLabel);

                    root.getChildren().addAll(quantityLabel, bookIcon, textContainer, priceContainer);
                    this.setGraphic(root);

                } else {
                    setText(null);
                    setGraphic(null);
                }
            }
        };
    }
}
