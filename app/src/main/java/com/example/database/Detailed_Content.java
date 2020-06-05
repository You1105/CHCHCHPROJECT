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
    String chatemail="";
    String chatkey="";
    String s;


    TextView title,text;
    ImageView imageView;
    Button chatbutton;
    TextView chatid;
    String gps;
    String stUid;
    Spinner spinner;
    DatabaseReference databaseReference;

    @Override
    protected void onStart(){
        super.onStart();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed__content);
        //SharedPreferences를 사용하여 gps,key값 가져오기
        SharedPreferences sharedPref =getSharedPreferences("shared" , Context.MODE_PRIVATE);

        gps=sharedPref.getString("gps", "");
        stUid = sharedPref.getString("key", "");


        //넘겨줬던 정보 받아오기
        Bundle extras = getIntent().getExtras();
        tit = extras.getString("tit");
        tex = extras.getString("tex");
        image = extras.getString("image");
        chatemail=extras.getString("chatid");
        chatkey=extras.getString("chatkey");


        chatbutton=(Button)findViewById(R.id.chatbutton);
        chatid=(TextView)findViewById(R.id.chatid);
        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.imageView2);

        //받아온 정보 변수에 넘겨주기
        s = tit;
        final String t = tex;
        final String r = image;
        final String c=cate;
        final String chat=chatemail;


        title.setText(s);
        text.setText(t);
        chatid.setText(chat);


        databaseReference=FirebaseDatabase.getInstance().getReference("users").child(gps);

        //이미지 넣어주기
        Glide.with(this).load(r).into(imageView);

        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼클릭시 채팅화면으로 넘어가기
                //chatkey값과 chatmail 값 공유하기
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("userUid", chatkey);
                intent.putExtra("userEmail", chatemail);
                startActivity(intent);



            }});
    }


}