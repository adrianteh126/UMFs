package com.example.umfs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText ETSiswamail;
    private EditText ETUsername;
    private EditText ETPassword;
    private EditText ETRepeatPassword;
    private Button BTRegister;

    private FirebaseAuth auth;  //for sign-in authentication, create new user
    private FirebaseDatabase databaseRoot;  //access to UMFs realtime database @ firebase
    private DatabaseReference reference;    //reference to node under UMFs database aka Users node

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ETSiswamail = findViewById(R.id.ETSiswamail);
        ETUsername = findViewById(R.id.ETRegisterUsername);
        ETPassword = findViewById(R.id.ETRegisterPassword);
        ETRepeatPassword = findViewById(R.id.ETRepeatPassword);
        BTRegister = findViewById(R.id.BTGoToRegister);

        //link current authentication to UMFs (instance) sign-in authentication
        auth = FirebaseAuth.getInstance();
        //get access to UMFs realtime database
        String databaseURL = "https://umfs-2cb55-default-rtdb.asia-southeast1.firebasedatabase.app";
        databaseRoot = FirebaseDatabase.getInstance(databaseURL);
        //reference to Users node under UMFs realtime database
        reference = databaseRoot.getReference().child("Users");

        ETSiswamail.addTextChangedListener(TWRegister);
        ETPassword.addTextChangedListener(TWRegister);
        ETUsername.addTextChangedListener(TWRegister);
        ETRepeatPassword.addTextChangedListener(TWRegister);

        BTRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_siswamail = ETSiswamail.getText().toString().trim();
                String txt_username = ETUsername.getText().toString().trim();
                String txt_password = ETPassword.getText().toString().trim();
                String txt_repeatpassword = ETRepeatPassword.getText().toString().trim();
                String[] checkDomain = txt_siswamail.split("[@]");
                String domain = checkDomain[1];

                if (TextUtils.isEmpty(txt_siswamail) || TextUtils.isEmpty(txt_password) ||
                        TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_repeatpassword)) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all information", Toast.LENGTH_SHORT).show();
                } else if (!domain.equals("siswa.um.edu.my")) {
                    Toast.makeText(RegisterActivity.this, "Use your siswamail", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                } else if (!txt_repeatpassword.equals(txt_password)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_siswamail, txt_username, txt_password);
                }
            }
        });
    }

    private TextWatcher TWRegister = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String txt_siswamail = ETSiswamail.getText().toString().trim();
            String txt_password = ETPassword.getText().toString().trim();
            String txt_username = ETUsername.getText().toString().trim();
            String txt_repeatpassword = ETRepeatPassword.getText().toString().trim();

            //button will only be enabled if both fields are filled
            BTRegister.setEnabled(!txt_siswamail.isEmpty() && !txt_password.isEmpty()
                    && !txt_username.isEmpty() && !txt_repeatpassword.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };


    private void registerUser(String siswamail, String username, String password) {
        auth.createUserWithEmailAndPassword(siswamail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(siswamail,username,password);
                    //Get the Unique ID generated when user sign in authentication is successful
                    String id = task.getResult().getUser().getUid();
                    //Create a new sub-node/instance under "Users" with the user object variables
                    databaseRoot.getReference().child("Users").child(id).setValue(user);
                    Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, SetupProfileActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Registration Failed :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}



