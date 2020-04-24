package com.example.database;

import android.content.Context;
import android.content.Intent;
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

    int num=0;
    StorageReference storageReference;
    DatabaseReference databaseReference;


    public RecyclerViewAdapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

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
            databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
            button.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    num++;
                    int pos = getAdapterPosition();


                        String ImageUploadId = databaseReference.push().getKey();

                        if (num % 2 == 0) {
                            if (pos != RecyclerView.NO_POSITION) {
                                ImageUploadInfo imageUploadInfo = MainImageUploadInfoList.get(pos);
                                button.setBackgroundColor(Color.WHITE);
                                // Adding image upload id s child element into databaseReference.


                           }}

                       else {

                           if (pos != RecyclerView.NO_POSITION) {
                               ImageUploadInfo imageUploadInfo = MainImageUploadInfoList.get(pos);
                               button.setBackgroundColor(Color.RED);

                               // Adding image upload id s child element into databaseReference.
                               databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                           }
                       }

                   }
            });





            itemView.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {


                   int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        ImageUploadInfo imageUploadInfo=MainImageUploadInfoList.get(pos);
                    //  Toast.makeText(v.getContext(),""+imageUploadInfo.getImageName()+imageUploadInfo.getImageURL()+imageUploadInfo.gettext(), Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(v.getContext(),Detailed_Content.class);
                    intent.putExtra("tit",imageUploadInfo.getImageName());
                    intent.putExtra("tex",imageUploadInfo.gettext());
                   intent.putExtra("image",imageUploadInfo.getImageURL());
                   intent.putExtra("cate",imageUploadInfo.getcategory());
                        v.getContext().startActivity(intent);
                    }

                }


            });


        }}}
