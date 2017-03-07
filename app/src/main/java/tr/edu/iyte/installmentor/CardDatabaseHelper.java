package tr.edu.iyte.installmentor;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import tr.edu.iyte.installmentor.Entities.Card;
import tr.edu.iyte.installmentor.Entities.Installment;

public class CardDatabaseHelper extends SQLiteOpenHelper {
    interface DatabaseOpenedListener {
        void onDatabaseOpened(SQLiteDatabase db);
    }

    private static final String DB_NAME = "carddb";

    private static final String ID = "id";
    private static final String CARD_TABLE_NAME = "cards";
    private static final String CARD_NUMBER = "cardnumber";
    private static final String CARD_TYPE = "cardtype";
    private static final String CARD_HOLDER_NAME = "cardholdername";
    private static final String INSTALLMENT_TABLE_NAME = "installments";
    private static final String INSTALLMENT_CARD_ID = "inscard";
    private static final String INSTALLMENT_AMOUNT = "insamount";
    private static final String INSTALLMENT_DATE = "insdate";

    public CardDatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s(" +
                "%s INTEGER AUTOINCREMENT PRIMARY KEY, " +
                "%s INTEGER, " +
                "%s TEXT UNIQUE, " +
                "%s TEXT);",
                CARD_TABLE_NAME,
                ID,                 //INTEGER AUTOINCREMENT PRIMARY KEY
                CARD_TYPE,          //INTEGER
                CARD_NUMBER,        //TEXT UNIQUE
                CARD_HOLDER_NAME)); //TEXT

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s(" +
                "%s INTEGER AUTOINCREMENT PRIMARY KEY, " +
                "%s INTEGER, " +
                "FOREIGN KEY(%s) REFERENCES %s(%s), " +
                "%s REAL, " +
                "%s INTEGER);",
                INSTALLMENT_TABLE_NAME,
                ID,                     //INTEGER AUTOINCREMENT PRIMARY KEY
                INSTALLMENT_CARD_ID,    //INTEGER, FOREIGN KEY
                INSTALLMENT_CARD_ID,
                CARD_TABLE_NAME,
                ID,
                INSTALLMENT_AMOUNT,     //REAL
                INSTALLMENT_DATE));     //INTEGER
    }

    // TODO: 22/02/2017 implement get functions

    public void addCard(Card card) {
        final ContentValues cardV = initCard(card);

        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                db.insert(CARD_TABLE_NAME, null, cardV);
                db.close();
            }
        });
    }

    public void addInstallment(Installment installment) {
        final ContentValues installmentV = initInstallment(installment);

        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                db.insert(INSTALLMENT_TABLE_NAME, null, installmentV);
                db.close();
            }
        });
    }

    public void deleteCard(Card card) {
        final String cardId = Long.toString(card.getId());
        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                db.delete(INSTALLMENT_TABLE_NAME, "WHERE ?=?", new String[]{INSTALLMENT_CARD_ID, cardId});
                db.delete(CARD_TABLE_NAME, "WHERE ? = ?", new String[]{ID, cardId});
                db.close();
            }
        });
    }

    public void deleteInstallment(Installment installment) {
        final String installmentId = Long.toString(installment.getId());
        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                db.delete(INSTALLMENT_TABLE_NAME, "WHERE ? = ?", new String[]{ID, installmentId});
                db.close();
            }
        });
    }

    public void editCard(Card card) {
        final String cardId = Long.toString(card.getId());
        final ContentValues cardV = initCard(card);

        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                db.update(CARD_TABLE_NAME, cardV, "WHERE ? = ?", new String[]{ID, cardId});
                db.close();
            }
        });
    }

    public void editInstallment(Installment installment) {
        final String installmentId = Long.toString(installment.getId());
        final ContentValues installmentV = initInstallment(installment);

        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                db.update(INSTALLMENT_TABLE_NAME, installmentV, "WHERE ? = ?", new String[]{ID, installmentId});
                db.close();
            }
        });
    }

    private ContentValues initCard(Card card) {
        ContentValues cv = new ContentValues();
        cv.put(CARD_TYPE, card.getType());
        cv.put(CARD_NUMBER, card.getNumber());
        cv.put(CARD_HOLDER_NAME, card.getHolderName());
        return cv;
    }

    private ContentValues initInstallment(Installment installment) {
        ContentValues cv = new ContentValues();
        cv.put(INSTALLMENT_CARD_ID, installment.getCardId());
        cv.put(INSTALLMENT_AMOUNT, installment.getAmount());
        cv.put(INSTALLMENT_DATE, installment.getDate().toString()/*TODO format this date*/);
        return cv;
    }

    private void openWritableDatabaseAsync(final DatabaseOpenedListener listener) {
        new AsyncTask<Void, Void, SQLiteDatabase>() {
            @Override
            protected void onPostExecute(SQLiteDatabase sqLiteDatabase) {
                listener.onDatabaseOpened(sqLiteDatabase);
            }

            @Override
            protected SQLiteDatabase doInBackground(Void... params) {
                return getWritableDatabase();
            }
        }.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
