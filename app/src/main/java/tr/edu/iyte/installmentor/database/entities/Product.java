package tr.edu.iyte.installmentor.database.entities;

import java.util.Date;

public class Product extends Entity {
    private long cardId;
    private String description;
    private Date buyDate;
    private long totalAmount;
    private long remainingAmount;

    public Product(long cardId, long totalAmount, String description, Date buyDate) {
        this(0, cardId, totalAmount, description, buyDate);
    }

    public Product(long id, long cardId, long totalAmount, String description, Date buyDate) {
        this.id = id;
        this.cardId = cardId;
        setTotalAmount(totalAmount);
        setDescription(description);
        setBuyDate(buyDate);
    }

    public long getId() {
        return id;
    }

    public long getCardId() {
        return cardId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(long remainingAmount) {
        this.remainingAmount = remainingAmount;
    }
}
