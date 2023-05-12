import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class TopBar<T extends AbstractUser> extends HBox {

    public static TopBar<Customer> getCustomerTopBar(Customer customer) {
        TopBar<Customer> topBar = getBaseTopBar(customer);

        FontIcon paymentIcon = new FontIcon("mdi2c-currency-usd");
        paymentIcon.getStyleClass().addAll("payment-icon", "icon");

        Button paymentButton = new Button();
        paymentButton.setGraphic(paymentIcon);

        paymentButton.setOnMouseClicked((event) -> Main.getPrimaryStage().setScene(PaymentScene.getPaymentScene(customer)));

        FontIcon basketIcon = new FontIcon("mdi2b-basket");
        basketIcon.getStyleClass().addAll("basket-icon", "icon");

        Button basketButton = new Button();
        basketButton.setGraphic(basketIcon);

        basketButton.setOnMouseClicked((event) -> Main.getPrimaryStage().setScene(BasketScene.getBasketScene(customer)));

        topBar.getChildren().add(2, paymentButton);
        topBar.getChildren().add(3, basketButton);
        return topBar;
    }

    public static TopBar<Admin> getAdminTopBar(Admin admin) {
        TopBar<Admin> topBar = getBaseTopBar(admin);

        FontIcon addIcon = new FontIcon("mdi2b-book-plus");
        addIcon.getStyleClass().addAll("add-icon", "icon");

        Button addButton = new Button();
        addButton.setGraphic(addIcon);
        addButton.setOnMouseClicked((event) -> Main.getPrimaryStage().setScene(AddScene.getAddScene(admin)));

        topBar.getChildren().add(2, addButton);
        return topBar;
    }

    private static <TUser extends AbstractUser> TopBar<TUser> getBaseTopBar(TUser user) {
        TopBar<TUser> root = new TopBar<>();
        root.setSpacing(12);
        root.getStyleClass().addAll("hbox", "box", "top-bar");

        FontIcon backIcon = new FontIcon("mdi2c-chevron-left");
        backIcon.getStyleClass().addAll("icon", "back-icon");

        Button backButton = new Button();
        backButton.getStyleClass().addAll("button", "back-button");
        backButton.setGraphic(backIcon);

        backButton.setOnMouseClicked((event) -> Main.getPrimaryStage().setScene(LoginScene.getLoginScene(Main.getBookshopManager())));

        HBox labelContainer = new HBox();
        labelContainer.getStyleClass().add("label-container");
        Label userLabel = new Label(getUserString(user));
        userLabel.getStyleClass().addAll("label", "user-label", "text");
        userLabel.setFont(GUIConstants.montserrat25Bold);
        labelContainer.getChildren().add(userLabel);
        HBox.setHgrow(labelContainer, Priority.ALWAYS);

        root.getChildren().addAll(backButton, labelContainer);
        return root;
    }

    private static <TUser extends AbstractUser> String getUserString(TUser user) {
        if (user.getClass() == Admin.class) {
            return getAdminString((Admin) user);
        } else if (user.getClass() == Customer.class) {
            return getCustomerString((Customer) user);
        } else {
            return "?????";
        }
    }

    private static String getCustomerString(Customer customer) {
        return customer.username +
                " (" +
                customer.id +
                ") @ " +
                customer.address.city +
                " " +
                customer.address.postcode;
    }

    private static String getAdminString(Admin admin) {
        return "Administrator: " +
                admin.username +
                " (" +
                admin.id +
                ")";
    }
}
