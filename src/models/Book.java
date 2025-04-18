package models;

public class Book extends BaseEntity {
    private String title;
    private Author author;
    private Genre genre;
    private boolean available;
    private int year;

    public Book(int id, String title, Author author, Genre genre, int year) {
        super(id);
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.available = true;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', author=" + author.getName() +
                ", genre=" + genre + ", available=" + available + ", year=" + year + "}";
    }
}