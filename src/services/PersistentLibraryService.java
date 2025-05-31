package services;

import models.*;
import dao.*;
import java.util.*;
import java.util.stream.Collectors;

public class PersistentLibraryService {
    private static PersistentLibraryService instance;
    
    private final AuthorDAO authorDAO;
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    private final BorrowingDAO borrowingDAO;
    private final AuditService auditService;
    
    private int nextBookId = 1;
    private int nextMemberId = 1;
    private int nextAuthorId = 1;
    private int nextBorrowingId = 1;
    
    private PersistentLibraryService() {
        this.authorDAO = AuthorDAO.getInstance();
        this.bookDAO = BookDAO.getInstance();
        this.memberDAO = MemberDAO.getInstance();
        this.borrowingDAO = BorrowingDAO.getInstance();
        this.auditService = AuditService.getInstance();
        initializeIdCounters();
    }
    
    public static synchronized PersistentLibraryService getInstance() {
        if (instance == null) {
            instance = new PersistentLibraryService();
        }
        return instance;
    }
    
    private void initializeIdCounters() {
        List<Author> authors = authorDAO.findAll();
        if (!authors.isEmpty()) {
            nextAuthorId = authors.stream().mapToInt(Author::getId).max().orElse(0) + 1;
        }
        
        List<Book> books = bookDAO.findAll();
        if (!books.isEmpty()) {
            nextBookId = books.stream().mapToInt(Book::getId).max().orElse(0) + 1;
        }
        
        List<Member> members = memberDAO.findAll();
        if (!members.isEmpty()) {
            nextMemberId = members.stream().mapToInt(Member::getId).max().orElse(0) + 1;
        }
        
        List<Borrowing> borrowings = borrowingDAO.findAll();
        if (!borrowings.isEmpty()) {
            nextBorrowingId = borrowings.stream().mapToInt(Borrowing::getId).max().orElse(0) + 1;
        }
    }
    
    // Author CRUD operations
    public Author createAuthor(String name, String biography) {
        auditService.logAction(AuditService.CREATE_AUTHOR);
        Author author = new Author(nextAuthorId++, name, biography);
        return authorDAO.save(author);
    }
    
    public Optional<Author> getAuthorById(int id) {
        auditService.logAction(AuditService.READ_AUTHOR);
        return authorDAO.findById(id);
    }
    
    public List<Author> getAllAuthors() {
        auditService.logAction(AuditService.LIST_ALL_AUTHORS);
        return authorDAO.findAll();
    }
    
    public Author updateAuthor(Author author) {
        auditService.logAction(AuditService.UPDATE_AUTHOR);
        return authorDAO.update(author);
    }
    
    public boolean deleteAuthor(int id) {
        auditService.logAction(AuditService.DELETE_AUTHOR);
        return authorDAO.delete(id);
    }
    
    // Book CRUD operations
    public Book createBook(String title, Author author, Genre genre, int year) {
        auditService.logAction(AuditService.CREATE_BOOK);
        Book book = new Book(nextBookId++, title, author, genre, year);
        return bookDAO.save(book);
    }
    
    public EBook createEBook(String title, Author author, Genre genre, int year, String format, int sizeInMB) {
        auditService.logAction(AuditService.CREATE_EBOOK);
        EBook ebook = new EBook(nextBookId++, title, author, genre, year, format, sizeInMB);
        return (EBook) bookDAO.save(ebook);
    }
    
    public Optional<Book> getBookById(int id) {
        auditService.logAction(AuditService.READ_BOOK);
        return bookDAO.findById(id);
    }
    
    public List<Book> getAllBooks() {
        auditService.logAction(AuditService.LIST_ALL_BOOKS);
        return bookDAO.findAll();
    }
    
    public List<Book> getAvailableBooks() {
        auditService.logAction(AuditService.LIST_AVAILABLE_BOOKS);
        return bookDAO.findAvailable();
    }
    
