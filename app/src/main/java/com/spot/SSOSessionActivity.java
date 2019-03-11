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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SSOSessionActivity extends AppCompatActivity {

    private static final String TAG = "";
    Button btnEnter;
    EditText user;
    EditText password;

    ImageView facebookBtn;
    ImageView googleBtn;

    TextView titleSignUp;

    private FirebaseAuth mAuth;
    static FirebaseUser userLogged;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        facebookBtn = findViewById(R.id.facebook);
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        googleBtn = findViewById(R.id.google);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signIn();

                Intent signin = new Intent(SSOSessionActivity.this, SigInActivity.class);

                startActivity(signin);

                user.setText("");
                password.setText("");
                //finish();

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
    }

    private void updateUI(FirebaseUser currentUser) {
        userLogged = currentUser;
    }

}
