package models;

import java.util.Date;

public class Document {

    Book book;
    Person customer;
    Person writer;
    Integer price;
    Date publishingDate;
    Integer pageNumber;
    Date purchaseDate;

    public Document() {
    }

    public Document(Book book, Person customer, Person writer, Integer price, Date publishingDate, Integer pageNumber, Date purchaseDate) {
        this.book = book;
        this.customer = customer;
        this.writer = writer;
        this.price = price;
        this.publishingDate = publishingDate;
        this.pageNumber = pageNumber;
        this.purchaseDate = purchaseDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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
