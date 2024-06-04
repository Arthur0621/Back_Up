package model;

public abstract class LibraryItem {
    protected String itemId;
    protected String title;
    public LibraryItem() {}
    
    public LibraryItem(String itemId, String title) {
        this.itemId = itemId;
        this.title = title;
    }

    public String getItemId() {
        return itemId;
    }

    public String getTitle() {
        return title;
    }
}