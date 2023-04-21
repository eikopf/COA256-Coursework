import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Provides a factory method to generate the cells in the Login scene's {@link ListView}
 */
public class LoginCellFactory implements Callback<ListView<AbstractUser>, ListCell<AbstractUser>> {

    @Override
    public ListCell<AbstractUser> call(ListView<AbstractUser> param) {
        return new ListCell<>(){
            @Override
            protected void updateItem(AbstractUser item, boolean empty) {
                super.updateItem(item, empty);
                this.getStyleClass().add("list-cell");

                if (!empty && item != null) {
                    // primary layout node
                    HBox mainContainer = new HBox();
                    mainContainer.getStyleClass().addAll("box", "hbox", "main-container");

                    // left "icon" section
                    VBox leftContainer = new VBox();
                    leftContainer.getStyleClass().addAll("box", "vbox", "left-vbox");
                    FontIcon userIcon = this.getItem().isAdmin()
                        ? new FontIcon("mdi2t-tools")
                        : new FontIcon("mdi2b-basket");

                    Label userText = this.getItem().isAdmin()
                        ? new Label("Admin")
                        : new Label("Customer");
                    userText.getStyleClass().addAll("text", "icon-subtext");
                    userText.setFont(GUIConstants.montserrat20);
                    leftContainer.getChildren().addAll(userIcon, userText);
                    leftContainer.getStyleClass().addAll("box", "vbox", "left-vbox");

                    // left "text" section
                    VBox rightContainer = new VBox();
                    rightContainer.getStyleClass().addAll("right-vbox", "box", "vbox");
                    rightContainer.setPrefWidth(400);
                    Label topRightLabel = new Label(this.getItem().getLabel());
                    topRightLabel.setFont(GUIConstants.montserrat20);
                    Label bottomRightLabel = new Label(this.getItem().getSubLabel());
                    bottomRightLabel.setFont(GUIConstants.montserrat12);
                    rightContainer.getChildren().addAll(topRightLabel, bottomRightLabel);

                    mainContainer.getChildren().addAll(leftContainer, rightContainer);

                    // tooltips
                    Tooltip tooltip= this.getItem().isAdmin()
                        ? new Tooltip("This user is an administrator, and cannot purchase items")
                        : new Tooltip("This user is a customer, and cannot alter the system");

                    this.setTooltip(tooltip);

                    // using setGraphic is required for having list cells of nodes, rather than
                    // of just raw text. without this, the nodes will only display when the
                    // cell is first clicked on.
                    this.setGraphic(mainContainer);
                } else {
                    setText("");
                }
            };
        };
    }
}
