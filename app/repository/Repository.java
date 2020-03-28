package repository;

import models.Document;

import java.util.List;

public interface Repository {

    Document createBook(Document book);

    Document updateBook(Document updatedBook, String docId);

    String deleteBook(String docId);

    List<Document> findAll();

    List<Document> findByTitle(String title);

    List<Document> findByWriter(String firstName,String lastName);

    List<Document> findByCategory(String category);

    List<Document> findByPrice(Integer price);

    List<Document> findByPageNumber(Integer pageNumber);

    List<Document> findByPublishingDate(String date);

    List<Document> findByNumberRange(String field, Integer min, Integer max);

    List<Document> findByDateRange(String field, String startDate, String endDate);

    List<Document> searchBooks(String searchInput);

}
