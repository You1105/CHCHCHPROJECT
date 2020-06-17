package com.example.database;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class chatcolor extends Fragment {

    Button yellow,green,blue,white,purple,pink;
    String stUid,gps;
    Task<Void> databaseReference;

    public static chatcolor newInstance() {
        return new chatcolor();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.chatcolor_fragment, container, false);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared" , Context.MODE_PRIVATE);
        stUid = sharedPref.getString("key", "");
        gps=sharedPref.getString("gps","");
        yellow=(RadioButton)root.findViewById(R.id.yellow);
        purple=(RadioButton)root.findViewById(R.id.purple);
        green=(RadioButton)root.findViewById(R.id.green);
        pink=(RadioButton)root.findViewById(R.id.pink);
        blue=(RadioButton)root.findViewById(R.id.blue);
        white=(RadioButton)root.findViewById(R.id.white);


        //노란색을 클릭했을 때
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(gps).child(stUid).child("color").setValue("yellow");
                Toast.makeText(getContext().getApplicationContext(),"yellow컬러 설정되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
        //초록색을 클릭했을 때
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(gps).child(stUid).child("color").setValue("green");
                Toast.makeText(getContext().getApplicationContext(),"green컬러 설정되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
        //분홍색을 클릭했을 때
        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(gps).child(stUid).child("color").setValue("pink");
                Toast.makeText(getContext().getApplicationContext(),"pink컬러 설정되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
        //보라색을 클릭했을 때
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(gps).child(stUid).child("color").setValue("purple");
                Toast.makeText(getContext().getApplicationContext(),"purple컬러 설정되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
        //파란색을 클릭했을 때
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(gps).child(stUid).child("color").setValue("blue");
                Toast.makeText(getContext().getApplicationContext(),"blue컬러 설정되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
        //하얀색을 클릭했을 때
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(gps).child(stUid).child("color").setValue("white");
                Toast.makeText(getContext().getApplicationContext(),"white컬러 설정되었습니다.", Toast.LENGTH_LONG).show();
            }
        });

        return  root;
    }

}