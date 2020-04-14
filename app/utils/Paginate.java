package utils;

import java.util.List;

public class Paginate<T> {
    List<T> items;
    Options options;

    public Paginate() {
    }

    public Paginate(List<T> items, Integer currentPage, Integer size, String sort, String order, Long totalItems) {
        this.items = items;
        this.options = new Options(currentPage,size,sort,order,totalItems);
    }

    public Paginate(List<T> items, Options options) {
        this.items = items;
        this.options = options;
    }

    public List<T> getItems() {
        return items;
    }

}
