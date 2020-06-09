package com.example.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StarActivity extends AppCompatActivity {
    private RatingBar mRatingVote;
    private Button sendbt;
    private TextView textaverage;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();
    String stEmail, Uid,gps,rating, mykey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);
        mRatingVote = (RatingBar) findViewById(R.id.ratingVote);
        listView = (ListView) findViewById(R.id.listviewstar);
        //userUid값 가져오기
        Uid = getIntent().getStringExtra("userUid");

        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
        mykey=  sharedPref.getString("key", "");

        initDatabase();



        //저장할 데이터 베이스
        mReference = mDatabase.getReference("youRating").child(Uid);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageData : dataSnapshot.getChildren()) {

                    //값 가져와서 변수에 넣기
                    String msg2 = messageData.getValue().toString();


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();



        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    //디비 저장 버튼 클릭했을 때
    public void mOnClick(View v) {
        switch (v.getId()) {

            case R.id.btnDB:
                //자신의 별점인지 아닌지 구분
                //자신의 별점일 때
                if(mykey.equals(Uid)){
                    Toast.makeText(this,"자신이 자신의 별점 추가는 불가합니다.", Toast.LENGTH_LONG).show();
                }
                //자신의 별점이 아닐 때
                else{
                    //youRating데이터베이스에 지정된 별점 수 만큼 저장하기
                    databaseReference.child("youRating").child(Uid).push().setValue( mRatingVote.getRating());
                    Toast.makeText(this,mRatingVote.getRating()+"별점이 추가되었습니다.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}