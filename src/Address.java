import java.util.Objects;

public class Address {
    private int id;
    private String street;

    public int getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public Address(int id, String street) {
        this.id = id;
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id == address.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
