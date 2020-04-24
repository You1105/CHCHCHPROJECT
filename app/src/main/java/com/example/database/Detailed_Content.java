package com.example.database;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Detailed_Content extends AppCompatActivity {

    String tit = "";
    String tex = "";
    String image = "";
    String cate="";
    TextView title,text;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed__content);

// Assign id to RecyclerView.


        Bundle extras = getIntent().getExtras();
        tit = extras.getString("tit");
        tex = extras.getString("tex");
        image = extras.getString("image");
        title = (TextView) findViewById(R.id.title);
        text = (TextView) findViewById(R.id.text);

        imageView = (ImageView) findViewById(R.id.imageView2);
        final String s = tit;
        final String t = tex;
        final String r = image;
        final String c=cate;
        title.setText(s);
        text.setText(t);


       Glide.with(this).load(r).into(imageView);

    }
}







