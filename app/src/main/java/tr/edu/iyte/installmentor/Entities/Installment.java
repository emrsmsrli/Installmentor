package tr.edu.iyte.installmentor.Entities;

import java.util.Date;

public class Installment {
    private long id;
    private long cardId;
    private float amount;
    private Date date;

    public Installment(long cardId, float amount, Date date) {
        this(0, cardId, amount, date);
    }

    public Installment(long id, long cardId, float amount, Date date) {
        this.id = id;
        this.cardId = cardId;
        this.amount = amount;
        this.date = date;
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
}
