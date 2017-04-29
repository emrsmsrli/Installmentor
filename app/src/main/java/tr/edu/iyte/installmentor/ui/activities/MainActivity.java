package tr.edu.iyte.installmentor.ui.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import tr.edu.iyte.installmentor.database.CardDatabaseHelper;
import tr.edu.iyte.installmentor.database.entities.Card;
import tr.edu.iyte.installmentor.R;
import tr.edu.iyte.installmentor.ui.adapters.CardRecyclerAdapter;

public class MainActivity extends AppCompatActivity {
    private CardDatabaseHelper db = null;
    private List<Card> cards;

    private FloatingActionButton fab;
    private View loading;
    private RecyclerView cardRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CardRecyclerAdapter cardRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*---------------------*/
        //deleteDatabase("carddb");
        /*---------------------*/

        fab = (FloatingActionButton) findViewById(R.id.fab);
        loading = findViewById(R.id.loading);
        cardRecyclerView = (RecyclerView) findViewById(R.id.card_recycler);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CardEditActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new CardDatabaseHelper(this, new CardDatabaseHelper.DatabaseOpenedListener() {
            @Override
            public void onOpen(boolean success) {
                if(success) {
                    cards = db.getAllCards();
                    layoutManager = new LinearLayoutManager(MainActivity.this);
                    cardRecyclerAdapter = new CardRecyclerAdapter(cards);
                    cardRecyclerView.setLayoutManager(layoutManager);
                    cardRecyclerView.setAdapter(cardRecyclerAdapter);

                    loading.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                } // TODO: 16/03/2017 implement else
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(db != null)
            db.close();
    }

}
