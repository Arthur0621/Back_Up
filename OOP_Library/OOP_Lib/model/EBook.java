package model;

public class EBook extends Book {
    private String format;
    private double size;

    public EBook() {
        super();
        this.setQuantity(1);
    }

    public EBook(String itemId, String title, String author, int yearPublished, int quantity, String format, double size) {
        super(itemId, title, author, yearPublished, quantity);
        this.format = format;
        this.size = size;
    }

    public EBook(String bookId, String title, String author, int yearPublished, double size, String format) {

    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

}