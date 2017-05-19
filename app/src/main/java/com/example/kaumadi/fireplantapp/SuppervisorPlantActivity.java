package com.example.kaumadi.fireplantapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class SuppervisorPlantActivity extends AppCompatActivity {

    private RecyclerView mPlantList;

    private DatabaseReference databasePlants;
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseCurrentGreen;
    private FirebaseAuth firebaseAuth;

    Toolbar toolbar;
    private  String mGreen_id=null;
    private  String mUser_id=null;
    FirebaseRecyclerAdapter<Plant, PlantViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppervisor_plant);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGreen_id=getIntent().getExtras().getString("green_id");
        mUser_id=getIntent().getExtras().getString("user_id");

        firebaseAuth=FirebaseAuth.getInstance();
        databasePlants= FirebaseDatabase.getInstance().getReference("plants").child(mUser_id);
        databasePlants.keepSynced(true);
        databaseUsers= FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.keepSynced(true);

        mPlantList=(RecyclerView)findViewById(R.id.plant_list);
        mPlantList.setHasFixedSize(true);
        mPlantList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Plant, PlantViewHolder>(
                Plant.class,
                R.layout.plant_row,
                PlantViewHolder.class,
                databasePlants.child(mGreen_id)
        ) {
            @Override
            protected void populateViewHolder(final PlantViewHolder viewHolder, final Plant model, int position) {
                final String plant_id=getRef(position).getKey();


                viewHolder.setId(model.getId());
                viewHolder.setName(model.getName());
                viewHolder.setDate(model.getDate());
                viewHolder.setImage(getApplicationContext(), model.getImage());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Toast.makeText(MyGardenActivity.this,post_key,Toast.LENGTH_LONG).show();
                        Intent singlePlantIntent=new Intent(SuppervisorPlantActivity.this,SuppervisorSinglePlantActivity.class);
                        singlePlantIntent.putExtra("plant_id",plant_id);
                        singlePlantIntent.putExtra("green_id",mGreen_id);
                        singlePlantIntent.putExtra("user_id",mUser_id);
                        startActivity(singlePlantIntent);


                    }
                });
            }
        };
        mPlantList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public PlantViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setId(String id)
        {
            TextView post_id=(TextView)mView.findViewById(R.id.post_id);
            post_id.setText(id);
        }
        public void setName(String name)
        {
            TextView post_name=(TextView)mView.findViewById(R.id.post_name);
            post_name.setText(name);
        }
        public void setDate(String date)
        {
            TextView post_date=(TextView)mView.findViewById(R.id.post_date);
            post_date.setText(date);
        }
        public void setImage(final Context ctx, final String image)
        {
            final ImageView post_image=(ImageView) mView.findViewById(R.id.post_image);
            //Picasso.with(ctx).load(image).into(post_image);



            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(post_image);
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_back)
        {
            Intent greenIntent=new Intent(SuppervisorPlantActivity.this,SuppervisorGreenHouseActivity.class);
            greenIntent.putExtra("green_id",mGreen_id);
            greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(greenIntent);
        }
        else if(item.getItemId()==R.id.action_search)
        {

        }
        return super.onOptionsItemSelected(item);
    }
}
