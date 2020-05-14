package com.example.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//리사이클러뷰 어댑터 구현
//어댑터는 RecyclerView.Adapter를 상속하여 구현해야 한다.
//RecyclerView.Adapter를 상속받아 새로운 어댑터를 만들 때, 오버라이드가 필요한
//메서드는 onCreateViewHoler(), onBindViewHolder(), getItemCount()가 있다.
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Chat> chatArrayList;
    String stMyEmail = "";
    Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    //아이템 뷰를 저장하는 뷰홀더 클래스
    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView, tvEmail;
        public MyViewHolder(View v) {
            super(v);

            //뷰 객체에 대한 참조
            textView = (TextView)v.findViewById(R.id.tvChat);
            tvEmail = (TextView)v.findViewById(R.id.tvEmail);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    //생성자에서 데이터를 전달받음
    public MyAdapter(ArrayList<Chat> chatArrayList, String stEmail, Context context)
    {
        this.chatArrayList = chatArrayList;
        this.stMyEmail = stEmail;
        this.context = context;
    }

/*    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if(chatArrayList.get(position).email.equals(stMyEmail)){
            return 1;
        } else{
            return 2;
        }
    }*/

    // Create new views (invoked by the layout manager)
    //onCreateViewHolder(): 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        // create a new view
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.left_text_view, parent, false);
/*        if(viewType == 1){
            v =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.right_text_view, parent, false);
        }*/
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    //onBindViewHolder(): position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(chatArrayList.get(position).getText());
        holder.tvEmail.setText(chatArrayList.get(position).getEmail());
    }

    // Return the size of your dataset (invoked by the layout manager)
    //getItemCount(): 전체 데이터 개수 리턴
    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }
}
