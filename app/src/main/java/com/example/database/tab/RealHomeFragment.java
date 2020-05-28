package com.example.database.tab;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database.ImageUploadInfo;
import com.example.database.MainActivity;
import com.example.database.R;
import com.example.database.RecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RealHomeFragment extends Fragment {
    // Creating RecyclerView.Adapter.

    Parcelable recyclerViewState;
    // Creating RecyclerView.
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ImageUploadInfo> list;
    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;
    DatabaseReference databaseReference;
    // Creating Progress dialog
    ProgressDialog progressDialog;
    String stUid;
    String key;
    String gps;
    private int someVarA;
    private String someVarB;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedinstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        final TextView textView = root.findViewById(R.id.text_home);
        // Creating RecyclerView.
        recyclerView=root.findViewById(R.id.recyclerView);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared" , Context.MODE_PRIVATE);
        stUid = sharedPref.getString("key", "");
        gps=sharedPref.getString("gps", "");

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new RecyclerViewAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);


        //users 밑 gps 경로 지정합니다.
        databaseReference= FirebaseDatabase.getInstance().getReference("users").child(gps);

        //Query를 사용하여 이름이 imageupload경로의 값에 따라 결과를 정렬합니다.
        final Query query=databaseReference.orderByChild("imageupload");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    key=postSnapshot.getKey();
                    //Query를 사용해서 가져온 모든 key값 아래 imageupload 아래의 정보를 가져옵니다.
                    databaseReference.child(key).child("imageupload").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot datasnapshot : dataSnapshot.getChildren()){
                                ImageUploadInfo imageUploadInfo = datasnapshot.getValue(ImageUploadInfo.class);

                                list.add(imageUploadInfo);

                                adapter.notifyItemInserted(list.size() - 1);


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        });


     
        ActionBar actionBar=((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Home");
        return root;
    }


}