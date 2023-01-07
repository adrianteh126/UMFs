package com.example.umfs;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button BTRegister;
    private Button BTLogin;
    private Button BTForgotPassword;
    private EditText ETSiswamail;
    private EditText ETPassword;
    private FirebaseAuth auth;  //for sign-in authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BTRegister = findViewById(R.id.BTGoToRegister);
        BTLogin = findViewById(R.id.BTLogin);
        BTForgotPassword = findViewById(R.id.BTForgotPassword);
        ETSiswamail = findViewById(R.id.ETSiswamailLogin);
        ETPassword = findViewById(R.id.ETPasswordLogin);
        auth = FirebaseAuth.getInstance();


        ETSiswamail.addTextChangedListener(TWLogin);
        ETPassword.addTextChangedListener(TWLogin);

        BTLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_siswamail = ETSiswamail.getText().toString();
                String txt_password = ETPassword.getText().toString();

                // if format of email doesn't matches return null
                if (!Patterns.EMAIL_ADDRESS.matcher(txt_siswamail).matches()) {
                    ETSiswamail.setError("Invalid Email");
                    ETSiswamail.setFocusable(true);

                } else if (txt_password.isEmpty()) {
                    ETPassword.setError("Invalid password");
                    ETSiswamail.setFocusable(true);
                } else {
                    loginUser(txt_siswamail, txt_password);
                }
            }
        });

        BTRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        BTForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });
    }

    private TextWatcher TWLogin = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String txt_siswamail = ETSiswamail.getText().toString().trim();
            String txt_password = ETPassword.getText().toString().trim();

            //button will only be enabled if both fields are filled
            BTLogin.setEnabled(!txt_siswamail.isEmpty() && !txt_password.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void loginUser(String txt_siswamail, String txt_password) {
        auth.signInWithEmailAndPassword(txt_siswamail, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //store user that is logged in, so they don't have to log in again
                    FirebaseUser user = auth.getCurrentUser();
                    Intent intent = new Intent();
                    Toast.makeText(LoginActivity.this, "Welcome to UMFs", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect password or email!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}