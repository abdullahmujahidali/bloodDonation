package com.example.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseAuth auth;
    DatabaseReference UserDataBase;
    EditText ed1, ed2, ed3, ed4,ed6,ed7;
    private Spinner spinner;
    Button btn;
    ProgressDialog progressDialog;
    private String blood_grp;
    Address add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth = FirebaseAuth.getInstance();
        ed1 = (EditText) findViewById(R.id.fname);
        ed2 = (EditText) findViewById(R.id.lname);
        ed3 = (EditText) findViewById(R.id.password);
        ed4 = (EditText) findViewById(R.id.email);
        ed6 = (EditText) findViewById(R.id.address);
        ed7 = (EditText) findViewById(R.id.pincode);
        btn = (Button) findViewById(R.id.regis_btn);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.blood_groups,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void AddUser(View view) {
        final String firstname = ed1.getText().toString().trim();
        final String lastname = ed2.getText().toString().trim();
        final String email = ed4.getText().toString().trim();
        final String password = ed3.getText().toString().trim();
        final String address = ed6.getText().toString().trim();
        final String pincode = ed7.getText().toString().trim();
        final String blood = blood_grp;
        Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> list = gc.getFromLocationName(address,1);
            if (list !=null && list.size()>0) {
                add = list.get(0);
            }else{
                LoginError error = new LoginError("RegistrationError","Please Enter a valid Address");
                error.show(getSupportFragmentManager(),"reg");
                ed6.setError("Enter a valid Address");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (firstname.isEmpty()) {
            ed1.setError("FirstName ShouldNot be Empty");
            return;
        } else if (lastname.isEmpty()) {
            ed2.setError("LastName Should not be empty");
            return;
        } else if (password.length() < 6) {
            ed3.setError("Password should be more than 6 characters");
        } else if (blood_grp.isEmpty()) {
            Toast.makeText(this, "Please choose Blood Group", Toast.LENGTH_LONG).show();
        } else if (address.isEmpty()) {
            ed6.setError("Enter an Address");
        } else if (pincode.length() < 6) {
            ed7.setError("Enter a valid PinCode");
        }else if (add == null){
            ed6.setError("Enter a valid address");
        }
        else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Registering");
            progressDialog.setMessage("Registering......");
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.show();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        User usr = new User(firstname, lastname, email, address, pincode, blood,add.getLatitude(),add.getLongitude());
                        FirebaseDatabase.getInstance().getReference("Data").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"data Added",Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),"Unsuccessfull",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        Intent redirectLogin = new Intent(Registration.this, MainActivity.class);
                        startActivity(redirectLogin);
                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        progressDialog.dismiss();
                        LoginError error = new LoginError("RegistrationError",task.getException().getMessage().toString());
                        error.show(getSupportFragmentManager(),"reg");
                        Toast.makeText(Registration.this, "User already Exist", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        LoginError error = new LoginError("RegistrationError",task.getException().getMessage().toString());
                        error.show(getSupportFragmentManager(),"reg");
                        Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        blood_grp = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        blood_grp = "";
    }
}