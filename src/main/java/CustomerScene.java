import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class CustomerScene extends Scene {

    private Customer customer;

    private CustomerScene(Parent root, Customer customer) {
        super(root);
        this.customer = customer;
    }

    public static CustomerScene getCustomerScene(BookshopManager manager, Customer customer) {
        return new CustomerScene(new VBox(), customer);
    }
}
