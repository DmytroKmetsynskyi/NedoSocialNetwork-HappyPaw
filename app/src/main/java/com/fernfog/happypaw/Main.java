package com.fernfog.happypaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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
                        String key3 = "user";

                        String shortText = (String) map.get(key1);
                        String image = (String) map.get(key2);
                        String email = (String) map.get(key3);

                        Double latitude = (Double) map.get("latitudeOfAnimal");
                        Double longitude = (Double) map.get("longitudeOfAnimal");


                        addCardToView(shortText, image, parentLayout, latitude, longitude, email);
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
            }
        });
    }

    public void addCardToView(String shortText, String image, LinearLayout parentLayout, double latitude, double longitude, String emailText_) {
        CardView mCard = new CardView(this);
        LinearLayout.LayoutParams mCardParams = new LinearLayout.LayoutParams(780, ViewGroup.LayoutParams.WRAP_CONTENT);
        mCardParams.gravity = Gravity.CENTER;
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
        imageParams.setMargins(20,55,20,20);
        mImageView.setScaleType(ImageView.ScaleType.CENTER);
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
        mText.setGravity(Gravity.CENTER);

        MaterialButton mButton = new MaterialButton(this);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFullScreenMapDialog(latitude, longitude);
            }
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(525, 110);
        layoutParams.gravity = Gravity.CENTER;

        mButton.setBackgroundColor(Color.parseColor("#2B73E0"));
        mButton.setText(R.string.expand_map_button_text);
        mButton.setTextSize(12);
        mButton.setTypeface(customFont);
        mButton.setIcon(getDrawable(R.drawable.mapicon));
        mButton.setIconGravity(MaterialButton.ICON_GRAVITY_END);
        layoutParams.setMargins(0,10,0,0);
        mButton.setCornerRadius(16);


        TextView emailText = new TextView(this);
        emailText.setTextColor(Color.parseColor("#777a80"));
        emailText.setTextSize(10);
        emailText.setText(emailText_);
        emailText.setTypeface(customFont);
        emailText.setGravity(Gravity.LEFT);

        insideCardLayout.addView(emailText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        insideCardLayout.addView(mImageView);
        insideCardLayout.addView(mText);
        insideCardLayout.addView(mButton, layoutParams);
        mCard.addView(insideCardLayout);
        parentLayout.addView(mCard);
    }

    private void showFullScreenMapDialog(double latitude, double longitude) {
        FullScreenMapDialogFragment dialogFragment = new FullScreenMapDialogFragment(latitude, longitude, getResources().getDrawable(R.drawable.locationicon));
        dialogFragment.show(getSupportFragmentManager(), "FullScreenMapDialog");
    }
}