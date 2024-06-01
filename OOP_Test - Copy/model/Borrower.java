package model;

public class Borrower {
    private int borrowerId;
    private String name;
    private String address;
    private String phoneNumber;
    private String username;
    private String password;

    public Borrower() {}
    public Borrower(int borrowerId, String name, String address, String phoneNumber, String username, String password) {
        this.borrowerId = borrowerId;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
    }

    public Borrower(int borrowerId, String name, String address, String phoneNumber) {
        this.borrowerId = borrowerId;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}