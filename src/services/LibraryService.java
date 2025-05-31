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
    private final AuditService auditService;

    public LibraryService() {
        this.books = new TreeSet<>(new BookComparator());
        this.members = new ArrayList<>();
        this.authors = new HashMap<>();
        this.borrowings = new ArrayList<>();
        this.auditService = AuditService.getInstance();
    }

    public Book addBook(String title, Author author, Genre genre, int year) {
        auditService.logAction(AuditService.CREATE_BOOK);
        Book book = new Book(nextBookId++, title, author, genre, year);
        books.add(book);
        return book;
    }

    public EBook addEBook(String title, Author author, Genre genre, int year, String format, int sizeInMB) {
        auditService.logAction(AuditService.CREATE_EBOOK);
        EBook ebook = new EBook(nextBookId++, title, author, genre, year, format, sizeInMB);
        books.add(ebook);
        return ebook;
    }

    public List<Book> getAllBooks() {
        auditService.logAction(AuditService.LIST_ALL_BOOKS);
        return new ArrayList<>(books);
    }

    public List<Book> getAvailableBooks() {
        auditService.logAction(AuditService.LIST_AVAILABLE_BOOKS);
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isAvailable()) {
                availableBooks.add(book);
            }
        }
        return availableBooks;
    }

    public List<Book> getBorrowedBooks() {
        auditService.logAction(AuditService.GET_BORROWED_BOOKS);
        List<Book> borrowedBooks = new ArrayList<>();
        for (Book book : books) {
            if (!book.isAvailable()) {
                borrowedBooks.add(book);
            }
        }
        return borrowedBooks;
    }

    public List<Book> searchBooksByTitle(String title) {
        auditService.logAction(AuditService.SEARCH_BOOKS_BY_TITLE);
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> searchBooksByAuthor(String authorName) {
        auditService.logAction(AuditService.SEARCH_BOOKS_BY_TITLE); // Using same audit action for search
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().getName().toLowerCase().contains(authorName.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public Author addAuthor(String name, String biography) {
        auditService.logAction(AuditService.CREATE_AUTHOR);
        Author author = new Author(nextAuthorId++, name, biography);
        authors.put(author.getId(), author);
        return author;
    }

    public Author getAuthorById(int id) {
        auditService.logAction(AuditService.READ_AUTHOR);
        return authors.get(id);
    }

    public Collection<Author> getAllAuthors() {
        auditService.logAction(AuditService.LIST_ALL_AUTHORS);
        return authors.values();
    }

    public Member addMember(String firstName, String lastName, Address address, String email, String phone) {
        auditService.logAction(AuditService.CREATE_MEMBER);
        Member member = new Member(nextMemberId++, firstName, lastName, address, email, phone);
        members.add(member);
        return member;
    }

    public List<Member> getAllMembers() {
        auditService.logAction(AuditService.LIST_ALL_MEMBERS);
        return new ArrayList<>(members);
    }

    public Member getMemberById(int id) {
        auditService.logAction(AuditService.READ_MEMBER);
        for (Member member : members) {
            if (member.getId() == id) {
                return member;
            }
        }
        return null;
    }

    public Borrowing borrowBook(Book book, Member member) {
        auditService.logAction(AuditService.CREATE_BORROWING);
        if (!book.isAvailable()) {
            return null;
        }

        Borrowing borrowing = new Borrowing(nextBorrowingId++, book, member);
        borrowings.add(borrowing);
        member.addBorrowing(borrowing);
        return borrowing;
    }

    public boolean returnBook(Borrowing borrowing) {
        auditService.logAction(AuditService.RETURN_BOOK);
        if (borrowing == null || borrowing.isReturned()) {
            return false;
        }

        borrowing.returnBook();
        return true;
    }

    public List<Borrowing> getMemberBorrowingHistory(Member member) {
        auditService.logAction(AuditService.GET_MEMBER_BORROWING_HISTORY);
        return member.getBorrowingHistory();
    }

    public Map<Book, Integer> getMostPopularBooks() {
        auditService.logAction(AuditService.GET_MOST_POPULAR_BOOKS);
        Map<Book, Integer> bookPopularity = new HashMap<>();

        for (Borrowing borrowing : borrowings) {
            Book book = borrowing.getBook();
            bookPopularity.put(book, bookPopularity.getOrDefault(book, 0) + 1);
        }

        return bookPopularity;
    }
}