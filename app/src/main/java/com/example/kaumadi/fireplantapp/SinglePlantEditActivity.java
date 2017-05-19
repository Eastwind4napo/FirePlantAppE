package com.example.kaumadi.fireplantapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
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

public class SinglePlantEditActivity extends AppCompatActivity {

    private Uri resultUri;

    private String mPost=null;
    private String mKey=null;
    private DatabaseReference databasePlants;
    private StorageReference storage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseGreens;

    private EditText mSinglePlantId;
    private EditText mSinglePlantName;
    private EditText mSinglePlantDate;
    private EditText mSinglePlantSci;
    private EditText mSinglePlantGenus;
    private ImageView mSinglePlantImage;
    private Button mSinglePlantButton;

    private DatabaseReference current_user_db;
    private ProgressDialog progress;
    private static final int GALLERY_REQUEST=1;

    Toolbar toolbar;

   // String post_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_plant_edit);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();
        progress=new ProgressDialog(this);

        storage= FirebaseStorage.getInstance().getReference();
        databasePlants= FirebaseDatabase.getInstance().getReference("plants");
        databasePlants.keepSynced(true);
        String id=firebaseAuth.getCurrentUser().getUid();
        current_user_db=databasePlants.child(id);

        mPost=getIntent().getExtras().getString("plant_id");
        mKey=getIntent().getExtras().getString("green_id");

        databaseGreens=current_user_db.child(mKey);

        mSinglePlantId=(EditText) findViewById(R.id.txtSinglePlantEditId);
        mSinglePlantName=(EditText)findViewById(R.id.txtSinglePlantEditName);
        mSinglePlantDate=(EditText)findViewById(R.id.txtSinglePlantEditDate);
        mSinglePlantSci=(EditText)findViewById(R.id.txtSinglePlantEditScientifictName);
        mSinglePlantGenus=(EditText)findViewById(R.id.txtSinglePlantEditGenus);
        mSinglePlantImage=(ImageView)findViewById(R.id.imgSinglePlantEditSelect);
        mSinglePlantButton=(Button)findViewById(R.id.btnSaveEdit) ;

        databaseGreens.child(mPost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_id=(String)dataSnapshot.child("id").getValue();
                String post_name=(String)dataSnapshot.child("name").getValue();
                String post_date=(String)dataSnapshot.child("date").getValue();
                final String post_image=(String)dataSnapshot.child("image").getValue();
                String post_sci=(String)dataSnapshot.child("sci").getValue();
                String post_genus=(String)dataSnapshot.child("genus").getValue();

                mSinglePlantId.setText(post_id);
                mSinglePlantName.setText(post_name);
                mSinglePlantDate.setText(post_date);
                mSinglePlantSci.setText(post_sci);
                mSinglePlantGenus.setText(post_genus);

                Picasso.with(SinglePlantEditActivity.this).load(post_image).into(mSinglePlantImage);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSinglePlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //current_user_db.child(mPost).removeValue();
                addNewData();
            }
        });

        mSinglePlantImage.setOnClickListener(new View.OnClickListener() {
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


        final String id_val = mSinglePlantId.getText().toString().trim();
        final String name_val = mSinglePlantName.getText().toString().trim();
        final String date_val = mSinglePlantDate.getText().toString().trim();
        final String sci_val = mSinglePlantSci.getText().toString().trim();
        final String genus_val = mSinglePlantGenus.getText().toString().trim();

        if (!TextUtils.isEmpty(id_val) && !TextUtils.isEmpty(name_val) && !TextUtils.isEmpty(date_val) && !TextUtils.isEmpty(sci_val) && !TextUtils.isEmpty(genus_val)) {

            if(resultUri !=null) {
                StorageReference filepath = storage.child("Blog_Images").child(resultUri.getLastPathSegment());

                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        DatabaseReference newPost = databaseGreens.child(mPost);

                        newPost.child("id").removeValue();
                        newPost.child("name").removeValue();
                        newPost.child("date").removeValue();
                        newPost.child("sci").removeValue();
                        newPost.child("genus").removeValue();
                        newPost.child("image").removeValue();


                        newPost.child("id").setValue(id_val);
                        newPost.child("name").setValue(name_val);
                        newPost.child("date").setValue(date_val);
                        newPost.child("sci").setValue(sci_val);
                        newPost.child("genus").setValue(genus_val);
                        newPost.child("image").setValue(downloadUrl.toString());

                        // newPost.equals(current_user_db.child(id_val));

                        progress.dismiss();

                        Toast.makeText(SinglePlantEditActivity.this, "Save Succesfully...", Toast.LENGTH_LONG).show();
                        clearFields();

                        Intent mainIntent = new Intent(SinglePlantEditActivity.this, MyGardenActivity.class);
                        mainIntent.putExtra("green_id", mKey);
                        startActivity(mainIntent);
                    }
                });
            }else
            {
                DatabaseReference newPost = databaseGreens.child(mPost);

                newPost.child("id").removeValue();
                newPost.child("name").removeValue();
                newPost.child("date").removeValue();
                newPost.child("sci").removeValue();
                newPost.child("genus").removeValue();

                newPost.child("id").setValue(id_val);
                newPost.child("name").setValue(name_val);
                newPost.child("date").setValue(date_val);
                newPost.child("sci").setValue(sci_val);
                newPost.child("genus").setValue(genus_val);

                progress.dismiss();

                Toast.makeText(SinglePlantEditActivity.this, "Save Succesfully...", Toast.LENGTH_LONG).show();
                clearFields();

                Intent mainIntent = new Intent(SinglePlantEditActivity.this, MyGardenActivity.class);
                mainIntent.putExtra("green_id", mKey);
                startActivity(mainIntent);
            }

        }
        else
        {
            progress.dismiss();
            Toast.makeText(SinglePlantEditActivity.this, "Fill all fields...", Toast.LENGTH_LONG).show();
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
                mSinglePlantImage.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }

    private void clearFields() {
        mSinglePlantId.setText(null);
        mSinglePlantName.setText(null);
        mSinglePlantDate.setText(null);
        mSinglePlantImage.setImageURI(null);
        mSinglePlantSci.setText(null);
        mSinglePlantGenus.setText(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_back)
        {
            Intent greenIntent=new Intent(SinglePlantEditActivity.this,SinglePlantActivity.class);
            greenIntent.putExtra("green_id",mKey);
            greenIntent.putExtra("plant_id",mPost);
            greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(greenIntent);
        }
        else if(item.getItemId()==R.id.action_search)
        {

        }
        return super.onOptionsItemSelected(item);
    }


}
