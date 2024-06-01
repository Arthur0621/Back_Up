package model;

public class EBook extends Book {
    private String format;
    private double size;

    public EBook() {
        super();
    }

    public EBook(int itemId, String title, String author, int yearPublished, int quantity, String format, double size) {
        super(itemId, title, author, yearPublished, quantity);
        this.format = format;
        this.size = size;
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