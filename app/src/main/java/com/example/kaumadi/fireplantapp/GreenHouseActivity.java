package com.example.kaumadi.fireplantapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

//import static com.example.kaumadi.fireplantapp.R.styleable.RecyclerView;

public class GreenHouseActivity extends AppCompatActivity {
    private RecyclerView mGreenList;

    private DatabaseReference databaseGreens;
    private DatabaseReference databaseUsers;
    private DatabaseReference current_user_db;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private FloatingActionButton mFab;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_house);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseGreens= FirebaseDatabase.getInstance().getReference("greens");
        databaseGreens.keepSynced(true);

        mGreenList=(RecyclerView)findViewById(R.id.greenhouse_list);
        mGreenList.setHasFixedSize(true);
        mGreenList.setLayoutManager(new LinearLayoutManager(this));

        mFab=(FloatingActionButton)findViewById(R.id.fabGreen);


        firebaseAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null) {
                    Intent mainIntent = new Intent(GreenHouseActivity.this, LoginActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }
        };


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent greenIntent=new Intent(GreenHouseActivity.this,GreenHouseRegisterActivity.class);
                greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(greenIntent);
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);

        final  String user_id=firebaseAuth.getCurrentUser().getUid();
        FirebaseRecyclerAdapter<GreenHouse,GreenViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<GreenHouse,GreenViewHolder>(
                GreenHouse.class,
                R.layout.greenhouse_row,
               GreenViewHolder.class,
                databaseGreens.child(user_id)
        ) {
            @Override
            protected void populateViewHolder(GreenViewHolder viewHolder, GreenHouse model, int position) {
                final String post_key=getRef(position).getKey();


                viewHolder.setLocation(model.getGreenLocation());
                viewHolder.setName(model.getGreenName());
                viewHolder.setDate(model.getGreenDate());
                viewHolder.setImage(getApplicationContext(), model.getGreenImage());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Toast.makeText(MyGardenActivity.this,post_key,Toast.LENGTH_LONG).show();
                        Intent singlePlantIntent=new Intent(GreenHouseActivity.this,MainActivity.class);
                        singlePlantIntent.putExtra("green_id",post_key);
                        startActivity(singlePlantIntent);


                    }
                });

                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Intent singlePlantIntent=new Intent(GreenHouseActivity.this,GreenHouseDetailActivity.class);
                        singlePlantIntent.putExtra("green_id",post_key);
                        startActivity(singlePlantIntent);
                        return true;
                    }
                });
            }
        };
        mGreenList.setAdapter(firebaseRecyclerAdapter);



    }

    public static class GreenViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public GreenViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setName(String name)
        {
            TextView post_name=(TextView)mView.findViewById(R.id.green_name);
            post_name.setText(name);
        }
        public void setLocation(String location)
        {
            TextView post_location=(TextView)mView.findViewById(R.id.green_location);
            post_location.setText(location);
        }
        public void setDate(String date)
        {
            TextView post_date=(TextView)mView.findViewById(R.id.green_date);
            post_date.setText(date);
        }
        public void setImage(final Context ctx, final String image)
        {
            final ImageView post_image=(ImageView) mView.findViewById(R.id.green_image);
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
        getMenuInflater().inflate(R.menu.menu_logout,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_logout)
        {
            Intent greenIntent=new Intent(GreenHouseActivity.this,LoginActivity.class);
            greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(greenIntent);
        }
        return super.onOptionsItemSelected(item);
    }



}
