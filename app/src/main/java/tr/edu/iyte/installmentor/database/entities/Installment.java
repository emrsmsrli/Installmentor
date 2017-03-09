package tr.edu.iyte.installmentor.database.entities;

import java.util.Date;

public class Installment extends Entity {
    private long productId;
    private long cardId;
    private float amount;
    private Date date;

    public Installment(long productId, long cardId, float amount, Date date) {
        this(0, productId, cardId, amount, date);
    }

    public Installment(long id, long productId, long cardId, float amount, Date date) {
        this.id = id;
        this.productId = productId;
        this.cardId = cardId;
        setAmount(amount);
        setDate(date);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public long getCardId() {
        return cardId;
    }

    public long getId() {
        return id;
    }

    public long getProductId() {
        return productId;
    }
}
