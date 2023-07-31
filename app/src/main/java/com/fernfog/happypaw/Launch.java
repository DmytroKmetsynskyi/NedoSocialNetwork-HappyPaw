package com.fernfog.happypaw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Launch extends AppCompatActivity {

    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        if (mUser == null) {
            Intent mIntent = new Intent(Launch.this, LoginOrRegister.class);
            startActivity(mIntent);

            finish();

        } else {
            Intent mIntent = new Intent(Launch.this, Main.class);
            startActivity(mIntent);

            finish();
        }
    }
}