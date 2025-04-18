package models;

public class Library extends BaseEntity {
    private String name;
    private Address address;

    public Library(int id, String name, Address address) {
        super(id);
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Library{id=" + id + ", name='" + name + "', address=" + address + "}";
    }
}