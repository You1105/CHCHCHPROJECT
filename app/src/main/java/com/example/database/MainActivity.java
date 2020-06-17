package com.example.database;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;

    long lastPressed;


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

        MobileAds.initialize(this, getString(R.string.admob_app_id));



        mAdView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);




        // 광고가 제대로 로드 되는지 테스트 하기 위한 코드입니다.

        mAdView.setAdListener(new AdListener() {

            @Override

            public void onAdLoaded() {

                // Code to be executed when an ad finishes loading.

                // 광고가 문제 없이 로드시 출력됩니다.

                Log.d("@@@", "onAdLoaded");

            }



            @Override

            public void onAdFailedToLoad(int errorCode) {

                // Code to be executed when an ad request fails.

                // 광고 로드에 문제가 있을시 출력됩니다.

                Log.d("@@@", "onAdFailedToLoad " + errorCode);

            }



            @Override

            public void onAdOpened() {

                // Code to be executed when an ad opens an overlay that

                // covers the screen.

            }



            @Override

            public void onAdClicked() {

                // Code to be executed when the user clicks on an ad.

            }



            @Override

            public void onAdLeftApplication() {

                // Code to be executed when the user has left the app.

            }



            @Override

            public void onAdClosed() {

                // Code to be executed when the user is about to return

                // to the app after tapping on an ad.

            }

        });


        ChooseButton = (Button)findViewById(R.id.ButtonChooseImage);
        UploadButton = (Button)findViewById(R.id.ButtonUploadImage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_toy, R.id.nav_clothes, R.id.nav_donation, R.id.nav_machine, R.id.nav_rental, R.id.nav_stationery)
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

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //뒤로가기 두 번 눌렀을 때 앱 종료
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastPressed < 1500){
            ActivityCompat.finishAffinity(this);
        }
        Toast.makeText(MainActivity.this, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        lastPressed = System.currentTimeMillis();
    }

}
