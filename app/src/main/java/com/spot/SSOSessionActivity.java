package com.spot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spot.models.CreditCard;
import com.spot.models.Parking;
import com.spot.models.Reserva;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SSOSessionActivity extends AppCompatActivity {

    private static final String TAG = "";
    private static final int RC_SIGN_IN = 721;
    Button btnEnter;
    EditText user;
    EditText password;

    ImageView googleBtn;

    TextView titleSignUp;

    private FirebaseAuth mAuth;
    public static FirebaseUser userLogged;
    static boolean facebookAuthentication = false;

    GoogleSignInClient mGoogleSignInClient;


    CallbackManager callbackManager;
    LoginButton facebookBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_ssosession);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firebase_key_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI(currentUser);

        btnEnter = findViewById(R.id.enter);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getText() != null && password.getText() != null) {

                    final ProgressDialog progressDialog = new ProgressDialog(SSOSessionActivity.this);
                    progressDialog.setTitle("Iniciando...");
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(user.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(SSOSessionActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");

                                        SSOSessionActivity.userLogged = mAuth.getCurrentUser();

                                        Intent home = new Intent(SSOSessionActivity.this, HomeActivity.class);

                                        startActivity(home);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(SSOSessionActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }else {
                    Toast.makeText(SSOSessionActivity.this, "Debe ingresar Credenciales .",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        user = findViewById(R.id.username);
        password= findViewById(R.id.password);

        callbackManager = CallbackManager.Factory.create();

        facebookBtn = findViewById(R.id.login_button);
        facebookBtn.setReadPermissions("email", "public_profile");
        facebookBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        googleBtn = findViewById(R.id.google);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

                user.setText("");
                password.setText("");

            }
        });


        titleSignUp = findViewById(R.id.titleSignUp);
        titleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin = new Intent(SSOSessionActivity.this, SigInActivity.class);

                startActivity(signin);

                finish();
            }
        });

        if(!true) {
            writeParkingProfiles("P1", "Juan Mansilla", "59368745", "12", 14.598848, -90.486804, "20", "07:00 a.m. - 18:00 p.m.");
            writeParkingProfiles("P2", "Pedro Lopez", "43368725", "profile", 14.598521, -90.484368, "25", "00:00 a.m. - 20:00 p.m.");
            writeParkingProfiles("P3", "María Pereira", "58996745", "sdfasd", 14.599284, -90.486058, "30", "08:00 a.m. - 12:00 p.m.");
        }

        if(!true) {

            Calendar cn = Calendar.getInstance();
            cn.set(Calendar.HOUR, 0);
            cn.set(Calendar.MINUTE, 0);
            cn.set(Calendar.SECOND, 0);
            cn.set(Calendar.MILLISECOND, 0);

            writeBooking("-Ldg6WYY8WaNr68BNN0o", cn.getTimeInMillis(), "09:00", "10:00");
           /* writeBooking("-Ldg6WYY8WaNr68BNN0o", cn.getTimeInMillis(), "22:00", "23:00");
            writeBooking("-Ldg6WYu0YyZudsx4h0Q", cn.getTimeInMillis(), "11:00", "15:00");
            writeBooking("-Ldg6WYu0YyZudsx4h0Q", cn.getTimeInMillis(), "17:00", "23:00");*/
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(FirebaseUser currentUser) {
        userLogged = currentUser;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);


                            SSOSessionActivity.userLogged = mAuth.getCurrentUser();

                            facebookAuthentication = true;

                            Intent home = new Intent(SSOSessionActivity.this, HomeActivity.class);

                            startActivity(home);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SSOSessionActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void writeParkingProfiles(String title, String owner, String phone, String url, double longitude, double latitude, String costPerHour, String availability) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("parking");
        String key = mDatabase.child("parking").push().getKey();


        //tring uid, String owner, String phone, String url, String longitude, String latitude, String costPerHour
        Parking parking = new Parking(key, title, owner, phone, url, longitude, latitude, costPerHour, availability);
        Map<String, Object> parkingValues = parking.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/parking-profiles/" + key, parkingValues);

        mDatabase.updateChildren(childUpdates);

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
