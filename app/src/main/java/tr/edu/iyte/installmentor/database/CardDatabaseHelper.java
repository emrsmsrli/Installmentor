package tr.edu.iyte.installmentor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tr.edu.iyte.installmentor.database.entities.Card;
import tr.edu.iyte.installmentor.database.entities.Installment;
import tr.edu.iyte.installmentor.database.entities.Product;
import tr.edu.iyte.installmentor.util.DateFormatBuilder;

public class CardDatabaseHelper extends SQLiteOpenHelper {
    public interface DatabaseOpenedListener {
        void onOpen(boolean success);
    }

    private static final String TAG = CardDatabaseHelper.class.getSimpleName();

    private enum Mode {
        CREATE,
        READ,
        UPDATE,
        DELETE
    }

    private static final String DB_NAME = "carddb";
    private static final String ID = "_id";

    private static final String CARD_TABLE_NAME = "cards";
    private static final String CARD_NUMBER = "card_number";
    private static final String CARD_BANK_NAME = "card_bankname";
    private static final String CARD_HOLDER_NAME = "card_holdername";

    private static final String PRODUCT_TABLE_NAME = "products";
    private static final String PRODUCT_CARD_ID = "pro_card";
    private static final String PRODUCT_DESCRIPTION = "pro_des";
    private static final String PRODUCT_BUY_DATE = "pro_buydate";
    private static final String PRODUCT_TOTAL_AMOUNT = "pro_totalamount";

