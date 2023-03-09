import java.util.Date;

/**
 * Represents an ebook in the system. Extends {@link AbstractBook}.
 */
public class Ebook extends AbstractBook{

    /**
     * Represents the number of (virtual) pages in the ebook.
     */
    int pages;

    /**
     * Represents the file format of the ebook.
     */
    Format format;

    public Ebook(String barcode,
                 String title,
                 AbstractBook.Language language,
                 AbstractBook.Genre genre,
                 Date releaseDate,
                 double retailPrice,
                 int pages,
                 Format format) {
        super(barcode, title, language, genre, releaseDate, retailPrice);
        this.pages = pages;
        this.format = format;
    }

    @Override
    public String toString() {
        return "Ebook{" +
                "pages=" + pages +
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
     * Represents the file format of an {@link Ebook}.
     */
    public enum Format {
        EPUB,
        MOBI,
        PDF;

        /*
         * Converts a string to a reasonable equivalent value in Ebook.Format
         * @param format a string
         * @return a reasonable equivalent value of Ebook.Format
         */
        public static Format toFormat(String format) throws EnumConstantNotPresentException{
            switch (format.toLowerCase()) {
                case "epub" : return Format.EPUB;
                case "mobi" : return Format.MOBI;
                case "pdf" : return Format.PDF;
                default : throw new EnumConstantNotPresentException(Format.class, format);
            }
        }
    }
}
