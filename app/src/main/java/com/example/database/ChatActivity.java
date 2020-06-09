package com.example.database;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;

    MyAdapter mAdapter;

    EditText etText;
    Button btnFinish, btnSend;

    String Email,stUid;
    String stEmail, stText;

    ArrayList<Chat> chatArrayList;

    String gps;
    FirebaseUser user;
    String myemail;
    private FirebaseAuth mAuth;
    ConstraintLayout mLayout;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //SHaredPreferences을 사용하여 key값과 email,userEmail값을 가져오기
        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
        gps=  sharedPref.getString("gps", "");
        stUid = sharedPref.getString("key", "");
        etText = (EditText)findViewById(R.id.etText);
        myemail=sharedPref.getString("email","");
        stEmail = getIntent().getStringExtra("userEmail");

        chatArrayList = new ArrayList<>();

        btnFinish = (Button)findViewById(R.id.btnFinish);
        //finish버튼을 눌렀을 때 종료
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        mLayout = (ConstraintLayout) findViewById(R.id.mLayout);



        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] myDataset = {"test1", "test2", "test3", "test4"};

        //내 아이디에서 보낼 상대 아이디
        mAdapter = new MyAdapter(chatArrayList,myemail+"->"+stEmail, ChatActivity.this);

        recyclerView.setAdapter(mAdapter);

        database = FirebaseDatabase.getInstance();

        //파이어베이스에서 가입된 이용자 이메일 가져오기(유저 정보)
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {

            stEmail = user.getEmail();

        }



        //userEmail받아오기
        Email = getIntent().getStringExtra("userEmail");
        btnSend = (Button)findViewById(R.id.btnSend);
        //btnSend를 누를시
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stText = etText.getText().toString();
                if(stText.isEmpty() || stText.equals("")){
                    Toast.makeText(ChatActivity.this, "Please insert Message", Toast.LENGTH_LONG).show();
                    return;
                } else{
                    //보낸 시간을 알기 위하여
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String datetime = dateFormat.format(c.getTime());

                    //users 아래 message 아래 datatime 아래 송수신자와 보낸 내용 저장
                    DatabaseReference myRef = database.getReference("users").child("message").child(datetime);
                    Hashtable<String, String> message = new Hashtable<String, String>();


                    message.put("email", stEmail+"->"+Email);
                    message.put("text", stText);
                    myRef.setValue(message);
                    //텍스트를 비어둔다.
                    etText.setText("");
                }
            }
        });


        //데이터를 읽고 쓰기 위한 DatabaseReference 가져오기
        DatabaseReference myRef = database.getReference("users");
        //gps아래 자신의 아이디 밑에서 정보가져오기
        myRef.child(gps).child(stUid).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String color=dataSnapshot.getValue().toString();

                if(color.equals("yellow")){
                    mLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.yellow));
                }
                if(color.equals("blue")){
                    mLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                }
                if(color.equals("green")){
                    mLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                }
                if(color.equals("white")){
                    mLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                }
                if(color.equals("purple")){
                    mLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.purple));
                }
                if(color.equals("pink")){
                    mLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.pink));
                }




            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());





            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ChatActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        myRef.child("message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());


                Chat chat = dataSnapshot.getValue(Chat.class);
                //userPhoto정보 받아오기
                final String stPhoto = getIntent().getStringExtra("userPhoto");
                //chat setPhoto에 유아엘 set하기
                chat.setPhoto(stPhoto);

                //getEmail,getText를 이용하여 이메일과 내용을 받아오기
                String STEmail = chat.getEmail();
                String stText = chat.getText();
                //자신이 상대에게 상대가 자신에게 온 것이 맞는지 판단
                if(STEmail.equals(myemail+"->"+Email)||STEmail.equals(Email+"->"+myemail)){

//                chatArrayList.add(dataSnapshot.getKey()); //키값을 가져오는 것
                    chatArrayList.add(chat);
                    recyclerView.scrollToPosition(chatArrayList.size() - 1);
                    //채팅 시작할 때 마지막 채팅에 화면 배치, position이 0부터 시작이므로 -1을 해야함
                    mAdapter.notifyItemInserted(chatArrayList.size() - 1);

                }

                etText.setText("");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ChatActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}