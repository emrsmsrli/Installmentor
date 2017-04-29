package tr.edu.iyte.installmentor.ui.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import tr.edu.iyte.installmentor.R;
import tr.edu.iyte.installmentor.database.CardDatabaseHelper;
import tr.edu.iyte.installmentor.database.entities.Product;
import tr.edu.iyte.installmentor.util.DateFormatBuilder;

public class ProductEditActivity extends AppCompatActivity {

    private View activityLayout;
    private EditText descriptionText;
    private EditText totalAmountText;
    private DatePicker datePicker;

    private CardDatabaseHelper db;
    private int cardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        if((cardId = getIntent().getIntExtra("cardid", -1)) == -1)
            finish();
        cardId++;

        activityLayout = findViewById(R.id.product_edit_layout);
        descriptionText = (EditText) findViewById(R.id.product_desc);
        totalAmountText = (EditText) findViewById(R.id.total_amount);
        datePicker = (DatePicker) findViewById(R.id.date);

        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new CardDatabaseHelper(ProductEditActivity.this, new CardDatabaseHelper.DatabaseOpenedListener() {
                    @Override
                    public void onOpen(boolean success) {
                        if(success) {
                            addProduct();
                        } else {
                            Snackbar.make(activityLayout, "Error adding product", Snackbar.LENGTH_LONG)
                                    .setAction("Try again", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            addProduct();
                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(R.color.colorPrimaryDark, getTheme()))
                                    .show();
                        }
                    }
                });
            }
        });
    }

    private boolean checkErrors() {
        if(descriptionText.length() < 3) {
            descriptionText.setError("Wrong length");
            descriptionText.requestFocus();
        } else if(totalAmountText.length() == 0) {
            totalAmountText.setError("Wrong amount");
            totalAmountText.requestFocus();
        } else return false;

        return true;
    }

    private void addProduct() {
        if(checkErrors())
            return;

        Product product = new Product(
                cardId,
                Float.parseFloat(totalAmountText.getText().toString()),
                descriptionText.getText().toString(),
                new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()).getTime()
        );

        db.addProduct(product);
        db.close();
        finish();
    }
}
