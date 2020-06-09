package com.example.database;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.example.database.tab.RealHomeFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class WritingFragment extends Fragment {

    private RealHomeFragment realHomeFragment;

    String Storage_Path = "All_Image_Uploads/";

    Button ChooseButton, UploadButton;

    EditText ImageName ;
    EditText Text;
    ImageView SelectImage;

    Uri FilePathUri;
    String stUid,gps;
    String check;
    Spinner yearSpinner;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    int Image_Request_Code = 7;

    ProgressDialog progressDialog ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.fragment_writing, container, false);

        //SHaredPreferences를 사용하여 key,gps값 가져오기
        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared" , Context.MODE_PRIVATE);
        stUid = sharedPref.getString("key", "");
        gps=sharedPref.getString("gps","");

        storageReference = FirebaseStorage.getInstance().getReference();
        yearSpinner = root.findViewById(R.id.spinner_year);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.date_year, android.R.layout.simple_spinner_item);
        yearSpinner.setAdapter(yearAdapter);

        //users 경로
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        ChooseButton = root.findViewById(R.id.ButtonChooseImage);
        UploadButton = root.findViewById(R.id.ButtonUploadImage);

        ImageName = root.findViewById(R.id.imageNameEditText);
        Text = root.findViewById(R.id.Text);

        SelectImage =root.findViewById(R.id.ShowImageView);

        progressDialog = new ProgressDialog(getActivity());

        //버튼 클릭시
        ChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //사진 고를 수 있는 페이지로 넘어가기
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

            }
        });

        //업로드 버튼 누렸을 시
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UploadImageFileToFirebaseStorage();
            }
        });

        // actionBar로 writing 지정하기
        ActionBar actionBar=((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Writing");
        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                //bitmap를 사용하여 이미지 띄우기
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), FilePathUri);

                SelectImage.setImageBitmap(bitmap);

                ChooseButton.setText("Image Selected");

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    public void UploadImageFileToFirebaseStorage() {
        //유알엘이 없을 시
        if (FilePathUri != null) {

            progressDialog.setTitle("Image is Uploading...");

            progressDialog.show();

            final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            UploadTask uploadTask = storageReference2nd.putFile(FilePathUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageReference2nd.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String Url = String.valueOf(downloadUri);

                        //사진에 대한 내용
                        String TempImageName = ImageName.getText().toString().trim();
                        String TempText = Text.getText().toString().trim();
                        String category = yearSpinner.getSelectedItem().toString();

                        progressDialog.dismiss();
                        realHomeFragment = new RealHomeFragment();
                        RealHomeFragment fragHome = new RealHomeFragment();
                        //이름이 없을 시
                        if( TempImageName.equals("")){
                            Toast.makeText(getContext().getApplicationContext(), "Please insert ImageName", Toast.LENGTH_LONG).show();
                        }
                        //내용이 없을 시
                        else if(TempText.equals("")){
                            Toast.makeText(getContext().getApplicationContext(), "Please insert Text", Toast.LENGTH_LONG).show();}
                        //다 작성 했을 시
                        else{
                            Toast.makeText(getContext().getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().beginTransaction().add((R.id.nav_host_fragment), realHomeFragment).addToBackStack(null).commit();

                            final ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, Url, TempText, category);
                            //users 밑 gps 밑 자신의 아이디(stUid) 밑 imageupload 밑에 키값을 부여합니다.
                            String ImageUploadId = databaseReference.child(gps).child(stUid).child("imageupload").push().getKey();
                            //users 밑 gps 밑 자신의 아이디(stUid) 밑 imageupload 밑에 내용을 저장합니다.
                            databaseReference.child(gps).child(stUid).child("imageupload").child(ImageUploadId).setValue(imageUploadInfo);
                            // 작성 후 빈 칸으로 두기
                            ImageName.setText("");
                            SelectImage.setImageURI(null);
                            Text.setText("");
                        }

                    } else {

                   }
                }
            });
        }
        else{
            Toast.makeText(getContext().getApplicationContext(), "Image select please ", Toast.LENGTH_LONG).show();
        }

    }
}
