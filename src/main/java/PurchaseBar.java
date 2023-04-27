import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PurchaseBar extends HBox {

    private SimpleIntegerProperty basketTotalCount;
    private SimpleDoubleProperty basketTotalPrice;
    private Customer customer;
    private SimpleMapProperty<AbstractBook, Integer> basket;

    private PurchaseBar(Customer customer) {
        this.basketTotalCount = new SimpleIntegerProperty(0);
        this.basketTotalPrice = new SimpleDoubleProperty(0);
        this.customer = customer;
        this.basket = new SimpleMapProperty<>(FXCollections.observableHashMap());
        basket.putAll(basket);

        // bind basket changes to update local values
        basket.addListener((MapChangeListener.Change<? extends AbstractBook, ? extends Integer> change) -> {
                this.basketTotalPrice.set(0);
                this.basketTotalCount.set(0);
                basket.keySet().stream().forEach((key) -> this.basketTotalPrice.add(key.retailPrice));
                basket.values().stream().forEach((value) -> this.basketTotalCount.add(value));
            });
    }

    public static PurchaseBar getPurchaseBar(Customer customer) {
        PurchaseBar root = new PurchaseBar(customer);
        root.getStyleClass().addAll("purchase-bar", "box", "hbox");
        root.setSpacing(12);

        Label totalCountLabel = new Label("x" + Integer.toString(root.getBasketTotalCount().get()));
        totalCountLabel.getStyleClass().addAll("text", "total-count", "label");
        totalCountLabel.setFont(GUIConstants.montserrat25Bold);

        VBox stretchBar = new VBox();
        HBox.setHgrow(stretchBar, Priority.ALWAYS);

        Label totalPriceLabel = new Label(Double.toString(root.getBasketTotalPrice().get()));
        totalPriceLabel.getStyleClass().addAll("text", "total-price", "label");
        totalPriceLabel.setFont(GUIConstants.montserrat25Bold);

        Button paymentButton = new Button("Purchase");
        paymentButton.getStyleClass().addAll("text", "button", "payment-button");
        paymentButton.setFont(GUIConstants.montserrat25Bold);

        root.getChildren().addAll(totalCountLabel, stretchBar, totalPriceLabel, paymentButton);
        return root;
    }

    public SimpleIntegerProperty getBasketTotalCount() {
        return this.basketTotalCount;
    }

    public SimpleDoubleProperty getBasketTotalPrice() {
        return this.basketTotalPrice;
    }
}
