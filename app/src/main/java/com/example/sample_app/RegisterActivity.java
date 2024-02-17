package com.example.sample_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullname,editTextRegisterEmail,editTextRegisterDoB,editTextRegisterMobile,editTextRegisterPwd,editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");

        Toast.makeText(RegisterActivity.this, "You can register Now", Toast.LENGTH_LONG).show();

        progressBar=findViewById(R.id.progressBar);
        editTextRegisterFullname=findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail=findViewById(R.id.editText_register_email);
        editTextRegisterDoB=findViewById(R.id.editText_register_dob);
        editTextRegisterMobile=findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd=findViewById(R.id.editText_register_password);

        radioGroupRegisterGender=findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        Button buttonRegister =findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectGenderId= radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected=findViewById(selectGenderId);

                String Fullname =editTextRegisterFullname.getText().toString();
                String Email=editTextRegisterEmail.getText().toString();
                String Password=editTextRegisterPwd.getText().toString();
                String Mobile=editTextRegisterMobile.getText().toString();
                String gender;
                String dob=editTextRegisterDoB.getText().toString();

                gender=radioButtonRegisterGenderSelected.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                registerUser(Fullname,Email,dob,gender,Mobile,Password);
            }
        });
    }
    private void registerUser(String Fullname, String Email,String dob,String gender,String Mobile,String Password){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "USER REGISTERED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                    FirebaseUser fbUser=auth.getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder().setDisplayName(Fullname).build();
                    fbUser.updateProfile(profileChangeRequest);

                    ReadWriteUserDetails writeUserDetails =new ReadWriteUserDetails(dob,gender,Mobile);
                    DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");
                    progressBar.setVisibility(View.GONE);
                    referenceProfile.child(fbUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                            fbUser.sendEmailVerification();
                            Toast.makeText(RegisterActivity.this, "User registered successfully.Please verify your email", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegisterActivity.this,UserProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();}else{
                                Toast.makeText(RegisterActivity.this, "User registration failed.TRY AGAIN", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
            }
        });
    }
}