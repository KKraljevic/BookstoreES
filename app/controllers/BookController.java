package controllers;

import models.Book;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.BookRepository;

import javax.inject.Inject;
import java.util.List;

public class BookController extends Controller {

    private final BookRepository bookRepository;
    Integer defaultSize = 10;

    @Inject
    public BookController(BookRepository BookRepository) {
        this.bookRepository = BookRepository;

    }

    public Result getBooks() {
        return ok(Json.toJson(bookRepository.findAll()));
    }

    public Result getBooksById(Long id) {
        final List<Book> b = bookRepository.findById(id);
        if (!b.isEmpty()) {
            return ok(Json.toJson(b.get(0)));
        } else {
            return notFound("No book with id:" + id.toString());
        }
    }

    public Result getBooksByTitle(String title) {
        return ok(Json.toJson(bookRepository.findByTitle(title)));
    }

    public Result getBooksByCategory(String category) {
        return ok(Json.toJson(bookRepository.findByCategory(category)));
    }

    public Result getBooksByWriter(String firstName, String lastName) {
        return ok(Json.toJson(bookRepository.findByWriter(firstName, lastName)));
    }

    public Result getBooksByPublishingDate(String publishingDate) {
        return ok(Json.toJson(bookRepository.findByPublishingDate(publishingDate)));
    }

    public Result getBooksByPublishingPeriod(String startDate, String endDate, Integer size) {
        return ok(Json.toJson(bookRepository.findByDateRange("publishingDate", startDate, endDate, size)));
    }

    public Result getBooksByPrice(Integer price) {
        return ok(Json.toJson(bookRepository.findByPrice(price)));
    }

    public Result getBooksByPriceRange(Integer minPrice, Integer maxPrice, Integer size) {
        return ok(Json.toJson(bookRepository.findByNumberRange("price", minPrice, maxPrice, size)));
    }

    public Result getBooksByPageNumber(Integer pageNumber) {
        return ok(Json.toJson(bookRepository.findByPageNumber(pageNumber)));
    }

    public Result getBooksByPageRange(Integer minLists, Integer maxLists, Integer size) {
        return ok(Json.toJson(bookRepository.findByNumberRange("pageNumber", minLists, maxLists, size)));
    }

    public Result searchBooks(String searchInput) {
        return ok(Json.toJson(bookRepository.searchBooks(searchInput)));
    }

    public Result getFeaturedBooks(Integer size) {
        return ok(Json.toJson(bookRepository.getFeaturedBooks(size)));
    }

    public Result getTop10() {
        return ok(Json.toJson(bookRepository.getTop10()));
    }

    public Result getAggPriceRange() {
        return ok(Json.toJson(bookRepository.aggPriceRange()));
    }

    public Result getAggPublishingDateRange() {
        return ok(Json.toJson(bookRepository.aggPublishingDateRange()));
    }

    public Result getAggCategories() {
        return ok(Json.toJson(bookRepository.aggCategories()));
    }


}
