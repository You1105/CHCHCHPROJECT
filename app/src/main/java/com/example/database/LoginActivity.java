package com.example.database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private AdView mAdView;
    long lastPressed;

    private static final String TAG = "LoginActivity";

    String stEmail, stPassword;
    EditText etEmail, etPassword;
    ProgressBar pb;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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


        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        pb = (ProgressBar) findViewById(R.id.pb);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        //btnRegister 버튼 누르면 RegisterActivity화면으로 가기
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(in);
            }
        });

        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        //로그인 버튼을 눌렀을 때
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = etEmail.getText().toString();
                stPassword = etPassword.getText().toString();
                //stEmail 비어있거든 ""일때
                if(stEmail.isEmpty() || stEmail.equals("")){
                    Toast.makeText(LoginActivity.this, "이메일을 입력하세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                //stPasswort 비어있거든 ""일때
                if(stPassword.isEmpty() || stPassword.equals("")){
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                pb.setVisibility(View.VISIBLE);

                //사용자가 앱에 로그인하면 사용자의 이메일 주소와 비밀번호를 signInWithEmailAndPassword에 전달한다.
                mAuth.signInWithEmailAndPassword(stEmail, stPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                pb.setVisibility(View.GONE);

                                if (task.isSuccessful()) {

                                    Toast.makeText(LoginActivity.this, "Authentication successed.", Toast.LENGTH_SHORT).show();

                                    FirebaseUser user = mAuth.getCurrentUser();

                                    String stUserName = user.getDisplayName();
                                    String stUserEmail = user.getEmail();
                                    String stUid = user.getUid();

                                    Log.d(TAG, "stUserName: " + stUserName + ", stUserEmail: " + stUserEmail + ", stUid: " + stUid);

                                    //SharedPreferences는 키-값 쌍을 포함하는 파일을 가리키며 키-값을 쌍을 읽고 쓸 수 있도록 한다.
                                    SharedPreferences sharedPref = getSharedPreferences("shared" , Context.MODE_PRIVATE);
                                    //공유 환경설정 파일을 쓰기 위해 SharedPreferences에서 edit() 호출
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("email", stUserEmail); //putString()으로 쓰려고 하는 키 및 값을 전달
                                    editor.putString("key", stUid); //putString()으로 쓰려고 하는 키 및 값을 전달
                                    editor.commit(); //commit()을 호출하여 변경사항을 저장한다.

                                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                                    //MainActivity로 email값을 전달
                                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                                    in.putExtra("email", stEmail);
                                    startActivity(in);
                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "로그인 실패, 다시 시도하세요.", Toast.LENGTH_SHORT).show();

                                }


                            }
                        });
            }
        });

    }

    //활동을 초기화할 때 사용자가 현재 로그인되어 있는지 확인
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    //뒤로가기 두 번 눌렀을 때 앱 종료
    @Override
    public void onBackPressed() {
        //뒤로가기 버튼 두 번 누르는 속도가 1500보다 빠를 때 앱 종료
        if(System.currentTimeMillis() - lastPressed < 1500){
            ActivityCompat.finishAffinity(this);
        }
        Toast.makeText(LoginActivity.this, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        lastPressed = System.currentTimeMillis();
    }

}
