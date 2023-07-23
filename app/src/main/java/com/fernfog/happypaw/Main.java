package com.fernfog.happypaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class Main extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://nedosocialnewtork.appspot.com");

    StorageReference storageReference = storage.getReference();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    LinearLayout parentLayout;

    ImageButton logOutButton;

    ImageButton addArticleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logOutButton = findViewById(R.id.logOutButton);
        addArticleButton = findViewById(R.id.addArticleButton);

        parentLayout = findViewById(R.id.parentLayout);

        db.collection("articles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();

                        String key1 = "shortDescription";
                        String key2 = "image";

                        String shortText = (String) map.get(key1);
                        String image = (String) map.get(key2);

                        addCardToView(shortText, image, parentLayout);
                    }
                } else
                    Log.w("query" , "Error getting documents", task.getException());
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent mIntent = new Intent(Main.this, LoginOrRegister.class);
                startActivity(mIntent);

                finish();
            }
        });

        addArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Main.this, CreateArticle.class);
                startActivity(mIntent);

                finish();
            }
        });

    }

    public void addCardToView(String shortText, String image, LinearLayout parentLayout) {
        CardView mCard = new CardView(this);
        LinearLayout.LayoutParams mCardParams = new LinearLayout.LayoutParams(780, 1100);

        mCard.setLayoutParams(mCardParams);

        mCardParams.setMargins(16, 16, 16, 16);
        mCard.setRadius(22.0f);
        mCard.setCardElevation(4.0f);
        mCard.setContentPadding(16,16,16,16);

        LinearLayout insideCardLayout = new LinearLayout(this);

        insideCardLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        insideCardLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView mImageView = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(620, 620);

        imageParams.gravity = Gravity.CENTER;

        imageParams.setMargins(20,20,20,20);

        mImageView.setLayoutParams(imageParams);

        storageReference.child("images/" + image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Main.this).load(uri).into(mImageView);
            }
        });

        TextView mText = new TextView(this);
        mText.setTextSize(15);
        mText.setText(shortText);

        Typeface customFont = ResourcesCompat.getFont(this, R.font.overpass_light);
        mText.setTypeface(customFont);

        insideCardLayout.addView(mImageView);

        mText.setGravity(Gravity.CENTER);

        insideCardLayout.addView(mText);

        mCard.addView(insideCardLayout);

        parentLayout.addView(mCard);
    }
}