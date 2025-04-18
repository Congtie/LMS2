package models;

public abstract class Person extends BaseEntity {
    protected String firstName;
    protected String lastName;
    protected Address address;

    public Person(int id, String firstName, String lastName, Address address) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}