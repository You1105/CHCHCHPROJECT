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

    private ArrayList<User> userArrayList;
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView ivUser;
        public TextView tvEmail;
        public Button btnChat;

        public ViewHolder(View itemView) {
            super(itemView);
            ivUser = (ImageView)itemView.findViewById(R.id.ivUser);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            btnChat = (Button)itemView.findViewById(R.id.btnChat);
            //imageView.setBackground(new ShapeDrawable(new OvalShape()));
            //imageView.setClipToOutline(true);
            //
            //출처: https://chocorolls.tistory.com/47 [초코롤의 개발이야기]
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserAdapter(ArrayList<User> userArrayList, Context context) {
        this.userArrayList = userArrayList;
        this.context = context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_user, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.tvEmail.setText(userArrayList.get(position).getEmail());

        String stPhoto = userArrayList.get(position).getPhoto();
        if(TextUtils.isEmpty(stPhoto)) {
            Glide.with(context)
                    .load(R.drawable.ic_sentiment_nophoto_black_24dp)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivUser);
        } else {
            Glide.with(context)
                    .load(userArrayList.get(position).getPhoto())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivUser);
        }

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stUseUid = userArrayList.get(position).getKey();
                String stUserEmail = userArrayList.get(position).getEmail();
                String stUserPhoto = userArrayList.get(position).getPhoto();
                Intent in = new Intent(context, ChatActivity.class);
                in.putExtra("userUid", stUseUid);
                in.putExtra("userEmail", stUserEmail);
                in.putExtra("userPhoto", stUserPhoto);
                context.startActivity(in);;

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
}