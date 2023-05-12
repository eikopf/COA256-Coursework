import java.net.URL;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class UserScene<T extends AbstractUser> extends Scene {

    private static final URL styleSheetAddress = Main.class.getResource("css/user-scene.css");

    private static final EventHandler<KeyEvent> keyReleaseHandler = event -> {
        switch (event.getCode()) {
            case ESCAPE -> loadLoginScene();
            default -> {}
        }
    };

    private UserScene(Parent root) {
        super(root);
        addEventHandler(KeyEvent.KEY_RELEASED, keyReleaseHandler);
    }

    private static void loadLoginScene() {
        Main.getPrimaryStage().setScene(LoginScene.getLoginScene(Main.getBookshopManager()));
    }

    public static UserScene<Customer> getCustomerScene(BookshopManager manager, Customer customer) {
        VBox root = new VBox();
        root.setSpacing(8);
        root.getChildren().addAll(TopBar.getCustomerTopBar(customer),
                                  BookSearch.getBookSearch(manager, customer));

        UserScene<Customer> scene = new UserScene<>(root);
        scene.getStylesheets().add(styleSheetAddress.toExternalForm());

        return scene;
    }

    public static UserScene<Admin> getAdminScene(BookshopManager manager, Admin admin) {
        VBox root = new VBox();
        root.setSpacing(8);
        root.getChildren().addAll(TopBar.getAdminTopBar(admin),
                                  BookSearch.getBookSearch(manager, admin));

        UserScene<Admin> scene = new UserScene<>(root);
        scene.getStylesheets().add(styleSheetAddress.toExternalForm());

        return scene;
    }
}
