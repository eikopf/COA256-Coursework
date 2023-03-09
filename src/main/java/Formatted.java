/**
 * Indicates that a class can provide an enum of 'formats,' i.e.
 * the object represents a variety of filetypes that are
 * fundamentally equivalent.
 */
public interface Formatted {

    /**
     * Provides a reference to the enum containing format constants.
     * @return an enum
     */
    public static Class<? extends Enum> getFormatEnum() {
        return null;
    }

    /**
     * Converts a string into a reasonable equivalent enum constant value
     * @param format a string that could be reasonably converted into an enum constant
     * @return an enum constant
     */
    public static Enum<?> getFormatString(String format) {
        return null;
    }
}
