package com.example.database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Detailed_Content extends AppCompatActivity {

    String tit = "";
    String tex = "";
    String image = "";
    String cate="";
    String chatemail="";
    String chatkey="";
    String s;
    float star;


    TextView title,text;
    ImageView imageView;
    Button chatbutton;
    TextView chatid;
    int count;
    String gps;
    String stUid;
    private RatingBar mRatingVote;
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
        mRatingVote = (RatingBar) findViewById(R.id.ratingVote);

        //받아온 정보 변수에 넘겨주기
        s = tit;
        final String t = tex;
        final String r = image;
        final String c=cate;
        final String chat=chatemail;


        title.setText(s);
        text.setText(t);
        chatid.setText("작성자: " + chat);


        //youRating아래 그 페이지 key값아래
        databaseReference=FirebaseDatabase.getInstance().getReference("youRating").child(chatkey);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageData : dataSnapshot.getChildren()) {

                    //key 값 가져오기
                    String key=messageData.getKey();
                    Log.d("dsagag",key);
                    String msg2 = messageData.getValue().toString();
                    Boolean found;
                    //key에 -가 있는지 확인
                    found=key.contains("-");
                    //-가 있다면 실행
                    if(found==true){
                        //key에 -있는 만큼 숫자 세기
                        count++;
                    }
                    //String을 float형으로 변형
                    float l = Float.valueOf(msg2).floatValue();
                    //모든 별점 합계
                    star=star+l;
                    //평균을 구하기
                    float avgstar=star/count;
                    Log.d("adgag",avgstar+"");
                    //별점 부가하기
                    mRatingVote.setRating(avgstar);
                    //리스트 추가하기



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //이미지 넣어주기
        Glide.with(this).load(r).into(imageView);

        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼클릭시 채팅화면으로 넘어가기
                //chatkey값과 chatmail 값 공유하기
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("userUid", chatkey);
                intent.putExtra("userEmail", chatemail);
                startActivity(intent);



            }});
    }


}