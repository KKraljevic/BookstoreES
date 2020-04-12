package utils;

import java.util.List;

public class Paginate<T> {
    List<T> items;
    Integer currentPage;
    Integer totalPages;
    Integer size;
    String sort;
    String order;
    Long totalItems;

    public Paginate() {
    }

    public Paginate(List<T> items, Integer currentPage, Integer size, String sort, String order, Long totalItems) {
        this.items = items;
        this.currentPage = currentPage;
        this.size = size;
        this.sort = sort;
        this.order = order;
        this.totalItems = totalItems;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        if(size!=0) {
            return (int)(totalItems+size-1)/size;
        }
        return 0;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
