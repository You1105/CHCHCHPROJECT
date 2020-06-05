package com.example.database;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
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

    private ArrayList<Chat> chatArrayList;
    String stMyEmail = "";
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        // each data item is just a string in this case
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

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Chat> chatArrayList, String stEmail, Context context)
    {
        this.chatArrayList = chatArrayList;
        this.stMyEmail = stEmail;
        Log.d("gdsddggd",stMyEmail);
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if(chatArrayList.get(position).getEmail().equals(stMyEmail)){
            return 1;
        } else{
            return 2;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        // create a new view
        // create a new view

        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.left_text_view, parent, false);
        if(viewType == 1){
            v =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.right_text_view, parent, false);
        }
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        String email = chatArrayList.get(position).getEmail().substring(0,chatArrayList.get(position).getEmail().lastIndexOf("->"));
        holder.tvEmail.setText(email);

        String stPhoto = chatArrayList.get(position).getPhoto();
        Log.d("sdfag",stPhoto);
        if(TextUtils.isEmpty(stPhoto)) {
            Glide.with(context)
                    .load(R.drawable.ic_sentiment_nophoto_black_24dp)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivUser);
        } else {
            Glide.with(context)
                    .load(stPhoto)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivUser);
        }
        holder.textView.setText(chatArrayList.get(position).getText());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }
}