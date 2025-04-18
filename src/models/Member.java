package models;

import java.util.ArrayList;
import java.util.List;

public class Member extends Person {
    private String email;
    private String phone;
    private List<Borrowing> borrowingHistory;

    public Member(int id, String firstName, String lastName, Address address, String email, String phone) {
        super(id, firstName, lastName, address);
        this.email = email;
        this.phone = phone;
        this.borrowingHistory = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Borrowing> getBorrowingHistory() {
        return borrowingHistory;
    }

    public void addBorrowing(Borrowing borrowing) {
        borrowingHistory.add(borrowing);
    }

    @Override
    public String toString() {
        return "Member{id=" + id + ", name='" + getFullName() + "', email='" + email + "', phone='" + phone + "'}";
    }
}