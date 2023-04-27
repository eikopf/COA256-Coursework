import java.util.HashMap;

/**
 * Represents a particular instance of a customer in the system. Extends {@link AbstractUser}.
 */
public class Customer extends AbstractUser {

    /**
     * Represents the user's credit balance in pence.
     */
    private double balance;

    /**
     * Stores the contents of the user's basket.
     * The value assigned to each key represents the number of that key in the basket
     */
    private HashMap<AbstractBook, Integer> basket;

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
                    double balance,
                    HashMap<AbstractBook, Integer> basket) {
        super(id, username, surname, address);
        this.balance = balance;
        this.basket = basket;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "balanceInPence=" + balance +
                ", basket=" + basket +
                ", id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", surname='" + surname + '\'' +
                ", address=" + address +
                '}';
    }

    public HashMap<AbstractBook, Integer> getBasket() {
        return this.basket;
    }

    public boolean setCountInBasket(AbstractBook book, int count) {
        assert count > 0; // no values in basket should be mapped to 0
        if (Main.getBookshopManager().getBooks().get(book) > count) { return false; }

        basket.put(book, count);
        return true;
    }

    public boolean incrementCountInBasket(AbstractBook book) {
        if (Main.getBookshopManager().getBooks().get(book) > basket.getOrDefault(book, 0) + 1) {
            return false;
        }

        basket.put(book, basket.getOrDefault(book, 0) + 1);
        return true;
    }

    public void decrementCountInBasket(AbstractBook book) {
        assert basket.containsKey(book); // should never be called on books that aren't in the basket
        if (basket.get(book) == 1) { basket.remove(book); return; }
        basket.put(book, basket.get(book) - 1);
    }

    public void clearBasket() {
        basket.clear();
    }

    public void purchase() {
        //TODO: to be implemented
    }
}
