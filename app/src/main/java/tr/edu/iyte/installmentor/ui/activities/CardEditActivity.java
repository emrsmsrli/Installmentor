package tr.edu.iyte.installmentor.ui.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tr.edu.iyte.installmentor.R;
import tr.edu.iyte.installmentor.database.CardDatabaseHelper;
import tr.edu.iyte.installmentor.database.entities.Card;

public class CardEditActivity extends AppCompatActivity {

    private View activityLayout;
    private EditText cardNumberText;
    private EditText cardHolderText;
    private EditText cardBankText;

    private CardDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);

        activityLayout = findViewById(R.id.card_edit_layout);
        cardNumberText = (EditText) findViewById(R.id.card_number);
        cardHolderText = (EditText) findViewById(R.id.card_holder);
        cardBankText = (EditText) findViewById(R.id.bank_name);
        Button okButton = (Button) findViewById(R.id.ok);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkErrors())
                    return;

                db = new CardDatabaseHelper(CardEditActivity.this, new CardDatabaseHelper.DatabaseOpenedListener() {
                    @Override
                    public void onOpen(boolean success) {
                        if(success) {
                            addCard();
                        } else {
                            Snackbar.make(activityLayout, "Error adding card", Snackbar.LENGTH_LONG)
                                    .setAction("Try again", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            addCard();
                                        }
                                    }).setActionTextColor(getResources().getColor(R.color.colorPrimaryDark, getTheme())).show();
                        }
                    }
                });
            }
        });
    }

    private boolean checkErrors() {
        // TODO: 29/04/2017 check more errors
        if(cardNumberText.length() < 6) {
            cardNumberText.setError("Wrong card number");
            cardNumberText.requestFocus();
        } else if(cardHolderText.length() == 0) {
            cardHolderText.setError("Wrong holder name length");
            cardHolderText.requestFocus();
        } else if(cardBankText.length() == 0) {
            cardBankText.setError("Wrong bank name length");
            cardBankText.requestFocus();
        } else return false;

        return true;
    }

    private void addCard() {
        if(checkErrors())
            return;

        Card card = new Card(
                cardNumberText.getText().toString(),
                cardHolderText.getText().toString(),
                cardBankText.getText().toString()
        );

        db.addCard(card);
        db.close();
        finish();
    }
}
