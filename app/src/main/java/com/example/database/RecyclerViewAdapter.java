package com.example.database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by AndroidJSon.com on 6/18/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public static final String Database_Path = "Jimm";
    Context context;
    List<ImageUploadInfo> MainImageUploadInfoList;


    String stUid;
    int num=0;
    StorageReference storageReference;
    DatabaseReference databaseReference;


    public RecyclerViewAdapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;
        SharedPreferences sharedPref = context.getSharedPreferences("email", Context.MODE_PRIVATE);
        stUid = sharedPref.getString("uid", "");
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);

        holder.imageNameTextView.setText(UploadInfo.getImageName());

        //Loading image from Glide library.
        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }



     class ViewHolder extends RecyclerView.ViewHolder {

        public Button button;
        public ImageView imageView;
        public TextView imageNameTextView;


        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);
            button = (Button) itemView.findViewById(R.id.button);

            storageReference = FirebaseStorage.getInstance().getReference();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(stUid).child(Database_Path);


            button.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {


                   }
            });





            itemView.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {


                }


            });


        }}}
