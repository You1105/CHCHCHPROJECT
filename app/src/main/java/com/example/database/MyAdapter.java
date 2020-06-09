package com.example.database;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    //어댑터는 RecyclerView.Adapter를 상속하여 구현해야 한다.
    //어댑터 클래스 상속시 구현해야할 함수 3가지
    //onCreateViewHolder, onBindViewHolder, getItemCount


    private ArrayList<Chat> chatArrayList;
    String stMyEmail = "";
    Context context;

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public static class MyViewHolder extends  RecyclerView.ViewHolder {

        public TextView textView, tvEmail;
        ImageView ivUser;

        public MyViewHolder(View v) {
            super(v);

            //뷰 객체에 대한 참조
            textView = (TextView)v.findViewById(R.id.tvChat);
            tvEmail = (TextView)v.findViewById(R.id.tvEmail);
            ivUser=v.findViewById(R.id.ivUser);

        }
    }

    //생성자에서 데이터를 전달받는다.
    public MyAdapter(ArrayList<Chat> chatArrayList, String stEmail, Context context)
    {
        this.chatArrayList = chatArrayList;
        this.stMyEmail = stEmail;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
       //자신의 이메일이 맞다면  return 1
        if(chatArrayList.get(position).getEmail().equals(stMyEmail)){
            return 1;
        }
        //자신의 이메일이 아니라면 return 2
        else{
            return 2;
        }
    }

    //onCreateViewHolder(): 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       //위에서 받아온 걸로 확인하여 위치를 정해준다.

        //ViewHolder와 레이아웃 파일을 연결
        //리사이클러뷰에 들어갈 뷰 홀더를 할당하는 함수, 뷰 홀더는 실제 레이아웃 파일과 매핑되어야하며, extends의 Adapter<>에서 <>안에 들어가는 타입을 따름

        //위에서 받아온 걸로 확인하여 위치를 정해준다.
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.left_text_view, parent, false);
        if(viewType == 1){
            v =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.right_text_view, parent, false);
        }
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    //onBindViewHolder(): position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //->앞에있는 이메일을 받아온다
        String email = chatArrayList.get(position).getEmail().substring(0,chatArrayList.get(position).getEmail().lastIndexOf("->"));
        holder.tvEmail.setText(email);
       //이미지를 받아온다
        String stPhoto = chatArrayList.get(position).getPhoto();
      //이미지 유알엘이 있다면
        if(TextUtils.isEmpty(stPhoto)) {
            Glide.with(context)
                    .load(R.drawable.ic_sentiment_nophoto_black_24dp)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivUser);
        }
        //이미지 유알엘이 없다면
        else {
            Glide.with(context)
                    .load(stPhoto)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivUser);
        }

        holder.textView.setText(chatArrayList.get(position).getText());
    }

    //getItemCount(): 전체 데이터 개수를 리턴한다.
    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }
}