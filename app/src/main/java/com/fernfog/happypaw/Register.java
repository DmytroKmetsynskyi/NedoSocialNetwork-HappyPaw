package com.fernfog.happypaw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Button submitRegister;
    EditText passwordField;
    EditText emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        submitRegister = findViewById(R.id.submitRegister);
        passwordField = findViewById(R.id.registerPasswordField);
        emailField = findViewById(R.id.registerEmailField);

        submitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(emailField.getText().toString().trim(), passwordField.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Ви успішно зареєструвались, та увійшли в систему!", Toast.LENGTH_LONG).show();

                            FirebaseUser mUser = mAuth.getCurrentUser();
                            mUser.reload();

                            Intent mIntent = new Intent(Register.this, Main.class);
                            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mIntent);

                            finish();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e.getMessage() == "The email address is already in use by another account.")
                            Toast.makeText(Register.this, "Вже інший акаунт використовує цю пошту!", Toast.LENGTH_LONG).show();

                        if (e.getMessage() == "The email address is badly formatted.")
                            Toast.makeText(Register.this, "Адреса електронної пошти неправильно відформатована!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}