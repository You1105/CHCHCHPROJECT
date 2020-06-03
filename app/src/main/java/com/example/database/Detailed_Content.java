package com.example.database;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Detailed_Content extends AppCompatActivity {

    String tit = "";
    String tex = "";
    String image = "";
    String cate="";

    TextView title,text;
    ImageView imageView;
    Button chatbutton;
    TextView chatid;
    String gps;
    String stUid;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed__content);

        //SharedPrefernces를 사용하여 "shared" 저장된 데이터 정보 받아오기
        SharedPreferences sharedPref =getSharedPreferences("shared" , Context.MODE_PRIVATE);
        //키 값이 gps인 값 가져오기
        gps=sharedPref.getString("gps", "");
        // 키값이 "key"인 값 가져오기
        stUid = sharedPref.getString("key", "");

        chatbutton=(Button)findViewById(R.id.chatbutton);
        chatid=(TextView)findViewById(R.id.chatid);
        spinner=(Spinner)findViewById(R.id.spinner2);

        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.deal, android.R.layout.simple_spinner_item);
        spinner.setAdapter(yearAdapter);
        //RecyclerView Adapter에 있는 정보를 받아오기
        Bundle extras = getIntent().getExtras();
        // 키 값이 tit인 값 가져오기
        tit = extras.getString("tit");
        // 키 값이 tit인 값 가져오기
        tex = extras.getString("tex");
        // 키 값이 image인 값 가져오기
        image = extras.getString("image");




        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);


        imageView = (ImageView) findViewById(R.id.imageView2);
        //받아온 데이터를 변수에 지정하기
        final String s = tit;
        final String t = tex;
        final String r = image;
        final String c=cate;



        title.setText(s);
        text.setText(t);


        //이미지URL 받아온 것을 GLide을 이용하여 보여주기
        Glide.with(this).load(r).into(imageView);


    }


}