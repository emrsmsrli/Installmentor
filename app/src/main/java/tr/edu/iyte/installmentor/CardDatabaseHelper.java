package tr.edu.iyte.installmentor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tr.edu.iyte.installmentor.Entities.Card;
import tr.edu.iyte.installmentor.Entities.Installment;
import tr.edu.iyte.installmentor.Entities.Product;

public class CardDatabaseHelper extends SQLiteOpenHelper {
    public interface DatabaseOpenedListener {
        void onOpen(boolean success);
    }

    private static final String TAG = CardDatabaseHelper.class.getSimpleName();

    private static final int MODE_CREATE = 0;
    private static final int MODE_READ = 1;
    private static final int MODE_UPDATE = 2;
    private static final int MODE_DELETE = 3;

    private static final String DB_NAME = "carddb";
    private static final String ID = "_id";

    private static final String CARD_TABLE_NAME = "CardTable";
    private static final String CARD_NUMBER = "card_number";
    private static final String CARD_BANK_NAME = "card_bankname";
    private static final String CARD_HOLDER_NAME = "card_holdername";

    private static final String PRODUCT_TABLE_NAME = "ProductTable";
    private static final String PRODUCT_CARD_ID = "pro_card";
    private static final String PRODUCT_DESCRIPTION = "pro_des";
    private static final String PRODUCT_BUY_DATE = "pro_buydate";

    private static final String INSTALLMENT_TABLE_NAME = "InstallmentTable";
    private static final String INSTALLMENT_PRODUCT_ID = "ins_pro";
    private static final String INSTALLMENT_CARD_ID = "ins_card";
    private static final String INSTALLMENT_AMOUNT = "ins_amount";
    private static final String INSTALLMENT_DATE = "ins_date";

    private SQLiteDatabase db = null;

