package tr.edu.iyte.installmentor.database.entities;

public class Card extends Entity {
    private static final int CARD_NUMBER_LENGTH = 18;

    public enum CardType {
        UNKNOWN,
        MASTER_CARD,
        VISA,
        DISCOVER,
        AMERICAN_EXPRESS,
        MAESTRO // TODO: 16/03/2017 add more card types
    }

    private String number;
    private CardType type;
    private String holderName;
    private String bankName;

    public Card(String number, String holderName, String bankName) {
        this(0, number, holderName, bankName);
    }

    public Card(long id, String number, String holderName, String bankName) {
        this.id = id;
        setHolderName(holderName);
        setNumber(number);
        setBankName(bankName);
    }

    public static boolean isCardNumberValid(String cardNumber) {
        int cnLength = cardNumber.length();
        if(cnLength != CARD_NUMBER_LENGTH) // TODO: 16/03/2017 necessary?
            return false;

        String cardN = cardNumber.replaceAll("\\s*", "");

        int sum = 0;
        for(int i = cnLength - 1; i >= 0; i -= 2) {
            sum += Character.getNumericValue(cardN.codePointAt(i - 1)) * 2;
            sum += Character.getNumericValue(cardN.codePointAt(i));
        }

        return sum % 10 == 0;
    }

    public long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    private void setType() {
        int firstSix = Integer.parseInt(number.replaceAll("\\s*", "").substring(0, 6));
        int firstFour = firstSix / 100;
        int firstThree = firstFour / 10;
        int firstTwo = firstThree / 10;
        int first = firstTwo / 10;

        if(firstSix > 622125 && firstSix < 622926
                || firstFour == 6011
                || firstThree > 643 && firstThree < 650
                || firstTwo == 65)
            type = CardType.DISCOVER;
        else if(firstFour > 2220 && firstFour < 2721
                || firstTwo > 50
                && firstTwo < 56)
            type = CardType.MASTER_CARD;
        else if(firstTwo == 50
                || firstTwo == 58
                || firstTwo == 63
                || firstTwo == 67)
            type = CardType.MAESTRO;
        else if(firstTwo == 34
                || firstTwo == 37)
            type = CardType.AMERICAN_EXPRESS;
        else if(first == 4)
            type = CardType.VISA;
        else
            type = CardType.UNKNOWN;

    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        if(number != null)
            setType();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
