package models;

public class EBook extends Book {
    private String format;
    private int sizeInMB;

    public EBook(int id, String title, Author author, Genre genre, int year, String format, int sizeInMB) {
        super(id, title, author, genre, year);
        this.format = format;
        this.sizeInMB = sizeInMB;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getSizeInMB() {
        return sizeInMB;
    }

    public void setSizeInMB(int sizeInMB) {
        this.sizeInMB = sizeInMB;
    }

    @Override
    public String toString() {
        return "EBook{" + super.toString() + ", format='" + format + "', sizeInMB=" + sizeInMB + "}";
    }
}