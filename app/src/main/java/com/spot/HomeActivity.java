package com.spot;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity  implements NavigationDrawerFragment.FragmentDrawerListener {

    private Toolbar mToolbar;
    private static final int PERMS_REQUEST_CODE = 123;

    private static final int REQUEST = 112;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        mToolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_fragmnet);

        drawerFragment.setUp((DrawerLayout)findViewById(R.id.drawer_layout), mToolbar, R.id.navigation_drawer_fragmnet);
        drawerFragment.setDrawerListener(this);

        onDrawerItemSelected(null, 1);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET};
            if (!hasPermissions(HomeActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, REQUEST );
            } else {
                //call get location here
            }
        } else {
            //call get location here
        }
    }

    public void onDrawerItemSelected(View view, int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (position){
            case 1:
                fragment = MapFragment.newInstance(this);
                break;
            case 2:
                fragment = new ConfigFragment();
                break;
            case 3:
                fragment = new HistoryFragment();
                break;
            case 4:
                fragment = new HelpFragment();
                break;
            case 5:
                mAuth.signOut();
                SSOSessionActivity.userLogged = null;

                Intent login = new Intent(HomeActivity.this, SSOSessionActivity.class);

                if(SSOSessionActivity.facebookAuthentication) {

                    LoginManager.getInstance().logOut();
                }

                startActivity(login);
                break;
            default:
                break;
        }

        if (fragment != null){
            ((RelativeLayout)findViewById(R.id.Contenedor)).removeAllViewsInLayout();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.Contenedor, fragment);
            fragmentTransaction.commit();
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //call get location here
                } else {
                    Toast.makeText(this, "The app was not allowed to access your location", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
