package com.example.sample_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome,textViewFullname,textViewEmail,textViewdob,textViewGender,textViewMobile;
    private ProgressBar progressBar;
    private String fullName,email,dob,gender,mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("HOME");
        textViewWelcome=findViewById(R.id.textView_show_welcome);
        textViewFullname=findViewById(R.id.textView_show_full_name);
        textViewEmail=findViewById(R.id.textView_show_email);
        textViewdob=findViewById(R.id.textView_show_dob);
        textViewGender=findViewById(R.id.textView_show_gender);
        textViewMobile=findViewById(R.id.textView_show_mobile);
        progressBar=findViewById(R.id.progressBar);

        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= authProfile.getCurrentUser();

        if(firebaseUser==null){
            Toast.makeText(UserProfileActivity.this, "Something went wrong!USER DETAILS ARE NOT AVAILABLE AT THE MOMENT", Toast.LENGTH_LONG).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId=firebaseUser.getUid();
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails =snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    fullName= firebaseUser.getDisplayName();
                    email=firebaseUser.getEmail();
                    dob=readUserDetails.dob;
                    gender=readUserDetails.gender;
                    mobile= readUserDetails.mobile;


                    textViewWelcome.setText("Welcome,  "+fullName+"!");
                    textViewFullname.setText(fullName);
                    textViewEmail.setText(email);
                    textViewdob.setText(dob);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}