package com.example.database;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.database.tab.SaveFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by AndroidJSon.com on 6/18/2017.
 */

public class jimmadapter extends RecyclerView.Adapter<jimmadapter.ViewHolder> {

    private static final String TAG = "jimmadapter";

    Context context;
    List<ImageUploadInfo> MainImageUploadInfoList;

    String key;
    String imageurlcheck;
    ImageUploadInfo imageUploadInfo;
    Boolean found;

    String gps;
    String stUid;
    int num=0;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseDatabase database;

    String ImageUploadId;
    public jimmadapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;
        //SharedPreferences를 사용하여 key,gps값 가져오기
        SharedPreferences sharedPref = context.getSharedPreferences("shared" , Context.MODE_PRIVATE);
        stUid = sharedPref.getString("key", "");
        gps=sharedPref.getString("gps", "");

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //화면에 보여질 layout지정
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jimmview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);
        //이미지 제목 보여주기
        holder.imageNameTextView.setText(UploadInfo.getImageName());
        //이미지 보여주기
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


            SharedPreferences sharedPref = context.getSharedPreferences("image", Context.MODE_PRIVATE);
            final String ImageUploadId = sharedPref.getString("ImageUploadId", "");

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
//            final  applesQuery = databaseReference.child(stUid).orderByChild("jjim").equalTo(ImageUploadId);

            /*button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child(stUid).orderByChild("jjim").equalTo(ImageUploadId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });
                }
            });*/




/*            button.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    num++;
                    int pos = getAdapterPosition();

                    imageUploadInfo = MainImageUploadInfoList.get(pos);
                    imagurlcheck = imageUploadInfo.getImageURL();

                    if (pos != RecyclerView.NO_POSITION) {
                        // ImageUploadInfo imageUploadInfo = MainImageUploadInfoList.get(pos);
                        button.setBackgroundColor(Color.WHITE);
                        // Adding image upload id s child element into databaseReference.

                        databaseReference.child(stUid).child("jimm").child(ImageUploadId).setValue("");
                    }
                }
            });*/


            button.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {


                        imageUploadInfo = MainImageUploadInfoList.get(pos);
                        //이미지 유알엘 넘겨주기
                        imageurlcheck = imageUploadInfo.getImageURL();

                        //stuid(자신의 아이디)아래 jimm 아래 정보 받아오기
                        databaseReference.child(stUid).child("jimm").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    key = postSnapshot.getKey();
                                    String a = postSnapshot.getValue() + "";
                                    String b="key"+key+","+a;

                                    //b에 imageurlcheck가 있는지 확인
                                    found = b.contains(imageurlcheck);
                                    if (found == true) {
                                        //, 기준으로 데이터 자르기
                                        String[] aa =b.split(",");

                                        for (String string : aa) {
                                            //string에 key값이 있는지 확인
                                            if (string.contains("key")) {
                                                //3번째부터 받아오기
                                                String jimmdelete=string.substring(3);
                                                //버튼 색깔 하얀색으로 지정
                                                button.setBackgroundColor(Color.WHITE);
                                                // stUid 아래 jimm 아래 위에서 받아온 키 값아래 정보 삭제
                                                databaseReference.child(stUid).child("jimm").child(jimmdelete).removeValue(null);

                                            }

                                        }

                                    }
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("getFirebaseDatabase", "loadPost:onCancelled", databaseError.toException());
                            }
                        });
                    }

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {

                String emailimage;
                String keyimage;
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    //아이템뷰 클릭 했을 때 그 위치 받아오기
                    if (pos != RecyclerView.NO_POSITION) {
                        //위치 값에 있는 내용 값 받아오기
                        imageUploadInfo=MainImageUploadInfoList.get(pos);
                        //이미지 유알엘 넘겨오기
                        imageurlcheck=imageUploadInfo.getImageURL();

                        //경로에 맞게 데이터 받아오기
                        databaseReference.child(gps).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                for ( DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    key=postSnapshot.getKey();
                                    String a=postSnapshot.getValue()+"";

                                    //imageurlcheck가 포함되있는지 여부
                                    found=a.contains(imageurlcheck);
                                    if(found.equals(true)){
                                        //,기준으로 데이터 자르기
                                        String [] aa=a.split(",");
                                        for(String string :aa){
                                            //email이 포함되어있는지 확인
                                            if(string.contains("email")){
                                                //7번째부터 데이터 받아오기
                                                emailimage=string.substring(7);

                                                for(String ss:aa){
                                                    //keyr값이 있는지 확인
                                                    if(ss.contains(("key"))){
                                                        //5번째부터 데이터 받아오기
                                                        keyimage=ss.substring(5);
                                                        //Detailed_Content.class로 정보 공유하기
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



                    }

                }


            });



        }
    }


}