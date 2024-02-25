package com.haseeb.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    TextInputEditText textmail, textpassword;
    ProgressBar progressBar;
    FirebaseAuth auth;
    DatabaseReference refrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            Intent i = new Intent(MainActivity.this,chatRoom.class);
            startActivity(i);
        }
        else {
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        textmail = findViewById(R.id.email_ed_login);
        textpassword = findViewById(R.id.password_ed_login);
        progressBar = findViewById(R.id.progressBarLogin);
        refrence = FirebaseDatabase.getInstance().getReference().child("Users");

    }

}

    public void  login(View v){
          progressBar.setVisibility(View.VISIBLE);

          String email=textmail.getText().toString();
        String password=textpassword.getText().toString();

        if(!email.equals("") && !password.equals(""))
        {
            auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Logged in",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this,chatRoom.class);
                                startActivity(i);
                            }

                            else {
                                Toast.makeText(getApplicationContext(),"Wrong email/password. Try again",Toast
                                        .LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }
    public void gotoRegister(View v){
        Intent i = new Intent(MainActivity.this,Register.class);
        startActivity(i);
    }

}