import java.net.URL;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class LoginScene extends Scene {

    private static SelectionModel<AbstractUser> selectedUserModel;

    /**
     * Custom event handler bound to {@link KeyEvent}.KEY_PRESSED at instantiation.
     */
    private static EventHandler<KeyEvent> keyPressHandler = event -> {

        // switches on the specific key event that occurs
        switch (event.getCode()) {
            default -> {}
        }
    };

    /**
     * Custom event handler bound to {@link KeyEvent}.KEYRELEASED at instantiation.
     */
    private static EventHandler<KeyEvent> keyReleaseHandler = event -> {

        // switches on the specific key event that occurs
        switch (event.getCode()) {
            case ENTER -> loadUserScene();
            default -> {}
        }
    };

    private static void loadUserScene() {
       AbstractUser selectedUser = LoginScene.selectedUserModel.getSelectedItem();

       if (selectedUser.isAdmin()) {
           Main.getPrimaryStage().setScene(AdminScene.getAdminScene(Main.getBookshopManager(),
                                                                    (Admin) selectedUser));
       } else {
           Main.getPrimaryStage().setScene(CustomerScene.getCustomerScene(Main.getBookshopManager(),
                                                                          (Customer) selectedUser));
       }
    }

   /**
    * This comparator is broken out from the instantiation of the user list,
    * to allow for it to be more easily adjusted.
    */
    private static Comparator<AbstractUser> userComparator = (a, b) -> {
        return a.id.compareTo(b.id);
    };

    private LoginScene(Parent root, BookshopManager manager) {
        super(root);
        addEventHandler(KeyEvent.KEY_PRESSED, keyPressHandler);
        addEventHandler(KeyEvent.KEY_RELEASED, keyReleaseHandler);

    }

   /**
    *  The {@link Scene} class provides no zero-parameter constructor, so this wrapper
    *  ensures that every LoginScene has the same root node.
    */
    public static LoginScene getLoginScene(BookshopManager manager) {
        ObservableList<AbstractUser> users = FXCollections.observableArrayList(manager.getUsers());
        SortedList<AbstractUser> sortedUsers = new SortedList<>(users, userComparator);

        ListView<AbstractUser> userListView = new ListView<>(sortedUsers);
        userListView.setCellFactory(new LoginCellFactory());

        VBox pane = new VBox();
        pane.getStyleClass().add("pane");
        userListView.setPadding(new Insets(20, 20, 20, 20));
        userListView.setPrefHeight(600);
        LoginScene.selectedUserModel = userListView.getSelectionModel();
        LoginScene.selectedUserModel.selectFirst();

        pane.getChildren().add(userListView);

        LoginScene scene = new LoginScene(pane, manager);
        URL styleSheet = Main.class.getResource("css/login-scene.css");
        scene.getStylesheets().add(styleSheet.toExternalForm());

        return scene;
    }
}
