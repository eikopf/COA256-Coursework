import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents an instance of a book in the system.
 * Implemented by {@link Audiobook}, {@link Paperback}, and {@link Ebook}.
 */
public abstract class AbstractBook {
    String barcode;
    String title;
    Language language;
    Genre genre;
    LocalDate releaseDate;
    double retailPrice;

    protected static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public AbstractBook(String barcode,
                        String title,
                        Language language,
                        Genre genre,
                        LocalDate releaseDate,
                        double retailPrice) {
        this.barcode = barcode;
        this.title = title;
        this.language = language;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.retailPrice = retailPrice;
    }

    /**
     * Represents the language of an {@link AbstractBook}.
     */
    public enum Language {
        English,
        French;

        /**
         * Converts a string to an instance of an AbstractBook.Language
         * @param language a string
         * @return an AbstractBook.Language
         */
        public static Language toLanguage(String language) throws EnumConstantNotPresentException {
            return switch (language.toLowerCase()) {
                case "english" -> Language.English;
                case "french" -> Language.French;
                default -> throw new EnumConstantNotPresentException(Language.class, language);
            };
        }

        @Override
        public String toString() {
            return switch (this) {
                case English -> "English";
                case French -> "French";
            };
        }
    }

    /**
     * Represents the genre of an {@link AbstractBook}.
     */
    public enum Genre {
        Politics,
        ComputerScience,
        Business,
        Biography;

        /**
         * Converts a string to an instance of an AbstractBook.Genre
         * @param genre a string
         * @return an AbstractBook.Genre
         */
        public static Genre toGenre(String genre) throws EnumConstantNotPresentException {
            return switch (genre.toLowerCase()) {
                case "politics" -> Genre.Politics;
                case "computer science" -> Genre.ComputerScience;
                case "business" -> Genre.Business;
                case "biography" -> Genre.Biography;
                default -> throw new EnumConstantNotPresentException(Genre.class, genre);
            };
        }

        @Override
        public String toString() {
            return switch (this) {
                case Politics -> "Politics";
                case ComputerScience -> "Computer Science";
                case Business -> "Business";
                case Biography -> "Biography";
            };
        }
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
        result = prime * result + ((genre == null) ? 0 : genre.hashCode());
        result = prime * result + ((language == null) ? 0 : language.hashCode());
        result = prime * result + ((releaseDate == null) ? 0 : releaseDate.hashCode());
        long temp;
        temp = Double.doubleToLongBits(retailPrice);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractBook other = (AbstractBook) obj;
        if (barcode == null) {
            if (other.barcode != null)
                return false;
        } else if (!barcode.equals(other.barcode))
            return false;
        if (genre != other.genre)
            return false;
        if (language != other.language)
            return false;
        if (releaseDate == null) {
            if (other.releaseDate != null)
                return false;
        } else if (!releaseDate.equals(other.releaseDate))
            return false;
        if (Double.doubleToLongBits(retailPrice) != Double.doubleToLongBits(other.retailPrice))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

    abstract public String toDataString(int count);

    public String getBarcode() {
        return barcode;
    }



    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }



    public String getTitle() {
        return title;
    }



    public void setTitle(String title) {
        this.title = title;
    }



    public Language getLanguage() {
        return language;
    }



    public void setLanguage(Language language) {
        this.language = language;
    }



    public Genre getGenre() {
        return genre;
    }



    public void setGenre(Genre genre) {
        this.genre = genre;
    }



    public LocalDate getReleaseDate() {
        return releaseDate;
    }



    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }



    public double getRetailPrice() {
        return retailPrice;
    }



    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }



    /**
     * Thrown when parsing an {@link AbstractBook} from a file fails because a string was malformed.
     */
    public static class MalformedBookCharacteristicException extends Exception {
        public MalformedBookCharacteristicException(String message) {
            super(message);
        }
    }
}
