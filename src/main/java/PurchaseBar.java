import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PurchaseBar extends HBox {

    private final SimpleIntegerProperty basketTotalCount;
    private final SimpleDoubleProperty basketTotalPrice;
    private final ObservableMap<AbstractBook, Integer> basket;

    private PurchaseBar(Customer customer) {
        this.basket = customer.getBasket();
        this.basketTotalCount = new SimpleIntegerProperty(computeBasketTotalCount(basket));
        this.basketTotalPrice = new SimpleDoubleProperty(computeBasketTotalPrice(basket));

        // bind basket changes to update local values
        basket.addListener((MapChangeListener.Change<? extends AbstractBook, ? extends Integer> change) -> {
                this.basketTotalPrice.set(computeBasketTotalPrice(basket));
                this.basketTotalCount.set(computeBasketTotalCount(basket));
            });
    }

    private static double computeBasketTotalPrice(ObservableMap<AbstractBook, Integer> basket) {
        double basketPrice = 0;

        for (AbstractBook key : basket.keySet()) {
            basketPrice += basket.get(key) * key.retailPrice;
        }

        return basketPrice;
    }

    private static int computeBasketTotalCount(ObservableMap<AbstractBook, Integer> basket) {
        int count = 0;

        for (AbstractBook key : basket.keySet()) {
            count += basket.get(key);
        }

        return count;
    }

    public static PurchaseBar getPurchaseBar(Customer customer) {
        PurchaseBar root = new PurchaseBar(customer);
        root.getStyleClass().addAll("purchase-bar", "box", "hbox");
        root.setSpacing(12);

        Label totalCountLabel = new Label("x" + root.getBasketTotalCount().get());
        totalCountLabel.getStyleClass().addAll("text", "total-count", "label");
        totalCountLabel.setFont(GUIConstants.montserrat25Bold);
        totalCountLabel.setPrefWidth(60);

        VBox stretchBar = new VBox();
        stretchBar.getStyleClass().addAll("stretch-bar");
        HBox.setHgrow(stretchBar, Priority.ALWAYS);

        Label totalPriceLabel = new Label("Basket Price: " + GUIConstants.currencyFormat.format(root.getBasketTotalPrice().get()));
        totalPriceLabel.getStyleClass().addAll("text", "total-price", "label");
        totalPriceLabel.setFont(GUIConstants.montserrat25Bold);
        stretchBar.getChildren().add(totalPriceLabel);

        FontIcon clearIcon = new FontIcon("mdi2t-trash-can");
        clearIcon.getStyleClass().addAll("icon", "clear-icon");
        Button clearButton = new Button();
        clearButton.setGraphic(clearIcon);
        clearButton.setPrefHeight(50);

        clearButton.setOnMouseClicked((event) -> customer.clearBasket());

        Button paymentButton = new Button("Purchase");
        paymentButton.getStyleClass().addAll("text", "button", "payment-button");
        paymentButton.setFont(GUIConstants.montserrat25Bold);

        paymentButton.setOnMouseClicked((event) -> customer.purchase());

        root.getChildren().addAll(totalCountLabel, stretchBar, clearButton, paymentButton);
        return root;
    }

    public SimpleIntegerProperty getBasketTotalCount() {
        return this.basketTotalCount;
    }

    public SimpleDoubleProperty getBasketTotalPrice() {
        return this.basketTotalPrice;
    }
}
