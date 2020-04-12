package repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Book;
import models.Pair;
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
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import utils.Paginate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BookRepository implements Repository {

    RestHighLevelClient client;
    SearchRequest searchRequest;
    final String[] excludes = {"category.field", "category.fields"};

    public BookRepository() {

        this.client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")));
        this.searchRequest = new SearchRequest("book-store");
    }

    @Override
    public Book createBook(Book book) {
        try {
            final XContentBuilder builder = buildBookSource(book);
            final IndexRequest indexRequest = new IndexRequest("book-store").source(builder);
            final IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
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
        final UpdateRequest request = new UpdateRequest("book-store", docId);
        try {
            final XContentBuilder builder = buildBookSource(updatedBook);
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
        final DeleteRequest deleteRequest = new DeleteRequest("book-store", docId);
        try {
            DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
            return deleteResponse.status().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Paginate<Book> findAll(Integer size, Integer page, String sort, String order) {
        return searchByField(null, null, size, page, sort, order);
    }

    @Override
    public Paginate<Book> findById(Long id) {
        return searchByField("id", id.toString(), 1, 1, "id", "DESC");
    }

    @Override
    public Paginate<Book> findByTitle(String title, Integer size, Integer page, String sort, String order) {
        return searchByField("title", title, size, page, sort, order);
    }

    @Override
    public Paginate<Book> findByWriter(String firstName, String lastName, Integer size, Integer page, String sort, String order
    ) {
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(firstName + " " + lastName,
                "writer.firstName", "writer.lastName").type(MultiMatchQueryBuilder.Type.CROSS_FIELDS));
        searchSourceBuilder.from((page - 1) * size).size(size);
        searchSourceBuilder.sort(sort, SortOrder.fromString(order));
        searchSourceBuilder.fetchSource(null, excludes);
        searchRequest.source(searchSourceBuilder);
        try {
            final SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            if (hits.getTotalHits().value == 0) {
                return null;
            }

            return new Paginate<Book>(mapBookList(hits), page, size, sort, order, hits.getTotalHits().value);

        } catch (NullPointerException | ElasticsearchException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Paginate<Book> findByCategory(String category, Integer size, Integer page, String sort, String order) {
        return searchByField("category.name", category, size, page, sort, order);
    }

    @Override
    public Paginate<Book> findByPrice(Integer price, Integer size, Integer page, String sort, String order) {
        return searchByField("price", price.toString(), size, page, sort, order);
    }

    @Override
    public Paginate<Book> findByPageNumber(Integer pageNumber, Integer size, Integer page, String sort, String order) {
        return searchByField("pageNumber", pageNumber.toString(), size, page, sort, order);
    }

    @Override
    public Paginate<Book> findByPublishingDate(String date, Integer size, Integer page, String sort, String order) {
        return searchByField("publishingDate", date, size, page, sort, order);
    }

    @Override
    public Paginate<Book> findByNumberRange(String field, Integer min, Integer max, Integer size, Integer page, String sort, String order) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.rangeQuery(field).gte(min).lte(max));
        searchSourceBuilder.from((page - 1) * size).size(size);
        searchSourceBuilder.sort(sort, SortOrder.fromString(order));
        String[] excludes = {"category.field", "category.fields"};
        searchSourceBuilder.fetchSource(null, excludes);
        searchRequest.source(searchSourceBuilder);
        try {
            final SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            if (hits.getTotalHits().value == 0) {
                return null;
            }
            return new Paginate<Book>(mapBookList(hits), page, size, sort, order, hits.getTotalHits().value);

        } catch (NullPointerException | ElasticsearchException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Paginate<Book> findByDateRange(String field, String startDate, String endDate, Integer size, Integer page, String sort, String order) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.rangeQuery(field).from(startDate).to(endDate));
        searchSourceBuilder.from((page - 1) * size).size(size);
        searchSourceBuilder.sort(sort, SortOrder.fromString(order));
        String[] excludes = {"category.field", "category.fields"};
        searchSourceBuilder.fetchSource(null, excludes);
        searchSourceBuilder.sort(new FieldSortBuilder("unitsSold").order(SortOrder.DESC));
        searchRequest.source(searchSourceBuilder);

        try {
            final SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            if (hits.getTotalHits().value == 0) {
                return null;
            }
            return new Paginate<Book>(mapBookList(hits), page, size, sort, order, hits.getTotalHits().value);

        } catch (NullPointerException | ElasticsearchException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Paginate<Book> searchBooks(String searchInput, Integer size, Integer page, String sort, String order) {
        return searchByField(null, searchInput, size, page, sort, order);
    }

    @Override
    public Paginate<Book> getFeaturedBooks(Integer size) {
        return searchByField(null, null, size, 1, "unitsSold", "DESC");
    }

    public Paginate<Book> searchByField(String fieldName, String searchText, Integer size, Integer page, String sort, String order) {
        final SearchRequest searchRequest = new SearchRequest("book-store");
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (fieldName == null && (searchText == null || searchText.isEmpty())) {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        } else {
            if (fieldName == null) {
                searchSourceBuilder.query(QueryBuilders.multiMatchQuery(searchText));
            } else {
                searchSourceBuilder.query(QueryBuilders.matchQuery(fieldName, searchText));
            }

        }
        searchSourceBuilder.from((page - 1) * size).size(size);
        searchSourceBuilder.sort(sort, SortOrder.fromString(order));
        searchSourceBuilder.fetchSource(null, excludes);
        searchSourceBuilder.sort(new FieldSortBuilder(sort).order(SortOrder.fromString(order)));
        searchRequest.source(searchSourceBuilder);
        try {
            final SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            if (hits.getTotalHits().value == 0) {
                return new Paginate<Book>(Arrays.asList(), page, size, sort, order, hits.getTotalHits().value);
            }
            return new Paginate<Book>(mapBookList(hits), page, size, sort, order, hits.getTotalHits().value);

        } catch (NullPointerException | ElasticsearchException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Book> mapBookList(SearchHits hits) {
        final ObjectMapper mapper = new ObjectMapper();
        final List<Book> books = new ArrayList<>();
        try {
            for (SearchHit hit : hits) {
                final Book book = mapper.readValue(hit.getSourceAsString(), Book.class);
                books.add(book);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return books;
    }

    public XContentBuilder buildBookSource(Book book) {
        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder();

            builder.startObject();
            {
                builder.field("id", book.getId());
                builder.field("title", book.getTitle());
                builder.field("category");
                builder.field("category.id", book.getCategory().getId());
                builder.field("category.name", book.getCategory().getName());
                builder.field("price", book.getPrice());
                builder.field("pageNumber", book.getPageNumber());
                builder.timeField("purchaseDate", book.getPurchaseDate());
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

    public List<Pair> aggPublishingDateRange() {
        final SearchRequest sr = new SearchRequest("book-store");
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        final AggregationBuilder aggregation =
                AggregationBuilders
                        .dateHistogram("agg")
                        .field("publishingDate")
                        .format("yyyy")
                        .minDocCount(1)
                        .calendarInterval(DateHistogramInterval.YEAR);
        searchSourceBuilder.query(QueryBuilders.matchAllQuery()).aggregation(aggregation);
        sr.source(searchSourceBuilder);
        try {
            final SearchResponse searchResponse = client.search(sr, RequestOptions.DEFAULT);
            Histogram agg = searchResponse.getAggregations().get("agg");

            final List<Pair> result = new ArrayList<>();
            for (Histogram.Bucket entry : agg.getBuckets()) {
                result.add(new Pair(entry.getKeyAsString(), entry.getDocCount()));
            }
            return result;
        } catch (NullPointerException | ElasticsearchException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Pair> aggPriceRange() {
        final SearchRequest sr = new SearchRequest("book-store");
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        final AggregationBuilder aggregation =
                AggregationBuilders
                        .range("agg")
                        .field("price")
                        .addUnboundedTo(100f)
                        .addRange(100, 200f)
                        .addRange(200, 300f)
                        .addUnboundedFrom(300);

        searchSourceBuilder.query(QueryBuilders.matchAllQuery()).aggregation(aggregation);
        sr.source(searchSourceBuilder);
        try {
            final SearchResponse searchResponse = client.search(sr, RequestOptions.DEFAULT);

            final Range agg = searchResponse.getAggregations().get("agg");
            final List<Pair> result = new ArrayList<>();
            for (Range.Bucket entry : agg.getBuckets()) {
                if (entry.getDocCount() > 0) {
                    result.add(new Pair(entry.getKeyAsString(), entry.getDocCount()));
                }
            }
            return result;
        } catch (NullPointerException | ElasticsearchException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Pair> aggCategories() {
        final SearchRequest sr = new SearchRequest("book-store");
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(false).aggregation(AggregationBuilders
                .terms("categories")
                .field("category.name.keyword")
        );
        sr.source(searchSourceBuilder);
        try {
            final SearchResponse searchResponse = client.search(sr, RequestOptions.DEFAULT);

            final Terms categories = searchResponse.getAggregations().get("categories");
            System.out.println(searchResponse.toString());
            final List<Pair> result = new ArrayList<>();
            for (Terms.Bucket entry : categories.getBuckets()) {
                result.add(new Pair(entry.getKeyAsString(), entry.getDocCount()));
            }
            return result;
        } catch (NullPointerException | ElasticsearchException | IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<Pair> getTop10() {
        final SearchRequest searchRequest = new SearchRequest("book-store");
        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.size(10);
        final String[] includes = {"title", "unitsSold"};
        searchSourceBuilder.fetchSource(includes, null);
        searchSourceBuilder.sort(new FieldSortBuilder("unitsSold").order(SortOrder.DESC));
        searchRequest.source(searchSourceBuilder);
        final List<Pair> result = new ArrayList<>();
        try {
            final SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            if (hits.getTotalHits().value == 0) {
                return null;
            }
            for (SearchHit hit : hits) {
                final Map<String, Object> sourcefields = hit.getSourceAsMap();
                result.add(new Pair(
                        sourcefields.get("title").toString(),
                        Long.parseLong(sourcefields.get("unitsSold").toString()
                        )));
            }
            return result;

        } catch (NullPointerException | ElasticsearchException | IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
