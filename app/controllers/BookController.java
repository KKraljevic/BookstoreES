package controllers;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repository.ElasticSearchRepository;

import javax.inject.Inject;

public class BookController extends Controller {

    ElasticSearchRepository elasticSearchRepository;

    @Inject
    public BookController(ElasticSearchRepository elasticSearchRepository){
        this.elasticSearchRepository = elasticSearchRepository;

    }

    public Result getBooks() {
       return ok(Json.toJson(elasticSearchRepository.findAll()));
    }

    public Result getBooksByTitle (String title) {
        return ok(Json.toJson(elasticSearchRepository.findByTitle(title)));
    }

    public Result getBooksByCategory (String category) {
        return ok(Json.toJson(elasticSearchRepository.findByCategory(category)));
    }

    public Result getBooksByWriter (String firstName, String lastName) {
        return ok(Json.toJson(elasticSearchRepository.findByWriter(firstName, lastName)));
    }

    public Result getBooksByPublishingDate (String publishingDate) {
        return ok(Json.toJson(elasticSearchRepository.findByPublishingDate(publishingDate)));
    }

    public Result getBooksByPublishingPeriod (String startDate, String endDate) {
        return ok(Json.toJson(elasticSearchRepository.findByDateRange("publishingDate", startDate, endDate)));
    }

    public Result getBooksByPrice (Integer price) {
        return ok(Json.toJson(elasticSearchRepository.findByPrice(price)));
    }

    public Result getBooksByPriceRange (Integer minPrice, Integer maxPrice) {
        return ok(Json.toJson(elasticSearchRepository.findByNumberRange("price", minPrice, maxPrice)));
    }

    public Result getBooksByPageNumber (Integer pageNumber) {
        return ok(Json.toJson(elasticSearchRepository.findByPageNumber(pageNumber)));
    }

    public Result getBooksByPageRange (Integer minLists, Integer maxLists) {
        return ok(Json.toJson(elasticSearchRepository.findByNumberRange("pageNumber", minLists, maxLists)));
    }

}
