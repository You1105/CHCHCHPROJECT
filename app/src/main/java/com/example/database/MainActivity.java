package com.example.database;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.database.tab.ProfileFragment;
import com.example.database.tab.RealHomeFragment;
import com.example.database.tab.SaveFragment;
import com.example.database.tab.UsersFragment;
import com.example.database.tab.WritingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    int Image_Request_Code = 7;
    Button ChooseButton, UploadButton;
    private FragmentManager fm;
    private FragmentTransaction ft;
    Uri FilePathUri;

    private AppBarConfiguration mAppBarConfiguration;

    //바텀네비게이션
    //FrameLayout에 각 메뉴의 Fragment를 바꿔줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    //5개의 메뉴에 들어갈 Fragment들
    private UsersFragment usersFragment = new UsersFragment();
    private SaveFragment saveFragment = new SaveFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private RealHomeFragment realHomeFragment = new RealHomeFragment();
    private WritingFragment writingFragment = new WritingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChooseButton = (Button)findViewById(R.id.ButtonChooseImage);
        UploadButton = (Button)findViewById(R.id.ButtonUploadImage);

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


        //BottomNavigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        //첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, realHomeFragment).commitAllowingStateLoss();

        //bottomNavigationView의 아이템이 선택될 때 호출된 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                //switch~case문으로 프래그먼트 교체
                switch (item.getItemId()){
                    case R.id.chat:
                        transaction.replace(R.id.nav_host_fragment, usersFragment).commitAllowingStateLoss();
                        break;
                    case R.id.save:
                        transaction.replace(R.id.nav_host_fragment, saveFragment).commitAllowingStateLoss();
                        break;
                    case R.id.profile:
                        transaction.replace(R.id.nav_host_fragment, profileFragment).commitAllowingStateLoss();
                        break;
                    case R.id.realhome:
                        transaction.replace(R.id.nav_host_fragment, realHomeFragment).commitAllowingStateLoss();
                        break;
                    case R.id.writing:
                        transaction.replace(R.id.nav_host_fragment, writingFragment).commitAllowingStateLoss();
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
