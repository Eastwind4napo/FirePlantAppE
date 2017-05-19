package com.example.kaumadi.fireplantapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class SinglePlantActivity extends AppCompatActivity {

    private String mPost_key=null;
    private String mGreen_key=null;
    private DatabaseReference databasePlants;

    private FirebaseAuth firebaseAuth;

    private TextView mSinglePlantId;
    private TextView mSinglePlantName;
    private TextView mSinglePlantDate;
    private TextView mSinglePlantSci;
    private TextView mSinglePlantGenus;
    private ImageView mSinglePlantImage;

    private DatabaseReference current_user_db;
    private DatabaseReference current_green;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_plant);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();



        databasePlants= FirebaseDatabase.getInstance().getReference("plants");
        databasePlants.keepSynced(true);
        String id=firebaseAuth.getCurrentUser().getUid();
        current_user_db=databasePlants.child(id);



        mPost_key=getIntent().getExtras().getString("plant_id");
        mGreen_key=getIntent().getExtras().getString("green_id");

        current_green=current_user_db.child(mGreen_key);

        mSinglePlantId=(TextView)findViewById(R.id.txtSinglePlantId);
        mSinglePlantName=(TextView)findViewById(R.id.txtSinglePlantName);
        mSinglePlantDate=(TextView)findViewById(R.id.txtSinglePlantDate);
        mSinglePlantSci=(TextView)findViewById(R.id.txtSingleScientifictName);
        mSinglePlantGenus=(TextView)findViewById(R.id.txtSingleGenus);
        mSinglePlantImage=(ImageView)findViewById(R.id.imgSinglePlantSelect);


        //Toast.makeText(SinglePlantActivity.this,post_key, Toast.LENGTH_LONG).show();

            current_green.child(mPost_key).addValueEventListener(new ValueEventListener() {
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

                Picasso.with(SinglePlantActivity.this).load(post_image).into(mSinglePlantImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_del_back,menu);
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
            Intent mainIntent=new Intent(SinglePlantActivity.this,SinglePlantEditActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mainIntent.putExtra("green_id",mGreen_key);
            mainIntent.putExtra("plant_id",mPost_key);
            startActivity(mainIntent);
        }
        else if(item.getItemId()==R.id.action_back)
        {
           /* Intent mainIntent=new Intent(SinglePlantActivity.this,MyGardenActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mainIntent.putExtra("green_id",mGreen_key);
            startActivity(mainIntent);*/
        }
        return super.onOptionsItemSelected(item);
    }



    private void checkForDelete() {

        AlertDialog.Builder a_builder=new AlertDialog.Builder(SinglePlantActivity.this);
        a_builder.setMessage("This Plant will be deleted...")
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

        current_green.child(mPost_key).removeValue();
        Intent mainIntent=new Intent(SinglePlantActivity.this,MyGardenActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.putExtra("green_id",mGreen_key);
        startActivity(mainIntent);

    }


}

