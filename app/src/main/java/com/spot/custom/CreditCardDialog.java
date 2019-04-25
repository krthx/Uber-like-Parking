package com.spot.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spot.HomeActivity;
import com.spot.R;
import com.spot.SSOSessionActivity;
import com.spot.models.CreditCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreditCardDialog extends Dialog implements android.view.View.OnClickListener {

    private DatabaseReference mDatabase;
    Context mContext;

    private AlertMessage alert = new AlertMessage();

    private EditText txtPropietario;
    private EditText txtNumero;
    private EditText txtFechaVencimiento;
    private EditText txtCVV;
    private Button btnAceptar;
    private Button btnCancelar;

    public CreditCardDialog(Context context){
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.credit_card_layout);

        txtPropietario = findViewById(R.id.txtPropietario);
        txtNumero = findViewById(R.id.txtNumero);
        txtFechaVencimiento = findViewById(R.id.txtFechaVencimiento);
        txtCVV = findViewById(R.id.txtCVV);

        btnAceptar = findViewById(R.id.btnCrear);
        btnAceptar.setOnClickListener(this);

        btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference("cards");

    }

    @Override
    public void onClick(View view){
        int element = view.getId();

        if (element == R.id.btnCrear){
            System.out.println(SSOSessionActivity.userLogged.getUid());
            writeNewPost(SSOSessionActivity.userLogged.getUid(), txtPropietario.getText().toString(),
                    txtNumero.getText().toString(), txtFechaVencimiento.getText().toString(),
                    txtCVV.getText().toString());

            dismiss();
        }else if(element == R.id.btnCancelar) {
            dismiss();
        }
    }

    private void writeNewPost(String userId, String propietario, String numero, String fechaVencimiento, String cvv) {

        String key = mDatabase.child("cards").push().getKey();
        CreditCard card = new CreditCard(userId, propietario, numero, fechaVencimiento, cvv);
        Map<String, Object> creditCardValues = card.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/user-cards/" + userId + "/" + key, creditCardValues);

        mDatabase.updateChildren(childUpdates);

    }


    private static boolean validateCreditCardNumber(String str) {

        int[] ints = new int[str.length()];

        for (int i = 0; i < str.length(); i++)
            ints[i] = Integer.parseInt(str.substring(i, i + 1));

        for (int i = ints.length - 2; i >= 0; i = i - 2) {
            int j = ints[i];
            j = j * 2;

            if (j > 9)
                j = j % 10 + 1;

            ints[i] = j;
        }
        int sum = 0;

        for (int i = 0; i < ints.length; i++)
            sum += ints[i];


        if (sum % 10 == 0) {
            System.out.println(str + " is a valid credit card number");
            return true;
        }/* else {
            System.out.println(str + " is an invalid credit card number");

        }*/
            return false;
    }

}
