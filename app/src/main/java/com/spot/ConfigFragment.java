package com.spot;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spot.adapters.CreditCardAdapter;
import com.spot.custom.AlertMessage;
import com.spot.custom.CreditCardDialog;
import com.spot.models.CreditCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends Fragment {

    private View mView;

    private Button mCreateCard;
    private DatabaseReference mDatabase;
    private List<CreditCard> creditCards;
    private RecyclerView mCreditCardList;
    private RecyclerView.LayoutManager mLayoutManager;

    public ConfigFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_config, container, false);


        mDatabase = FirebaseDatabase.getInstance().getReference("cards/user-cards/" + SSOSessionActivity.userLogged.getUid() );

        mCreateCard = mView.findViewById(R.id.agregarTarjeta);
        mCreateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreditCardDialog dialog = new CreditCardDialog(getContext());
                dialog.show();
            }
        });

        creditCards = new ArrayList<>();

        mCreditCardList = mView.findViewById(R.id.payment_methods);

        mCreditCardList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mCreditCardList.setLayoutManager(mLayoutManager);


        final CreditCardAdapter adapter = new CreditCardAdapter(creditCards);

        mCreditCardList.setAdapter(adapter);


        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                CreditCard newCard = dataSnapshot.getValue(CreditCard.class);

                adapter.addCard(newCard);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return mView;
    }

}
