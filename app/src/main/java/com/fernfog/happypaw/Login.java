package com.fernfog.happypaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    Button submitLogin;
    EditText passwordField;
    EditText emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        submitLogin = findViewById(R.id.submitLogin);
        passwordField = findViewById(R.id.loginPasswordField);
        emailField = findViewById(R.id.loginEmailField);

        submitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(emailField.getText().toString().trim(), passwordField.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Ви успішно увійшли! (В систему)", Toast.LENGTH_LONG).show();

                            FirebaseUser mUser = mAuth.getCurrentUser();
                            mUser.reload();

                            Intent mIntent = new Intent(Login.this, Main.class);
                            startActivity(mIntent);

                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e.getMessage() == "The password is invalid or the user does not have a password.")
                            Toast.makeText(Login.this, "Пароль - інвалід!", Toast.LENGTH_LONG).show();

                        if (e.getMessage() == "The email address is badly formatted.")
                            Toast.makeText(Login.this, "Адреса електронної пошти неправильно відформатована!", Toast.LENGTH_LONG).show();

                        if (e.getMessage() == "There is no user record corresponding to this identifier. The user may have been deleted.")
                            Toast.makeText(Login.this, "Користувача з такою електронною поштою не існує!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}