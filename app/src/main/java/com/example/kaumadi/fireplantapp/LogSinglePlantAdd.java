package com.example.kaumadi.fireplantapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogSinglePlantAdd extends AppCompatActivity {
    private String mKey=null;
    private String mGreenKey=null;
    private String mTreat=null;
    private DatabaseReference databasePlants;

    private FirebaseAuth firebaseAuth;

    private EditText mLogSinglePlantNo;

    private EditText mLogSinglePlantedit1;
    private EditText mLogSinglePlantedit2;
    private EditText mLogSinglePlantedit3;
    private EditText mLogSinglePlantedit4;
    private EditText mLogSinglePlantedit5;

    private EditText mLogSinglePlanttext1;
    private EditText mLogSinglePlanttext2;
    private EditText mLogSinglePlanttext3;
    private EditText mLogSinglePlanttext4;
    private EditText mLogSinglePlanttext5;

    private Button mLogSinglePlantSave;

    private DatabaseReference current_user_db,current_plant,currentGreen,current_treatment;

    private ProgressDialog progress;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_single_plant_add);

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
        mTreat=getIntent().getExtras().getString("treat_id");

        mLogSinglePlantNo=(EditText)findViewById(R.id.txtNo);
        mLogSinglePlantedit1=(EditText)findViewById(R.id.edit_1);
        mLogSinglePlantedit2=(EditText)findViewById(R.id.edit_2);
        mLogSinglePlantedit3=(EditText)findViewById(R.id.edit_3);
        mLogSinglePlantedit4=(EditText)findViewById(R.id.edit_4);
        mLogSinglePlantedit5=(EditText)findViewById(R.id.edit_5);
        mLogSinglePlanttext1=(EditText)findViewById(R.id.txt_1);
        mLogSinglePlanttext2=(EditText)findViewById(R.id.txt_2);
        mLogSinglePlanttext3=(EditText)findViewById(R.id.txt_3);
        mLogSinglePlanttext4=(EditText)findViewById(R.id.txt_4);
        mLogSinglePlanttext5=(EditText)findViewById(R.id.txt_5);

        mLogSinglePlantedit1.setVisibility(View.GONE);
        mLogSinglePlantedit2.setVisibility(View.GONE);
        mLogSinglePlantedit3.setVisibility(View.GONE);
        mLogSinglePlantedit4.setVisibility(View.GONE);
        mLogSinglePlantedit5.setVisibility(View.GONE);
        mLogSinglePlanttext1.setVisibility(View.GONE);
        mLogSinglePlanttext2.setVisibility(View.GONE);
        mLogSinglePlanttext3.setVisibility(View.GONE);
        mLogSinglePlanttext4.setVisibility(View.GONE);
        mLogSinglePlanttext5.setVisibility(View.GONE);

        mLogSinglePlantNo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String num=mLogSinglePlantNo.getText().toString().trim();

                if(!TextUtils.isEmpty(num)) {
                    switch (num) {
                        case "1":
                            mLogSinglePlantedit1.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext1.setVisibility(View.VISIBLE);
                            break;
                        case "2":
                            mLogSinglePlantedit1.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext1.setVisibility(View.VISIBLE);
                            mLogSinglePlantedit2.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext2.setVisibility(View.VISIBLE);
                            break;
                        case "3":
                            mLogSinglePlantedit1.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext1.setVisibility(View.VISIBLE);
                            mLogSinglePlantedit2.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext2.setVisibility(View.VISIBLE);
                            mLogSinglePlantedit3.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext3.setVisibility(View.VISIBLE);
                            break;
                        case "4":
                            mLogSinglePlantedit1.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext1.setVisibility(View.VISIBLE);
                            mLogSinglePlantedit2.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext2.setVisibility(View.VISIBLE);
                            mLogSinglePlantedit3.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext3.setVisibility(View.VISIBLE);
                            mLogSinglePlantedit4.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext4.setVisibility(View.VISIBLE);
                            break;
                        case "5":
                            mLogSinglePlantedit1.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext1.setVisibility(View.VISIBLE);
                            mLogSinglePlantedit2.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext2.setVisibility(View.VISIBLE);
                            mLogSinglePlantedit3.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext3.setVisibility(View.VISIBLE);
                            mLogSinglePlantedit4.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext4.setVisibility(View.VISIBLE);
                            mLogSinglePlantedit5.setVisibility(View.VISIBLE);
                            mLogSinglePlanttext5.setVisibility(View.VISIBLE);
                            break;
                        default:
                            Toast.makeText(LogSinglePlantAdd.this, "no must be 1-5", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });


        mLogSinglePlantSave=(Button)findViewById(R.id.btnSaveDetails);

        currentGreen=current_user_db.child(mGreenKey);

        mLogSinglePlantSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
                Intent mainIntent=new Intent(LogSinglePlantAdd.this,LogActivity.class);
                mainIntent.putExtra("green_id",mGreenKey);
                startActivity(mainIntent);
            }
        });
    }

    private void startPosting() {

        progress.setMessage("Saving Plant Details...");
        progress.show();


        final String edit1_val = mLogSinglePlantedit1.getText().toString().trim();
        final String edit2_val = mLogSinglePlantedit2.getText().toString().trim();
        final String edit3_val = mLogSinglePlantedit3.getText().toString().trim();
        final String edit4_val = mLogSinglePlantedit4.getText().toString().trim();
        final String edit5_val = mLogSinglePlantedit5.getText().toString().trim();
        final String text1_val = mLogSinglePlanttext1.getText().toString().trim();
        final String text2_val = mLogSinglePlanttext2.getText().toString().trim();
        final String text3_val = mLogSinglePlanttext3.getText().toString().trim();
        final String text4_val = mLogSinglePlanttext4.getText().toString().trim();
        final String text5_val = mLogSinglePlanttext5.getText().toString().trim();

        final String no=mLogSinglePlantNo.getText().toString().trim();

       if (!TextUtils.isEmpty(no) )
        {
            currentGreen=current_user_db.child(mGreenKey);
            current_plant=currentGreen.child(mKey);


            current_treatment = current_plant.child(mTreat);


            switch(no) {
                case "1":
                    current_treatment.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        current_treatment.child(edit1_val).setValue(text1_val);
                        progress.dismiss();
                        Toast.makeText(LogSinglePlantAdd.this, "Save Succesfully...", Toast.LENGTH_LONG).show();
                        mLogSinglePlantedit1.setText(null);
                        mLogSinglePlanttext1.setText(null);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                    break;

                case "2":

                    current_treatment.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        current_treatment.child(edit1_val).setValue(text1_val);
                        current_treatment.child(edit2_val).setValue(text2_val);
                        progress.dismiss();
                        Toast.makeText(LogSinglePlantAdd.this, "Save Succesfully...", Toast.LENGTH_LONG).show();
                        mLogSinglePlantedit1.setText(null);
                        mLogSinglePlanttext1.setText(null);
                        mLogSinglePlantedit2.setText(null);
                        mLogSinglePlanttext2.setText(null);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                    break;

                case "3":

                    current_treatment.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            current_treatment.child(edit1_val).setValue(text1_val);
                            current_treatment.child(edit2_val).setValue(text2_val);
                            current_treatment.child(edit3_val).setValue(text3_val);
                            progress.dismiss();
                            Toast.makeText(LogSinglePlantAdd.this, "Save Succesfully...", Toast.LENGTH_LONG).show();
                            mLogSinglePlantedit1.setText(null);
                            mLogSinglePlanttext1.setText(null);
                            mLogSinglePlantedit2.setText(null);
                            mLogSinglePlanttext2.setText(null);
                            mLogSinglePlantedit3.setText(null);
                            mLogSinglePlanttext3.setText(null);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    break;

                case "4":

                    current_treatment.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            current_treatment.child(edit1_val).setValue(text1_val);
                            current_treatment.child(edit2_val).setValue(text2_val);
                            current_treatment.child(edit3_val).setValue(text3_val);
                            current_treatment.child(edit4_val).setValue(text4_val);
                            progress.dismiss();
                            Toast.makeText(LogSinglePlantAdd.this, "Save Succesfully...", Toast.LENGTH_LONG).show();
                            mLogSinglePlantedit1.setText(null);
                            mLogSinglePlanttext1.setText(null);
                            mLogSinglePlantedit2.setText(null);
                            mLogSinglePlanttext2.setText(null);
                            mLogSinglePlantedit3.setText(null);
                            mLogSinglePlanttext3.setText(null);
                            mLogSinglePlantedit4.setText(null);
                            mLogSinglePlanttext4.setText(null);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    break;

                case "5":

                    current_treatment.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            current_treatment.child(edit1_val).setValue(text1_val);
                            current_treatment.child(edit2_val).setValue(text2_val);
                            current_treatment.child(edit3_val).setValue(text2_val);
                            current_treatment.child(edit4_val).setValue(text4_val);
                            current_treatment.child(edit5_val).setValue(text5_val);
                            progress.dismiss();
                            Toast.makeText(LogSinglePlantAdd.this, "Save Succesfully...", Toast.LENGTH_LONG).show();
                            mLogSinglePlantedit1.setText(null);
                            mLogSinglePlanttext1.setText(null);
                            mLogSinglePlantedit2.setText(null);
                            mLogSinglePlanttext2.setText(null);
                            mLogSinglePlantedit3.setText(null);
                            mLogSinglePlanttext3.setText(null);
                            mLogSinglePlantedit4.setText(null);
                            mLogSinglePlanttext4.setText(null);
                            mLogSinglePlantedit5.setText(null);
                            mLogSinglePlanttext5.setText(null);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    break;
                default:
                    break;
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
       /* if(item.getItemId()==R.id.action_back) {
            Intent mainIntent=new Intent(LogSinglePlantAdd.this,LogActivity.class);
            mainIntent.putExtra("green_id",mGreenKey);
            startActivity(mainIntent);
        }*/

        return super.onOptionsItemSelected(item);
    }


}
