package model;

public abstract class LibraryItem {
    protected int itemId;
    protected String title;
    public LibraryItem() {}
    
    public LibraryItem(int itemId, String title) {
        this.itemId = itemId;
        this.title = title;
    }

    public int getItemId() {
        return itemId;
    }

    public String getTitle() {
        return title;
    }
}