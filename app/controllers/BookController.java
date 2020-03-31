package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repositories.BookRepository;

import javax.inject.Inject;

public class BookController extends Controller {

    private final BookRepository bookRepository;

    @Inject
    public BookController(BookRepository BookRepository){
        this.bookRepository = BookRepository;

    }

    public Result getBooks() {
       return ok(Json.toJson(bookRepository.findAll()));
    }

    public Result getBooksByTitle (String title) {
        return ok(Json.toJson(bookRepository.findByTitle(title)));
    }

    public Result getBooksByCategory (String category) {
        return ok(Json.toJson(bookRepository.findByCategory(category)));
    }

    public Result getBooksByWriter (String firstName, String lastName) {
        return ok(Json.toJson(bookRepository.findByWriter(firstName, lastName)));
    }

    public Result getBooksByPublishingDate (String publishingDate) {
        return ok(Json.toJson(bookRepository.findByPublishingDate(publishingDate)));
    }

    public Result getBooksByPublishingPeriod (String startDate, String endDate) {
        return ok(Json.toJson(bookRepository.findByDateRange("publishingDate", startDate, endDate)));
    }

    public Result getBooksByPrice (Integer price) {
        return ok(Json.toJson(bookRepository.findByPrice(price)));
    }

    public Result getBooksByPriceRange (Integer minPrice, Integer maxPrice) {
        return ok(Json.toJson(bookRepository.findByNumberRange("price", minPrice, maxPrice)));
    }

    public Result getBooksByPageNumber (Integer pageNumber) {
        return ok(Json.toJson(bookRepository.findByPageNumber(pageNumber)));
    }

    public Result getBooksByPageRange (Integer minLists, Integer maxLists) {
        return ok(Json.toJson(bookRepository.findByNumberRange("pageNumber", minLists, maxLists)));
    }

    public Result searchBooks (String searchInput) {
        return ok(Json.toJson(bookRepository.searchBooks(searchInput)));
    }
}
