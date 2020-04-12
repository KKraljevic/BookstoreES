package repositories;

import models.Book;
import utils.Paginate;

public interface Repository {

    Book createBook(Book book);

    Book updateBook(Book updatedBook, String docId);

    String deleteBook(String docId);

    Paginate<Book> findAll(Integer size, Integer page, String sort, String order);

    Paginate<Book> findById(Long id);

    Paginate<Book> findByTitle(String title, Integer size, Integer page, String sort, String order);

    Paginate<Book> findByWriter(String firstName, String lastName, Integer size, Integer page, String sort, String order);

    Paginate<Book> findByCategory(String category, Integer size, Integer page, String sort, String order);

    Paginate<Book> findByPrice(Integer price, Integer size, Integer page, String sort, String order);

    Paginate<Book> findByPageNumber(Integer pageNumber, Integer size, Integer page, String sort, String order);

    Paginate<Book> findByPublishingDate(String date, Integer size, Integer page, String sort, String order);

    Paginate<Book> findByNumberRange(String field, Integer min, Integer max, Integer size, Integer page, String sort, String order);

    Paginate<Book> findByDateRange(String field, String startDate, String endDate, Integer size, Integer page, String sort, String order);

    Paginate<Book> searchBooks(String searchInput, Integer size, Integer page, String sort, String order);

    Paginate<Book> getFeaturedBooks(Integer size);

}
