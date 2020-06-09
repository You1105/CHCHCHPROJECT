package com.example.database.tab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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


    Parcelable recyclerViewState;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ImageUploadInfo> list;
    RecyclerView.Adapter adapter ;
    DatabaseReference databaseReference;
    String stUid;
    String key;
    String gps;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedinstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView=root.findViewById(R.id.recyclerView);
        //LoginAticity의 SharedPreferences를 통해 저장된 값을 불러오기 위해 같은 네임파일을 찾는다.
        //프로필에서 필요한 정보: key, gps
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
                                //list에 추가하기
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


        // actionBar에 Home으로 지정하기
        ActionBar actionBar=((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Home");
        return root;
    }


}