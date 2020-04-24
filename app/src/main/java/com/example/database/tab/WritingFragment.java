package com.example.database.tab;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.database.ImageUploadInfo;
import com.example.database.R;
import com.example.database.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class WritingFragment extends Fragment {
    public final String Database_Path = "All_Image_Uploads_Database";

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






        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();
        yearSpinner = root.findViewById(R.id.spinner_year);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.date_year, android.R.layout.simple_spinner_item);
        yearSpinner.setAdapter(yearAdapter);
        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        //Assign ID'S to button.
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
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            String TempImageName = ImageName.getText().toString().trim();
                            String TempText=Text.getText().toString().trim();
                            String category =yearSpinner.getSelectedItem().toString();
                            //String Url= taskSnapshot.getDownloadUrl().toString();
String Url="d";
                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();
                            realHomeFragment= new RealHomeFragment();
                            RealHomeFragment fragHome = new RealHomeFragment();
                            // Showing toast message after done uploading.
                            Toast.makeText(getContext().getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().beginTransaction().add((R.id.main_frame),realHomeFragment).addToBackStack(null).commit();

                           // Intent in = new Intent(getActivity(), RealHomeFragment.class);
                           // startActivity(in);

                            // @SuppressWarnings("VisibleForTests")
                            final ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName,Url,TempText,category);


                            // Getting image upload ID.
                            String ImageUploadId = databaseReference.push().getKey();


                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);


                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });

        }
        else {

            Toast.makeText(getActivity(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }


}
