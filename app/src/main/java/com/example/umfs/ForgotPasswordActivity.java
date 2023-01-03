package com.example.umfs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button BTResetPassword;
    private EditText ETSiswamailFP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        BTResetPassword = findViewById(R.id.BTResetPassword);
        ETSiswamailFP = findViewById(R.id.ETSiswamailFP);

        auth = FirebaseAuth.getInstance();

        BTResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String siswamail_fp = ETSiswamailFP.getText().toString().trim();

                //If the email address is invalid or empty, cannot proceed
                if (!Patterns.EMAIL_ADDRESS.matcher(siswamail_fp).matches()) {
                    ETSiswamailFP.setError("Invalid Email");
                    ETSiswamailFP.setFocusable(true);
                }
                else{
                    forgotPassword(siswamail_fp);
                }
            }

            private void forgotPassword(String siswamail_fp) {
                auth.sendPasswordResetEmail(siswamail_fp)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ForgotPasswordActivity.this, "Reset Password Email Successfully Sent", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ForgotPasswordActivity.this, "Reset Password Email Failed to Send", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });



    }
}