    private static final String INSTALLMENT_TABLE_NAME = "installments";
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
                CARD_BANK_NAME,     //TEXT
                CARD_NUMBER));

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY, " +
                        "%s INTEGER, " +
                        "%s REAL, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE);",
                PRODUCT_TABLE_NAME,
                ID,                     //INTEGER AUTOINCREMENT PRIMARY KEY
                PRODUCT_CARD_ID,        //INTEGER, FOREIGN KEY, to cards
                PRODUCT_TOTAL_AMOUNT,   //REAL
                PRODUCT_DESCRIPTION,    //TEXT
                PRODUCT_BUY_DATE,       //TEXT
                PRODUCT_CARD_ID,
                CARD_TABLE_NAME,
                ID));

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "%s REAL, " +
                        "%s TEXT, " +
                        "FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE, " +
                        "FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE);",
                INSTALLMENT_TABLE_NAME,
                ID,                     //INTEGER AUTOINCREMENT PRIMARY KEY,
                INSTALLMENT_PRODUCT_ID, //INTEGER, FOREIGN KEY, to products
                INSTALLMENT_CARD_ID,    //INTEGER, FOREIGN KEY, to cards
                INSTALLMENT_AMOUNT,     //REAL
                INSTALLMENT_DATE,       //TEXT
                INSTALLMENT_PRODUCT_ID,
                PRODUCT_TABLE_NAME,
                ID,
                INSTALLMENT_CARD_ID,
                CARD_TABLE_NAME,
                ID));
    }

    public boolean addCard(Card card) {
        long id = db.insert(CARD_TABLE_NAME, null, initCard(card));
        log(id, Card.class, Mode.CREATE);
        return id >= 0;
    }

    public boolean addProduct(Product product) {
        long id = db.insert(PRODUCT_TABLE_NAME, null, initProduct(product));
        log(id, Product.class, Mode.CREATE);
        return id >= 0;
    }

    public boolean addInstallment(Installment installment) {
        long id = db.insert(INSTALLMENT_TABLE_NAME, null, initInstallment(installment));
        log(id, Installment.class, Mode.CREATE);
        return id >= 0;
    }

    public boolean deleteCard(Card card) {
        String cardId = Long.toString(card.getId());
        int d = db.delete(CARD_TABLE_NAME, "? = ?", new String[]{ID, cardId});
        log(d, Card.class, Mode.DELETE);
        return d > 0;
    }

    public boolean deleteProduct(Product product) {
        String productId = Long.toString(product.getId());
        int d = db.delete(PRODUCT_TABLE_NAME, "? = ?", new String[]{ID, productId});
        log(d, Product.class, Mode.DELETE);
        return d > 0;
    }

    public boolean deleteInstallment(Installment installment) {
        String installmentId = Long.toString(installment.getId());
        int d = db.delete(INSTALLMENT_TABLE_NAME, "? = ?", new String[]{ID, installmentId});
        log(d, Installment.class, Mode.DELETE);
        return d > 0;
    }

    public boolean editCard(Card card) {
        String cardId = Long.toString(card.getId());
        int u = db.update(CARD_TABLE_NAME, initCard(card), "? = ?", new String[]{ID, cardId});
        log(u, Card.class, Mode.UPDATE);
        return u > 0;
    }

    public boolean editProduct(Product product) {
        String productId = Long.toString(product.getId());
        int u = db.update(PRODUCT_TABLE_NAME, initProduct(product), "? = ?", new String[]{ID, productId});
        log(u, Product.class, Mode.UPDATE);
        return u > 0;
    }

    public boolean editInstallment(Installment installment) {
        String installmentId = Long.toString(installment.getId());
        int u = db.update(INSTALLMENT_TABLE_NAME, initInstallment(installment), "? = ?", new String[]{ID, installmentId});
        log(u, Installment.class, Mode.UPDATE);
        return u > 0;
    }

    public List<Card> getAllCards() {
        ArrayList<Card> cards;
        try(Cursor c = db.query(CARD_TABLE_NAME, null, null, null, null, null, null)) {
            cards = new ArrayList<>();
            while(c.moveToNext()) {
                cards.add(new Card(c.getLong(0), c.getString(1), c.getString(2), c.getString(3)));
            }
        }
        log(cards.size(), Card.class, Mode.READ);
        return cards;
    }

    public List<Product> getAllProducts() {
        ArrayList<Product> products;
        try(Cursor productC = db.query(PRODUCT_TABLE_NAME, null, null, null, null, null, null)) {
            products = new ArrayList<>();
            while(productC.moveToNext()) {
                Date d = DateFormatBuilder.parse(productC.getString(4));
                Product p = new Product(productC.getLong(0), productC.getLong(1), productC.getLong(2), productC.getString(3), d);
                p.setRemainingAmount(getSumOfInstallmentAmounts(p.getId()));
                products.add(p);
            }
        }
        log(products.size(), Product.class, Mode.READ);
        return products;
    }

    public List<Product> getProductsOf(Card card) {
        ArrayList<Product> products;
        try(Cursor productC = db.query(PRODUCT_TABLE_NAME, null, "? = ?", new String[]{PRODUCT_CARD_ID, String.valueOf(card.getId())}, null, null, null)) {
            products = new ArrayList<>();
            while(productC.moveToNext()) {
                Date d = DateFormatBuilder.parse(productC.getString(4));
                Product p = new Product(productC.getLong(0), productC.getLong(1), productC.getLong(2), productC.getString(3), d);
                p.setRemainingAmount(getSumOfInstallmentAmounts(p.getId()));
                products.add(p);
            }
        }
        log(products.size(), Product.class, Mode.READ);
        return products;
    }

    public List<Installment> getAllInstallments() {
        ArrayList<Installment> installments;
        try(Cursor c = db.query(INSTALLMENT_TABLE_NAME, null, null, null, null, null, null)) {
            installments = new ArrayList<>();
            while(c.moveToNext()) {
                Date d = DateFormatBuilder.parse(c.getString(4));
                installments.add(new Installment(c.getLong(0), c.getLong(1), c.getLong(2), c.getFloat(3), d));
            }
        }
        log(installments.size(), Installment.class, Mode.READ);
        return installments;
    }

    public List<Installment> getAllInstallmentsOf(Card card) {
        ArrayList<Installment> installments;
        try(Cursor c = db.query(INSTALLMENT_TABLE_NAME, null, "? = ?", new String[]{INSTALLMENT_CARD_ID, String.valueOf(card.getId())}, null, null, null)) {
            installments = new ArrayList<>();
            while(c.moveToNext()) {
                Date d = DateFormatBuilder.parse(c.getString(4));
                installments.add(new Installment(c.getLong(0), c.getLong(1), c.getLong(2), c.getFloat(3), d));
            }
        }
        log(installments.size(), Installment.class, Mode.READ);
        return installments;
    }

    public List<Installment> getAllInstallmentsOf(Product product) {
        ArrayList<Installment> installments;
        try(Cursor c = db.query(INSTALLMENT_TABLE_NAME, null, "? = ?", new String[]{INSTALLMENT_PRODUCT_ID, String.valueOf(product.getId())}, null, null, null)) {
            installments = new ArrayList<>();
            while(c.moveToNext()) {
                Date d = DateFormatBuilder.parse(c.getString(4));
                installments.add(new Installment(c.getLong(0), c.getLong(1), c.getLong(2), c.getFloat(3), d));
            }
        }
        log(installments.size(), Installment.class, Mode.READ);
        return installments;
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
        cv.put(PRODUCT_TOTAL_AMOUNT, product.getTotalAmount());
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

    private void log(long i, Class<?> c, Mode mode) {
        String cn = c.getSimpleName();
        switch(mode) {
            case CREATE:
                if(i < 0)
                    Log.e(TAG, "add" + cn + ": Error creating " + cn);
                else
                    Log.i(TAG, "add" + cn + ": new " + cn + " created, id: " + i);
                break;
            case READ:
                Log.i(TAG, "get" + cn + ": Read count: " + i);
                break;
            case UPDATE:
                if(i <= 0)
                    Log.e(TAG, "edit" + cn + ": Couldn't edit " + cn);
                else
                    Log.i(TAG, "edit" + cn + ": " + cn + " edited");
                break;
            case DELETE:
                if(i <= 0)
                    Log.e(TAG, "delete" + cn + ": Couldn't delete " + cn);
                else
                    Log.i(TAG, "delete" + cn + ": " + cn + " deleted");
                break;
            default:
                break;
        }
    }

    private float getSumOfInstallmentAmounts(long pId) {
        float r = 0;
        try(Cursor installmentC = db.rawQuery(
                "SELECT SUM(" + INSTALLMENT_AMOUNT + ") " +
                        "FROM " + INSTALLMENT_TABLE_NAME +
                        " WHERE " + INSTALLMENT_PRODUCT_ID +" = " + pId + ";", null)) {
            if(installmentC.moveToNext())
                r = installmentC.getFloat(0);
        }
        return r;
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
                    Log.e(TAG, "openDatabase: Couldn't open database");
                else
                    db = sqLiteDatabase;

                listener.onOpen(db != null);
            }
        }.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(TAG, "onUpgrade: DATABASE UPGRADE, ALL DATA WILL BE DESTROYED");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS *");
        onCreate(sqLiteDatabase);
    }
}
