package tr.edu.iyte.installmentor.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tr.edu.iyte.installmentor.R;
import tr.edu.iyte.installmentor.database.entities.Card;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardEditFragment.OnCardEditListener} interface
 * to handle interaction events.
 * Use the {@link CardEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardEditFragment extends Fragment {
    public interface OnCardEditListener {
        void onEditCard(Card card);
        void onCreateCard(Card card);
    }
    private static final String ARG_CARD_NUMBER = "cardNumber";
    private static final String ARG_HOLDER_NAME = "holderName";
    private static final String ARG_BANK_NAME = "bankName";

    private String cardNumber;
    private String holderName;
    private String bankName;
    private boolean editMode = false;

    private OnCardEditListener listener;

    public CardEditFragment() {}

    public static CardEditFragment newInstance(@Nullable Card card) {
        CardEditFragment fragment = new CardEditFragment();
        if(card != null) {
            Bundle args = new Bundle();
            args.putString(ARG_CARD_NUMBER, card.getNumber());
            args.putString(ARG_HOLDER_NAME, card.getHolderName());
            args.putString(ARG_BANK_NAME, card.getBankName());
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            cardNumber = getArguments().getString(ARG_CARD_NUMBER);
            holderName = getArguments().getString(ARG_HOLDER_NAME);
            bankName = getArguments().getString(ARG_BANK_NAME);
        } else editMode = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_edit, container, false);
    }

    /* TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if(listener != null) {
            listener.onEdit();
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnCardEditListener) {
            listener = (OnCardEditListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCardEditListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
