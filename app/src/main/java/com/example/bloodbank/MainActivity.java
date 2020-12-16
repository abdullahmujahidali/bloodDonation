package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Button reg_btn,log_btn;
    private EditText uname,pass;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private Spinner spinner;
    private FusedLocationProviderClient myLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uname = (EditText)findViewById(R.id.username);
        pass = (EditText)findViewById(R.id.password);
        reg_btn = findViewById(R.id.reg_btn);
        log_btn = findViewById(R.id.login_btn);
        auth = FirebaseAuth.getInstance();
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")) { //if gps is disabled
            showSettingAlert();
        }
    }

    public void RegisterUser(View view) {
        Intent regIntent = new Intent(this,Registration.class);
        startActivity(regIntent);
    }

    public void Login(View view) {
        final String username = uname.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        if (username.isEmpty()) {
            uname.setError("UserName should not be empty");
        } else if (password.isEmpty()) {
            pass.setError("Password should not be empty");
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Logging......");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_LONG).show();
                        Intent LoginIntent = new Intent(MainActivity.this, Home_page.class);
                        LoginIntent.putExtra("mail", username);
                        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(LoginIntent);
                        finish();

                    } else {
                        String error = task.getException().toString();
                        LoginError loginError = new LoginError("Login",error);
                        loginError.show(getSupportFragmentManager(),"Login Error");
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent LoginIntent  = new Intent(MainActivity.this,Home_page.class);
            startActivity(LoginIntent);
            finish();
        }
    }

    public void ResetPassword(View view) {
        Intent reset_intent = new Intent(this,PasswordReset.class);
        startActivity(reset_intent);
    }



    public void showSettingAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS setting!");
        alertDialog.setMessage("GPS is not enabled, Do you want to go to settings menu? ");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
                System.exit(0);
            }
        });
        alertDialog.show();
    }
}
