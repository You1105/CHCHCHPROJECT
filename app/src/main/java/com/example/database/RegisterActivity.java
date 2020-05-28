package com.example.database;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;
import static com.example.database.SendMail.context;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener { long lastPressed;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    GpsTracker gpsTracker;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private static final String TAG = "RegisterActivity";

    String stEmail, stPassword,result;
    EditText etEmail, etPassword;
    ProgressBar pb;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    //email code
//button, edit text 선언
    private EditText editTextEmail;
    private EditText editEmailCode;
    private EditText editTextSubject;
    private EditText editTextMessage;
    //Send button
    private Button buttonSend;
    private  Button buttonSend2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializing the views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editEmailCode = (EditText) findViewById(R.id.editEmailCode);
        // editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        // editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        //클릭 리스너 추가
        buttonSend = (Button) findViewById(R.id.buttonSend);

        //Adding click listener
        buttonSend.setOnClickListener(this);
        buttonSend2 = (Button) findViewById(R.id.buttonSend2);
        buttonSend2.setOnClickListener(this);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        pb = (ProgressBar) findViewById(R.id.pb);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });
        final TextView textview_address = (TextView)findViewById(R.id.gpstext);


        Button  gpsbutton = (Button) findViewById(R.id.gpsbutton);
        gpsbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {

                gpsTracker = new GpsTracker(RegisterActivity.this);

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                String address = getCurrentAddress(latitude, longitude);
                textview_address.setText(address);
                String str = address;
                result = str.substring(5,str.lastIndexOf("시")+1);
                SharedPreferences sharedPref = getSharedPreferences("shared" , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putString("gps", result);
                editor.commit();




                Toast.makeText(RegisterActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
            }
        });
//회원가입



    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음

            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(RegisterActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(RegisterActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }
    void checkRunTimePermission(){


        // 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {


            //  위치 값을 가져올 수 있음



        } else {   //퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다.

            // 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(RegisterActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(RegisterActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(RegisterActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    public String getCurrentAddress( double latitude, double longitude) {

        //(지오코더) GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }
    //GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다."
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        //Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    //이메일 코드
    //이메일의 내용 가져오기
    private void sendEmail() {
        //Getting content for email
        String email = editTextEmail.getText().toString().trim();
        //String subject = editTextSubject.getText().toString().trim();
        // String message = editTextMessage.getText().toString().trim();
// SendMail 객체 만들기
        //Creating SendMail object
        SendMail sm = new SendMail(this, email,null,null);

        //Executing sendmail to send email
        sm.execute();

    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonSend){
            sendEmail();
            //이메일 보내기 버튼 눌렀을때 sendEmail()함수 실행
        }
        //번호 인증 버튼을 눌렀을 때 동작
        else if(v.getId()==R.id.buttonSend2){
            if(editEmailCode.getText().toString().equals(SendMail.emailCode)){
//임의로 보낸 랜덤 인증번호와 내가 입력한 인증 번호가 일치하면 성공띄움 이후 register과 연결하여 회원가입 성공
                Toast.makeText(context,"success",Toast.LENGTH_LONG).show();
                Button btnRegister = (Button)findViewById(R.id.btnRegister);
                btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        stEmail = etEmail.getText().toString();
                        stPassword = etPassword.getText().toString();
                        //이메일이 비었을 때나 패스워드가 비었을 때 회원가입이 되지 않음
                        if(stEmail.isEmpty() || stEmail.equals("")){
                            Toast.makeText(RegisterActivity.this, "Please insert Email", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(stPassword.isEmpty() || stPassword.equals("")){
                            Toast.makeText(RegisterActivity.this, "Please insert Password", Toast.LENGTH_LONG).show();
                            return;
                        }

                        pb.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(stEmail, stPassword)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        pb.setVisibility(View.GONE);

                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            Toast.makeText(RegisterActivity.this, "Authentication successed.", Toast.LENGTH_SHORT).show();

                                            FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);
                                            DatabaseReference myRef = database.getReference("users").child(result).child(user.getUid());

                                            Hashtable<String, String> members = new Hashtable<String, String>();
                                            members.put("email", user.getEmail());
                                            members.put("key", user.getUid());
                                            members.put("photo", "");
                                            members.put("alarm","no");


//                                    members.put("photo", user.getPhotoUrl());
//                                    myRef.child(user.getUid()).setValue(members);
                                            myRef.setValue(members);

                                            Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                        }

                                        // ...
                                    }
                                });

                    }
                });
            }
            else{
                //일치하지 않으면 실패 띄우고 회원가입 또한 진행되지 않음
                Toast.makeText(context,"fail",Toast.LENGTH_LONG).show();
            }
        }
    }
}