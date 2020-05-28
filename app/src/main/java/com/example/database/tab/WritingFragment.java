package com.example.database.tab;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.database.ImageUploadInfo;
import com.example.database.MainActivity;
import com.example.database.R;
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

    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";

    // Root Database Name for Firebase Database.


    // Creating button.
    Button ChooseButton, UploadButton;

    // Creating EditText.
    EditText ImageName ;
    EditText Text;
    // Creating ImageView.
    ImageView SelectImage;


    // Creating URI.
    Uri FilePathUri;
    String stUid,gps;

    Spinner yearSpinner;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;


    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;

    ProgressDialog progressDialog ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_writing, container, false);


        SharedPreferences sharedPref = getActivity().getSharedPreferences("shared" , Context.MODE_PRIVATE);
        stUid = sharedPref.getString("key", "");
        gps=sharedPref.getString("gps","");



        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();
        yearSpinner = root.findViewById(R.id.spinner_year);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.date_year, android.R.layout.simple_spinner_item);
        yearSpinner.setAdapter(yearAdapter);


        // databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child(stUid).child("imageupload");
        //Assign ID'S to button.
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        ChooseButton = root.findViewById(R.id.ButtonChooseImage);
        UploadButton = root.findViewById(R.id.ButtonUploadImage);

        // Assign ID's to EditText.
        ImageName = root.findViewById(R.id.imageNameEditText);
        Text = root.findViewById(R.id.Text);





        // Assign ID'S to image view.
        SelectImage =root.findViewById(R.id.ShowImageView);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(getActivity());

        // Adding click listener to Choose image button.
        ChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Creating intent.
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

            }
        });





        // Adding click listener to Upload image button.
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to upload selected image on Firebase storage.
                UploadImageFileToFirebaseStorage();




            }
        });
        ActionBar actionBar=((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Writing");
        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                SelectImage.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                ChooseButton.setText("Image Selected");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }


    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));


            UploadTask uploadTask = storageReference2nd.putFile(FilePathUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return storageReference2nd.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String Url = String.valueOf(downloadUri);
                        Log.d("url", Url);

                        // Write a message to the database


                        String TempImageName = ImageName.getText().toString().trim();
                        String TempText = Text.getText().toString().trim();
                        String category = yearSpinner.getSelectedItem().toString();

                        progressDialog.dismiss();
                        realHomeFragment = new RealHomeFragment();
                        RealHomeFragment fragHome = new RealHomeFragment();
                        // Showing toast message after done uploading.
                        Toast.makeText(getContext().getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction().add((R.id.nav_host_fragment), realHomeFragment).addToBackStack(null).commit();


                        final ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, Url, TempText, category);


                        //users 밑 gps 밑 자신의 아이디(stUid) 밑 imageupload 밑에 키값을 부여합니다.
                        String ImageUploadId = databaseReference.child(gps).child(stUid).child("imageupload").push().getKey();


                        databaseReference.child(gps).child(stUid).child("imageupload").child(ImageUploadId).setValue(imageUploadInfo);


                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }
}