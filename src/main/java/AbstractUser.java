/**
 * An abstract class implemented by {@link Customer} and {@link Admin}. Represents a user of the system.
 */
public abstract class AbstractUser {
    String id, username, surname;
    Address address;

    public AbstractUser(String id, String username, String surname, Address address) {
        this.id = id;
        this.username = username;
        this.surname = surname;
        this.address = address;
    }

    public boolean isAdmin() {
        return this.getClass().equals(Admin.class);
    }

    public static class Address {
        int houseNumber;
        String postcode, city;

        public Address(int houseNumber, String postcode, String city) {
            this.houseNumber = houseNumber;
            this.postcode = postcode;
            this.city = city;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "houseNumber=" + houseNumber +
                    ", postcode='" + postcode + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }

    public String getLabel() {
        return username +
                " (ID: " +
                id +
                ")";
    }

    public String getSubLabel() {
        return surname +
                " @ " +
                address.city +
                " (" +
                address.postcode +
                ")";
    }
}
