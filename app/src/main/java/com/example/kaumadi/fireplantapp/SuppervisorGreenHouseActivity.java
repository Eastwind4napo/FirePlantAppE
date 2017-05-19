package com.example.kaumadi.fireplantapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StreamDownloadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class SuppervisorGreenHouseActivity extends AppCompatActivity {

    private RecyclerView mGreenList;

    private DatabaseReference databaseGreens,databaseUsers,databaseSuppervisors;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    Toolbar toolbar;
    String email;
    public String supper_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppervisor_green_house);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseGreens = FirebaseDatabase.getInstance().getReference("greens");
        databaseGreens.keepSynced(true);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.keepSynced(true);
        databaseSuppervisors = FirebaseDatabase.getInstance().getReference("suppervisors");
        databaseUsers.keepSynced(true);

        mGreenList = (RecyclerView) findViewById(R.id.greenhouse_list);
        mGreenList.setHasFixedSize(true);
        mGreenList.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {

                    Intent mainIntent=new Intent(SuppervisorGreenHouseActivity.this,LoginActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }
        };

    }

    public void chekUser()
    {
        final  String user_id=firebaseAuth.getCurrentUser().getUid();
        databaseSuppervisors.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email=(String) dataSnapshot.child("suppervisorUser1").getValue();
                //Toast.makeText(SuppervisorGreenHouseActivity.this,email, Toast.LENGTH_LONG).show();
                databaseUsers.orderByChild("userEmail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            if(!child.getKey().equals(null))
                            {
                                supper_user_id=child.getKey();
                                //Toast.makeText(SuppervisorGreenHouseActivity.this,supper_user_id, Toast.LENGTH_LONG).show();
                                printGreen(supper_user_id);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void printGreen(String s_id)
    {
        final String supper_id=s_id;
       // Toast.makeText(SuppervisorGreenHouseActivity.this,"printGreen"+supper_id, Toast.LENGTH_LONG).show();

        FirebaseRecyclerAdapter<GreenHouse,GreenViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<GreenHouse,GreenViewHolder>(
                GreenHouse.class,
                R.layout.greenhouse_row,
                GreenViewHolder.class,
                databaseGreens.child(supper_id)
        ) {
            @Override
            protected void populateViewHolder(GreenViewHolder viewHolder, GreenHouse model, int position) {
                final String greenId = getRef(position).getKey();


                viewHolder.setName(model.getGreenName());
                viewHolder.setLocation(model.getGreenLocation());
                viewHolder.setDate(model.getGreenDate());
                viewHolder.setImage(getApplicationContext(), model.getGreenImage());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent greenIntent=new Intent(SuppervisorGreenHouseActivity.this,SuppervisorPlantActivity.class);
                        //greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        greenIntent.putExtra("green_id",greenId);
                        greenIntent.putExtra("user_id",supper_id);
                        startActivity(greenIntent);


                    }
                });
            }
        };

        mGreenList.setAdapter(firebaseRecyclerAdapter);


    }


    public static class GreenViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public GreenViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView post_name = (TextView) mView.findViewById(R.id.green_name);
            post_name.setText(name);
        }

        public void setLocation(String location) {
            TextView post_location = (TextView) mView.findViewById(R.id.green_location);
            post_location.setText(location);
        }

        public void setDate(String date) {
            TextView post_date = (TextView) mView.findViewById(R.id.green_date);
            post_date.setText(date);
        }

        public void setImage(final Context ctx, final String image) {
            final ImageView post_image = (ImageView) mView.findViewById(R.id.green_image);
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
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);

        chekUser();

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
            Intent greenIntent=new Intent(SuppervisorGreenHouseActivity.this,SuppervisorMainActivity.class);
           // greenIntent.putExtra("green_id",mGreen_id);
            greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(greenIntent);
        }
        else if(item.getItemId()==R.id.action_search)
        {

        }
        return super.onOptionsItemSelected(item);
    }

}
