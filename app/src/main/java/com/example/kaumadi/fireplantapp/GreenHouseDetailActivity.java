package com.example.kaumadi.fireplantapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class GreenHouseDetailActivity extends AppCompatActivity {
    private String mGreen_key=null;
    private DatabaseReference databaseGreen,databasePlant;

    private FirebaseAuth firebaseAuth;

    private TextView mSingleGreenName;
    private TextView mSingleGreenLocation;
    private TextView mSingleGreenDuration;
    private ImageView mSingleGreenImage;

    private DatabaseReference current_user_db,current_user_plant;


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_house_detail);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();



        databaseGreen= FirebaseDatabase.getInstance().getReference("greens");
        databaseGreen.keepSynced(true);
        databasePlant= FirebaseDatabase.getInstance().getReference("plants");
        databasePlant.keepSynced(true);
        String id=firebaseAuth.getCurrentUser().getUid();
        current_user_db=databaseGreen.child(id);
        current_user_plant=databasePlant.child(id);

        mGreen_key=getIntent().getExtras().getString("green_id");

       // current_green=current_user_db.child(mGreen_key);


        mSingleGreenName=(TextView)findViewById(R.id.txtSingleGreenName);
        mSingleGreenLocation=(TextView)findViewById(R.id.txtSingleGreenLocation);
        mSingleGreenDuration=(TextView)findViewById(R.id.txtSingleGreenDuration);
        mSingleGreenImage=(ImageView)findViewById(R.id.imgSingleGreenSelect);


        //Toast.makeText(SinglePlantActivity.this,post_key, Toast.LENGTH_LONG).show();

        current_user_db.child(mGreen_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_name=(String)dataSnapshot.child("greenName").getValue();
                String post_location=(String)dataSnapshot.child("greenLocation").getValue();
                final String post_image=(String)dataSnapshot.child("greenImage").getValue();
                String post_duration=(String)dataSnapshot.child("greenDate").getValue();


                mSingleGreenName.setText(post_name);
                mSingleGreenLocation.setText(post_location);
                mSingleGreenDuration.setText(post_duration);


                Picasso.with(GreenHouseDetailActivity.this).load(post_image).into(mSingleGreenImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_delete)
        {
            checkForDelete();
        }
        else if(item.getItemId()==R.id.action_edit)
        {
            Intent mainIntent=new Intent(GreenHouseDetailActivity.this,GreenHouseEditActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mainIntent.putExtra("green_id",mGreen_key);
           // Toast.makeText(this, mGreen_key, Toast.LENGTH_LONG).show();
            startActivity(mainIntent);
        }
        return super.onOptionsItemSelected(item);
    }



    private void checkForDelete() {

        AlertDialog.Builder a_builder=new AlertDialog.Builder(GreenHouseDetailActivity.this);
        a_builder.setMessage("This GreenHouse/Plants will be deleted...")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        delete();

                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        AlertDialog alert=a_builder.create();
        alert.setTitle("Delete");
        alert.show();

    }

    private void delete() {

        current_user_db.child(mGreen_key).removeValue();
        current_user_plant.child(mGreen_key).removeValue();
        Intent mainIntent=new Intent(GreenHouseDetailActivity.this,GreenHouseActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);

    }
}
