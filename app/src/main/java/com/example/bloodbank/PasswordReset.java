package com.example.bloodbank;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordReset extends AppCompatActivity {
    private EditText email;
    private Button btn;
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        email = (EditText)findViewById(R.id.reset_input_mail);
        btn = (Button)findViewById(R.id.reset_button);
        myAuth = FirebaseAuth.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString().trim();
                if(!(mail.isEmpty()) && Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    myAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Please check your Mail",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            } else {
                                LoginError error = new LoginError(task.getException().getMessage().toString(),"Resetting Error");
                            }
                        }
                    });
                }else{
                    email.setError("Enter a valid email");
                }
            }
        });
    }

}
