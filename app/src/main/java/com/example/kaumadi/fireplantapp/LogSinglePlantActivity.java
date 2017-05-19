package com.example.kaumadi.fireplantapp;

        import android.app.Activity;
        import android.os.Environment;
        import android.provider.DocumentsContract;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.Toolbar;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.squareup.picasso.Picasso;



        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;


public class LogSinglePlantActivity extends AppCompatActivity {



    private String mKey=null;
    private String mGreenKey=null;
    private String treat=null;
    private DatabaseReference databasePlants;

    private FirebaseAuth firebaseAuth;

    private TextView mLogSinglePlantId;
    private TextView mLogSinglePlantSci;
    private TextView mLogSinglePlantDate;
    private ImageView mLogSinglePlantImage;

   private EditText mLogSinglePlantNo;
    private EditText mLogSinglePlantLength;
    private EditText mLogSinglePlantDiameter;
    private EditText mLogSinglePlantLeave;
    private EditText mLogSinglePlantChemical;
    private EditText mLogSinglePlantSFW;
    private EditText mLogSinglePlantSDW;
    private EditText mLogSinglePlantRFW;
    private EditText mLogSinglePlantRDW;

    private EditText mfileName;

    private Button mLogSinglePlantSave;

    private DatabaseReference current_user_db,current_plant,currentGreen,current_treatment;

    private ProgressDialog progress;
    //  private FloatingActionButton mFab;

