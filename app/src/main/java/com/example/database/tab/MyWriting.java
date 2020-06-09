package com.example.database.tab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database.ImageUploadInfo;
import com.example.database.MyWritingAdapter;
import com.example.database.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyWriting extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ImageUploadInfo> list;

    MyWritingAdapter adapter ;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;
    String stUid,gps;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root =inflater.inflate(R.layout.my_writing_fragment, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        recyclerView=root.findViewById(R.id.recyclerView);
        //LoginAticity의 SharedPreferences를 통해 저장된 값을 불러오기 위해 같은 네임파일을 찾는다.
        //마이라이팅에서 필요한 정보: key, gps
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        stUid = sharedPref.getString("key", "");
        gps= sharedPref.getString("gps", "");


        recyclerView.setHasFixedSize(true);


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new MyWritingAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);


        //users 아래 gps아래 자신의 아이디 아래 imageupload 경로 따라 정보 받아오기
        databaseReference= FirebaseDatabase.getInstance().getReference("users").child(gps).child(stUid).child("imageupload");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {

                    ImageUploadInfo imageUploadInfo = datasnapshot.getValue(ImageUploadInfo.class);

                    //list에  imageuploadinfo 정보 추가하기
                    list.add(imageUploadInfo);
                    adapter.notifyItemInserted(list.size() - 1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
