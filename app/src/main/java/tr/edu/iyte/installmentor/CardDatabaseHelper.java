package tr.edu.iyte.installmentor;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import tr.edu.iyte.installmentor.Entities.Card;
import tr.edu.iyte.installmentor.Entities.Installment;
import tr.edu.iyte.installmentor.Entities.Product;

public class CardDatabaseHelper extends SQLiteOpenHelper {
    interface DatabaseOpenedListener {
        void onDatabaseOpened(SQLiteDatabase db);
    }

    private static final String TAG = CardDatabaseHelper.class.getSimpleName();
    private static final String DB_NAME = "carddb";

    private static final String ID = "id";

    private static final String CARD_TABLE_NAME = "cards";
    private static final String CARD_NUMBER = "cardnumber";
    private static final String CARD_TYPE = "cardtype";
    private static final String CARD_HOLDER_NAME = "cardholdername";

    private static final String PRODUCT_TABLE_NAME = "products";
    private static final String PRODUCT_CARD_ID = "procard";
    private static final String PRODUCT_DESCRIPTION = "prodesc";
    private static final String PRODUCT_BUY_DATE = "probuydate";

    private static final String INSTALLMENT_TABLE_NAME = "installments";
    private static final String INSTALLMENT_PRODUCT_ID = "inspro";
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
                        "%s TEXT, " +
                        "%s TEXT);",
                PRODUCT_TABLE_NAME,
                ID,                     //INTEGER AUTOINCREMENT PRIMARY KEY
                PRODUCT_CARD_ID,        //INTEGER, FOREIGN KEY, to cards
                PRODUCT_CARD_ID,
                CARD_TABLE_NAME,
                ID,
                PRODUCT_DESCRIPTION,    //TEXT
                PRODUCT_BUY_DATE));     //TEXT

        sqLiteDatabase.execSQL(String.format("CREATE TABLE %s(" +
                        "%s INTEGER AUTOINCREMENT PRIMARY KEY, " +
                        "%s INTEGER, " +
                        "FOREIGN KEY(%s) REFERENCES %s(%s), " +
                        "%s INTEGER, " +
                        "FOREIGN KEY(%s) REFERENCES %s(%s), " +
                        "%s REAL, " +
                        "%s TEXT);",
                INSTALLMENT_TABLE_NAME,
                ID,                     //INTEGER AUTOINCREMENT PRIMARY KEY
                INSTALLMENT_PRODUCT_ID, //INTEGER, FOREIGN KEY, to products
                INSTALLMENT_PRODUCT_ID,
                PRODUCT_TABLE_NAME,
                ID,
                INSTALLMENT_CARD_ID,    //INTEGER, FOREIGN KEY, to cards
                INSTALLMENT_CARD_ID,
                CARD_TABLE_NAME,
                ID,
                INSTALLMENT_AMOUNT,     //REAL
                INSTALLMENT_DATE));     //TEXT
    }

    // TODO: 22/02/2017 implement get functions

    public void addCard(Card card) {
        final ContentValues cardV = initCard(card);

        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                if(db == null) {
                    Log.w(TAG, "addCard: Database didn't open");
                    return;
                }

                long id = db.insert(CARD_TABLE_NAME, null, cardV);
                db.close();
                Log.i(TAG, "addCard: new Card created, id: " + id);
            }
        });
    }

    public void addProduct(Product product) {
        final ContentValues productV = initProduct(product);

        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                if(db == null) {
                    Log.w(TAG, "addProduct: Database didn't open");
                    return;
                }

                long id = db.insert(PRODUCT_TABLE_NAME, null, productV);
                db.close();
                Log.i(TAG, "addProduct: new Product created, id: " + id);
            }
        });
    }

    public void addInstallment(Installment installment) {
        final ContentValues installmentV = initInstallment(installment);

        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                if(db == null || !db.isOpen()) {
                    Log.w(TAG, "addInstallment: Database didn't open");
                    return;
                }

                long id = db.insert(INSTALLMENT_TABLE_NAME, null, installmentV);
                db.close();
                Log.i(TAG, "addInstallment: new Installment created, id: " + id);
            }
        });
    }

    public void deleteCard(final Card card) {
        final String cardId = Long.toString(card.getId());
        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                if(db == null) {
                    Log.w(TAG, "deleteCard: Database didn't open");
                    return;
                }

                int ins = db.delete(INSTALLMENT_TABLE_NAME, "WHERE ? = ?", new String[]{INSTALLMENT_CARD_ID, cardId});
                int pro = db.delete(PRODUCT_TABLE_NAME, "WHERE ? = ?", new String[]{PRODUCT_CARD_ID, cardId});
                db.delete(CARD_TABLE_NAME, "WHERE ? = ?", new String[]{ID, cardId});
                db.close();
                Log.i(TAG, "deleteCard: Card deleted, deleted Products: " + pro + " deleted Installments: " + ins);
            }
        });
    }

    public void deleteProduct(Product product) {
        final String productId = Long.toString(product.getId());
        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                if(db == null) {
                    Log.w(TAG, "deleteProduct: Database didn't open");
                    return;
                }

                int ins = db.delete(INSTALLMENT_TABLE_NAME, "WHERE ? = ?", new String[]{INSTALLMENT_PRODUCT_ID, productId});
                db.delete(PRODUCT_TABLE_NAME, "WHERE ? = ?", new String[]{ID, productId});
                db.close();
                Log.i(TAG, "deleteProduct: Product deleted, deleted Installments: " + ins);
            }
        });
    }

    public void deleteInstallment(Installment installment) {
        final String installmentId = Long.toString(installment.getId());
        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                if(db == null) {
                    Log.w(TAG, "deleteInstallment: Database didn't open");
                    return;
                }

                db.delete(INSTALLMENT_TABLE_NAME, "WHERE ? = ?", new String[]{ID, installmentId});
                db.close();
                Log.i(TAG, "deleteInstallment: Installment deleted");
            }
        });
    }

    public void editCard(Card card) {
        final String cardId = Long.toString(card.getId());
        final ContentValues cardV = initCard(card);

        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                if(db == null) {
                    Log.w(TAG, "editCard: Database didn't open");
                    return;
                }

                db.update(CARD_TABLE_NAME, cardV, "WHERE ? = ?", new String[]{ID, cardId});
                db.close();
                Log.i(TAG, "editCard: Card edited");
            }
        });
    }

    public void editProduct(Product product) {
        final String productId = Long.toString(product.getId());
        final ContentValues productV = initProduct(product);

        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                if(db == null) {
                    Log.w(TAG, "editProduct: Database didn't open");
                    return;
                }

                db.update(PRODUCT_TABLE_NAME, productV, "WHERE ? = ?", new String[]{ID, productId});
                db.close();
                Log.i(TAG, "editProduct: Product edited");
            }
        });
    }

    public void editInstallment(Installment installment) {
        final String installmentId = Long.toString(installment.getId());
        final ContentValues installmentV = initInstallment(installment);

        openWritableDatabaseAsync(new DatabaseOpenedListener() {
            @Override
            public void onDatabaseOpened(SQLiteDatabase db) {
                if(db == null) {
                    Log.w(TAG, "editInstallment: Database didn't open");
                    return;
                }

                db.update(INSTALLMENT_TABLE_NAME, installmentV, "WHERE ? = ?", new String[]{ID, installmentId});
                db.close();
                Log.i(TAG, "editInstallment: Installment edited");
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

    private void openWritableDatabaseAsync(final DatabaseOpenedListener listener) {
        new AsyncTask<Void, Void, SQLiteDatabase>() {
            @Override
            protected SQLiteDatabase doInBackground(Void... params) {
                return getWritableDatabase();
            }

            @Override
            protected void onPostExecute(SQLiteDatabase sqLiteDatabase) {
                listener.onDatabaseOpened(sqLiteDatabase);
            }
        }.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
