package tr.edu.iyte.installmentor.database.entities;

public class Card extends Entity {
    private static final int CARD_NUMBER_LENGTH = 16;   // FIXME: 08/03/2017 maybe 18?
    public static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_MASTER_CARD = 0;
    public static final int TYPE_VISA = 1;
    public static final int TYPE_MAESTRO = 2;

    private String number;
    private int type;
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

    private void setType() {
        int firstTwo = Integer.parseInt(number.substring(0, 2));
        if(firstTwo / 10 == 4)
            type = TYPE_VISA;
        else if(firstTwo == 50
                || firstTwo == 58
                || firstTwo == 63
                || firstTwo == 67)
            type = TYPE_MAESTRO;
        else if(firstTwo > 50
                && firstTwo < 56)
            type = TYPE_MASTER_CARD;
        else
            type = TYPE_UNKNOWN;

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
