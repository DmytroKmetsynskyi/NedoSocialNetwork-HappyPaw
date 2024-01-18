package com.fernfog.happypaw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Main extends AppCompatActivity {

    ImageButton logOutButton;
    ImageButton addArticleButton;

    TabLayout tabLayout;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logOutButton = findViewById(R.id.logOutButton);
        addArticleButton = findViewById(R.id.addArticleButton);


        tabLayout = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frame_layout);

        tabLayout.getTabAt(0).select();
        replaceFragment(new ListFragment());

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        replaceFragment(new ListFragment());
                        break;
                    case 1:
                        replaceFragment(new AnalyzeFragment());
                        break;
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        replaceFragment(new ListFragment());
                        break;
                    case 1:
                        replaceFragment(new AnalyzeFragment());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
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

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}
