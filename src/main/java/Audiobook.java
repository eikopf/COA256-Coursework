import java.util.Date;

/**
 * Represents an audiobook in the system. Extends {@link AbstractBook}.
 */
public class Audiobook extends AbstractBook implements Formatted{

    /**
     * Stores the length of the audiobook in hours.
     */
    double length;

    /**
     * Stores the file format of the audiobook.
     */
    Format format;

    public Audiobook(String barcode,
                     String title,
                     AbstractBook.Language language,
                     AbstractBook.Genre genre,
                     Date releaseDate,
                     double retailPrice,
                     double length,
                     Format format) {
        super(barcode, title, language, genre, releaseDate, retailPrice);
        this.length = length;
        this.format = format;
    }

    @Override
    public String toString() {
        return "Audiobook{" +
                "length=" + length +
                ", format=" + format +
                ", barcode='" + barcode + '\'' +
                ", title='" + title + '\'' +
                ", language=" + language +
                ", genre=" + genre +
                ", releaseDate=" + releaseDate +
                ", retailPrice=" + retailPrice +
                '}';
    }

    /**
     * Represents the file format of an {@link Audiobook}.
     */
    public enum Format {
        MP3,
        WMA,
        AAC;

        /*
         * Converts a string to a reasonable equivalent value of Audiobook.Format
         * @param format a string
         * @return an appropriate constant from Audiobook.Format
         */
        public static Format toFormat(String format) throws EnumConstantNotPresentException{
            return switch (format.toLowerCase()) {
                case "mp3" -> Format.MP3;
                case "wma" -> Format.WMA;
                case "aac" -> Format.AAC;
                default -> throw new EnumConstantNotPresentException(Format.class, format);
            };
        }
    }

    public static Class<? extends Enum> getFormatEnum() {
        return Format.class;
    }

    public static Enum<?> getFormatString(String format) {
        return Format.toFormat(format);
    }
}
