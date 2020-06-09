package com.example.database.tab;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.database.MainActivity;
import com.example.database.R;
import com.example.database.chatcolor;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class ProfileFragment extends Fragment {

    //필요한 변수 선언

    private static final String TAG = "ProfileFragment";

    int REQUEST_IMAGE_CODE = 1001;
    int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1002;

    ImageView ivUser;
    ProgressBar pb;
    String stEmail, stUid;

    String gps;
    private StorageReference mStorageRef;
    Bitmap bitmap;
    StarStarFragment Star;
    private Button buttonStar, mywriting,color;
    FirebaseDatabase database;
    DatabaseReference myRef;
    MyWriting Writing;
    chatcolor chtcolor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        //LoginAticity의 SharedPreferences를 통해 저장된 값을 불러오기 위해 같은 네임파일을 찾는다.
        //프로필에서 필요한 정보: email, key, gps
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        stEmail = sharedPref.getString("email", ""); //email에 저장된 값이 있는지 확인, 아무값도 들어있지 않으면 ""을 반환
        stUid = sharedPref.getString("key", ""); //key 저장된 값이 있는지 확인, 아무값도 들어있지 않으면 ""을 반환
        gps= sharedPref.getString("gps", ""); //gps에 저장된 값이 있는지 확인, 아무값도 들어있지 않으면 ""을 반환

        color=(Button)root.findViewById(R.id.chatcolor);
        //color 버튼을 클릭했을 시 채팅방 색상을 변경할 수 있는 페이지로 넘어갑니다.
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chtcolor=new chatcolor();
                //chtcolr로 넘어가기
                getActivity().getSupportFragmentManager().beginTransaction().add((R.id.nav_host_fragment),chtcolor).addToBackStack(null).commit();
            }
        });


        mywriting=(Button)root.findViewById(R.id.mywriting);

        //mywritin을 클릭했을 때 내가 쓴 글을 볼 수 있는 페이지로 넘어갑니다.
        mywriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Writing=new MyWriting();
                getActivity().getSupportFragmentManager().beginTransaction().add((R.id.nav_host_fragment),Writing).addToBackStack(null).commit();
            }
        });

        //buttonStar을 클릭했을 때 별점을 줄 수 있는 페이지로 넘어갑니다.
        buttonStar=(Button)root.findViewById(R.id.buttonStar);
        buttonStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Star=new StarStarFragment();
                getActivity().getSupportFragmentManager().beginTransaction().add((R.id.nav_host_fragment),Star).addToBackStack(null).commit();
            }
        });

        database = FirebaseDatabase.getInstance();

        //users 밑 gps  자신의 아이디(stUid) 밑 경로 지정합니다.
        myRef = database.getReference("users").child(gps).child(stUid);

        //데이터베이스 읽기
        //addListenerForSingleValueEvent() 메소드로 데이터 한 번 읽기
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue().toString();
                //users 아래 gps아래 stUid 밑 photo에서 정보 받아오기
                String stPhoto = dataSnapshot.child("photo").getValue().toString();

                pb = (ProgressBar) root.findViewById(R.id.pb);

                //사진이 업로드 되어있는지 없는지 확인
                //TextUtils를 사용하여 null 체크 및 빈칸 여부 체크
                //stPhoto가 비어있을 때
                if (TextUtils.isEmpty(stPhoto)) {
                    //프로필이 없으므로 받아로 이미지가 없기 때문에 ProgressBar가 보이지 않도록 한다.
                    pb.setVisibility(View.GONE);
                }
                //stPhoto가 있을 때 피카소를 이용하여 사진 보여주기
                else{
                    //프로필을 받아올 동안 ProgressBar를 띄워 로딩중임을 알린다.
                    pb.setVisibility(View.VISIBLE);
                    //외부로부터 이미지를 불러오기 위해 피카소를 이용했다.
                    //Picasso 사용을 위해 모듈을 buil.gradle에 추가해야된다.
                    //load()를 불러 이미지를 로드하고.
                    //into()로 원하는 ImageView에 로드된 이미지를 표시
                    Picasso.get()
                            .load(stPhoto)
                            .fit()
                            .centerInside()
                            .into(ivUser, new Callback.EmptyCallback() {
                                @Override public void onSuccess() {
                                    //저장된 프로필을 받아오면 ProgressBar가 보이지 않도록 한다.
                                    pb.setVisibility(View.GONE);
                                }
                            });
                }
            }

            //프로필 읽어오기를 실패했을 때
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        //카메라 권한 요청
        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            //권한이 있는지 없는지 확인
            //권한이 없으면 권한이 필요한 이유에 대한 설명을 표시해야 하는지 확인하고, 설명이 필요없으면 권한을 요청
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            //이미 권한이 부여됨
            // Permission has already been granted
        }

        ivUser = (ImageView)root.findViewById(R.id.ivUser);
        //setOnClickListener를 이용해서 이미지뷰를 클릭할 수 있도록 한다.
        //이미지뷰를 클릭했을 때 갤러리에서 사진을 선택할 수 있도록 한다.
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //갤러리에서 이미지를 가져옴
                Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(in, REQUEST_IMAGE_CODE);
            }
        });

        Button btnLogout = (Button)root.findViewById(R.id.btnLogout);
        //로그아웃 버튼을 눌렀을 때 로그아웃
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사용자를 로그아웃시키기 위해 signOut 호출
                FirebaseAuth.getInstance().signOut();
                //로그아웃 및 종료
                getActivity().finish();
            }
        });
        // actionBar를 프로필로 지정
        ActionBar actionBar=((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Profile");
        return root;
    }

    //이미지 업로드 할 파일 위치 만들기
    public void uploadImage(){
        //스토리지에 저장, 다운로드 url 가져오기
        //파일을 업로드한 후 StroageReference에서
        //getDownloadUrl()메서드를 호출하면 파일 다운로드 URL을 가져올 수 있음
        final StorageReference riversRef = mStorageRef.child("usersProfile").child(stEmail+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //putBytes()는 byte[]를 취하고 UploadTask를 반환하여 이 반환 객체를 사용하여 업로드를 관리
        UploadTask uploadTask = riversRef.putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String photoUrl = String.valueOf(downloadUri);
                    Log.d("url", photoUrl);

                    //데이터베이스 인스턴스를 검색하고 쓸 위치 참조
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //데이터베이슬르 읽기 위해 DatabaseReference 가져오기
                    DatabaseReference myRef = database.getReference("users").child(gps);

                    //Hashtable로 객체 생성
                    Hashtable<String, String> profile = new Hashtable<String, String>();
                    profile.put("email", stEmail); // put(Object key, Object value)메소드, 데이터 삽입
                    profile.put("key", stUid); // put(Object key, Object value)메소드, 데이터 삽입
                    profile.put("photo", photoUrl); // put(Object key, Object value)메소드, 데이터 삽입
                    myRef.child(stUid).setValue(profile); //setValue()를 사용하여 지정된 위치에서 모든 데이터 덮어쓰기

                    //addListenerForSingleValueEvent() 메소드로 데이터 한 번 읽기
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String profile = dataSnapshot.getValue().toString();
                            Log.d("Profile", profile);

                            if (dataSnapshot != null) {
                                //이미지 업로드 성공 메시지
                                Toast.makeText(getActivity(), "프로필 업로드 성공", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    //onActivityResult() 콜백에서 ivUser.setOnClickListener에서의 결과를 수신한다.
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //REQUEST_IMAGE_CODE가 일치하면
        if (requestCode == REQUEST_IMAGE_CODE) {
            //Uri 형태의 이미지
            Uri image = data.getData();

            //예외처리
            try {
                //비트맵을 이용함
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
                //ivUser에 이미지 출력하기
                ivUser.setImageBitmap(bitmap);
                //이미지 업로드
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //권한 요청 응답 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}