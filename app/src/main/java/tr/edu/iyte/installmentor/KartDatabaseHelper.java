package tr.edu.iyte.installmentor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KartDatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "kartdb";

    public static final String CARD_TABLE_NAME = "cards";
    public static final String CARD_ID = "_cardid";
    public static final String CARD_LAST_FOUR_DIGITS = "lastfour";
    public static final String CARD_HOLDER_NAME = "cardholdername";

    public static final String INSTALLMENT_TABLE_NAME = "installments";
    public static final String INSTALLMENT_ID = "_insid";
    public static final String INSTALLMENT_CARD = "inscard";
    public static final String INSTALLMENT_AMOUNT = "insamount";
    public static final String INSTALLMENT_DATE = "insdate";

    public KartDatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s(" +
                "%s INTEGER AUTO INCREMENT PRIMARY KEY, " +
                "%s INTEGER, " +
                "%s TEXT)",
                CARD_TABLE_NAME,
                CARD_ID,
                CARD_LAST_FOUR_DIGITS,
                CARD_HOLDER_NAME));

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s(" +
                "%s INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                "%s INTEGER, " +
                "FOREIGN KEY(%s) REFERENCES %s(%s), " +
                "%s REAL, " +
                "%s INTEGER)",
                INSTALLMENT_TABLE_NAME,
                INSTALLMENT_ID,
                INSTALLMENT_CARD,
                INSTALLMENT_CARD,
                CARD_TABLE_NAME,
                CARD_ID,
                INSTALLMENT_AMOUNT,
                INSTALLMENT_DATE));
    }

    // TODO: 22/02/2017 implement insert delete and modify functions

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
