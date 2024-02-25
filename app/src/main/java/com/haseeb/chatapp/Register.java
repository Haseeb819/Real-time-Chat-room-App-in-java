package com.haseeb.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.haseeb.chatapp.Models.Users;

public class Register extends AppCompatActivity {

    TextInputEditText textmail, textpassword, textname;
    ProgressBar progressBar;
    FirebaseAuth auth;
    DatabaseReference refrence;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textmail = findViewById(R.id.email_ed_register);
        textpassword = findViewById(R.id.password_ed_register);
        textname = findViewById(R.id.name_register);
        progressBar = findViewById(R.id.progressBarRegister);
        refrence = FirebaseDatabase.getInstance().getReference().child("Users");
        auth = FirebaseAuth.getInstance();
    }

    public void RegisterUser(View v) {
        progressBar.setVisibility(View.VISIBLE);
        final String email = textmail.getText().toString();
        final String password = textpassword.getText().toString();
        final String name = textname.getText().toString();

        if (!email.equals("") && !password.equals("") && password.length() > 5) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = auth.getCurrentUser();
                                Users u = new Users();
                                u.setEmail(email);
                                u.setName(name);
                                Toast.makeText(getApplicationContext(), "Register Successfully ",
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                finish();
                                Intent i = new Intent(Register.this, chatRoom.class);
                                startActivity(i);

                                refrence.child(firebaseUser.getUid()).setValue(u);
                                       /* .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Register Successfully ",
                                                            Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    finish();
                                                    Intent i = new Intent(Register.this, chatRoom.class);
                                                    startActivity(i);*/
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Error creating User ",
                                        Toast.LENGTH_SHORT).show();
                            }



    }
});
        }
    }
    public  void gotoLogin(){
        Intent i = new Intent(Register.this,MainActivity.class);
        startActivity(i);
    }
}