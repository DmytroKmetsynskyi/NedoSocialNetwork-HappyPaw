package com.fernfog.happypaw;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class ListFragment extends Fragment {

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://nedosocialnewtork.appspot.com");
    StorageReference storageReference = storage.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LinearLayout parentLayout;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = new View(getContext());

        try {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            mAuth.getCurrentUser().reload();

            FirebaseUser currentUser = mAuth.getCurrentUser();

            view = inflater.inflate(R.layout.fragment_list, container, false);

            parentLayout = view.findViewById(R.id.parentLayout);

            if (currentUser == null) {

            } else {
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
            }
        } catch (Exception e) {

        }

        return view;
    }

    public void addCardToView(String shortText, String image, LinearLayout parentLayout, double latitude, double longitude, String emailText_, String date) {
        CardView mCard = new CardView(getContext());
        LinearLayout.LayoutParams mCardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        mCardParams.setMargins(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
        mCard.setLayoutParams(mCardParams);
        mCard.setRadius(dpToPx(22));
        mCard.setCardElevation(dpToPx(4));
        mCard.setContentPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        LinearLayout insideCardLayout = new LinearLayout(getContext());
        insideCardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        insideCardLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView mImageView = new ImageView(getContext());
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
                if (getActivity() != null) {
                    Glide.with(getActivity()).load(uri).into(mImageView);
                }
            }
        });


        TextView mText = new TextView(getContext());
        mText.setTextSize(dpToPx(8));
        mText.setText(shortText);
        Typeface customFont = ResourcesCompat.getFont(getContext(), R.font.overpass_light);
        mText.setTypeface(customFont);
        mText.setGravity(Gravity.CENTER);

        MaterialButton mButton = new MaterialButton(getContext());
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
        mButton.setIcon(getContext().getDrawable(R.drawable.mapicon));
        mButton.setIconGravity(MaterialButton.ICON_GRAVITY_END);
        mButton.setCornerRadius(dpToPx(8));
        buttonParams.gravity = Gravity.CENTER;
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFullScreenMapDialog(latitude, longitude);
            }
        });

        TextView emailText = new TextView(getContext());
        emailText.setTextColor(Color.parseColor("#777a80"));
        emailText.setTextSize(dpToPx(5));
        emailText.setText(emailText_);
        emailText.setTypeface(customFont);
        emailText.setGravity(Gravity.LEFT);

        TextView dateText = new TextView(getContext());
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
        dialogFragment.show(getActivity().getSupportFragmentManager(), "FullScreenMapDialog");
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}