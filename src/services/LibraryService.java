package services;

import models.*;
import java.util.*;

public class LibraryService {
    private Set<Book> books;
    private List<Member> members;
    private Map<Integer, Author> authors;
    private List<Borrowing> borrowings;
    private int nextBookId = 1;
    private int nextMemberId = 1;
    private int nextAuthorId = 1;
    private int nextBorrowingId = 1;

    public LibraryService() {
        this.books = new TreeSet<>(new BookComparator());
        this.members = new ArrayList<>();
        this.authors = new HashMap<>();
        this.borrowings = new ArrayList<>();
    }

    public Book addBook(String title, Author author, Genre genre, int year) {
        Book book = new Book(nextBookId++, title, author, genre, year);
        books.add(book);
        return book;
    }

    public EBook addEBook(String title, Author author, Genre genre, int year, String format, int sizeInMB) {
        EBook ebook = new EBook(nextBookId++, title, author, genre, year, format, sizeInMB);
        books.add(ebook);
        return ebook;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public List<Book> getAvailableBooks() {
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    public List<Book> getBorrowedBooks() {
        List<Book> borrowedBooks = new ArrayList<>();
        for (Book book : books) {
            if (!book.isAvailable()) {
                borrowedBooks.add(book);
            }
        }
        return borrowedBooks;
    }

    public List<Book> searchBooksByTitle(String title) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> searchBooksByAuthor(String authorName) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().getName().toLowerCase().contains(authorName.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public Author addAuthor(String name, String biography) {
        Author author = new Author(nextAuthorId++, name, biography);
        authors.put(author.getId(), author);
        return author;
    }

    public Author getAuthorById(int id) {
        return authors.get(id);
    }

    public Collection<Author> getAllAuthors() {
        return authors.values();
    }

    public Member addMember(String firstName, String lastName, Address address, String email, String phone) {
        Member member = new Member(nextMemberId++, firstName, lastName, address, email, phone);
        members.add(member);
        return member;
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    public Member getMemberById(int id) {
        for (Member member : members) {
            if (member.getId() == id) {
                return member;
            }
        }
        return null;
    }

    public Borrowing borrowBook(Book book, Member member) {
        if (!book.isAvailable()) {
            return null;
        }

        Borrowing borrowing = new Borrowing(nextBorrowingId++, book, member);
        borrowings.add(borrowing);
        member.addBorrowing(borrowing);
        return borrowing;
    }

    public boolean returnBook(Borrowing borrowing) {
        if (borrowing == null || borrowing.isReturned()) {
            return false;
        }

        borrowing.returnBook();
        return true;
    }

    public List<Borrowing> getMemberBorrowingHistory(Member member) {
        return member.getBorrowingHistory();
    }

    public Map<Book, Integer> getMostPopularBooks() {
        Map<Book, Integer> bookPopularity = new HashMap<>();

        for (Borrowing borrowing : borrowings) {
            Book book = borrowing.getBook();
            bookPopularity.put(book, bookPopularity.getOrDefault(book, 0) + 1);
        }

        return bookPopularity;
    }
}