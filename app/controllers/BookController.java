package controllers;

import models.Book;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.BookRepository;
import utils.Paginate;

import javax.inject.Inject;

public class BookController extends Controller {

    private final BookRepository bookRepository;

    @Inject
    public BookController(BookRepository BookRepository) {
        this.bookRepository = BookRepository;

    }

    public Result getBooks(Integer size, Integer page, String sort, String order) {
        return ok(Json.toJson(bookRepository.findAll(size, page, sort, order)));
    }

    public Result getBooksById(Long id) {
        final Paginate<Book> b = bookRepository.findById(id);
        if (b != null) {
            return ok(Json.toJson(b.getItems().get(0)));
        } else {
            return notFound("No book with id: " + id.toString());
        }
    }

    public Result getBooksByTitle(String title, Integer size, Integer page, String sort, String order) {
        return ok(Json.toJson(bookRepository.findByTitle(title, size, page, sort, order)));
    }

    public Result getBooksByCategory(String category, Integer size, Integer page, String sort, String order) {
        return ok(Json.toJson(bookRepository.findByCategory(category, size, page, sort, order)));
    }

    public Result getBooksByWriter(String firstName, String lastName, Integer size, Integer page, String sort, String order
    ) {
        return ok(Json.toJson(bookRepository.findByWriter(firstName, lastName, size, page, sort, order)));
    }

    public Result getBooksByPublishingDate(String publishingDate, Integer size, Integer page, String sort, String order) {
        return ok(Json.toJson(bookRepository.findByPublishingDate(publishingDate, size, page, sort, order)));
    }

    public Result getBooksByPublishingPeriod(String startDate, String endDate, Integer size, Integer page, String sort, String order) {
        return ok(Json.toJson(bookRepository.findByDateRange("publishingDate", startDate, endDate, size, page, sort, order)));
    }

    public Result getBooksByPrice(Integer price, Integer size, Integer page, String sort, String order) {
        return ok(Json.toJson(bookRepository.findByPrice(price, size, page, sort, order)));
    }

    public Result getBooksByPriceRange(Integer minPrice, Integer maxPrice, Integer size, Integer page, String sort, String order) {
        return ok(Json.toJson(bookRepository.findByNumberRange("price", minPrice, maxPrice, size, page, sort, order)));
    }

    public Result getBooksByPageNumber(Integer pageNumber, Integer size, Integer page, String sort, String order) {
        return ok(Json.toJson(bookRepository.findByPageNumber(pageNumber, size, page, sort, order)));
    }

    public Result getBooksByPageRange(Integer minLists, Integer maxLists, Integer size, Integer page, String sort, String order) {
        return ok(Json.toJson(bookRepository.findByNumberRange("pageNumber", minLists, maxLists, size, page, sort, order)));
    }

    public Result searchBooks(String searchInput, Integer size, Integer page, String sort, String order) {
        return ok(Json.toJson(bookRepository.searchBooks(searchInput, size, page, sort, order)));
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
