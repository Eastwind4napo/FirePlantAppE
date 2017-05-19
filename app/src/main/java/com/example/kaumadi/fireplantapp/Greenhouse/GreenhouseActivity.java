package com.example.kaumadi.fireplantapp.Greenhouse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaumadi.fireplantapp.R;
import com.example.kaumadi.fireplantapp.main.Login;
import com.example.kaumadi.fireplantapp.main.Tab_main;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

//import static android.media.CamcorderProfile.get;

public class GreenhouseActivity extends AppCompatActivity {


    private RecyclerView mGreenList;
   // private OnItemTouchListener itemTouchListener;
    private DatabaseReference databaseGreens;
    private DatabaseReference databaseUsers;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FloatingActionButton mFab;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greenhouse);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth= FirebaseAuth.getInstance();

//      String uid = firebaseAuth.getInstance().getCurrentUser().getUid();
        databaseGreens= FirebaseDatabase.getInstance().getReference("Greenhouse");
        databaseGreens.keepSynced(true);
        databaseUsers= FirebaseDatabase.getInstance().getReference("Users");
        databaseUsers.keepSynced(true);

        mGreenList=(RecyclerView)findViewById(R.id.greenhouse_list);
        mGreenList.setHasFixedSize(true);
        mGreenList.setLayoutManager(new LinearLayoutManager(this));

        mFab = (FloatingActionButton) findViewById(R.id.fab);


        firebaseAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
                    Intent mainIntent=new Intent(GreenhouseActivity.this,Login.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }
        };


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent greenIntent=new Intent(GreenhouseActivity.this,RegisterGreenhouse.class);
                greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(greenIntent);
            }
        });

//        OnItemTouchListener itemTouchListener = new OnItemTouchListener() {
//            @Override
//            public void onCardViewTap(View view, int position) {
//
//            }
//
//            @Override
//            public void onBEditClick(View view, int position) {
//                Toast.makeText(GreenhouseActivity.this, "Tapped 1" + get(position), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onEDeleteClick(View view, int position) {
//                Toast.makeText(GreenhouseActivity.this, "Tapped 2" + get(position), Toast.LENGTH_SHORT).show();
//            }
//        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);

        final  String user_id=firebaseAuth.getCurrentUser().getUid();



        FirebaseRecyclerAdapter<Greenhouses,GreenViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Greenhouses, GreenViewHolder>(
                Greenhouses.class,
                R.layout.greenhouse_row,
                GreenViewHolder.class,
                databaseGreens.child(user_id)
        ) {

            @Override
            protected void populateViewHolder(GreenViewHolder viewHolder, Greenhouses model, int position) {
                final String greenId = getRef(position).getKey();


                viewHolder.setName(model.getGreenName());
                viewHolder.setLocation(model.getGreenLocation());
                viewHolder.setDate(model.getGreenDate());
                viewHolder.setImage(getApplicationContext(), model.getGreenImage());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent greenIntent=new Intent(GreenhouseActivity.this,Tab_main.class);
                        //greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        greenIntent.putExtra("green_id",greenId);
                        startActivity(greenIntent);
                    }
                });
            }

        };
        mGreenList.setAdapter(firebaseRecyclerAdapter);
    }

//    public interface OnItemTouchListener {
//        /**
//         * Callback invoked when the user Taps one of the RecyclerView items
//         *
//         * @param view     the CardView touched
//         * @param position the index of the item touched in the RecyclerView
//         */
//        public void onCardViewTap(View view, int position);
//
//        /**
//         * Callback invoked when the Button1 of an item is touched
//         *
//         * @param view     the Button touched
//         * @param position the index of the item touched in the RecyclerView
//         */
//        public void onBEditClick(View view, int position);
//
//        /**
//         * Callback invoked when the Button2 of an item is touched
//         *
//         * @param view     the Button touched
//         * @param position the index of the item touched in the RecyclerView
//         */
//        public void onEDeleteClick(View view, int position);
//    }



    public static class GreenViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button bEdit;
        Button bDelete;

        public GreenViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            bEdit = (Button)mView.findViewById(R.id.bedit);
            bDelete = (Button)mView.findViewById(R.id.bdelete);

            bEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            bDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
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
        getMenuInflater().inflate(R.menu.menu_search_only,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_search)
        {

        }
        else if(item.getItemId()==R.id.action_logout)
        {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        firebaseAuth.signOut();
    }

}
