package repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Book;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookRepository implements Repository {

    RestHighLevelClient client;
    SearchRequest searchRequest;

    public BookRepository() {

        this.client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")));
        this.searchRequest  = new SearchRequest("book-store");
    }

    @Override
    public Book createBook(Book book) {
        try {
            XContentBuilder builder = buildBookSource(book);
            IndexRequest indexRequest = new IndexRequest("book-store").source(builder);
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                return book;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Book updateBook(Book updatedBook, String docId) {
        UpdateRequest request = new UpdateRequest("book-store", docId);
        try {
            XContentBuilder builder = buildBookSource(updatedBook);
            IndexRequest indexRequest = new IndexRequest("book-store").source(builder);
            UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
            if (updateResponse.getResult() == DocWriteResponse.Result.CREATED) {
                return updatedBook;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String deleteBook(String docId) {
        DeleteRequest deleteRequest = new DeleteRequest("book-store", docId);
        try {
            DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
            return deleteResponse.status().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Book> findAll() {
        return searchByField(null,null);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return searchByField("title",title);
    }

    @Override
    public List<Book> findByWriter(String firstName, String lastName) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(firstName+" "+lastName,
                "writer.firstName","writer.lastName").type(MultiMatchQueryBuilder.Type.CROSS_FIELDS));
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            if (hits.getTotalHits().value == 0) {
                return null;
            }
            return mapBookList(hits);

        } catch (NullPointerException | ElasticsearchException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Book> findByCategory(String category) {
        return searchByField("category.name",category);
    }

    @Override
    public List<Book> findByPrice(Integer price) {
        return searchByField("price",price.toString());
    }

    @Override
    public List<Book> findByPageNumber(Integer pageNumber) {
        return searchByField("pageNumber",pageNumber.toString());
    }

    @Override
    public List<Book> findByPublishingDate(String date) {
        return searchByField("publishingDate",date);
    }

    @Override
    public List<Book> findByNumberRange(String field, Integer min, Integer max) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.rangeQuery(field).gte(min).lte(max));
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            if (hits.getTotalHits().value == 0) {
                return null;
            }
            return mapBookList(hits);

        } catch (NullPointerException | ElasticsearchException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Book> findByDateRange(String field, String startDate, String endDate) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.rangeQuery(field).from(startDate).to(endDate));
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            if (hits.getTotalHits().value == 0) {
                return null;
            }
            return mapBookList(hits);

        } catch (NullPointerException | ElasticsearchException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Book> searchBooks(String searchInput) {
        return searchByField(null,searchInput);
    }

    public List<Book> searchByField(String fieldName, String searchText) {
        SearchRequest searchRequest = new SearchRequest("book-store");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (fieldName==null && searchText==null) {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        }
        else {
            if (fieldName == null) {
                searchSourceBuilder.query(QueryBuilders.multiMatchQuery(searchText));
            } else {
                searchSourceBuilder.query(QueryBuilders.matchQuery(fieldName, searchText));
            }

        }
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);
        try {
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        if (hits.getTotalHits().value == 0) {
            return null;
        }
        return mapBookList(hits);

        } catch (NullPointerException | ElasticsearchException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Book> mapBookList(SearchHits hits) {
        ObjectMapper mapper = new ObjectMapper();
        List<Book> books = new ArrayList<>();
        try {
            for (SearchHit hit : hits) {
                Book book = mapper.readValue(hit.getSourceAsString(), Book.class);
                books.add(book);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return books;
    }

    public XContentBuilder buildBookSource (Book book) {
        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder();

            builder.startObject();
            {
                builder.field("id",book.getId());
                builder.field("title", book.getTitle());
                builder.field("category");
                builder.field("category.id",book.getCategory().getId());
                builder.field("category.name",book.getCategory().getName());
                builder.field("price",book.getPrice());
                builder.field("pageNumber",book.getPageNumber());
                builder.timeField("purchaseDate",book.getPurchaseDate());
                builder.timeField("publishingDate", book.getPublishingDate());
                builder.field("writer.id", book.getWriter().getId());
                builder.field("writer.firstName", book.getWriter().getFirstName());
                builder.field("writer.lastName", book.getWriter().getLastName());
                builder.field("customer.id", book.getCustomer().getId());
                builder.field("customer.firstName", book.getCustomer().getFirstName());
                builder.field("customer.lastName", book.getCustomer().getLastName());
            }
            builder.endObject();
            return builder;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
