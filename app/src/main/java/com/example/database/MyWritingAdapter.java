package com.example.database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MyWritingAdapter extends RecyclerView.Adapter<MyWritingAdapter.ViewHolder> {
    Context context;
    List<ImageUploadInfo> MainImageUploadInfoList;

    ImageUploadInfo imageUploadInfo;
    String imageurl;
    String gps;

    String stUid;
    int pos=0;

    String key;
    String imagurlcheck;

    int checkbutton;
    String email;
    String check;
    Boolean found;



    StorageReference storageReference;
    DatabaseReference databaseReference;
    int g;



    public MyWritingAdapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;
        //SharedPrefernces를 사용하여 key,gps 값 가져오기
        SharedPreferences sharedPref = context.getSharedPreferences("shared" , Context.MODE_PRIVATE);
        stUid = sharedPref.getString("key", "");
        gps=sharedPref.getString("gps", "");

        this.context = context;



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mywritingview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);
        //이미지 이름 보여주기
        holder.imageNameTextView.setText(UploadInfo.getImageName());
        //이미지 Glide사용하여 보여주기
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

            //users 아래 경로
            databaseReference = FirebaseDatabase.getInstance().getReference("users");
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    pos = getAdapterPosition();

                    ImageUploadInfo imageUploadInfo = MainImageUploadInfoList.get(pos);


                    if (pos != RecyclerView.NO_POSITION) {
                        //이미지 유알엘 imageurl에 넘기기
                        imageurl= imageUploadInfo.getImageURL();
                        //users 아래 gps 아래 자신의 아이디 경로
                        databaseReference.child(gps).child(stUid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                for ( DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    key=postSnapshot.getKey();
                                    String a=postSnapshot.getValue()+"";
                                    //a에 imageurl에 있는지 여부
                                    found=a.contains(imageurl);
                                    if(found.equals(true)){
                                        //=단위로 데이터 자르기
                                        String [] aa=a.split("=");
                                        for(String string :aa){
                                            //-이 포함되어있는지 여부
                                            if(string.contains("{-")){
                                                //1번째부터 정보 받아오기
                                                String imagekey=string.substring(1);
                                                //users 아래 gps 아래 자신의 아이디 아래 imageupload아래 imagekey값아래 정보 지우기
                                                databaseReference.child(gps).child(stUid).child("imageupload").child(imagekey).removeValue(null);




                                            }

                                        }

                                    }}


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
                            }
                        });


                        //

                    }
                }


            });



            //리사이클러뷰 아이템뷰를 클릭 호출
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

                        imagurlcheck=imageUploadInfo.getImageURL();

                        databaseReference.child(gps).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                for ( DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    key=postSnapshot.getKey();
                                    String a=postSnapshot.getValue()+"";
                                    //imageurlcheck가 있는지 확인 여부
                                    found=a.contains(imagurlcheck);
                                    if(found.equals(true)){
                                        //,단위로 정보 자르기
                                        String [] aa=a.split(",");
                                        for(String string :aa){
                                            //email이 있는지 대한 확인
                                            if(string.contains("email")){
                                                //7번째부터 데이터 받아오기
                                                emailimage=string.substring(7);
                                                for(String ss:aa){
                                                    //key값이 포함되어있는지 여부
                                                    if(ss.contains(("key"))){
                                                        //5번째부터 데이터 받아오기
                                                        keyimage=ss.substring(5);
                                                        //Detailed_Content.class에 값정보 넘겨주기
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
