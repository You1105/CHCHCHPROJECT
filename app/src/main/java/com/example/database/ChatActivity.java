package com.example.database;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

    String stEmail, stText;
    String stChatId;
    ArrayList<Chat> chatArrayList;

    FirebaseUser user;
    private FirebaseAuth mAuth;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etText = (EditText)findViewById(R.id.etText);

        stEmail = getIntent().getStringExtra("userEmail");
        stChatId = getIntent().getStringExtra("userUid");
        chatArrayList = new ArrayList<>();

        btnFinish = (Button)findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        //리사이클러뷰에 LinearLayoutManager 객체 지정
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] myDataset = {"test1", "test2", "test3", "test4"};

        // specify an adapter (see also next example)
        //리사이클러뷰에 MyAdapter 객체 지정
        mAdapter = new MyAdapter(chatArrayList, stEmail, ChatActivity.this);
        recyclerView.setAdapter(mAdapter);

        database = FirebaseDatabase.getInstance();

        //파이어베이스에서 가입된 이용자 이메일 가져오기(유저 정보)
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        //유저가 null이 아니면 이메일 값을 가져오기
        if (user != null) {
            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
            stEmail = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
//            String uid = user.getUid();
        }

        //메시지 보내는 버튼 이벤트
        btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stText = etText.getText().toString();

                //메시지를 보내는 칸이 비었는지 아닌지 확인
                if(stText.isEmpty() || stText.equals("")){
                    Toast.makeText(ChatActivity.this, "Please insert Message", Toast.LENGTH_LONG).show();
                    return;
                } else{
//                    Toast.makeText(ChatActivity.this, "email: " + stEmail + ", msg: " + stText, Toast.LENGTH_LONG).show();

                    //현재 날짜 및 시간 가져오기
                    //데이터베이스에서 날짜와 시간 순서로 메시지 정렬
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String datetime = dateFormat.format(c.getTime());

                    // Write a message to the database
                    //데이터를 읽고 쓰기 위한 DatabaseReference 가져오기
                    //순서의 일관성을 위해서 push가 아닌 시간(datetime)을 사용
                    DatabaseReference myRef = database.getReference("users").child(stChatId).child("message").child(datetime);
//                    DatabaseReference myRef = database.getReference("users").child(user.getUid()).child("message").child(datetime);
                    //Hashtable 객체 생성
                    Hashtable<String, String> message = new Hashtable<String, String>();
                    message.put("email", stEmail); //put(): 데이터 삽입
                    message.put("text", stText); //put(): 데이터 삽입
                    myRef.setValue(message); //setValue()를 사용하여 지정된 위치에서 데이터를 덮어씀

                    etText.setText("");
                }
            }
        });

        //데이터를 읽고 쓰기 위한 DatabaseReference 가져오기
        DatabaseReference myRef = database.getReference("users").child(stChatId).child("message");
//        DatabaseReference myRef = database.getReference("users").child(user.getUid()).child("message");
        //하위 이벤트 수신 대기
        //DatabaseReference의 하위 이벤트를 수신 대기하려면 ChildEventListener를 연결해야 함
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            //onChildAdded(): 항목 목록을 검색하거나 항목 목록에 대한 추가를 수신 대기
            //리스너에 전달된 DataSnapshot 코드는 새 하위 데이터를 포함
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                //새 설명이 추가됨, 표시된 목록에 추가
                Chat chat = dataSnapshot.getValue(Chat.class);

                String stEmail = chat.getEmail();
                String stText = chat.getText();
                Log.d(TAG, "stEmail: " + stEmail);
                Log.d(TAG, "stText: " + stText);

                // [START_EXCLUDE]
                // Update RecyclerView
//                chatArrayList.add(dataSnapshot.getKey()); //키값을 가져오는 것
                chatArrayList.add(chat);
                recyclerView.scrollToPosition(chatArrayList.size() - 1); //채팅 시작할 때 마지막 채팅에 화면 배치, position이 0부터 시작이므로 -1을 해야함
                mAdapter.notifyItemInserted(chatArrayList.size() - 1); //특정 position에 데어터를 추가했을 경우 반영
                // [END_EXCLUDE]

                //메시지를 보내고 나서 빈 칸으로 만들어주기
                etText.setText("");
            }

            //onChildChanged(): 목록의 항목에 대한 변경사항을 수신 대기
            //이벤트 리스너에 전달되는 DataSnapshot에는 하위 항목의 업데이트된 데이터가 포함됨
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Chat chat = dataSnapshot.getValue(Chat.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            //onChildRemoved(): 목록의 항목 삭제를 수신 대기
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            //onChildMoved(): 순서가 지정된 목록의 항목 순서 변경사항을 수신대기
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.

                // ...
            }

            //에러가 났을 경우 실패 메시지를 띄운다.
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ChatActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}

