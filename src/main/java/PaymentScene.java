import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PaymentScene extends Scene {

    // instance fields
    private final Customer customer;

    /**
    * This field exists only to cache the value of customer.getBalance() when the scene loads
    */
    private final double balanceOnLoad;
    private double selectedBalanceToAdd = 0;

    // static fields
    private static final BookshopManager manager = Main.getBookshopManager();
    private static final URL styleSheetAddress = Main.class.getResource("css/user-scene.css");

    private PaymentScene(Parent root, Customer customer) {
        super(root);

        this.customer = customer;
        this.balanceOnLoad = customer.getBalance();

        EventHandler<KeyEvent> keyReleaseHandler = (event) -> {
            switch (event.getCode()) {
                case ESCAPE -> loadCustomerScene();
                default -> {
                }
            }
        };
        setEventHandler(KeyEvent.KEY_RELEASED, keyReleaseHandler);
    }

    private void loadCustomerScene() {
        Main.getPrimaryStage().setScene(UserScene.getCustomerScene(manager, customer));
    }

    private void setBalanceToAdd(double newBalance) {
        this.selectedBalanceToAdd = newBalance;
    }

    public static PaymentScene getPaymentScene(Customer customer) {
        VBox root = new VBox();
        root.setSpacing(12);
        root.getStyleClass().addAll("payment-scene-root-container");

        PaymentScene scene = new PaymentScene(root, customer);
        scene.getStylesheets().add(styleSheetAddress.toExternalForm());

        TopBar<Customer> topBar = TopBar.getCustomerTopBar(customer);
        topBar.getChildren().get(0).setOnMouseClicked((event) -> scene.loadCustomerScene());

        VBox paymentSceneInteractableContainer = new VBox();
        paymentSceneInteractableContainer.getStyleClass().add("payment-scene-interactable-container");
        paymentSceneInteractableContainer.setSpacing(20);
        VBox.setVgrow(paymentSceneInteractableContainer, Priority.ALWAYS);

        HBox currentBalanceContainer = new HBox();
        currentBalanceContainer.getStyleClass().addAll("box", "hbox", "current-balance-container");
        currentBalanceContainer.setMaxWidth(150 * 3);
        currentBalanceContainer.setSpacing(12);
        Label currentBalanceLabel = new Label("Current Balance: ");
        currentBalanceLabel.setFont(GUIConstants.montserrat20);
        HBox currentBalanceFillerField = new HBox();
        HBox.setHgrow(currentBalanceFillerField, Priority.ALWAYS);
        Label currentBalanceValueLabel = new Label(GUIConstants.currencyFormat.format(scene.balanceOnLoad));
        currentBalanceValueLabel.setFont(GUIConstants.montserrat20);
        currentBalanceContainer.getChildren().addAll(currentBalanceLabel,
                                                     currentBalanceFillerField,
                                                     currentBalanceValueLabel);

        HBox additionalBalanceContainer = new HBox();
        additionalBalanceContainer.getStyleClass().addAll("box", "hbox", "additional-balance-container");
        additionalBalanceContainer.setMaxWidth(150 * 3);
        additionalBalanceContainer.setSpacing(12);
        Label additionalBalanceLabel = new Label("Additional Balance: ");
        additionalBalanceLabel.setFont(GUIConstants.montserrat20);
        HBox additionalBalanceFillerField = new HBox();
        HBox.setHgrow(additionalBalanceFillerField, Priority.ALWAYS);
        scene.selectedBalanceToAdd = 0;
        TextField additionalBalanceField = new TextField(GUIConstants.currencyFormat.format(0));
        additionalBalanceField.textProperty().addListener((change) -> {
                String newVal = additionalBalanceField.getText();
                if (newVal.length() == 0) return;

                try {
                    double newBalance;
                    if (newVal.charAt(0) == '$') {
                        newBalance = GUIConstants.currencyFormat.parse(newVal).doubleValue();
                        scene.setBalanceToAdd(newBalance);
                    } else {
                        newBalance = Double.parseDouble(newVal);
                        scene.setBalanceToAdd(newBalance);
                    }
                } catch (NumberFormatException | ParseException ignored) {

                }
        });
        additionalBalanceField.setPrefWidth(150);
        additionalBalanceField.setFont(GUIConstants.montserrat20);
        additionalBalanceContainer.getChildren().addAll(additionalBalanceLabel,
                                                        additionalBalanceFillerField,
                                                        additionalBalanceField);

        HBox confirmButtonContainer = new HBox();
        confirmButtonContainer.getStyleClass().addAll("box", "hbox", "confirm-button-container");
        confirmButtonContainer.setMaxWidth(150 * 3);
        confirmButtonContainer.setSpacing(12);
        HBox confirmButtonFillerField = new HBox();
        HBox.setHgrow(confirmButtonFillerField, Priority.ALWAYS);
        Button confirmButton = new Button("Confirm");
        confirmButton.setFont(GUIConstants.montserrat20);
        confirmButton.setOnAction((event) -> {
                try {
                    manager.setUserBalance(customer, scene.balanceOnLoad + scene.selectedBalanceToAdd);
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
        });
        confirmButton.setPrefWidth(150);
        confirmButtonContainer.getChildren().addAll(confirmButtonFillerField, confirmButton);

        paymentSceneInteractableContainer.getChildren().addAll(currentBalanceContainer,
                                                               additionalBalanceContainer,
                                                               confirmButtonContainer);
        root.getChildren().addAll(topBar, paymentSceneInteractableContainer);
        return scene;
    }
}
