package models;

import java.time.LocalDate;

public class Borrowing extends BaseEntity {
    private Book book;
    private Member member;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Borrowing(int id, Book book, Member member) {
        super(id);
        this.book = book;
        this.member = member;
        this.borrowDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(14);
        this.book.setAvailable(false);
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.book.setAvailable(true);
    }

    @Override
    public String toString() {
        return "Borrowing{id=" + id + ", book=" + book.getTitle() +
                ", member=" + member.getFullName() +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + (returnDate != null ? returnDate : "Not returned") + "}";
    }
}