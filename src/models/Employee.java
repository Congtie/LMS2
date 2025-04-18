package models;

public class Employee extends Person {
    private String position;
    private double salary;

    public Employee(int id, String firstName, String lastName, Address address, String position, double salary) {
        super(id, firstName, lastName, address);
        this.position = position;
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + getFullName() + "', position='" + position + "'}";
    }
}