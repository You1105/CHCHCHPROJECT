package com.example.database;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    //어댑터 클래스 상속시 구현해야할 함수 3가지
    //onCreateViewHolder, onBindViewHolder, getItemCount


    //변수 선언
    private ArrayList<User> userArrayList;
    Context context;

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUser;
        public TextView tvEmail;
        public Button btnChat;
        public Button btnStar;
        public ViewHolder(View itemView) {
            super(itemView);
            //뷰 객체에 대한 참조
            ivUser = (ImageView)itemView.findViewById(R.id.ivUser);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            btnChat = (Button)itemView.findViewById(R.id.btnChat);
            btnStar = (Button)itemView.findViewById(R.id.btnStar);

        }
    }

    //생성자에서 데이터를 전달받는다.
    public UserAdapter(ArrayList<User> userArrayList, Context context) {
        //변수선언
        this.userArrayList = userArrayList;
        this.context = context;

    }

    //onCreateViewHolder(): 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        //ViewHolder와 레이아웃 파일을 연결
        //리사이클러뷰에 들어갈 뷰 홀더를 할당하는 함수, 뷰 홀더는 실제 레이아웃 파일과 매핑되어야하며, extends의 Adapter<>에서 <>안에 들어가는 타입을 따름
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_user, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    //ViewHoler: ItemView의 각 요소를 바로 엑세스 할 수 있도록 저장해주고 사용하기 위한 객체


    //onBindViewHolder(): position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tvEmail.setText(userArrayList.get(position).getEmail());

        //이미지가 있는지 없는지 확인
        //glide 사용을 위해 모듈을 buil.gradle에 추가해야됨
        //glide는 이미지, 움직이는 gif등을 재생시키는데 사용됨
        //어떤 이미지라도 빠르게 스크롤 할 수 있게 하는 것이 목적
        String stPhoto = userArrayList.get(position).getPhoto();
        //stPhoto가 있는지 확인
        //stPhoto이 비었을 때 nophoto 그림을 띄운다.
        if(TextUtils.isEmpty(stPhoto)) {
            Glide.with(context)
                    .load(R.drawable.ic_sentiment_nophoto_black_24dp)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivUser);
        }
        //stPhoto이 비었을 때 각자 설저한 프로필을 불러와서 띄운다.
        else {
            Glide.with(context)
                    .load(userArrayList.get(position).getPhoto())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivUser);
        }

        //버튼 누를시
        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stUseUid = userArrayList.get(position).getKey();
                String stUserEmail = userArrayList.get(position).getEmail();
                String stUserPhoto = userArrayList.get(position).getPhoto();

                //ChatActivity로 useruid,userEmail,userPhoto 값 넘기기
                Intent in = new Intent(context, ChatActivity.class);
                in.putExtra("userUid", stUseUid);
                in.putExtra("userEmail", stUserEmail);
                in.putExtra("userPhoto", stUserPhoto);
                context.startActivity(in);;

            }
        });
        //btnstar누를 시
        holder.btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //StarActivity에 useruid값 넘기기
                String stUseUid = userArrayList.get(position).getKey();
                Intent in = new Intent(context, StarActivity.class);
                in.putExtra("userUid", stUseUid);
                context.startActivity(in);




            }
        });
    }

    //ios의 numberOfRows, 리사이클러뷰안에 들어갈 뷰 홀더의 개수
    //getItemCount는 ios의 numberOfRows와 대응한다.
    //getItemCount(): 전체 데이터 개수를 리턴한다.
    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
}