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

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private ArrayList<User> userArrayList;
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        //변수 선언
        public ImageView ivUser;
        public TextView tvEmail;
        public Button btnChat;

        public ViewHolder(View itemView) {
            super(itemView);
            ivUser = (ImageView)itemView.findViewById(R.id.ivUser);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            btnChat = (Button)itemView.findViewById(R.id.btnChat);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserAdapter(ArrayList<User> userArrayList, Context context) {
        this.userArrayList = userArrayList;
        this.context = context;

    }

    //어댑터 클래스 상속시 구현해야할 함수 3가지 : onCreateViewHolder, onBindViewHolder, getItemCount
    // Create new views (invoked by the layout manager)
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        //ViewHolder와 레이아웃 파일을 연결
        //리사이클러뷰에 들어갈 뷰 홀더를 할당하는 함수, 뷰 홀더는 실제 레이아웃 파일과 매핑되어야하며, extends의 Adater<>에서 <>안에들어가는 타입을 따름
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_user, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    //ViewHolder : ItemView의 각 요소를 바로 엑세스 할 수 있도록 저장해두고 사용하기 위한 객체


    // 실제 각 뷰 홀더에 데이터를 연결해주는 함수

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.tvEmail.setText(userArrayList.get(position).getEmail());
        String stPhoto = userArrayList.get(position).getPhoto();

        //이미지가 있는지 없는지 확인
        //glide 사용을 위해 모듈을 build.gradle에 추가해야됨
        //glide는 이미지,움직이는 gif등을 재생시키는데 사용됨
        //어떤 이미지라도 빠르게 스크롤 할 수 있게 하는 것이 목적
        if(TextUtils.isEmpty(stPhoto)) {
            Glide.with(context)
                    .load(R.drawable.ic_sentiment_nophoto_black_24dp)
                    .into(holder.ivUser);
        } else {
            Glide.with(context)
                    .load(userArrayList.get(position).getPhoto())
                    .into(holder.ivUser);
        }
        //저장된 그림이 있다면 가져옴

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stUserId = userArrayList.get(position).getKey();
                Intent in = new Intent(context, ChatActivity.class);
                in.putExtra("userUid", stUserId);
                context.startActivity(in);
                //키를 가져오게 되면 채팅 액티비티 클래스로 화면이 넘어감
            }
        });
    }

    // iOS의 numberOfRows, 리사이클러뷰안에 들어갈 뷰 홀더의 개수
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
    //getItemCount는 iOS의 numberOfRows와 대응
}