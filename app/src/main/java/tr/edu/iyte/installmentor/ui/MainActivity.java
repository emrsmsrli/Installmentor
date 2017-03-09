package tr.edu.iyte.installmentor.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tr.edu.iyte.installmentor.database.CardDatabaseHelper;
import tr.edu.iyte.installmentor.database.entities.Card;
import tr.edu.iyte.installmentor.R;

public class MainActivity extends AppCompatActivity {
    CardDatabaseHelper db = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deleteDatabase("carddb");
        db = new CardDatabaseHelper(this, new CardDatabaseHelper.DatabaseOpenedListener() {
            @Override
            public void onOpen(boolean success) {
                db.addCard(new Card("5157 5570 2588 2910", "Emre", "Ininal"));
                db.getAllCards();
            }
        });
    }
}
