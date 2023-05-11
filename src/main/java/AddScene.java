import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AddScene extends Scene {
    private Admin admin;

    // general book fields
    private Label selectedBookType;
    private IntegerProperty selectedBookProperty;
    private String selectedBookTitle;
    private AbstractBook.Genre selectedBookGenre;
    private AbstractBook.Language selectedBookLanguage;
    private LocalDate selectedBookDate;
    private double selectedBookRetailPrice;

    // audiobook fields
    private Audiobook.Format selectedAudiobookFormat;
    private double selectedAudiobookLength;

    // ebook fields
    private Ebook.Format selectedEbookFormat;
    private int selectedEbookPages;

    // paperback fields
    private Paperback.Condition selectedPaperbackCondition;
    private int selectedPaperbackPages;

    // static fields
    private static BookshopManager manager = Main.getBookshopManager();
    private static final URL styleSheetAddress = Main.class.getResource("css/user-scene.css");
    private static final Random source = new Random();

    // key handler
    private EventHandler<KeyEvent> keyReleaseHandler = event -> {
        switch (event.getCode()) {
            case ESCAPE -> loadAdminScene();
            default -> {
            }
        }
    };

    private AddScene(Parent root, Admin admin) {
        super(root);
        this.admin = admin;
        this.selectedBookProperty = new SimpleIntegerProperty(0);
        addEventHandler(KeyEvent.KEY_RELEASED, keyReleaseHandler);
    }

    public static AddScene getAddScene(Admin admin) {
        VBox root = new VBox();
        root.getStyleClass().addAll("box", "vbox", "add-scene-root-container");

        AddScene scene = new AddScene(root, admin);
        scene.getStylesheets().add(styleSheetAddress.toExternalForm());

        // instantiate and alter topBar
        TopBar<Admin> topBar = TopBar.getAdminTopBar(admin);
        topBar.getChildren().get(0).setOnMouseClicked(event -> scene.loadAdminScene());

        // construct interactable section
        VBox interactableContainer = new VBox();
        interactableContainer.getStyleClass().addAll("box", "vbox", "add-scene-interactable-container");
        interactableContainer.setSpacing(8);
        VBox.setVgrow(interactableContainer, Priority.ALWAYS);

        // container for type selection labels
        HBox typeSelectContainer = new HBox();
        typeSelectContainer.getStyleClass().addAll("box", "hbox", "type-select-container");
        typeSelectContainer.setSpacing(12);

        // type selection labels
        Label[] typeLabels = new Label[3];
        typeLabels[0] = new Label("Paperback");
        typeLabels[1] = new Label("Audiobook");
        typeLabels[2] = new Label("Ebook");

        // set default selection
        scene.selectedBookType = typeLabels[0];
        scene.selectedBookProperty.set(0);
        scene.selectedBookType.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));

        // changes for all 3 labels
        Arrays.stream(typeLabels).forEach(label -> {
            label.setFont(GUIConstants.montserrat20);
            label.getStyleClass().addAll("text", "type-select-label");
            label.setPrefWidth(150);
            label.setOnMouseClicked(event -> {
                scene.selectedBookType.setBackground(null);
                scene.selectedBookType = label;
                scene.selectedBookType.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
                if (label.getText().charAt(0) == 'P') {
                    scene.selectedBookProperty.set(0);
                } else if (label.getText().charAt(0) == 'A') {
                    scene.selectedBookProperty.set(1);
                } else {
                    scene.selectedBookProperty.set(2);
                }
            });
        });

        typeSelectContainer.getChildren().addAll(typeLabels);

        // title field
        HBox titleEntryContainer = new HBox();
        titleEntryContainer.getStyleClass().addAll("box", "hbox", "add-field-container");
        titleEntryContainer.setMaxWidth(150 * 3);
        titleEntryContainer.setSpacing(12);
        Label titleLabel = new Label("Title: ");
        titleLabel.setFont(GUIConstants.montserrat20);
        TextField titleField = new TextField();
        titleField.setOnKeyReleased((event) -> {
            scene.selectedBookTitle = titleField.getText();
        });
        titleField.setFont(GUIConstants.montserrat12Italic);
        HBox.setHgrow(titleField, Priority.ALWAYS);
        titleEntryContainer.getChildren().addAll(titleLabel, titleField);

        // genre dropdown
        HBox genreEntryContainer = new HBox();
        genreEntryContainer.getStyleClass().addAll("box", "hbox", "add-field-container");
        genreEntryContainer.setMaxWidth(150 * 3);
        genreEntryContainer.setSpacing(12);
        Label genreLabel = new Label("Genre: ");
        genreLabel.setFont(GUIConstants.montserrat20);
        HBox genreFillerField = new HBox();
        HBox.setHgrow(genreFillerField, Priority.ALWAYS);
        ObservableList<AbstractBook.Genre> genreItems = FXCollections.observableArrayList(AbstractBook.Genre.Business,
                AbstractBook.Genre.Biography,
                AbstractBook.Genre.ComputerScience,
                AbstractBook.Genre.Politics);
        ComboBox<AbstractBook.Genre> genreBox = new ComboBox<>(genreItems);
        genreBox.getSelectionModel().selectedItemProperty().addListener((change) -> {
            scene.selectedBookGenre = genreBox.getSelectionModel().getSelectedItem();
        });
        genreBox.setPrefWidth(150);
        genreEntryContainer.getChildren().addAll(genreLabel, genreFillerField, genreBox);

        // language dropdown
        HBox languageEntryContainer = new HBox();
        languageEntryContainer.getStyleClass().addAll("box", "hbox", "add-field-container");
        languageEntryContainer.setMaxWidth(150 * 3);
        languageEntryContainer.setSpacing(12);
        Label languageLabel = new Label("Language: ");
        languageLabel.setFont(GUIConstants.montserrat20);
        HBox languageFillerField = new HBox();
        HBox.setHgrow(languageFillerField, Priority.ALWAYS);
        ObservableList<AbstractBook.Language> languageItems = FXCollections.observableArrayList(
                AbstractBook.Language.English,
                AbstractBook.Language.French);
        ComboBox<AbstractBook.Language> languageBox = new ComboBox<>(languageItems);
        languageBox.getSelectionModel().selectedItemProperty().addListener((change) -> {
            scene.selectedBookLanguage = languageBox.getSelectionModel().getSelectedItem();
        });
        languageBox.setPrefWidth(150);
        languageEntryContainer.getChildren().addAll(languageLabel, languageFillerField, languageBox);

        // publication date calendar view
        HBox dateEntryContainer = new HBox();
        dateEntryContainer.getStyleClass().addAll("box", "hbox", "add-field-container");
        dateEntryContainer.setMaxWidth(150 * 3);
        dateEntryContainer.setSpacing(12);
        Label dateLabel = new Label("Publication Date:");
        dateLabel.setFont(GUIConstants.montserrat20);
        HBox dateFillerField = new HBox();
        HBox.setHgrow(dateFillerField, Priority.ALWAYS);
        DatePicker datePicker = new DatePicker(LocalDate.now());
        scene.selectedBookDate = LocalDate.now();
        datePicker.valueProperty().addListener((change) -> {
            scene.selectedBookDate = datePicker.getValue();
        });
        datePicker.setPrefWidth(150);
        dateEntryContainer.getChildren().addAll(dateLabel, dateFillerField, datePicker);

        // retail price field
        HBox priceEntryContainer = new HBox();
        priceEntryContainer.getStyleClass().addAll("box", "hbox", "add-field-container");
        priceEntryContainer.setMaxWidth(150 * 3);
        priceEntryContainer.setSpacing(12);
        Label priceLabel = new Label("Retail Price: ");
        priceLabel.setFont(GUIConstants.montserrat20);
        HBox priceFillerField = new HBox();
        HBox.setHgrow(priceFillerField, Priority.ALWAYS);
        double initialPrice = 20;
        Label priceValue = new Label(GUIConstants.currencyFormat.format(initialPrice));
        Slider priceSlider = new Slider(0, 100, initialPrice);
        scene.selectedBookRetailPrice = initialPrice;
        priceSlider.valueProperty().addListener((change) -> {
            scene.selectedBookRetailPrice = priceSlider.getValue();
            priceValue.setText(GUIConstants.currencyFormat.format(scene.selectedBookRetailPrice));
        });
        priceSlider.setPrefWidth(150);
        priceEntryContainer.getChildren().addAll(priceLabel,
                priceFillerField,
                priceValue,
                priceSlider);

        // state (condition vs format) dropdown
        // initialized for paperback and bound to changes
        HBox stateEntryContainer = new HBox();
        stateEntryContainer.getStyleClass().addAll("box", "hbox", "add-field-container");
        stateEntryContainer.setMaxWidth(150 * 3);
        stateEntryContainer.setSpacing(12);
        Label stateLabel = new Label("Condition: ");
        stateLabel.setFont(GUIConstants.montserrat20);
        HBox stateFillerField = new HBox();
        HBox.setHgrow(stateFillerField, Priority.ALWAYS);
        ObservableList<Object> stateList = FXCollections.observableArrayList(Paperback.Condition.New,
                Paperback.Condition.Used);
        ComboBox<Object> stateBox = new ComboBox<>(stateList);
        stateBox.setPrefWidth(150);
        stateEntryContainer.getChildren().addAll(stateLabel, stateFillerField, stateBox);

        stateBox.valueProperty().addListener((change) -> {
            if (stateBox.getValue().getClass() == Paperback.Condition.class) {
                scene.selectedPaperbackCondition = (Paperback.Condition) stateBox.getValue();
            } else if (stateBox.getValue().getClass() == Audiobook.Format.class) {
                scene.selectedAudiobookFormat = (Audiobook.Format) stateBox.getValue();
            } else {
                scene.selectedEbookFormat = (Ebook.Format) stateBox.getValue();
            }
        });

        scene.selectedBookProperty.addListener((change) -> {
            switch (scene.selectedBookProperty.get()) {
                case 0 -> { // paperback
                    stateLabel.setText("Condition: ");
                    stateList.setAll(Paperback.Condition.New,
                            Paperback.Condition.Used);
                }
                case 1 -> { // audiobook
                    stateLabel.setText("Format: ");
                    stateList.setAll(Audiobook.Format.AAC,
                            Audiobook.Format.MP3,
                            Audiobook.Format.WMA);
                }
                case 2 -> { // ebook
                    stateLabel.setText("Format: ");
                    stateList.setAll(Ebook.Format.EPUB,
                            Ebook.Format.MOBI,
                            Ebook.Format.PDF);
                }
            }
        });

        // length (pages vs hrs) field
        // initialized for paperback and bound to changes
        HBox lengthEntryContainer = new HBox();
        lengthEntryContainer.getStyleClass().addAll("box", "hbox", "add-field-container");
        lengthEntryContainer.setMaxWidth(150 * 3);
        lengthEntryContainer.setSpacing(12);
        Label lengthLabel = new Label("Pages: ");
        lengthLabel.setFont(GUIConstants.montserrat20);
        HBox lengthFillerField = new HBox();
        HBox.setHgrow(lengthFillerField, Priority.ALWAYS);
        TextField lengthField = new TextField();
        lengthField.setPrefWidth(150);
        lengthEntryContainer.getChildren().addAll(lengthLabel, lengthFillerField, lengthField);

        lengthField.textProperty().addListener((change) -> {
            switch (scene.selectedBookProperty.get()) {
                case 0 -> { // paperback
                    scene.selectedPaperbackPages = Integer.parseInt(lengthField.getText());
                }
                case 1 -> { // audiobook
                    scene.selectedAudiobookLength = Double.parseDouble(lengthField.getText());
                }
                case 2 -> { // ebook
                    scene.selectedEbookPages = Integer.parseInt(lengthField.getText());
                }
            }
        });

        scene.selectedBookProperty.addListener((change) -> {
                switch (scene.selectedBookProperty.get()) {
                    case 0 -> { // paperback
                        lengthLabel.setText("Pages: ");
                    }
                    case 1 -> {
                        lengthLabel.setText("Length (Hours): ");
                    }
                    case 2 -> {
                        lengthLabel.setText("Pages: ");
                    }
                }
        });

        // construct add button (with count parameter)
        HBox addButtonContainer = new HBox();
        addButtonContainer.getStyleClass().addAll("box", "hbox", "add-field-container", "add-button-container");
        addButtonContainer.setMaxWidth(150 * 3);
        addButtonContainer.setSpacing(12);
        TextField countField = new TextField("1");
        countField.setFont(GUIConstants.montserrat12);
        countField.setPrefWidth(30);
        Button addButton = new Button("Add Book");
        addButton.setFont(GUIConstants.montserrat20Bold);
        HBox.setHgrow(addButton, Priority.ALWAYS);
        addButtonContainer.getChildren().addAll(countField, addButton);

        addButton.setOnAction((event) -> {
                AbstractBook book = scene.getSelectedBook();
                int addCount = Integer.parseInt(countField.getText());
                try {
                    manager.processBookAddition(book, addCount);
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
        });

        interactableContainer.getChildren().addAll(typeSelectContainer,
                titleEntryContainer,
                genreEntryContainer,
                languageEntryContainer,
                dateEntryContainer,
                priceEntryContainer,
                stateEntryContainer,
                lengthEntryContainer,
                addButtonContainer);

        root.getChildren().addAll(topBar, interactableContainer);
        return scene;
    }

    public void loadAdminScene() {
        Main.getPrimaryStage().setScene(UserScene.getAdminScene(manager, admin));
    }

    public AbstractBook getSelectedBook() {
        // construct book
        String sentinelBarcode = "00000000";
        AbstractBook book;

        if (this.selectedBookProperty.get() == 0) {
            book = new Paperback(sentinelBarcode,
                                 selectedBookTitle,
                                 selectedBookLanguage,
                                 selectedBookGenre,
                                 selectedBookDate,
                                 selectedBookRetailPrice,
                                 selectedPaperbackPages,
                                 selectedPaperbackCondition);
        } else if (this.selectedBookProperty.get() == 1) {
            book = new Audiobook(sentinelBarcode,
                                 selectedBookTitle,
                                 selectedBookLanguage,
                                 selectedBookGenre,
                                 selectedBookDate,
                                 selectedBookRetailPrice,
                                 selectedAudiobookLength,
                                 selectedAudiobookFormat);
        } else {
            book = new Ebook(sentinelBarcode,
                             selectedBookTitle,
                             selectedBookLanguage,
                             selectedBookGenre,
                             selectedBookDate,
                             selectedBookRetailPrice,
                             selectedEbookPages,
                             selectedEbookFormat);
        }

        // check for existing barcode
        Set<String> barcodes = new HashSet<>();
        manager.getBooks().keySet().stream().forEach(b -> barcodes.add(b.barcode));

        String assignedBarcode = null;
        for (AbstractBook b : manager.getBooks().keySet()) {
            book.setBarcode(b.barcode); // change barcode to preexisting book's barcode

            if (b.equals(book)) { // if the books match (including barcode)
                assignedBarcode = b.barcode; // record matching barcode
                break; // stop searching
            }
        }

        // otherwise assign new barcode
        if (assignedBarcode == null) {
            for (;;) {
                String newBarcode = randomBarcode();

                if (!barcodes.contains(newBarcode)) {
                    book.setBarcode(newBarcode);
                    break;
                }
            }
        }

        return book;
    }

    private static String randomBarcode() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            builder.append(String.valueOf(source.nextInt(0, 10)));
        }

        return builder.toString();
    }
}