    public List<Book> searchBooksByTitle(String title) {
        auditService.logAction(AuditService.SEARCH_BOOKS_BY_TITLE);
        return bookDAO.findByTitle(title);
    }
    
    public Book updateBook(Book book) {
        auditService.logAction(AuditService.UPDATE_BOOK);
        return bookDAO.update(book);
    }
    
    public boolean deleteBook(int id) {
        auditService.logAction(AuditService.DELETE_BOOK);
        return bookDAO.delete(id);
    }
    
    // Member CRUD operations
    public Member createMember(String firstName, String lastName, Address address, String email, String phone) {
        auditService.logAction(AuditService.CREATE_MEMBER);
        Member member = new Member(nextMemberId++, firstName, lastName, address, email, phone);
        return memberDAO.save(member);
    }
    
    public Optional<Member> getMemberById(int id) {
        auditService.logAction(AuditService.READ_MEMBER);
        return memberDAO.findById(id);
    }
    
    public List<Member> getAllMembers() {
        auditService.logAction(AuditService.LIST_ALL_MEMBERS);
        return memberDAO.findAll();
    }
    
    public Member updateMember(Member member) {
        auditService.logAction(AuditService.UPDATE_MEMBER);
        return memberDAO.update(member);
    }
    
    public boolean deleteMember(int id) {
        auditService.logAction(AuditService.DELETE_MEMBER);
        return memberDAO.delete(id);
    }
    
    // Borrowing CRUD operations
    public Borrowing createBorrowing(Book book, Member member) {
        auditService.logAction(AuditService.CREATE_BORROWING);
        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is not available for borrowing");
        }
        
        Borrowing borrowing = new Borrowing(nextBorrowingId++, book, member);
        borrowingDAO.save(borrowing);
        
        // Update book availability
        book.setAvailable(false);
        bookDAO.update(book);
        
        return borrowing;
    }
    
    public Optional<Borrowing> getBorrowingById(int id) {
        auditService.logAction(AuditService.READ_BORROWING);
        return borrowingDAO.findById(id);
    }
    
    public List<Borrowing> getAllBorrowings() {
        auditService.logAction(AuditService.LIST_ALL_BORROWINGS);
        return borrowingDAO.findAll();
    }
    
    public List<Borrowing> getActiveBorrowings() {
        auditService.logAction(AuditService.LIST_ACTIVE_BORROWINGS);
        return borrowingDAO.findActiveBorrowings();
    }
    
    public List<Borrowing> getMemberBorrowingHistory(int memberId) {
        auditService.logAction(AuditService.GET_MEMBER_BORROWING_HISTORY);
        return borrowingDAO.findByMemberId(memberId);
    }
    
    public boolean returnBook(int borrowingId) {
        auditService.logAction(AuditService.RETURN_BOOK);
        Optional<Borrowing> borrowingOpt = borrowingDAO.findById(borrowingId);
        if (borrowingOpt.isEmpty()) {
            return false;
        }
        
        Borrowing borrowing = borrowingOpt.get();
        if (borrowing.isReturned()) {
            return false;
        }
        
        borrowing.returnBook();
        borrowingDAO.update(borrowing);
        
        // Update book availability
        Book book = borrowing.getBook();
        book.setAvailable(true);
        bookDAO.update(book);
        
        return true;
    }
    
    public boolean deleteBorrowing(int id) {
        auditService.logAction(AuditService.DELETE_BORROWING);
        return borrowingDAO.delete(id);
    }
    
    // Additional business methods
    public Map<Book, Long> getMostPopularBooks() {
        auditService.logAction(AuditService.GET_MOST_POPULAR_BOOKS);
        return borrowingDAO.findAll().stream()
                .collect(Collectors.groupingBy(
                    Borrowing::getBook,
                    Collectors.counting()
                ));
    }
    
    public List<Book> getBorrowedBooks() {
        auditService.logAction(AuditService.GET_BORROWED_BOOKS);
        return getAllBooks().stream()
                .filter(book -> !book.isAvailable())
                .collect(Collectors.toList());
    }
}