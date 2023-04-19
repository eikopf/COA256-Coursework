import java.net.URL;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class UserScene<T extends AbstractUser> extends Scene {

    private T user;
    private static final URL styleSheetAddress = Main.class.getResource("css/user-scene.css");

    private UserScene(Parent root, T user) {
        super(root);
        this.user = user;
    }

    public static UserScene<Customer> getCustomerScene(BookshopManager manager, Customer customer) {
        VBox root = new VBox();
        root.setSpacing(8);
        root.getChildren().addAll(TopBar.getCustomerTopBar(customer),
                                  BookSearch.getBookSearch(manager));

        UserScene<Customer> scene = new UserScene<Customer>(root, customer);
        scene.getStylesheets().add(styleSheetAddress.toExternalForm());

        return scene;
    }

    public static UserScene<Admin> getAdminScene(BookshopManager manager, Admin admin) {
        VBox root = new VBox();
        root.setSpacing(8);
        root.getChildren().addAll(TopBar.getAdminTopBar(admin),
                                  BookSearch.getBookSearch(manager));

        UserScene<Admin> scene = new UserScene<Admin>(root, admin);
        scene.getStylesheets().add(styleSheetAddress.toExternalForm());

        return scene;
    }
}
