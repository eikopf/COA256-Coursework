import java.util.HashMap;

/**
 * Represents a particular instance of a customer in the system. Extends {@link AbstractUser}.
 */
public class Customer extends AbstractUser {

    /**
     * Represents the user's credit balance in pence.
     */
    int balanceInPence;

    /**
     * Stores the contents of the user's basket.
     * The value assigned to each key represents the number of that key in the basket
     */
    HashMap<AbstractBook, Integer> basket;

    /*
     * Constructs a Customer
     * @param id
     * @param username
     * @param surname
     * @param address
     * @param balanceInPence
     * @param basket
     */
    public Customer(String id,
                    String username,
                    String surname,
                    Address address,
                    int balanceInPence,
                    HashMap<AbstractBook, Integer> basket) {
        super(id, username, surname, address);
        this.balanceInPence = balanceInPence;
        this.basket = basket;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "balanceInPence=" + balanceInPence +
                ", basket=" + basket +
                ", id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", surname='" + surname + '\'' +
                ", address=" + address +
                '}';
    }

    public void addBookToBasket(AbstractBook book) {
        basket.put(book, basket.getOrDefault(book, 0) + 1);
    }

    void removeBookFromBasket(AbstractBook book) {
        basket.remove(book);
    }

    void clearBasket() {
        basket.clear();
    }

    void purchase() {
        //TODO: to be implemented
    }
}
