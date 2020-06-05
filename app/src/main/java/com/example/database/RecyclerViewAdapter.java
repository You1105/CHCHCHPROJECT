package com.example.database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.database.tab.RealHomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AndroidJSon.com on 6/18/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    String value;
    Context context;
    List<ImageUploadInfo> MainImageUploadInfoList;
    String ImageUploadId;

    String ab;
    ImageUploadInfo imageUploadInfo;
    Parcelable recyclerViewState;
    String gps;

    String stUid;
    int pos=0;
    String ud;
    String key;
    String imagurlcheck;
    String e;
    int checkbutton;
    String email;
    String check;
    Boolean found;



    StorageReference storageReference;
    DatabaseReference databaseReference;
    int g;



    public RecyclerViewAdapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;
        SharedPreferences sharedPref = context.getSharedPreferences("shared" , Context.MODE_PRIVATE);
        stUid = sharedPref.getString("key", "");

        gps=sharedPref.getString("gps", "");
        email=sharedPref.getString("email", "");

        SharedPreferences sharedPref1 = context.getSharedPreferences(email , Context.MODE_PRIVATE);
        checkbutton=sharedPref1.getInt("position",0);
        check=sharedPref1.getString("check","");

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
            databaseReference = FirebaseDatabase.getInstance().getReference("users");




            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    pos = getAdapterPosition();
                    Log.d("pos", pos + "");
                    ImageUploadInfo imageUploadInfo = MainImageUploadInfoList.get(pos);


                    if (pos != RecyclerView.NO_POSITION) {


                        ImageUploadId = databaseReference.child(stUid).child("jimm").push().getKey();

                        SharedPreferences sharedPref = context.getSharedPreferences("image" , Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("ImageUploadId", ImageUploadId);
                        editor.commit();

                        button.setBackgroundColor(Color.RED);
                        databaseReference.child(stUid).child("jimmbutton").child(pos + "").setValue("check");
                        databaseReference.child(stUid).child("jimm").child(ImageUploadId).setValue(imageUploadInfo);

                    }
                }


            });

            Log.d("checkbutton",checkbutton+"");
           /* if(check.equals("check")){



                        if (checkbutton != RecyclerView.NO_POSITION) {

                            button.setBackgroundColor(Color.RED);
                        }

            }*/

          /*databaseReference.child("jimmbutton").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                        pos = getAdapterPosition();
                        Log.d("ddd",datasnapshot.getValue().toString()+pos);

                                        if (pos != RecyclerView.NO_POSITION) {
                                            Log.d("tagpos",pos+"");
                                            if(datasnapshot.getValue().toString().equals("check")) {


                                                button.setBackgroundColor(Color.RED);
                                            }
                                            else{
                                                button.setBackgroundColor(Color.WHITE);
                                            }

                                        }



                                    }

                                }


                @Override
                public void onCancelled(DatabaseError databaseError) {



                }


            });*/






//리사이클러뷰 아이템뷰를 클릭 호출
            itemView.setOnClickListener(new View.OnClickListener() {
                String emailimage;
                String keyimage;
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    ab=pos+"";
                    Log.d("125",ab+"");
                    //아이템뷰 클릭 했을 때 그 위치 받아오기
                    if (pos != RecyclerView.NO_POSITION) {
                        //위치 값에 있는 내용 값 받아오기
                        imageUploadInfo=MainImageUploadInfoList.get(pos);

                        imagurlcheck=imageUploadInfo.getImageURL();
                        Log.d("15q34qew6", imagurlcheck);

                        databaseReference.child(gps).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                for ( DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    key=postSnapshot.getKey();
                                    String a=postSnapshot.getValue()+"";
                                    Log.d("1251626246",a);

                                    found=a.contains(imagurlcheck);
                                    if(found.equals(true)){
                                        String [] aa=a.split(",");
                                        Log.d("ttteee",aa+"");

                                        for(String string :aa){
                                            Log.d("125q46",string);
                                            if(string.contains("email")){
                                                emailimage=string.substring(7);
                                                Log.d("12556dsag5d56",emailimage);
                                                for(String ss:aa){
                                                    if(ss.contains(("key"))){
                                                        keyimage=ss.substring(5);
                                                        Intent in = new Intent(context, Detailed_Content.class);
                                                        in.putExtra("chatid", emailimage);
                                                        in.putExtra("tit",imageUploadInfo.getImageName());
                                                        in.putExtra("chatkey", keyimage);
                                                        in.putExtra("tex",imageUploadInfo.gettext());
                                                        in.putExtra("image",imageUploadInfo.getImageURL());
                                                        in.putExtra("cate",imageUploadInfo.getcategory());
                                                        context.startActivity(in);

                                                    }
                                                }




                                            }

                                        }

                                    }}


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
                            }
                        });


                        //클릭한 후 Detailed_Content.Class로 데이터 공유하기


                       /*Intent intent=new Intent(v.getContext(),Detailed_Content.class);

                        //키값의 이름을 지정한 후 공유할 데이터 값을 적어두기
                      intent.putExtra("tit",imageUploadInfo.getImageName());
                      intent.putExtra("tex",imageUploadInfo.gettext());
                       intent.putExtra("image",imageUploadInfo.getImageURL());
                       intent.putExtra("cate",imageUploadInfo.getcategory());
                        v.getContext().startActivity(intent);

*/
                    }

                }


            });



        }
    }


}