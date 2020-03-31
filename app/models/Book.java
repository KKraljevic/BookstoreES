package models;

import java.util.Date;

public class Book {

    Long id;
    String title;
    Category category;
    Person customer;
    Person writer;
    Integer price;
    Date publishingDate;
    Integer pageNumber;
    Date purchaseDate;

    public Book() {
    }

    public Book(Long id, String title, Category category, Person customer, Person writer, Integer price, Date publishingDate, Integer pageNumber, Date purchaseDate) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.customer = customer;
        this.writer = writer;
        this.price = price;
        this.publishingDate = publishingDate;
        this.pageNumber = pageNumber;
        this.purchaseDate = purchaseDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Person getCustomer() {
        return customer;
    }

    public void setCustomer(Person customer) {
        this.customer = customer;
    }

    public Person getWriter() {
        return writer;
    }

    public void setWriter(Person writer) {
        this.writer = writer;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(Date publishingDate) {
        this.publishingDate = publishingDate;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
