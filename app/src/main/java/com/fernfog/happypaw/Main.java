package com.fernfog.happypaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
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

    LinearLayout layoutInScrollView;
    ImageButton logOutButton;
    ImageButton addArticleButton;
    ConstraintLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutInScrollView = findViewById(R.id.layoutInScrollView);
        parentLayout = findViewById(R.id.parentLayout);

        initToolBar(parentLayout);

        db.collection("articles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();

                        String shortText = (String) map.get("shortDescription");
                        String image = (String) map.get("image");
                        boolean status = (boolean) map.get("status");

                        addCardToView(shortText, image, status, layoutInScrollView);
                    }
                }
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

    public void addCardToView(String shortText, String image, boolean status, LinearLayout parentLayout) {
        CardView mCard = new CardView(this);
        LinearLayout.LayoutParams mCardParams = new LinearLayout.LayoutParams(780, 1100);
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

        TextView descriptionText = new TextView(this);
        descriptionText.setTextSize(15);
        descriptionText.setText(shortText);
        descriptionText.setGravity(Gravity.CENTER);

        Typeface customFont = ResourcesCompat.getFont(this, R.font.overpass_light);

        TextView statusText = new TextView(this);
        statusText.setTextSize(20);
        statusText.setGravity(Gravity.LEFT);
        LinearLayout.LayoutParams statusTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        statusTextParams.leftMargin = 20;
        statusText.setLayoutParams(statusTextParams);

        descriptionText.setTypeface(customFont);
        statusText.setTypeface(customFont);

        int statusTextColor;

        if (status) {
            statusText.setText("ЗНАЙДЕНО");

            statusTextColor = Color.parseColor("#32a852");
            statusText.setTextColor(statusTextColor);
        } else {
            statusText.setText("ЩЕ НІ");

            statusTextColor = Color.parseColor("#a83242");
            statusText.setTextColor(statusTextColor);
        }

        insideCardLayout.addView(mImageView);
        insideCardLayout.addView(descriptionText);
        insideCardLayout.addView(statusText);

        mCard.addView(insideCardLayout);
        parentLayout.addView(mCard);
    }

    public void initToolBar(ConstraintLayout parentLayout) {
        ConstraintSet constraintSet = new ConstraintSet();

        Toolbar topToolBar = new Toolbar(this);
        topToolBar.setId(View.generateViewId());
        Toolbar.LayoutParams topToolBarParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
        topToolBar.setLayoutParams(topToolBarParams);
        topToolBar.setBackgroundColor(Color.parseColor("#E0A42B"));
        constraintSet.connect(topToolBar.getId(), ConstraintSet.TOP, R.id.parentLayout, ConstraintSet.TOP);
        constraintSet.connect(topToolBar.getId(), ConstraintSet.LEFT, R.id.parentLayout, ConstraintSet.LEFT);
        constraintSet.connect(topToolBar.getId(), ConstraintSet.RIGHT, R.id.parentLayout, ConstraintSet.RIGHT);

        addArticleButton = new ImageButton(this);
        addArticleButton.setId(View.generateViewId());
        ViewGroup.LayoutParams addArticleButtonParams = new ViewGroup.LayoutParams(50,50);
        addArticleButton.setLayoutParams(addArticleButtonParams);
        addArticleButton.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        addArticleButton.setImageResource(R.drawable.add_circle);
        constraintSet.connect(addArticleButton.getId(), ConstraintSet.BOTTOM, topToolBar.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(addArticleButton.getId(), ConstraintSet.RIGHT, topToolBar.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(addArticleButton.getId(), ConstraintSet.TOP, topToolBar.getId(), ConstraintSet.TOP);

        logOutButton = new ImageButton(this);
        logOutButton.setId(View.generateViewId());
        ViewGroup.LayoutParams logOutButtonParams = new ViewGroup.LayoutParams(50,50);
        logOutButton.setLayoutParams(logOutButtonParams);
        logOutButton.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        logOutButton.setImageResource(R.drawable.logout);
        constraintSet.connect(logOutButton.getId(), ConstraintSet.BOTTOM, topToolBar.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(logOutButton.getId(), ConstraintSet.LEFT, topToolBar.getId(), ConstraintSet.LEFT);
        constraintSet.connect(logOutButton.getId(), ConstraintSet.TOP, topToolBar.getId(), ConstraintSet.TOP);

        TextView toolBarText = new TextView(this);
        toolBarText.setId(View.generateViewId());
        toolBarText.setTextSize(30);
        toolBarText.setText(R.string.app_name);
        toolBarText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        toolBarText.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface customFont = ResourcesCompat.getFont(this, R.font.marmelad);
        toolBarText.setTypeface(customFont);
        ViewGroup.LayoutParams toolBarTextParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        toolBarText.setLayoutParams(toolBarTextParams);
        constraintSet.connect(toolBarText.getId(), ConstraintSet.BOTTOM, topToolBar.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(toolBarText.getId(), ConstraintSet.RIGHT, logOutButton.getId(), ConstraintSet.LEFT);
        constraintSet.connect(toolBarText.getId(), ConstraintSet.LEFT, addArticleButton.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(toolBarText.getId(), ConstraintSet.TOP, topToolBar.getId(), ConstraintSet.TOP);

        constraintSet.applyTo(parentLayout);
        parentLayout.addView(topToolBar);
        parentLayout.addView(addArticleButton);
        parentLayout.addView(logOutButton);
        parentLayout.addView(toolBarText);
    }
}