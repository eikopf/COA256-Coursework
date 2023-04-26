import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class TopBar<T extends AbstractUser> extends HBox {

    public static TopBar<Customer> getCustomerTopBar(Customer customer) {
        TopBar<Customer> topBar = getBaseTopBar(customer);

        FontIcon paymentIcon = new FontIcon("mdi2c-currency-gbp");
        paymentIcon.getStyleClass().addAll("payment-icon", "icon");
        FontIcon basketIcon = new FontIcon("mdi2b-basket");
        basketIcon.getStyleClass().addAll("basket-icon", "icon");
        basketIcon.setOnMouseClicked((event) -> {
                Main.getPrimaryStage().setScene(BasketScene.getBasketScene(customer));
            });

        topBar.getChildren().add(2, paymentIcon);
        topBar.getChildren().add(3, basketIcon);
        return topBar;
    }

    public static TopBar<Admin> getAdminTopBar(Admin admin) {
        TopBar<Admin> topBar = getBaseTopBar(admin);

        FontIcon addIcon = new FontIcon("mdi2b-book-plus");
        addIcon.getStyleClass().addAll("add-icon", "icon");

        topBar.getChildren().add(2, addIcon);
        return topBar;
    }

    private static <TUser extends AbstractUser> TopBar<TUser> getBaseTopBar(TUser user) {
        TopBar<TUser> root = new TopBar<>();
        root.setSpacing(12);
        root.getStyleClass().addAll("hbox", "box");

        FontIcon backIcon = new FontIcon("mdi2c-chevron-left");
        backIcon.getStyleClass().addAll("icon", "back-icon");
        backIcon.setOnMouseClicked((event) -> {
                Main.getPrimaryStage().setScene(LoginScene.getLoginScene(Main.getBookshopManager()));
            });

        HBox labelContainer = new HBox();
        labelContainer.getStyleClass().add("label-container");
        Label userLabel = new Label(getUserString(user));
        userLabel.getStyleClass().addAll("label", "user-label", "text");
        userLabel.setFont(GUIConstants.montserrat25Bold);
        labelContainer.getChildren().add(userLabel);
        HBox.setHgrow(labelContainer, Priority.ALWAYS);

        FontIcon settingsIcon = new FontIcon("mdi2d-dots-horizontal-circle");
        settingsIcon.getStyleClass().addAll("settings-icon", "icon");

        root.getChildren().addAll(backIcon, labelContainer, settingsIcon);
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
        return new StringBuilder(customer.username)
            .append(" (")
            .append(customer.id)
            .append(") @ ")
            .append(customer.address.city)
            .append(" ")
            .append(customer.address.postcode)
            .toString();
    }

    private static String getAdminString(Admin admin) {
        return new StringBuilder("Administrator: ")
            .append(admin.username)
            .append(" (")
            .append(admin.id)
            .append(")")
            .toString();
    }
}
