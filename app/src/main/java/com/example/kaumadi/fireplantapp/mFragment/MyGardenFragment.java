package com.example.kaumadi.fireplantapp.mFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaumadi.fireplantapp.MainActivity;

import com.example.kaumadi.fireplantapp.MyGardenActivity;
import com.example.kaumadi.fireplantapp.Plant;
import com.example.kaumadi.fireplantapp.PlantRegisterActivity;
import com.example.kaumadi.fireplantapp.R;
import com.example.kaumadi.fireplantapp.SinglePlantActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MyGardenFragment extends Fragment {

    private RecyclerView mPlantList;

    private DatabaseReference databasePlants;
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseCurrentGreen;
    private FirebaseAuth firebaseAuth;

    private FloatingActionButton mFab;
    private  String mgh_id=null;
    FirebaseRecyclerAdapter<Plant,PlantViewHolder> firebaseRecyclerAdapter;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle=getArguments();
        mgh_id=bundle.getString("green_id");
        Log.i("green id=======",mgh_id);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_garden, container, false);

        firebaseAuth=FirebaseAuth.getInstance();
        databasePlants=FirebaseDatabase.getInstance().getReference("plants").child(firebaseAuth.getCurrentUser().getUid());
        databasePlants.keepSynced(true);
        databaseUsers= FirebaseDatabase.getInstance().getReference("users");
        databaseUsers.keepSynced(true);



        mPlantList=(RecyclerView)rootView.findViewById(R.id.plant_list);
        mPlantList.setHasFixedSize(true);
        mPlantList.setLayoutManager(new LinearLayoutManager(getContext()));

        mFab=(FloatingActionButton)rootView.findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MyGardenActivity.this,PlantRegisterActivity.class));
                Intent mainIntent=new Intent(getActivity().getApplicationContext(),PlantRegisterActivity.class);
                //mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainIntent.putExtra("green_id",mgh_id);
                startActivity(mainIntent);
            }
        });

        return rootView;
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
        public void setImage(final Context ctx,final String image)
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
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Plant, PlantViewHolder>(
                Plant.class,
                R.layout.plant_row,
               PlantViewHolder.class,
                databasePlants.child(mgh_id)
        ) {
            @Override
            protected void populateViewHolder(final PlantViewHolder viewHolder, final Plant model, int position) {
                final String post_key=getRef(position).getKey();


                viewHolder.setId(model.getId());
                viewHolder.setName(model.getName());
                viewHolder.setDate(model.getDate());
                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Toast.makeText(MyGardenActivity.this,post_key,Toast.LENGTH_LONG).show();
                        Intent singlePlantIntent=new Intent(getActivity().getApplicationContext(),SinglePlantActivity.class);
                        singlePlantIntent.putExtra("plant_id",post_key);
                        singlePlantIntent.putExtra("green_id",mgh_id);
                        startActivity(singlePlantIntent);


                    }
                });
            }
        };
        mPlantList.setAdapter(firebaseRecyclerAdapter);

    }
}






