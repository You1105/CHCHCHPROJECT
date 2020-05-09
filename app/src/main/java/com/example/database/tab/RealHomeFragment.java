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
    // Creating RecyclerView.Adapter.

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

    // Creating List of ImageUploadInfo class.

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedinstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        final TextView textView = root.findViewById(R.id.text_home);
        // Creating RecyclerView.
        recyclerView=root.findViewById(R.id.recyclerView);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared" , Context.MODE_PRIVATE);
        stUid = sharedPref.getString("key", "");

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new RecyclerViewAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);

        //databaseReference = FirebaseDatabase.getInstance().getReference("users").child(stUid).child(RecyclerViewAdapter.Database_Path);

       databaseReference= FirebaseDatabase.getInstance().getReference("users").child(stUid).child("imageupload");

       // databaseReference= FirebaseDatabase.getInstance().getReference("users");

        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {

                    ImageUploadInfo imageUploadInfo = datasnapshot.getValue(ImageUploadInfo.class);


                    list.add(imageUploadInfo);


                    adapter.notifyItemInserted(list.size() - 1);
                }



                // Hiding the progress dialog.

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }


        });

        ActionBar actionBar=((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Home");
        return root;
    }
}