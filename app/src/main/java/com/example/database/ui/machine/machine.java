package com.example.database.ui.machine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database.ImageUploadInfo;
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

public class machine extends Fragment {
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
    private MachineViewModel mViewModel;

    public static machine newInstance() {
        return new machine();
    }

    //카테고리에서 가전을 선택했을 시 machine_fragment 보여줌
    //Layout을 inflate 하는 곳, View 객체를얻어서 초기화
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View root=inflater.inflate(R.layout.machine_fragment, container, false);
        recyclerView=root.findViewById(R.id.recyclerView);

        //SharedPreferences로 저장된 데이터 불러오기
        //key(uid) 값과 gps 불러오기
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared" , Context.MODE_PRIVATE);
        stUid = sharedPref.getString("key", "");
        gps=sharedPref.getString("gps", "");

        Log.d("ddd",stUid);



        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new RecyclerViewAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);


        databaseReference= FirebaseDatabase.getInstance().getReference("users").child(gps);

        //Query를 사용하여 이름이 imageupload경로의 값에 따라 결과를 정렬
        final Query query=databaseReference.orderByChild("imageupload");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    key=postSnapshot.getKey();




                    databaseReference.child(key).child("imageupload").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot datasnapshot : dataSnapshot.getChildren()){

                                ImageUploadInfo imageUploadInfo = datasnapshot.getValue(ImageUploadInfo.class);

                                if (imageUploadInfo.getcategory().equals("가전")) {
                                    list.add(imageUploadInfo);
                                    adapter.notifyItemInserted(list.size() - 1);}

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

        return root;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MachineViewModel.class);
        // TODO: Use the ViewModel
    }

}
