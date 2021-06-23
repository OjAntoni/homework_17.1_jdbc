import java.util.ArrayList;
import java.util.Objects;

public class User {
    private int id;
    private String name;
    private String surname;
    private String password;
    private ArrayList<Phone> phones;
    private ArrayList<Address> addresses;

    public User(int id, String name, String surname, String password, ArrayList<Phone> phones, ArrayList<Address> addresses) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.phones = phones;
        this.addresses = addresses;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Phone> getPhones() {
        return phones;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