    public CardDatabaseHelper(Context context, DatabaseOpenedListener listener) {
        super(context, DB_NAME, null, 1);
        openDatabase(listener);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s TEXT," +
                        "UNIQUE(%s));",
                CARD_TABLE_NAME,
                ID,                 //INTEGER AUTOINCREMENT PRIMARY KEY
                CARD_NUMBER,        //TEXT
                CARD_HOLDER_NAME,   //TEXT
                CARD_BANK_NAME,
                CARD_NUMBER));   //TEXT

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s INTEGER, " +
                        "FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE);",
                PRODUCT_TABLE_NAME,
                ID,                     //INTEGER AUTOINCREMENT PRIMARY KEY
                PRODUCT_DESCRIPTION,    //TEXT
                PRODUCT_BUY_DATE,       //TEXT
                PRODUCT_CARD_ID,        //INTEGER, FOREIGN KEY, to cards
                PRODUCT_CARD_ID,
                CARD_TABLE_NAME,
                ID));

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY, " +
                        "%s REAL, " +
                        "%s TEXT, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE, " +
                        "FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE);",
                INSTALLMENT_TABLE_NAME,
                ID,                     //INTEGER AUTOINCREMENT PRIMARY KEY,
                INSTALLMENT_AMOUNT,     //REAL
                INSTALLMENT_DATE,       //TEXT
                INSTALLMENT_PRODUCT_ID, //INTEGER, FOREIGN KEY, to products
                INSTALLMENT_CARD_ID,    //INTEGER, FOREIGN KEY, to cards
                INSTALLMENT_PRODUCT_ID,
                PRODUCT_TABLE_NAME,
                ID,
                INSTALLMENT_CARD_ID,
                CARD_TABLE_NAME,
                ID));
    }

    // TODO: 22/02/2017 implement get functions

    public boolean addCard(Card card) {
        long id = db.insert(CARD_TABLE_NAME, null, initCard(card));
        log(id, Card.class, MODE_CREATE);
        return id >= 0;
    }

    public boolean addProduct(Product product) {
        long id = db.insert(PRODUCT_TABLE_NAME, null, initProduct(product));
        log(id, Product.class, MODE_CREATE);
        return id >= 0;
    }

    public boolean addInstallment(Installment installment) {
        long id = db.insert(INSTALLMENT_TABLE_NAME, null, initInstallment(installment));
        log(id, Installment.class, MODE_CREATE);
        return id >= 0;
    }

    public boolean deleteCard(Card card) {
        String cardId = Long.toString(card.getId());
        int d = db.delete(CARD_TABLE_NAME, "? = ?", new String[]{ID, cardId});
        log(d, Card.class, MODE_DELETE);
        return d > 0;
    }

    public boolean deleteProduct(Product product) {
        String productId = Long.toString(product.getId());
        int d = db.delete(PRODUCT_TABLE_NAME, "? = ?", new String[]{ID, productId});
        log(d, Product.class, MODE_DELETE);
        return d > 0;
    }

    public boolean deleteInstallment(Installment installment) {
        String installmentId = Long.toString(installment.getId());
        int d = db.delete(INSTALLMENT_TABLE_NAME, "? = ?", new String[]{ID, installmentId});
        log(d, Installment.class, MODE_DELETE);
        return d > 0;
    }

    public boolean editCard(Card card) {
        String cardId = Long.toString(card.getId());
        int u = db.update(CARD_TABLE_NAME, initCard(card), "? = ?", new String[]{ID, cardId});
        log(u, Card.class, MODE_UPDATE);
        return u > 0;
    }

    public boolean editProduct(Product product) {
        String productId = Long.toString(product.getId());
        int u = db.update(PRODUCT_TABLE_NAME, initProduct(product), "? = ?", new String[]{ID, productId});
        log(u, Product.class, MODE_UPDATE);
        return u > 0;
    }

    public boolean editInstallment(Installment installment) {
        String installmentId = Long.toString(installment.getId());
        int u = db.update(INSTALLMENT_TABLE_NAME, initInstallment(installment), "? = ?", new String[]{ID, installmentId});
        log(u, Installment.class, MODE_UPDATE);
        return u > 0;
    }

    public List<Card> getAllCards() {
        ArrayList<Card> cards;
        try(Cursor c = db.query(CARD_TABLE_NAME, null, null, null, null, null, null)) {
            cards = new ArrayList<>();
            while(c.moveToNext()) {
                cards.add(new Card(c.getLong(0), c.getString(1), c.getString(2), c.getString(3)));
                Log.i(TAG, "getCards: " + c.getLong(0));
            }
        }
        return cards;
    }

    private ContentValues initCard(Card card) {
        ContentValues cv = new ContentValues();
        cv.put(CARD_NUMBER, card.getNumber());
        cv.put(CARD_HOLDER_NAME, card.getHolderName());
        cv.put(CARD_BANK_NAME, card.getBankName());
        return cv;
    }

    private ContentValues initProduct(Product product) {
        String dateString = DateFormatBuilder
                .getBuilder()
                .includeDate(false)
                .buildFormat(product.getBuyDate());
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT_CARD_ID, product.getCardId());
        cv.put(PRODUCT_DESCRIPTION, product.getDescription());
        cv.put(PRODUCT_BUY_DATE, dateString);
        return cv;
    }

    private ContentValues initInstallment(Installment installment) {
        String dateString = DateFormatBuilder
                .getBuilder()
                .includeDate(false)
                .buildFormat(installment.getDate());

        ContentValues cv = new ContentValues();
        cv.put(INSTALLMENT_PRODUCT_ID, installment.getProductId());
        cv.put(INSTALLMENT_CARD_ID, installment.getCardId());
        cv.put(INSTALLMENT_AMOUNT, installment.getAmount());
        cv.put(INSTALLMENT_DATE, dateString);
        return cv;
    }

    private void openDatabase(final DatabaseOpenedListener listener) {
        new AsyncTask<Void, Void, SQLiteDatabase>() {
            @Override
            protected SQLiteDatabase doInBackground(Void... params) {
                return getWritableDatabase();
            }

            @Override
            protected void onPostExecute(SQLiteDatabase sqLiteDatabase) {
                if(sqLiteDatabase == null || !sqLiteDatabase.isOpen())
                    Log.w(TAG, "openDatabase: Couldn't open database");
                else
                    db = sqLiteDatabase;

                listener.onOpen(db != null);
            }
        }.execute();
    }

    private void log(long i, Class<?> c, int mode) {
        String cn = c.getSimpleName();
        switch(mode) {
            case MODE_CREATE:
                if(i < 0)
                    Log.w(TAG, "add" + cn + ": Error creating " + cn);
                else
                    Log.i(TAG, "add" + cn + ": new " + cn + " created, id: " + i);
                break;
            case MODE_READ:
                break;
            case MODE_UPDATE:
                if(i <= 0)
                    Log.i(TAG, "edit" + cn + ": Couldn't edit " + cn);
                else
                    Log.i(TAG, "edit" + cn + ": " + cn + " edited");
                break;
            case MODE_DELETE:
                if(i <= 0)
                    Log.i(TAG, "delete" + cn + ": Couldn't delete " + cn);
                else
                    Log.i(TAG, "delete" + cn + ": " + cn + " deleted");
                break;
            default:
                break;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
