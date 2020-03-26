package controllers;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.IOException;

public class BookController extends Controller {

    RestHighLevelClient client;
    SearchRequest searchRequest = new SearchRequest("bookstore");

    public BookController(){
        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")));
    }

    public Result getBooks() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.size(20);
        searchRequest.source(searchSourceBuilder);
        return ok(showResponse(searchRequest));
    }

    public Result getBooksByTitle (String title) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("book.title", title));
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);
        return ok(showResponse(searchRequest));
    }

    public Result getBooksByCategory (String category) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("book.category.name", category));
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);
        return ok(showResponse(searchRequest));
    }

    public Result getBooksByWriter (String firstName, String lastName) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(firstName+" "+lastName,
                "writer.firstName","writer.lastName").type(MultiMatchQueryBuilder.Type.CROSS_FIELDS));
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);
        return ok(showResponse(searchRequest));
    }

    public Result getBooksByPublishingDate (String publishingDate) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("publishingDate",publishingDate));
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);
        return ok(showResponse(searchRequest));
    }

    public Result getBooksByPrice (Long price) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("price",price));
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);
        return ok(showResponse(searchRequest));
    }

    public Result getBooksByPageNumber (Long pageNumber) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("pageNumber",pageNumber));
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);
        return ok(showResponse(searchRequest));
    }

    public String showResponse(SearchRequest sr) {
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            if(hits.getTotalHits().value==0) {
                return "No results";
            }
            SearchHit[] searchHits = hits.getHits();
            String response = "";
            for (SearchHit hit : searchHits) {
                response +=hit.getSourceAsString()+"\n";
            }
            return response;
        } catch (IOException | NullPointerException | ElasticsearchException e) {
            return e.getMessage();
        }
    }

}
