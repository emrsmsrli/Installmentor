package tr.edu.iyte.installmentor.Entities;

import java.util.Date;

public class Product extends Entity {
    private long cardId;
    private String description;
    private Date buyDate;

    public Product(long cardId, String description, Date buyDate) {
        this(0, cardId, description, buyDate);
    }

    public Product(long id, long cardId, String description, Date buyDate) {
        this.id = id;
        this.cardId = cardId;
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
}
