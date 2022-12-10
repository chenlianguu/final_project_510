package models;

public class Book {

    int bid;
    String author;
    String title;
    String status; //0 available 1 unavaible

    public Book() {
    }

    public Book(String author, String title) {
        this.author = author;
        this.title = title;

    }


    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Book{" +
                "bId=" + bid +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                '}';
    }

}
