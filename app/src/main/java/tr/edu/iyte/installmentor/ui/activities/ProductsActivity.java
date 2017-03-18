package tr.edu.iyte.installmentor.ui.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Date;
import java.util.List;
import java.util.Random;

import tr.edu.iyte.installmentor.R;
import tr.edu.iyte.installmentor.database.CardDatabaseHelper;
import tr.edu.iyte.installmentor.database.entities.Product;
import tr.edu.iyte.installmentor.ui.adapters.ProductRecyclerAdapter;

public class ProductsActivity extends AppCompatActivity {

    private CardDatabaseHelper db;
    private FloatingActionButton fab;
    private View loading;
    private RecyclerView productRecyclerView;
    private LinearLayoutManager layoutManager;
    private ProductRecyclerAdapter productRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        loading = findViewById(R.id.loading);
        productRecyclerView = (RecyclerView) findViewById(R.id.card_recycler);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product(1, new Random(System.currentTimeMillis()).nextFloat(), "", new Date());
                if(db.addProduct(product))
                    productRecyclerAdapter.addProduct(product);
            }
        });

        db = new CardDatabaseHelper(this, new CardDatabaseHelper.DatabaseOpenedListener() {
            @Override
            public void onOpen(boolean success) {
                if(success) {
                    List<Product> products = db.getAllProducts();
                    layoutManager = new LinearLayoutManager(ProductsActivity.this);
                    productRecyclerView.setLayoutManager(layoutManager);
                    productRecyclerAdapter = new ProductRecyclerAdapter(products);
                    productRecyclerView.setLayoutManager(layoutManager);
                    productRecyclerView.setAdapter(productRecyclerAdapter);

                    loading.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                } // TODO: 16/03/2017 implement else
            }
        });
    }
}
