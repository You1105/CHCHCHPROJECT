package com.example.database.tab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.example.database.MainActivity;
import com.example.database.R;
import com.example.database.User;
import com.example.database.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersFragment extends Fragment {

    private  static final String TAG = "UsersFragment";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    UserAdapter mUserAdapter;

    ArrayList<User> userArrayList;

    FirebaseDatabase database;


    String stUid,gps;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = (RecyclerView)root.findViewById(R.id.rvUser);


        //레이아웃 사이즈 고정
        recyclerView.setHasFixedSize(true);


        //레이아웃매니저: 리사이클러뷰가 아이템을 화면에 표시할 때,
        //아이템 뷰들이 리사이클러뷰 내부에서 배치되는 형태를 관리
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        userArrayList = new ArrayList<>();


        mUserAdapter= new UserAdapter(userArrayList, getActivity());
        recyclerView.setAdapter(mUserAdapter);
        //SharedPreferences를 사용하여 key값과 gps값을 가져오기
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        stUid = sharedPref.getString("key", "");
        gps= sharedPref.getString("gps", "");

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(gps);

        //데이터베이스 읽기
        //addListenerForSingleValueEvent() 메소드로 데이터 한 번 읽기
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //하위 노드가 수정될 때마다 발생
                //이벤트 리스너에 전달되는 DataSnapshot에는 하위 항목의 업데이트된 데이터가 포함됨

                String value = dataSnapshot.getValue().toString();

                for(DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                    String value2 = dataSnapshot2.getValue().toString();

                    User user = dataSnapshot2.getValue(User.class);


                    userArrayList.add(user); //추가
                    //UserAdapter에서 RecyclerView에 반영하도록 함
                    mUserAdapter.notifyItemInserted(userArrayList.size() - 1);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

       // actionBar를 chatlist로 지정하기
        ActionBar actionBar=((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("ChatList");

        return root;
    }

}
