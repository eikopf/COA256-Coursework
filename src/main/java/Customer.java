import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

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
    private final SimpleMapProperty<AbstractBook, Integer> basket;

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
        this.basket = new SimpleMapProperty<>(FXCollections.observableHashMap());
        this.basket.putAll(basket);
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

    public ObservableMap<AbstractBook, Integer> getBasket() {
        return this.basket;
    }

    public boolean setCountInBasket(AbstractBook book, int count) {
        assert count > 0; // no values in basket should be mapped to 0
        if (Main.getBookshopManager().getBooks().get(book) > count) { return false; }

        basket.put(book, count);
        return true;
    }

    public boolean incrementCountInBasket(AbstractBook book) {
        if (Main.getBookshopManager().getBooks().get(book) < basket.getOrDefault(book, 0) + 1) {
            return false;
        }

        basket.put(book, basket.getOrDefault(book, 0) + 1);
        return true;
    }

    public void clearBasket() {
        basket.clear();
    }

    public boolean purchase() {
        if (balance < getBasketPrice()) return false;
        balance -= getBasketPrice();

        try {
            Main.getBookshopManager().processPurchase(this, basket);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        clearBasket();
        return true;
    }

    public double getBasketPrice() {
        double basketPrice = 0;

        for (AbstractBook book : basket.keySet()) {
            basketPrice += basket.get(book) * book.retailPrice;
        }

        return basketPrice;
    }

    public double getBalance() {
        return this.balance;
    }
}
