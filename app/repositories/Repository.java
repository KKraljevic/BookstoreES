package repositories;

import models.Book;

import java.util.List;

public interface Repository {

    Book createBook(Book book);

    Book updateBook(Book updatedBook, String docId);

    String deleteBook(String docId);

    List<Book> findAll();

    List<Book> findById(Long id);

    List<Book> findByTitle(String title);

    List<Book> findByWriter(String firstName,String lastName);

    List<Book> findByCategory(String category);

    List<Book> findByPrice(Integer price);

    List<Book> findByPageNumber(Integer pageNumber);

    List<Book> findByPublishingDate(String date);

    List<Book> findByNumberRange(String field, Integer min, Integer max, Integer size);

    List<Book> findByDateRange(String field, String startDate, String endDate, Integer size);

    List<Book> searchBooks(String searchInput);

    List<Book> getFeaturedBooks(Integer size);

}
