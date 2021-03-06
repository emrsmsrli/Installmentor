package tr.edu.iyte.installmentor.ui.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tr.edu.iyte.installmentor.R;
import tr.edu.iyte.installmentor.database.entities.Card;
import tr.edu.iyte.installmentor.ui.activities.ProductsActivity;

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder> {

    private List<Card> cards;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView cardNumberTextView;
        private TextView holderNameTextView;
        private TextView bankNameTextView;
        private ImageView cardLogo;

        private ViewHolder(CardView cardView) {
            super(cardView);
            cardNumberTextView = (TextView) cardView.findViewById(R.id.card_number);
            holderNameTextView = (TextView) cardView.findViewById(R.id.holder_name);
            bankNameTextView = (TextView) cardView.findViewById(R.id.bank_name);
            cardLogo = (ImageView) cardView.findViewById(R.id.card_logo);
        }
    }

    public CardRecyclerAdapter(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.cardNumberTextView.setText(card.getNumber());
        holder.holderNameTextView.setText(card.getHolderName());
        holder.bankNameTextView.setText(card.getBankName());

        switch(card.getType()) {
            case MASTER_CARD:
                holder.cardLogo.setImageResource(R.mipmap.mastercardlogo);
                break;
            case MAESTRO:
                holder.cardLogo.setImageResource(R.mipmap.maestrologo);
                break;
            case VISA:
                holder.cardLogo.setImageResource(R.mipmap.visalogo);
                break;
            case DISCOVER:
                holder.cardLogo.setImageResource(R.mipmap.discoverlogo);
                break;
            case AMERICAN_EXPRESS:
                holder.cardLogo.setImageResource(R.mipmap.amexlogo);
                break;
            default:
                break;
        }

        final RecyclerView.ViewHolder h = holder;
        final View holderView = holder.itemView;
        holderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holderView.getContext(), ProductsActivity.class);
                i.putExtra("cardid", h.getAdapterPosition());
                holderView.getContext().startActivity(i);
            }
        });
    }
}
