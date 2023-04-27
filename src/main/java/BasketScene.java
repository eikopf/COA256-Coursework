import java.net.URL;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BasketScene extends Scene{

    private Customer customer;
    private static BookshopManager manager = Main.getBookshopManager();
    private static URL styleSheetAddress = Main.class.getResource("css/user-scene.css");

    private EventHandler<KeyEvent> keyReleaseHandler = event -> {
        switch (event.getCode()) {
            case ESCAPE -> loadCustomerScene();
            default -> {}
        }
    };

    private void loadCustomerScene() {
        Main.getPrimaryStage().setScene(UserScene.getCustomerScene(manager, customer));
    }

    private BasketScene(Parent root, Customer customer) {
       super(root);
       this.customer = customer;
       addEventHandler(KeyEvent.KEY_RELEASED, keyReleaseHandler);
    }

    public static BasketScene getBasketScene(Customer customer) {
        VBox root = new VBox();
        root.setSpacing(12);

        BasketScene scene = new BasketScene(root, customer);

        // construct topbar and override back button functionality
        TopBar<Customer> topBar = TopBar.getCustomerTopBar(customer);
        topBar.getChildren().get(0).setOnMouseClicked((event) -> {
                scene.loadCustomerScene();
            });

        ListView<AbstractBook> basketListView = getBasketListView(customer);
        PurchaseBar purchaseBar = PurchaseBar.getPurchaseBar(customer);

        root.getChildren().addAll(topBar, basketListView, purchaseBar);
        scene.getStylesheets().add(styleSheetAddress.toExternalForm());
        return scene;
    }

    private static ListView<AbstractBook> getBasketListView(Customer customer) {
        ListView<AbstractBook> root = new ListView<>(FXCollections.observableArrayList(customer.getBasket().keySet()));
        root.getStyleClass().addAll("list-view", "basket-list-view");
        root.setCellFactory(new BasketCellFactory(customer));
        VBox.setVgrow(root, Priority.ALWAYS);

        return root;
    }

}
