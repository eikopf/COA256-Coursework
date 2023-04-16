import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class AdminScene extends Scene{

    private Admin admin;

    private AdminScene(Parent root, Admin admin) {
        super(root);
        this.admin = admin;
    }

    public static AdminScene getAdminScene(BookshopManager manager, Admin admin) {
        return new AdminScene(new VBox(), admin);
    }
}
