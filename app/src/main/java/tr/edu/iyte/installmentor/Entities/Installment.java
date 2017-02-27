package tr.edu.iyte.installmentor.Entities;

import java.util.Date;

public class Installment {
    private float amount;
    private Date date;
    private long cardId;

    public Installment(float amount, Date date) {
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

}
