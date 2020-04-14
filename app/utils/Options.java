package utils;

public class Options {

    Integer currentPage;
    Integer totalPages;
    Integer size;
    String sort;
    String order;
    Long totalItems;

    public Options() {
    }

    public Options(Integer currentPage, Integer size, String sort, String order, Long totalItems) {
        this.currentPage = currentPage;
        this.size = size;
        this.sort = sort;
        this.order = order;
        this.totalItems = totalItems;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        if (size != 0) {
            return (int) (totalItems + size - 1) / size;
        }
        return 0;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
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
        if (totalItems < 1) {
            this.totalItems = (long) 0;
        } else {
            this.totalItems = totalItems;
        }
    }
}
