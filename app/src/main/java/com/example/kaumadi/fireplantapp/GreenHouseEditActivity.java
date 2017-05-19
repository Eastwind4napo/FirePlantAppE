package com.example.kaumadi.fireplantapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class GreenHouseEditActivity extends AppCompatActivity {
    private Uri resultUri;
    private String mgreen_id=null;
    private StorageReference storage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseGreens;

    private EditText mSingleGreenName;
    private EditText mSingleGreenLocation;
    private EditText mSingleGreenDuration;
    private ImageView mSingleGreenImage;
    private Button mSingleGreenButton;

    private DatabaseReference current_user_db;
    private ProgressDialog progress;
    private static final int GALLERY_REQUEST=1;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_house_edit);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();
        progress=new ProgressDialog(this);

        storage= FirebaseStorage.getInstance().getReference();
        databaseGreens= FirebaseDatabase.getInstance().getReference("greens");
        databaseGreens.keepSynced(true);
        String id=firebaseAuth.getCurrentUser().getUid();
        current_user_db=databaseGreens.child(id);

        mgreen_id=getIntent().getExtras().getString("green_id");

        mSingleGreenName=(EditText)findViewById(R.id.txtSingleGreenEditName);
        mSingleGreenDuration=(EditText)findViewById(R.id.txtSingleGreenEditDuration);
        mSingleGreenLocation=(EditText)findViewById(R.id.txtSingleGreenEditLocation);
        mSingleGreenImage=(ImageView)findViewById(R.id.imgSingleGreenEditSelect);
        mSingleGreenButton=(Button)findViewById(R.id.btnSaveEdit) ;

        current_user_db.child(mgreen_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_name=(String)dataSnapshot.child("greenName").getValue();
                String post_location=(String)dataSnapshot.child("greenLocation").getValue();
                String post_duration=(String)dataSnapshot.child("greenDate").getValue();
                final String post_image=(String)dataSnapshot.child("greenImage").getValue();


                mSingleGreenName.setText(post_name);
                mSingleGreenLocation.setText(post_location);
                mSingleGreenDuration.setText(post_duration);


                Picasso.with(GreenHouseEditActivity.this).load(post_image).into(mSingleGreenImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSingleGreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //current_user_db.child(mPost).removeValue();
                addNewData();
            }
        });

        mSingleGreenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });
    }

    private void addNewData() {

        progress.setMessage("Saving new data...");
        progress.show();



        final String name_val = mSingleGreenName.getText().toString().trim();
        final String location_val = mSingleGreenLocation.getText().toString().trim();
        final String duration_val = mSingleGreenDuration.getText().toString().trim();

        if (!TextUtils.isEmpty(name_val) && !TextUtils.isEmpty(location_val) && !TextUtils.isEmpty(duration_val)) {

            if(resultUri !=null) {
                StorageReference filepath = storage.child("Blog_Images").child(resultUri.getLastPathSegment());

                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        DatabaseReference newPost = current_user_db.child(mgreen_id);


                        newPost.child("greenName").removeValue();
                        newPost.child("greenLocation").removeValue();
                        newPost.child("greenDate").removeValue();
                        newPost.child("greenImage").removeValue();



                        newPost.child("greenName").setValue(name_val);
                        newPost.child("greenLocation").setValue(location_val);
                        newPost.child("greenDate").setValue(duration_val);
                        newPost.child("greenImage").setValue(downloadUrl.toString());

                        // newPost.equals(current_user_db.child(id_val));

                        progress.dismiss();

                        Toast.makeText(GreenHouseEditActivity.this, "Save Succesfully...", Toast.LENGTH_LONG).show();
                        clearFields();

                        Intent mainIntent = new Intent(GreenHouseEditActivity.this, GreenHouseActivity.class);
                        mainIntent.putExtra("green_id", mgreen_id);
                        startActivity(mainIntent);
                    }
                });
            }else
            {
                DatabaseReference newPost = current_user_db.child(mgreen_id);


                newPost.child("greenName").removeValue();
                newPost.child("greenLocation").removeValue();
                newPost.child("greenDate").removeValue();

                newPost.child("greenName").setValue(name_val);
                newPost.child("greenLocation").setValue(location_val);
                newPost.child("greenDate").setValue(duration_val);


                progress.dismiss();

                Toast.makeText(GreenHouseEditActivity.this, "Save Succesfully...", Toast.LENGTH_LONG).show();
                clearFields();

                Intent mainIntent = new Intent(GreenHouseEditActivity.this, GreenHouseActivity.class);
                mainIntent.putExtra("green_id", mgreen_id);
                startActivity(mainIntent);
            }

        }
        else
        {
            progress.dismiss();
            Toast.makeText(GreenHouseEditActivity.this, "Fill all fields...", Toast.LENGTH_LONG).show();
        }
    }

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
                mSingleGreenImage.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }

    private void clearFields() {

        mSingleGreenName.setText(null);
        mSingleGreenLocation.setText(null);
        mSingleGreenImage.setImageURI(null);
        mSingleGreenDuration.setText(null);

    }


}
