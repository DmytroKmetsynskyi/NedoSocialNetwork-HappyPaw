package com.fernfog.happypaw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginOrRegister extends AppCompatActivity {

    Button goToRegisterButton;
    Button goToLoginButton;
    Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        goToRegisterButton = findViewById(R.id.goToRegisterButton);
        goToLoginButton = findViewById(R.id.goToLoginButton);
        resetPasswordButton = findViewById(R.id.goToResetPasswordButton);

        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(LoginOrRegister.this, Login.class);
                startActivity(mIntent);

                finish();
            }
        });

        goToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(LoginOrRegister.this, Register.class);
                startActivity(mIntent);

                finish();
            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(LoginOrRegister.this, ResetPassword.class);
                startActivity(mIntent);

                finish();
            }
        });
    }
}