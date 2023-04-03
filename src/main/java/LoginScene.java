import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class LoginScene extends Scene {

    /**
     * Custom event handler bound to KeyEvent.KEY_PRESSED at instantiation.
     */
    private static EventHandler<KeyEvent> keyHandler = event -> {
        System.out.println(event.toString());

        // switches on the specific key event that occurs
        switch (event.getCode().getCode()) {
            default -> {}
        }
    };

    public LoginScene(Parent root) {
        super(root);
        addEventHandler(KeyEvent.KEY_PRESSED, keyHandler);
    }


}
