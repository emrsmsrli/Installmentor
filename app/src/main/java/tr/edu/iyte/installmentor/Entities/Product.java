package tr.edu.iyte.installmentor.Entities;

import java.util.Date;

public class Product {
    private long id;
    private long cardId;
    private String description;
    private Date buyDate;

    public Product(long cardId, String description, Date buyDate) {
        this(0, cardId, description, buyDate);
    }

    public Product(long id, long cardId, String description, Date buyDate) {
        this.id = id;
        this.cardId = cardId;
        this.description = description;
        this.buyDate = buyDate;
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

    public Date getBuyDate() {
        return buyDate;
    }
}
