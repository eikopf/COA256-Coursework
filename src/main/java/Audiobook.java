import java.time.LocalDate;

/**
 * Represents an audiobook in the system. Extends {@link AbstractBook}.
 */
public class Audiobook extends AbstractBook {

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
                     LocalDate releaseDate,
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

        /**
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

        @Override
        public String toString() {
            return switch (this) {
                case MP3 -> "MP3";
                case WMA -> "WMA";
                case AAC -> "AAC";
            };
        }
    }

    public static Enum<?> getFormatString(String format) {
        return Format.toFormat(format);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((format == null) ? 0 : format.hashCode());
        long temp;
        temp = Double.doubleToLongBits(length);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Audiobook other = (Audiobook) obj;
        if (format != other.format)
            return false;
        return Double.doubleToLongBits(length) == Double.doubleToLongBits(other.length);
    }

    @Override
    public String toDataString(int count) {
        return barcode +
                ", audiobook" +
                ", " +
                title +
                ", " +
                language +
                ", " +
                genre +
                ", " +
                dateFormatter.format(releaseDate) +
                ", " +
                count +
                retailPrice +
                ", " +
                length +
                ", " +
                format;
    }
}
