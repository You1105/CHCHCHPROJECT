package com.example.database;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    //    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    MyAdapter mAdapter;

    EditText etText;
    Button btnFinish, btnSend;

    String Email;
    String stEmail, stText;
    String stChatId;
    ArrayList<Chat> chatArrayList;

    String gps;
    FirebaseUser user;
    String myemail;
    private FirebaseAuth mAuth;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //SharedPreferences로 이메일 값 가져오기
        SharedPreferences sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE);
        myemail=sharedPref.getString("email","");

        etText = (EditText)findViewById(R.id.etText);

        Log.d("dgag",myemail);
        stEmail = getIntent().getStringExtra("userEmail");
        Log.d("gdag",stEmail);
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
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] myDataset = {"test1", "test2", "test3", "test4"};

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(chatArrayList, stEmail, ChatActivity.this);
        recyclerView.setAdapter(mAdapter);

        database = FirebaseDatabase.getInstance();

        //파이어베이스에서 가입된 이용자 이메일 가져오기(유저 정보)
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
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

        Intent in = getIntent();
        stChatId = in.getStringExtra("userUid");

        Email = getIntent().getStringExtra("userEmail");
        btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stText = etText.getText().toString();
                if(stText.isEmpty() || stText.equals("")){
                    Toast.makeText(ChatActivity.this, "Please insert Message", Toast.LENGTH_LONG).show();
                    return;
                } else{
//                    Toast.makeText(ChatActivity.this, "email: " + stEmail + ", msg: " + stText, Toast.LENGTH_LONG).show();

                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String datetime = dateFormat.format(c.getTime());

                    // Write a message to the database
                    DatabaseReference myRef = database.getReference("users").child("message").child(datetime);
//                    DatabaseReference myRef = database.getReference("users").child(user.getUid()).child("message").child(datetime); //순서의 일관성을 위해서 push가 아닌 시간(datetime)을 사용
                    Hashtable<String, String> message = new Hashtable<String, String>();

                    Log.d("gdag",Email);
                    message.put("email", stEmail+"->"+Email);
                    Log.d("emailag",stEmail);
                    message.put("text", stText);
                    myRef.setValue(message);

                    etText.setText("");
                }
            }
        });


        //데이터를 읽고 쓰기 위한 DatabaseReference 가져오기
        DatabaseReference myRef = database.getReference("users").child("message");
//        DatabaseReference myRef = database.getReference("users").child(user.getUid()).child("message");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                Chat chat = dataSnapshot.getValue(Chat.class);

                String STEmail = chat.getEmail();
                String stText = chat.getText();
                Log.d(TAG, "stEmail: " + STEmail);
                Log.d(TAG, "stText: " + stText);
                if(STEmail.equals(myemail+"->"+Email)||STEmail.equals(Email+"->"+myemail)){
                    Log.d("ddd",myemail+Email);

                    // [START_EXCLUDE]
                    // Update RecyclerView
//                chatArrayList.add(dataSnapshot.getKey()); //키값을 가져오는 것
                    chatArrayList.add(chat);
                    recyclerView.scrollToPosition(chatArrayList.size() - 1); //채팅 시작할 때 마지막 채팅에 화면 배치, position이 0부터 시작이므로 -1을 해야함
                    mAdapter.notifyItemInserted(chatArrayList.size() - 1);
                    // [END_EXCLUDE]
                }



                etText.setText("");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Chat chat = dataSnapshot.getValue(Chat.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.

                // ...
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
