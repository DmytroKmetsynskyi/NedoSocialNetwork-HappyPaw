package com.fernfog.happypaw;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.osmdroid.views.MapView;

import java.util.HashMap;
import java.util.Map;

public class CreateArticle extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://nedosocialnewtork.appspot.com");
    StorageReference storageReference = storage.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static final int PICK_IMAGE_FILE = 1;
    ImageView mImage;
    MaterialButton chooseFileButton;
    MaterialButton submitButton;
    MaterialButton expandButton;
    ImageButton backToMainScreenButton;
    EditText shortDescription;
    Uri image;
    private MapView map = null;
    Location userLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);

        try {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            mAuth.getCurrentUser().reload();

            mImage = findViewById(R.id.ImageView);
            chooseFileButton = findViewById(R.id.chooseImageFileButton);
            submitButton = findViewById(R.id.submitArticleButton);
            backToMainScreenButton = findViewById(R.id.arBackToMainButton);
            shortDescription = findViewById(R.id.addShortDescription);

            expandButton = findViewById(R.id.expandMapButton);

            /*map = findViewById(R.id.map);*/

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            backToMainScreenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(CreateArticle.this, Main.class);
                    startActivity(mIntent);

                    finish();
                }
            });

            chooseFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFile();
                }
            });




            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StorageReference imageRef = storageReference.child("images/"+image.getLastPathSegment());
                    imageRef.putFile(image);

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    String currentDate = year + "." + month + "." + day;

                    Map<String, Object> data = new HashMap<>();
                    data.put("image", imageRef.getName());
                    data.put("shortDescription", shortDescription.getText().toString());
                    data.put("user" , mUser.getEmail());
                    data.put("status", false);
                    data.put("latitudeOfAnimal", userLocation.getLatitude());
                    data.put("longitudeOfAnimal", userLocation.getLongitude());
                    data.put("date", currentDate);

                    // Toast.makeText(getApplicationContext(), "Latitude: " + userLocation.getLatitude() + " Longitude: " + userLocation.getLongitude(), Toast.LENGTH_LONG).show();

                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if (currentUser == null) {

                    } else {
                        db.collection("articles").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(v.getContext(), "Success", Toast.LENGTH_LONG).show();
                            }
                        });
                    }


                }
            });

            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFullScreenMapDialog();
                }
            });



            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            userLocation = location;

                        /*com.fernfog.happypaw.Map mMap = new com.fernfog.happypaw.Map());

                        mMap.initMap();*/
                        } else {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            finish();
                        }
                    }
                });

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
                finish();
            }
        } catch (Exception e) {

        }
    }

    private void showFullScreenMapDialog() {
        FullScreenMapDialogFragment dialogFragment = new FullScreenMapDialogFragment(userLocation.getLatitude(), userLocation.getLongitude(), getResources().getDrawable(R.drawable.locationicon));
        dialogFragment.show(getSupportFragmentManager(), "FullScreenMapDialog");
    }

    public void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
        startActivityForResult(intent, PICK_IMAGE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                image = data.getData();
                Glide.with(getApplicationContext()).load(data.getData()).into(mImage);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}