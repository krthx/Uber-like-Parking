package com.spot.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spot.R;
import com.spot.SSOSessionActivity;
import com.spot.models.CreditCard;
import com.spot.models.Reserva;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingDialog extends Dialog implements View.OnClickListener,  MultiSpinner.MultiSpinnerListener{

    private DatabaseReference mDatabase;
    Context mContext;
    boolean[] schsSelected;
    List<String> scheAvailable;
    String mAvailable;
    Button btnCrear;
    Button btnCancelar;
    List<Reserva> mReserved;
    String parking;


    private AlertMessage alert = new AlertMessage();


    public BookingDialog(Context context, String available, List<Reserva> reserved, String parking){
        super(context);
        this.mContext = context;
        this.mAvailable = available;
        this.mReserved = reserved;
        this.parking = parking;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_layout);

        mDatabase = FirebaseDatabase.getInstance().getReference("booking");

        MultiSpinner multiSpinner = (MultiSpinner) findViewById(R.id.multi_spinner);
        String[] items = getContext().getResources().getStringArray(R.array.schs);

        scheAvailable = new ArrayList<>();

        int stA = Integer.parseInt(mAvailable.split("-")[0].split(":")[0].trim());
        int edA = Integer.parseInt(mAvailable.split("-")[1].split(":")[0].trim());


        for(int i = 0; i < items.length; i++){
            if(stA <= i && i < edA){
                scheAvailable.add(items[i]);
            }
        }

        List<String> schToDelete = new ArrayList<>();

        for(int i = 0; i < mReserved.size(); i++) {
            int st = Integer.parseInt(mReserved.get(i).startHour.split("-")[0].split(":")[0].trim());
            int ed = Integer.parseInt(mReserved.get(i).endHour.split("-")[0].split(":")[0].trim());

            for(int ii = 0; ii < scheAvailable.size(); ii++) {
                int stAv = Integer.parseInt(scheAvailable.get(ii).split("-")[0].split(":")[0].trim());
                int edAv = Integer.parseInt(scheAvailable.get(ii).split("-")[1].split(":")[0].trim());

                //System.out.println("st: " + st + " ed: " + ed + " stAv: " + stAv + " edAv: " + edAv);
                if( st <= stAv && edAv <= ed) {
                    //System.out.println("Elem removed: " + scheAvailable.get(ii));
                    /*scheAvailable.remove(scheAvailable.get(ii));*/
                    schToDelete.add(scheAvailable.get(ii));
                }
            }
        }

        scheAvailable.removeAll(schToDelete);

        multiSpinner.setItems(scheAvailable, getContext().getResources().getString(R.string.sch_all), this);

        btnCrear = findViewById(R.id.btnCrearBooking);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cn = Calendar.getInstance();
                cn.set(Calendar.HOUR, 0);
                cn.set(Calendar.MINUTE, 0);
                cn.set(Calendar.SECOND, 0);
                cn.set(Calendar.MILLISECOND, 0);

                int c = 0;

                if(schsSelected != null) {
                    for(int i = 0; i < schsSelected.length; i++) {
                        if(schsSelected[i]){

                            boolean next = false;
                            int cont = i;
                            int cont2 = i;

                            do {
                                cont++;
                                next = (cont < schsSelected.length) && schsSelected[cont] && scheAvailable.get(cont - 1).split("-")[1].trim().equals(scheAvailable.get(cont).split("-")[0].trim());
                            }while(next);

                            writeBooking(parking, cn.getTimeInMillis(), scheAvailable.get(i).split("-")[0].trim(), scheAvailable.get(cont - 1).split("-")[1].trim() );
                            i = cont - 1;
                        }
                    }

                    dismiss();
                }else {
                    Toast.makeText(getContext(), "Debe seleccionar al menos un registro", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancelar = findViewById(R.id.btnCancelarBooking);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view){
        int element = view.getId();

        if (element == R.id.btnCrear){
        }else if(element == R.id.btnCancelar) {
            dismiss();
        }
    }


    @Override
    public void onItemsSelected(boolean[] selected) {
        schsSelected = selected;
    }


    private void writeBooking(String parking, long today, String startDate, String endDate) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("booking");
        String key = mDatabase.child("booking").push().getKey();

        Reserva newParking = new Reserva(key, parking, SSOSessionActivity.userLogged.getUid(), today, endDate, startDate);
        Map<String, Object> parkingValues = newParking.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + key , parkingValues);

        mDatabase.updateChildren(childUpdates);

    }
}
