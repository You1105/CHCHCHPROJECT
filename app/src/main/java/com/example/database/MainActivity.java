package com.example.database;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.database.tab.ChatFragment;
import com.example.database.tab.ProfileFragment;
import com.example.database.tab.RealHomeFragment;
import com.example.database.tab.SaveFragment;
import com.example.database.tab.WritingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    int Image_Request_Code = 7;
    Button ChooseButton, UploadButton;
    private FragmentManager fm;
    private FragmentTransaction ft;
    Uri FilePathUri;
    private RealHomeFragment fragHome;
    private ChatFragment fragChat;
    private ProfileFragment fragProfile;
    private SaveFragment fragSave;
    private WritingFragment fragWriting;
    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChooseButton = (Button)findViewById(R.id.ButtonChooseImage);
        UploadButton = (Button)findViewById(R.id.ButtonUploadImage);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.realhome:
                        setFrag(0);
                        break;
                    case R.id.chat:
                        setFrag(1);
                        break;
                    case R.id.profile:
                        setFrag(2);
                        break;
                    case R.id.save:
                        setFrag(3);
                        break;
                    case R.id.writing:
                        setFrag(4);
                        break;
                }
                return true;
            }
        });

        fragHome = new RealHomeFragment();
        fragChat = new ChatFragment();
        fragProfile = new ProfileFragment();
        fragSave = new SaveFragment();
        fragWriting = new WritingFragment();

        setFrag(0); //첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_toy,R.id.nav_clothes,R.id.nav_donation,R.id.nav_machine,R.id.nav_rental,R.id.nav_stationery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    //프래그먼트 교체가 일어나는 실행문
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (n) {
            case 0:
                ft.replace(R.id.main_frame,fragHome);
                ft.commit(); //저장의미
                break;
            case 1:
                ft.replace(R.id.main_frame, fragChat);
                ft.commit(); //저장의미
                break;
            case 2:
                ft.replace(R.id.main_frame,fragProfile);
                ft.commit(); //저장의미
                break;
            case 3:
                ft.replace(R.id.main_frame,fragSave);
                ft.commit(); //저장의미
                break;
            case 4:

               ft.replace(R.id.main_frame, fragWriting );
                ft.commit(); //저장의미
                break;
        }

    }
}
