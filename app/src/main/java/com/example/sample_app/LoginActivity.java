package com.example.sample_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextLoginEmail;
    private EditText editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("LOGIN");
        editTextLoginEmail=findViewById(R.id.editText_login_email);
        editTextLoginPwd=findViewById(R.id.editText_login_pwd);
        authProfile=FirebaseAuth.getInstance();

        Button buttonLogin =findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail=editTextLoginEmail.getText().toString();
                String textPwd=editTextLoginPwd.getText().toString();
                if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email is required");
                    editTextLoginEmail.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(LoginActivity.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Valid Email is required");
                    editTextLoginEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(LoginActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                    editTextLoginPwd.setError("Email is required");
                    editTextLoginPwd.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail,textPwd);
                }
            }
        });

    }

    private void loginUser(String email, String pwd) {
        authProfile.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "You are logged in successfully now", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,UserProfileActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

        }
