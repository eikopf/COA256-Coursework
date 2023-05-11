import java.time.LocalDate;;

/**
 * Represents an ebook in the system. Extends {@link AbstractBook}.
 */
public class Ebook extends AbstractBook {

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
                 LocalDate releaseDate,
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

        @Override
        public String toString() {
            return switch (this) {
                case EPUB -> "EPUB";
                case MOBI -> "MOBI";
                case PDF -> "PDF";
            };
        }
    }

    @Override
    public String toDataString(int count) {
        return new StringBuilder(barcode)
            .append(", ebook, ")
            .append(title)
            .append(", ")
            .append(language)
            .append(", ")
            .append(genre)
            .append(", ")
            .append(dateFormatter.format(releaseDate))
            .append(", ")
            .append(count)
            .append(", ")
            .append(retailPrice)
            .append(", ")
            .append(pages)
            .append(", ")
            .append(format)
            .toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((format == null) ? 0 : format.hashCode());
        result = prime * result + pages;
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
        Ebook other = (Ebook) obj;
        if (format != other.format)
            return false;
        if (pages != other.pages)
            return false;
        return true;
    }
}
