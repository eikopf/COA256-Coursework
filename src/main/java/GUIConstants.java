import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Locale;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class GUIConstants {
    // PALETTE
    // to limit visual noise, this will consist
    // of four primary hues, and a limited set
    // of monochromatic colors
    public static final Color WHITE = Color.valueOf("0xfcfafa");
    public static final Color DARK_GREY = Color.valueOf("0x363946");
    public static final Color LIGHT_GREY = Color.valueOf("0x62657F");
    public static final Color RED = Color.valueOf("0xA65A4E");
    // TODO: add some brighter hues

    // FONTS
    private static InputStream getFont(String relativePath) {
        return GUIConstants.class.getResourceAsStream("/fonts/" + relativePath);
    }

    public static final Font montserrat20 = Font.loadFont(getFont("montserrat/Montserrat-Regular.ttf"), 20);
    public static final Font montserrat12 = Font.loadFont(getFont("montserrat/Montserrat-Regular.ttf"), 12);
    public static final Font montserrat8 = Font.loadFont(getFont("montserrat/Montserrat-Regular.ttf"), 8);
    public static final Font montserrat20Bold = Font.loadFont(getFont("montserrat/Montserrat-Bold.ttf"), 20);
    public static final Font montserrat25Bold = Font.loadFont(getFont("montserrat/Montserrat-Bold.ttf"), 25);
    public static final Font montserrat12Italic = Font.loadFont(getFont("montserrat/Montserrat-Italic.ttf"), 12);

    // WINDOW CONSTANTS
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    // NUMERICAL FORMATTING
    public static final DecimalFormat currencyFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance();
}
