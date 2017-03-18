package tr.edu.iyte.installmentor.database.entities;

import java.util.Date;

public class Product extends Entity {
    private long cardId;
    private String description;
    private Date buyDate;
    private float totalAmount;
    private float remainingAmount;

    public Product(long cardId, float totalAmount, String description, Date buyDate) {
        this(0, cardId, totalAmount, description, buyDate);
    }

    public Product(long id, long cardId, float totalAmount, String description, Date buyDate) {
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

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(float remainingAmount) {
        this.remainingAmount = remainingAmount;
    }
}
