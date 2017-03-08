package tr.edu.iyte.installmentor.Entities;

public class Card {
    private static final int CARD_NUMBER_LENGTH = 16;   // FIXME: 08/03/2017 maybe 18?
    private static final int TYPE_UNKNOWN = -1;
    private static final int TYPE_MASTER_CARD = 0;
    private static final int TYPE_VISA = 1;

    private long id;
    private String number;
    private int type;
    private String holderName;

    public Card(String number, String holderName) {
        this(0, number, holderName);
    }

    public Card(long id, String number, String holderName) {
        this.id = id;
        setHolderName(holderName);
        setNumber(number);
    }

    private void setType() {
        int firstTwo = Integer.parseInt(number.substring(0, 2));
        if(firstTwo / 10 == 4)
            type = TYPE_VISA;
        else if(firstTwo > 50 && firstTwo < 56)
            type = TYPE_MASTER_CARD;
        else
            type = TYPE_UNKNOWN;

    }

    public static boolean isCardNumberValid(String cardNumber) {
        int cnLength = cardNumber.length();
        if(cnLength != CARD_NUMBER_LENGTH)
            return false;

        int sum = 0;
        for(int i = cnLength - 1; i >= 0; i -= 2) {
            sum += Character.getNumericValue(i - 1) * 2;
            sum += Character.getNumericValue(i);
        }

        return sum % 10 == 0;
    }

    public long getId() {
        return id;
    }

    public int getType() {
        return type;
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
        setType();
    }
}
