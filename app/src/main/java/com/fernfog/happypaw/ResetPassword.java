package com.fernfog.happypaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    EditText emailField;
    Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        emailField = findViewById(R.id.resetPasswordField);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailField.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ResetPassword.this, "Надіслано!", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e.getMessage() == "The email address is badly formatted.")
                            Toast.makeText(ResetPassword.this, "Адреса електронної пошти неправильно відформатована!", Toast.LENGTH_LONG).show();

                        if (e.getMessage() == "There is no user record corresponding to this identifier. The user may have been deleted.")
                            Toast.makeText(ResetPassword.this, "Користувача з такою електронною поштою не існує!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}