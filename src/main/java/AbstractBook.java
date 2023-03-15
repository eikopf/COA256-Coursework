import java.util.Date;

/**
 * Represents an instance of a book in the system.
 * Implemented by {@link Audiobook}, {@link Paperback}, and {@link Ebook}.
 */
public abstract class AbstractBook {
    String barcode;
    String title;
    Language language;
    Genre genre;
    Date releaseDate;
    double retailPrice;

    public AbstractBook(String barcode,
                        String title,
                        Language language,
                        Genre genre,
                        Date releaseDate,
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
            switch (language.toLowerCase()) {
                case "english" : return Language.English;
                case "french" : return Language.French;
                default : throw new EnumConstantNotPresentException(Language.class, language);
            }
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
