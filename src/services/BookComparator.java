package services;

import models.Book;
import java.util.Comparator;

public class BookComparator implements Comparator<Book> {
    @Override
    public int compare(Book b1, Book b2) {
        int titleCompare = b1.getTitle().compareTo(b2.getTitle());
        if (titleCompare != 0) {
            return titleCompare;
        }

        return b1.getAuthor().getName().compareTo(b2.getAuthor().getName());
    }
}
