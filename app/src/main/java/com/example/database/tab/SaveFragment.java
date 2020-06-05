package com.example.database.tab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database.ImageUploadInfo;
import com.example.database.MainActivity;
import com.example.database.R;
import com.example.database.RecyclerViewAdapter;
import com.example.database.jimmadapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SaveFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ImageUploadInfo> list;
    // Creating RecyclerView.Adapter.
    jimmadapter adapter ;
    DatabaseReference databaseReference;
    // Creating Progress dialog
    ProgressDialog progressDialog;
    String stUid,gps;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =inflater.inflate(R.layout.fragment_save, container, false);

        recyclerView=root.findViewById(R.id.recyclerView);

        //SharedPreferences를 사용하여 key 값과 gps값을 가져오기
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared" , Context.MODE_PRIVATE);
        stUid = sharedPref.getString("key", "");
        gps=sharedPref.getString("gps", "");


        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new jimmadapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
        //users아래 자신의 아이디 아래 jimm경로
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(stUid).child("jimm");

        //경로에서 있는 정보 받아오기
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    ImageUploadInfo imageUploadInfo = snapshot.getValue(ImageUploadInfo.class);

                    //리사이클러뷰에 추가하기
                    list.add(imageUploadInfo);
                    adapter.notifyItemInserted(list.size() - 1);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }


        });

        //bar이름을 Like로 지정하기
        ActionBar actionBar=((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Like");
        return root;
    }
}
