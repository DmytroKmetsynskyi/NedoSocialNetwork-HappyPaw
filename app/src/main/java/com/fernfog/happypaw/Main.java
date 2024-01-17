package com.fernfog.happypaw;

import android.content.Intent;
import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

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
                        String key4 = "date";

                        String shortText = (String) map.get(key1);
                        String image = (String) map.get(key2);
                        String email = (String) map.get(key3);
                        String date = (String) map.get(key4);

                        Double latitude = (Double) map.get("latitudeOfAnimal");
                        Double longitude = (Double) map.get("longitudeOfAnimal");

                        addCardToView(shortText, image, parentLayout, latitude, longitude, email, date);
                    }
                } else
                    Log.w("query", "Error getting documents", task.getException());
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

    public void addCardToView(String shortText, String image, LinearLayout parentLayout, double latitude, double longitude, String emailText_, String date) {
        CardView mCard = new CardView(this);
        LinearLayout.LayoutParams mCardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        mCardParams.setMargins(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
        mCard.setLayoutParams(mCardParams);
        mCard.setRadius(dpToPx(22));
        mCard.setCardElevation(dpToPx(4));
        mCard.setContentPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        LinearLayout insideCardLayout = new LinearLayout(this);
        insideCardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        insideCardLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView mImageView = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(200)
        );
        imageParams.setMargins(dpToPx(20), dpToPx(16), dpToPx(20), dpToPx(16));
        mImageView.setScaleType(ImageView.ScaleType.CENTER);
        mImageView.setLayoutParams(imageParams);

        storageReference.child("images/" + image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Main.this).load(uri).into(mImageView);
            }
        });

        TextView mText = new TextView(this);
        mText.setTextSize(dpToPx(8));
        mText.setText(shortText);
        Typeface customFont = ResourcesCompat.getFont(this, R.font.overpass_light);
        mText.setTypeface(customFont);
        mText.setGravity(Gravity.CENTER);

        MaterialButton mButton = new MaterialButton(this);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMargins(0, dpToPx(10), 0, 0);
        mButton.setLayoutParams(buttonParams);
        mButton.setBackgroundColor(Color.parseColor("#2B73E0"));
        mButton.setText(R.string.expand_map_button_text);
        mButton.setTextSize(dpToPx(6));
        mButton.setTypeface(customFont);
        mButton.setIcon(getDrawable(R.drawable.mapicon));
        mButton.setIconGravity(MaterialButton.ICON_GRAVITY_END);
        mButton.setCornerRadius(dpToPx(8));
        buttonParams.gravity = Gravity.CENTER;
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFullScreenMapDialog(latitude, longitude);
            }
        });

        TextView emailText = new TextView(this);
        emailText.setTextColor(Color.parseColor("#777a80"));
        emailText.setTextSize(dpToPx(5));
        emailText.setText(emailText_);
        emailText.setTypeface(customFont);
        emailText.setGravity(Gravity.LEFT);

        TextView dateText = new TextView(this);
        dateText.setTextColor(Color.parseColor("#777a80"));
        dateText.setTextSize(dpToPx(5));
        dateText.setText(date);
        dateText.setTypeface(customFont);
        dateText.setGravity(Gravity.LEFT);

        insideCardLayout.addView(emailText);
        insideCardLayout.addView(dateText);
        insideCardLayout.addView(mImageView);
        insideCardLayout.addView(mText);
        insideCardLayout.addView(mButton);
        mCard.addView(insideCardLayout);
        parentLayout.addView(mCard);
    }

    private void showFullScreenMapDialog(double latitude, double longitude) {
        FullScreenMapDialogFragment dialogFragment = new FullScreenMapDialogFragment(latitude, longitude, getResources().getDrawable(R.drawable.locationicon));
        dialogFragment.show(getSupportFragmentManager(), "FullScreenMapDialog");
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
