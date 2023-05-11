import java.net.URL;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class PaymentScene extends Scene {

    // instance fields
    private Customer customer;

    /**
    * This field exists only to cache the value of customer.getBalance() when the scene loads
    */
    private double balanceOnLoad;
    private double selectedBalanceToAdd = 0;

    private EventHandler<KeyEvent> keyReleaseHandler = (event) -> {
        switch (event.getCode()) {
            case ESCAPE -> loadCustomerScene();
            default -> {}
        }
    };

    // static fields
    private static BookshopManager manager = Main.getBookshopManager();
    private static URL styleSheetAddress = Main.class.getResource("css/user-scene.css");

    private PaymentScene(Parent root, Customer customer) {
        super(root);

        this.customer = customer;
        this.balanceOnLoad = customer.getBalance();

        setEventHandler(KeyEvent.KEY_RELEASED, keyReleaseHandler);
    }

    private void loadCustomerScene() {
        Main.getPrimaryStage().setScene(UserScene.getCustomerScene(manager, customer));
    }

    public static PaymentScene getPaymentScene(Customer customer) {
        VBox root = new VBox();
        root.setSpacing(12);

        PaymentScene scene = new PaymentScene(root, customer);
        scene.getStylesheets().add(styleSheetAddress.toExternalForm());

        TopBar<Customer> topBar = TopBar.getCustomerTopBar(customer);
        topBar.getChildren().get(0).setOnMouseClicked((event) -> {
                scene.loadCustomerScene();
        });

        // do stuff...

        root.getChildren().addAll(topBar);
        return scene;
    }
}
