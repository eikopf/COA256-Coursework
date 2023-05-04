import java.time.LocalDate;

/**
 * Represents a paperback in the system. Extends {@link AbstractBook}.
 */
public class Paperback extends AbstractBook{

    /**
     * Represents the number of (physical) pages in the {@link Paperback}.
     */
    int pages;

    /**
     * Represents the physical condition of the {@link Paperback}.
     */
    Condition condition;

    public Paperback(String barcode,
                     String title,
                     AbstractBook.Language language,
                     AbstractBook.Genre genre,
                     LocalDate releaseDate,
                     double retailPrice,
                     int pages,
                     Condition condition) {
        super(barcode, title, language, genre, releaseDate, retailPrice);
        this.pages = pages;
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Paperback{" +
                "pages=" + pages +
                ", condition=" + condition +
                ", barcode='" + barcode + '\'' +
                ", title='" + title + '\'' +
                ", language=" + language +
                ", genre=" + genre +
                ", releaseDate=" + releaseDate +
                ", retailPrice=" + retailPrice +
                '}';
    }

    /**
     * Represents the physical condition of a {@link Paperback}.
     */
    public enum Condition {
        New,
        Used;

        /*
         * Converts a string to a reasonable equivalent value in Paperback.Condition
         * @param condition a string
         * @return a reasonable equivalent value of Paperback.Condition
         */
        public static Condition toCondition(String condition) {
            switch (condition.toLowerCase()) {
                case "new" : return Condition.New;
                case "used" : return Condition.Used;
                default : throw new EnumConstantNotPresentException(Condition.class, condition);
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((condition == null) ? 0 : condition.hashCode());
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
        Paperback other = (Paperback) obj;
        if (condition != other.condition)
            return false;
        if (pages != other.pages)
            return false;
        return true;
    }


}
