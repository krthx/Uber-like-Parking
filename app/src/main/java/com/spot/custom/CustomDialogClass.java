package com.spot.custom;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.spot.R;

public class CustomDialogClass extends DialogFragment  {
    public Activity c;
    public Dialog d;
    public Button crear;
    EditText txtPropietario, txtNumero, txtFechaVencimiento, txtCVV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.credit_card_layout, container, false);

        crear = (Button) v.findViewById(R.id.btnCrear);

        txtPropietario = v.findViewById(R.id.txtPropietario);
        txtNumero = v.findViewById(R.id.txtNumero);
        txtFechaVencimiento = v.findViewById(R.id.txtFechaVencimiento);
        txtCVV = v.findViewById(R.id.txtCVV);

        //crear.setOnClickListener(this);

        return v;
    }

//    public CustomDialogClass(Activity a) {
//        super(a);
//
//        this.c = a;
//    }
}
