package tr.edu.iyte.installmentor.Entities;

public class Card {
    private static final int CARD_NUMBER_LENGTH = 16;
    private static final int TYPE_UNKNOWN = -1;
    private static final int TYPE_MASTER_CARD = 0;
    private static final int TYPE_VISA = 1;

    private String cardNumber;
    private int cardType;
    private String cardHolderName;

    public Card(String cardNumber, String cardHolderName) {
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        setType();
    }

    private void setType() {
        int firstTwo = Integer.parseInt(cardNumber.substring(0, 2));
        if(firstTwo / 10 == 4)
            cardType = TYPE_VISA;
        else if(firstTwo > 50 && firstTwo < 56)
            cardType = TYPE_MASTER_CARD;
        else
            cardType = TYPE_UNKNOWN;

    }

    public static boolean isCardNumberValid(String cardNumber) {
        int cnLength = cardNumber.length();
        if(cnLength != CARD_NUMBER_LENGTH)
            return false;

        int sum = 0;
        for(int i = 0; i < cnLength; i += 2) {
            sum += Character.getNumericValue(i) * 2;
            sum += Character.getNumericValue(i + 1);
        }

        return sum % 10 == 0;
    }

    public int getCardType() {
        return cardType;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }
}