    Toolbar toolbar;
    public String path=Environment.getExternalStorageDirectory() + "/greenhouse";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_single_plant);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();

        progress=new ProgressDialog(this);

        databasePlants= FirebaseDatabase.getInstance().getReference("plants");
        databasePlants.keepSynced(true);
        String id=firebaseAuth.getCurrentUser().getUid();
        current_user_db=databasePlants.child(id);

        mKey=getIntent().getExtras().getString("key_id");
        mGreenKey=getIntent().getExtras().getString("green_id");

        mLogSinglePlantId=(TextView)findViewById(R.id.txtLogPlantId);
        mLogSinglePlantSci=(TextView)findViewById(R.id.txtLogScientifictName);
        mLogSinglePlantDate=(TextView)findViewById(R.id.txtLogDate);
        mLogSinglePlantImage=(ImageView)findViewById(R.id.imgSinglePlantSelect);


        mLogSinglePlantNo=(EditText)findViewById(R.id.txtLogNo);
        mLogSinglePlantLength=(EditText)findViewById(R.id.txtLogLegth);
        mLogSinglePlantDiameter=(EditText)findViewById(R.id.txtLogDiameter);
        mLogSinglePlantLeave=(EditText)findViewById(R.id.txtLogNoLeaves);
        mLogSinglePlantChemical=(EditText)findViewById(R.id.txtLogChlorophyll);
        mLogSinglePlantSFW=(EditText)findViewById(R.id.txtLogShootFreshWeigth);
        mLogSinglePlantSDW=(EditText)findViewById(R.id.txtLogShootDryWeigth);
        mLogSinglePlantRFW=(EditText)findViewById(R.id.txtLogRootFreshWeigth);
        mLogSinglePlantRDW=(EditText)findViewById(R.id.txtLogRootDryWeigth);

        mfileName=(EditText)findViewById(R.id.txtFileName);


        mLogSinglePlantSave=(Button)findViewById(R.id.btnSaveDetails);



        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
        final String strDate = mdformat.format(calendar.getTime());

        currentGreen=current_user_db.child(mGreenKey);

        currentGreen.child(mKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_id=(String)dataSnapshot.child("id").getValue();
                final String post_image=(String)dataSnapshot.child("image").getValue();
                String post_sci=(String)dataSnapshot.child("sci").getValue();


                mLogSinglePlantId.setText(post_id);
                mLogSinglePlantSci.setText(post_sci);
                mLogSinglePlantDate.setText(strDate);

                Picasso.with(LogSinglePlantActivity.this).load(post_image).into(mLogSinglePlantImage);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mLogSinglePlantSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPosting();

            }
        });

    }





    private void startPosting() {

        progress.setMessage("Saving Plant Details...");
        progress.show();


        final String no_val = mLogSinglePlantNo.getText().toString().trim();
        final String length_val = mLogSinglePlantLength.getText().toString().trim();
        final String diameter_val =mLogSinglePlantDiameter.getText().toString().trim();
        final String leave_val = mLogSinglePlantLeave.getText().toString().trim();
        final String chemical_val = mLogSinglePlantChemical.getText().toString().trim();
        final String sfw_val = mLogSinglePlantSFW.getText().toString().trim();
        final String sdw_val =mLogSinglePlantSDW.getText().toString().trim();
        final String rfw_val = mLogSinglePlantRFW.getText().toString().trim();
        final String rdw_val = mLogSinglePlantRDW.getText().toString().trim();
        final String date_val=mLogSinglePlantDate.getText().toString().trim();

        final String fileName_val=mfileName.getText().toString().trim();


        // if (!TextUtils.isEmpty(no_val) && !TextUtils.isEmpty(length_val) && !TextUtils.isEmpty(diameter_val) && !TextUtils.isEmpty(leave_val) && !TextUtils.isEmpty(chemical_val) && !TextUtils.isEmpty(sfw_val) && !TextUtils.isEmpty(sdw_val) && !TextUtils.isEmpty(rfw_val) && !TextUtils.isEmpty(sdw_val)) {
        if(!TextUtils.isEmpty(fileName_val) && !TextUtils.isEmpty(no_val) && !TextUtils.isEmpty(length_val) && !TextUtils.isEmpty(diameter_val) && !TextUtils.isEmpty(leave_val) && !TextUtils.isEmpty(chemical_val) ) {


            currentGreen = current_user_db.child(mGreenKey);
            current_plant = currentGreen.child(mKey);


            current_treatment = current_plant.child(no_val);
            current_treatment.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.child("no").exists())
                    {
                        current_treatment.child("no").setValue(no_val);
                        current_treatment.child("length").setValue(length_val);
                        current_treatment.child("diameter").setValue(diameter_val);
                        current_treatment.child("leave").setValue(leave_val);
                        current_treatment.child("chemical").setValue(chemical_val);
                        current_treatment.child("sfw").setValue(sfw_val);
                        current_treatment.child("sdw").setValue(sdw_val);
                        current_treatment.child("rfw").setValue(rfw_val);
                        current_treatment.child("rdw").setValue(rdw_val);
                        current_treatment.child("date").setValue(date_val);

                        progress.dismiss();

                        Toast.makeText(LogSinglePlantActivity.this, "Save Succesfully...", Toast.LENGTH_LONG).show();
                        clearFields();
                        return;

                    }
                    else {
                        progress.dismiss();
                        Toast.makeText(LogSinglePlantActivity.this, no_val + " treatment already done...", Toast.LENGTH_LONG).show();
                        mLogSinglePlantNo.setText(null);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else
        {
            progress.dismiss();
            Toast.makeText(LogSinglePlantActivity.this, "Enter the first 5 fields and file name...", Toast.LENGTH_LONG).show();
        }
    }





    private void clearFields() {

        mLogSinglePlantNo.setText(null);
        mLogSinglePlantLength.setText(null);
        mLogSinglePlantDiameter.setText(null);
        mLogSinglePlantLeave.setText(null);
        mLogSinglePlantChemical.setText(null);
        mLogSinglePlantSFW.setText(null);
        mLogSinglePlantSDW.setText(null);
        mLogSinglePlantRFW.setText(null);
        mLogSinglePlantRDW.setText(null);
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* if(item.getItemId()==R.id.action_back) {
            // startActivity(new Intent(LogSinglePlantActivity.this, LogActivity.class));
            Intent mainIntent=new Intent(LogSinglePlantActivity.this,LogActivity.class);
            mainIntent.putExtra("green_id",mGreenKey);
            startActivity(mainIntent);
        }*/
        if(item.getItemId()==R.id.action_add)
        {
            Intent greenIntent=new Intent(LogSinglePlantActivity.this,LogSinglePlantAdd.class);
            greenIntent.putExtra("green_id",mGreenKey);
            greenIntent.putExtra("key_id",mKey);
            greenIntent.putExtra("treat_id",treat);
            greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(greenIntent);
        }
        else if(item.getItemId()==R.id.action_histry)
        {
            Intent greenIntent=new Intent(LogSinglePlantActivity.this,LogSelectActivity.class);
            greenIntent.putExtra("green_id",mGreenKey);
            greenIntent.putExtra("plant_id",mKey);
            greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(greenIntent);
        }

        return super.onOptionsItemSelected(item);
    }


}
