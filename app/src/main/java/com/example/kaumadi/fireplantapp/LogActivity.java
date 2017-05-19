package com.example.kaumadi.fireplantapp;

import android.content.Context;
import android.content.Intent;
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

public class LogActivity extends AppCompatActivity {

    private RecyclerView mPlantList;

    private DatabaseReference databasePlants;
    private DatabaseReference databaseUsers;
    private DatabaseReference current_green;
    private FirebaseAuth firebaseAuth;

    Toolbar toolbar;
    private  String mgh_id=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseAuth=FirebaseAuth.getInstance();
        databasePlants= FirebaseDatabase.getInstance().getReference("plants").child(firebaseAuth.getCurrentUser().getUid());
        databasePlants.keepSynced(true);
        databaseUsers= FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.keepSynced(true);


        mgh_id=getIntent().getExtras().getString("green_id");

        current_green=databasePlants.child(mgh_id);

        mPlantList=(RecyclerView)findViewById(R.id.log_plant_list);
        mPlantList.setHasFixedSize(true);
        mPlantList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Plant, LogActivity.PlantViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Plant, LogActivity.PlantViewHolder>(
                Plant.class,
                R.layout.plant_row,
                LogActivity.PlantViewHolder.class,
                current_green
        ) {
            @Override
            protected void populateViewHolder(final LogActivity.PlantViewHolder viewHolder, final Plant model, int position) {
                final String key=getRef(position).getKey();


                viewHolder.setId(model.getId());
                viewHolder.setName(model.getName());
                viewHolder.setDate(model.getDate());
                viewHolder.setImage(getApplicationContext(), model.getImage());

               viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Toast.makeText(MyGardenActivity.this,post_key,Toast.LENGTH_LONG).show();
                        Intent logSinglePlantIntent=new Intent(LogActivity.this,LogSinglePlantActivity.class);
                        logSinglePlantIntent.putExtra("key_id",key);
                        logSinglePlantIntent.putExtra("green_id",mgh_id);
                        startActivity(logSinglePlantIntent);


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
        getMenuInflater().inflate(R.menu.second_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_back)
        {
            //startActivity(new Intent(LogActivity.this,MainActivity.class));
            Intent mainIntent=new Intent(LogActivity.this,MainActivity.class);
            mainIntent.putExtra("green_id",mgh_id);
            startActivity(mainIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
