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


public class jimmadapter extends RecyclerView.Adapter<jimmadapter.ViewHolder> {

    //어댑터는 RecyclerView.Adapter를 상속하여 구현해야 한다.
    //어댑터 클래스 상속시 구현해야할 함수 3가지
    //onCreateViewHolder, onBindViewHolder, getItemCount


    //변수 선언
    private static final String TAG = "jimmadapter";

    Context context;
    List<ImageUploadInfo> MainImageUploadInfoList;

    String key;
    String imageurlcheck;
    ImageUploadInfo imageUploadInfo;
    Boolean found;

    String gps;
    String stUid;

    StorageReference storageReference;


    String ImageUploadId;
    public jimmadapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;
        //SharedPreferences를 사용하여 key,gps값 가져오기
        SharedPreferences sharedPref = context.getSharedPreferences("shared" , Context.MODE_PRIVATE);
        stUid = sharedPref.getString("key", "");
        gps=sharedPref.getString("gps", "");

        this.context = context;
    }

    //onCreateViewHolder(): 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //ViewHolder와 레이아웃 파일을 연결
        //리사이클러뷰에 들어갈 뷰 홀더를 할당하는 함수, 뷰 홀더는 실제 레이아웃 파일과 매핑되어야하며, extends의 Adapter<>에서 <>안에 들어가는 타입을 따름
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jimmview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    //onBindViewHolder(): position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);
        //이미지 제목 보여주기
        holder.imageNameTextView.setText(UploadInfo.getImageName());
        //이미지 보여주기
        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);
    }

    //getItemCount(): 전체 데이터 개수를 리턴한다.
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
            //users아래 경로
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");


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

                                                // stUid 아래 jimm 아래 위에서 받아온 키 값아래 정보 삭제
                                                databaseReference.child(stUid).child("jimm").child(jimmdelete).removeValue(null);
                                                //자신의 아이디 밑 jimm밑 imageuploadid 밑에 넣어주기

                                                Toast.makeText(context.getApplicationContext(),imageUploadInfo.getImageName()+"이(가) 취소되었습니다.", Toast.LENGTH_LONG).show();
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