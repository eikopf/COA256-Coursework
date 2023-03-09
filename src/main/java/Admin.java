public class Admin extends AbstractUser{
    public Admin(String id, String username, String surname, Address address) {
        super(id, username, surname, address);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", surname='" + surname + '\'' +
                ", address=" + address +
                '}';
    }
}
