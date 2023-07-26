package com.fernfog.happypaw;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class CreateArticle extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://nedosocialnewtork.appspot.com");

    StorageReference storageReference = storage.getReference();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    public static final int PICK_IMAGE_FILE = 1;

    ImageView mImage;
    MaterialButton chooseFileButton;
    Button submitButton;
    ImageButton backToMainScreenButton;

    EditText shortDescription;

    Uri image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);

        mImage = findViewById(R.id.ImageView);
        chooseFileButton = findViewById(R.id.chooseImageFileButton);
        submitButton = findViewById(R.id.submitArticleButton);
        backToMainScreenButton = findViewById(R.id.arBackToMainButton);
        shortDescription = findViewById(R.id.addShortDescription);

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

                Map<String, String> data = new HashMap<>();
                data.put("image", imageRef.getName());
                data.put("shortDescription", shortDescription.getText().toString());
                data.put("user" , mUser.getEmail());

                db.collection("articles").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(v.getContext(), "Success", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimetypes = {"image/jpeg", "image/png", "image/jpg"};
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);

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