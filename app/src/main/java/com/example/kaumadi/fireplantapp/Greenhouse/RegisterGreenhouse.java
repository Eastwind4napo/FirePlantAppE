package com.example.kaumadi.fireplantapp.Greenhouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kaumadi.fireplantapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class RegisterGreenhouse extends AppCompatActivity {

    private ImageButton imageSelect;
    private EditText editTextName;
    private EditText editTextLocation;
    private EditText editTextDate;
    private Button buttonSave;

    //private  Uri imageUri=null;
    private Uri resultUri = null;

    private StorageReference storage;
    private DatabaseReference databaseGreens;
    private DatabaseReference databaseUsers;
    private FirebaseAuth mAuth;

    private static final int GALLERY_REQUEST=1;

    String imageUrl;
    String name_val;
    String location_val;
    String date_val;

    ProgressDialog progress;
    Greenhouses greenhouse;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_greenhouse);

       // toolbar=(Toolbar)findViewById(R.id.toolbarmain);
       // setSupportActionBar(toolbar);

        FirebaseApp.initializeApp(this);
        storage= FirebaseStorage.getInstance().getReference();
        databaseGreens= FirebaseDatabase.getInstance().getReference("Greenhouse");
        //databaseGreens.keepSynced(true);
        databaseUsers= FirebaseDatabase.getInstance().getReference("Users");
        //databaseUsers.keepSynced(true);
        mAuth= FirebaseAuth.getInstance();


        imageSelect=(ImageButton)findViewById(R.id.imgSelect);
        editTextName=(EditText)findViewById(R.id.txtGreenName);
        editTextLocation=(EditText)findViewById(R.id.txtGreenLocation);
        editTextDate=(EditText)findViewById(R.id.txtGreenDate);
        buttonSave=(Button)findViewById(R.id.btnSave);

        progress=new ProgressDialog(this);

        imageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
    }

    private void startPosting() {

        progress.setMessage("GreenHouse Registering...");
        progress.show();

        name_val=editTextName.getText().toString().trim();
        location_val=editTextLocation.getText().toString().trim();
        date_val=editTextDate.getText().toString().trim();


        if(!TextUtils.isEmpty(name_val) && !TextUtils.isEmpty(location_val) && !TextUtils.isEmpty(date_val) && resultUri!=null) {

            StorageReference filepath=storage.child("Green_Images").child(resultUri.getLastPathSegment());
            filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    imageUrl=downloadUrl.toString();
                    String id = mAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db = databaseGreens.child(id);
                    DatabaseReference newPost = current_user_db.push();

                    //DatabaseReference newPost=current_user_db.child(id_val);

                    newPost.child("greenName").setValue(name_val);
                    newPost.child("greenLocation").setValue(location_val);
                    newPost.child("greenDuration").setValue(date_val);
                    newPost.child("greenImage").setValue(downloadUrl.toString());

                    String image = resultUri.toString();
                    SharedPreferences myPrefrence = getSharedPreferences("App_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefrence.edit();
                    editor.putString("imagePreferance", image);
                    editor.commit();



//                    String id = mAuth.getCurrentUser().getUid();
//                    createGreenhouse(id,name_val,location_val,imageUrl,date_val);

                    progress.dismiss();

                    Toast.makeText(RegisterGreenhouse.this, "Save Succesfully...", Toast.LENGTH_LONG).show();


                    RegisterGreenhouse.this.startActivity(new Intent(RegisterGreenhouse.this, GreenhouseActivity.class));
                }
            });

        } else {
            progress.dismiss();
            Toast.makeText(RegisterGreenhouse.this, "Select Photo & Fill all fields...", Toast.LENGTH_LONG).show();
        }

    }

//    private void createGreenhouse(String id, String name_val, String location_val,String imageUrl, String date_val) {
//        greenhouse = new Greenhouses(id,name_val,location_val,imageUrl,date_val);
//        databaseGreens.child("GreenHouse").child(id).setValue(greenhouse);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            resultUri=data.getData();
            CropImage.activity(resultUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(this);
            //imageSelect.setImageURI(imageUri);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                resultUri = result.getUri();
                imageSelect.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }

}
