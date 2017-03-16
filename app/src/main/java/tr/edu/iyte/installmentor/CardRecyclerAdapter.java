package tr.edu.iyte.installmentor;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tr.edu.iyte.installmentor.database.entities.Card;

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
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cview, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void addCard(Card card) {
        cards.add(card);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.cardNumberTextView.setText(card.getNumber());
        holder.holderNameTextView.setText(card.getHolderName());
        holder.bankNameTextView.setText(card.getBankName());

        switch(card.getType()) {
            case MASTER_CARD:
                holder.cardLogo.setImageResource(R.mipmap.mclogo);
                break;
            case MAESTRO:
                holder.cardLogo.setImageResource(R.mipmap.maestrologo);
                break;
            case VISA:
                holder.cardLogo.setImageResource(R.mipmap.visalogo);
                break;
            default:
                break;
        }
    }
}
