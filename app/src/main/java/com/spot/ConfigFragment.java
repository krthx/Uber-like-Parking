package com.spot;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spot.R;
import com.spot.custom.CustomDialogClass;
import com.spot.models.CreditCard;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends Fragment {

    private View mView;
    private DatabaseReference mDatabase;

    private Button mCreateCard;

    public ConfigFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_config, container, false);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        //DatabaseReference myRef = mDatabase.getReference("message");

        mCreateCard = mView.findViewById(R.id.agregarTarjeta);
        mCreateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null)
                    ft.remove(prev);


                ft.addToBackStack(null);
                DialogFragment dialogFragment = new CustomDialogClass();

                dialogFragment.show(ft, "dialog");
            }
        });

        return mView;
    }

    private void writeNewPost(String userId, String propietario, String numero, String fechaVencimiento, String cvv) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("cards").push().getKey();
        CreditCard card = new CreditCard(userId, propietario, numero, fechaVencimiento, cvv);
        Map<String, Object> creditCardValues = card.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/cards/" + key, creditCardValues);
        childUpdates.put("/user-cards/" + userId + "/" + key, creditCardValues);

        mDatabase.updateChildren(childUpdates);
    }

}
