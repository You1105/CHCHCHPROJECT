package com.example.database.tab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.database.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StarStarFragment extends Fragment {
    float star;
    RatingBar mRatingVote;
    Button btnDec, btnInc,btnResult,btnDB;
    Button sendbt;
    int count;
    TextView textaverage;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    ChildEventListener mChild;

    ListView listView;
    ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    String stEmail, stUid,gps,rating;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =inflater.inflate(R.layout.star_fragment, container, false);
        mRatingVote = (RatingBar)root.findViewById(R.id.ratingVote);
        listView = (ListView)root. findViewById(R.id.listviewstar);
        textaverage = (TextView)root. findViewById(R.id.textview2);

        initDatabase();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);

        //데이터베이스에서 받아온 별점들을 리스트뷰로 나열
        stUid = sharedPref.getString("key", "");
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listView.setAdapter(adapter);

        mReference = mDatabase.getReference("youRating").child(stUid); // 변경값을 확인할 child 이름
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {

                    // child 내에 있는 데이터만큼 반복합니다.
                    String key=messageData.getKey();
                    Log.d("dsfa",key);
                    Boolean found;
                    found=key.contains("-");
                    String msg2 = messageData.getValue().toString();

                    if(found==true){
                        count++;


                    }
                    Log.d("dfasdg",count+"");

                    //별점 평균
                    float l = Float.valueOf(msg2).floatValue();

                    Log.d("dasgag",l+"");
                    star=star+l;
                    Log.d("dagag",star/count+"");
                    float avgstar=star/count;
                    textaverage.setText(avgstar+"");
                    mRatingVote.setRating(avgstar);
                    Array.add(msg2);
                    adapter.add(msg2);


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return root;
    }




    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();


        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }



}