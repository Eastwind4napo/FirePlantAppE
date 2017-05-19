package com.example.kaumadi.fireplantapp;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;


public class GreenHouseRegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private ImageButton imageSelect;
    private EditText editTextName;
    private EditText editTextLocation;
    private EditText editTextDate;
    private Button buttonSave;

    //private  Uri imageUri=null;
    private Uri resultUri=null;

    private StorageReference storage;
    private DatabaseReference databaseGreens;
    private DatabaseReference databaseUsers;
    private FirebaseAuth firebaseAuth;

    private static final int GALLERY_REQUEST=1;

    private ProgressDialog progress;

    Toolbar toolbar;

    int day,month,year;
    int day_final,month_final,year_final;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_house_register);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storage= FirebaseStorage.getInstance().getReference();
        databaseGreens= FirebaseDatabase.getInstance().getReference("greens");
        databaseGreens.keepSynced(true);
        databaseUsers= FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.keepSynced(true);
        firebaseAuth=FirebaseAuth.getInstance();



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
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c=Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month=c.get(Calendar.MONTH);
                day=c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(GreenHouseRegisterActivity.this,GreenHouseRegisterActivity.this,year,month,day);
                datePickerDialog.show();

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
        final String name_val=editTextName.getText().toString().trim();
        final String location_val=editTextLocation.getText().toString().trim();
        final String date_val=editTextDate.getText().toString().trim();

        if(!TextUtils.isEmpty(name_val) && !TextUtils.isEmpty(location_val) && !TextUtils.isEmpty(date_val)&&  resultUri !=null) {
            StorageReference filepath=storage.child("Green_Images").child(resultUri.getLastPathSegment());
            filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    String id=firebaseAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db=databaseGreens.child(id);

                    DatabaseReference newPost=current_user_db.push();


                  //  DatabaseReference newPost=current_user_db.child(id_val);

                    newPost.child("greenName").setValue(name_val);
                    newPost.child("greenLocation").setValue(location_val);
                    newPost.child("greenDate").setValue(date_val);
                    newPost.child("greenImage").setValue(downloadUrl.toString());

                    progress.dismiss();

                    Toast.makeText(GreenHouseRegisterActivity.this, "Save Succesfully...", Toast.LENGTH_LONG).show();
                    clearFields();


                }
            });
        }
        else {
            progress.dismiss();
            Toast.makeText(GreenHouseRegisterActivity.this, "Fill all fields...", Toast.LENGTH_LONG).show();
        }

    }
    private void clearFields() {

        editTextName.setText(null);
        editTextDate.setText(null);
        imageSelect.setImageURI(null);
        editTextLocation.setText(null);

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
                imageSelect.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
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
            startActivity(new Intent(GreenHouseRegisterActivity.this,GreenHouseActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        year_final=i;
        month_final=i1+1;
        day_final=i2;
        editTextDate.setText(year_final+"/"+month_final+"/"+day_final);

    }
}
