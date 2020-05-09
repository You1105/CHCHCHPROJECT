package com.example.database;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

public class RegisterActivity extends AppCompatActivity { long lastPressed;

    private static final String TAG = "RegisterActivity";

    String stEmail, stPassword;
    EditText etEmail, etPassword;
    ProgressBar pb;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        pb = (ProgressBar) findViewById(R.id.pb);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });

//회원가입
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stEmail = etEmail.getText().toString();
                stPassword = etPassword.getText().toString();

                if(stEmail.isEmpty() || stEmail.equals("")){
                    Toast.makeText(RegisterActivity.this, "Please insert Email", Toast.LENGTH_LONG).show();
                    return;
                }
                if(stPassword.isEmpty() || stPassword.equals("")){
                    Toast.makeText(RegisterActivity.this, "Please insert Password", Toast.LENGTH_LONG).show();
                    return;
                }

                pb.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(stEmail, stPassword)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                pb.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(RegisterActivity.this, "Authentication successed.", Toast.LENGTH_SHORT).show();

                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);
                                    DatabaseReference myRef = database.getReference("users").child(user.getUid());

                                    Hashtable<String, String> members = new Hashtable<String, String>();
                                    members.put("email", user.getEmail());
                                    members.put("uid", user.getUid());
                                    members.put("photo", "");
//                                    members.put("photo", user.getPhotoUrl());
//                                    myRef.child(user.getUid()).setValue(members);
                                    myRef.setValue(members);

                                    Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                }

                                // ...
                            }
                        });

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

}
