package controllers;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.*;

import play.mvc.*;

import java.io.IOException;

public class HomeController extends Controller {

    RestHighLevelClient client;

    public HomeController() {

        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")));
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result getBook(Long bookId) {
        try {
            GetRequest getRequest = new GetRequest( "bookstore", bookId.toString());
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            return ok(getResponse.getSourceAsString());
        } catch (IOException | NullPointerException | ElasticsearchException e) {
            return ok(e.getMessage());
        }
    }
}
