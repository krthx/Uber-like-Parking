package com.spot.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.spot.R;
import com.spot.SSOSessionActivity;
import com.spot.models.CreditCard;

import java.util.ArrayList;
import java.util.List;

public class CreditCardAdapter extends  RecyclerView.Adapter<CreditCardAdapter.ViewHolder>{

    Context context;
    public static List<CreditCard> mDataSet;
    private int lastPosition = -1;

    public CreditCardAdapter(List<CreditCard> mList) {
        mDataSet = mList;
    }

    public  List<CreditCard> getDataset() {
        return mDataSet;
    }


    public void addCard(CreditCard c) {
        mDataSet.add(c);

        this.notifyItemInserted(mDataSet.size() - 1);
    }


    public void removeCard(CreditCard c, int pos) {
        mDataSet.remove(c);

        this.notifyItemRemoved(pos);
    }


    @NonNull    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();

        View v = LayoutInflater.from(context)
                .inflate(R.layout.creadit_card_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CreditCard cc = mDataSet.get(i);

        viewHolder.txtCreditCardNum.setText(cc.numero);
    }

    @Override
    public int getItemCount() {
        return  mDataSet.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView btnTrash;
        TextView txtCreditCardNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btnTrash = itemView.findViewById(R.id.btnTrash);
            txtCreditCardNum = itemView.findViewById(R.id.txtCreditCardNum);

            btnTrash.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            System.out.println(v.getId());
            if(v.getId() == R.id.btnTrash) {
                final DatabaseReference mDatabase = FirebaseDatabase
                                    .getInstance()
                                    .getReference("cards/user-cards/" + SSOSessionActivity.userLogged.getUid() );


                Query cardNumFilter = mDatabase.orderByChild("numero").equalTo(txtCreditCardNum.getText().toString());

                cardNumFilter.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren())
                            appleSnapshot.getRef().removeValue();

                        int count = 0;

                        for(CreditCard c : mDataSet){
                            if(c.numero.equals(txtCreditCardNum.getText().toString())) {
                                removeCard(c, count);
                                break;
                            }

                            count++;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context,"Error al borrar registro", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
