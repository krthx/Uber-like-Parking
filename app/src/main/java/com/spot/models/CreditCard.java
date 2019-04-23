package com.spot.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class CreditCard {

    public String uid;
    public String propietario;
    public String numero;
    public String fechaVencimiento;
    public String cvv;

    public CreditCard() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public CreditCard(String uid, String propietario, String numero, String fechaVencimiento, String cvv) {
        this.uid = uid;
        this.propietario = propietario;
        this.numero = numero;
        this.fechaVencimiento = fechaVencimiento;
        this.cvv = cvv;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("propietario", propietario);
        result.put("numero", numero);
        result.put("fechaVencimiento", fechaVencimiento);
        result.put("cvv", cvv);

        return result;
    }
}
