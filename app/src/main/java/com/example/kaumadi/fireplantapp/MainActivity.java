package com.example.kaumadi.fireplantapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kaumadi.fireplantapp.mFragment.HomeFragment;
import com.example.kaumadi.fireplantapp.mFragment.ChatFragment;
import com.example.kaumadi.fireplantapp.mFragment.LogFragment;

import com.example.kaumadi.fireplantapp.mFragment.MyGardenFragment;
import com.example.kaumadi.fireplantapp.mFragment.PlantDBFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private  FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference databaseusers;
    private BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

    private String mGreen_id;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseusers= FirebaseDatabase.getInstance().getReference().child("users");
        databaseusers.keepSynced(true);
        firebaseAuth=FirebaseAuth.getInstance();

        //mGreen_id=getIntent().getExtras().getString("green_id");

        firebaseAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {

                    Intent mainIntent=new Intent(MainActivity.this,LoginActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }
        };

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.NavBot) ;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mGreen_id=getIntent().getExtras().getString("green_id");

                if(item.getItemId()==R.id.myGarden)
                {

                  /*Intent mainIntent=new Intent(MainActivity.this,MyGardenActivity.class);
                   mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mainIntent.putExtra("green_id",mGreen_id);
                    startActivity(mainIntent);*/
                  fragment = new MyGardenFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.ifragment, fragment)
                            .commit();
                    Bundle bundle = new Bundle();
                    bundle.putString("green_id", mGreen_id);
                    fragment.setArguments(bundle);
                    return true;

                }
                else if(item.getItemId()==R.id.plantDb)
                {
                    fragment = new PlantDBFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.ifragment, fragment)
                            .commit();
                    return true;
                   //startActivity(new Intent(MainActivity.this,PlantDBFragment.class));

                }
                else if(item.getItemId()==R.id.log)
                {
                  /* Intent mainIntent=new Intent(MainActivity.this,LogActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mainIntent.putExtra("green_id",mGreen_id);
                    startActivity(mainIntent);*/
                   fragment = new LogFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.ifragment, fragment)
                            .commit();
                    Bundle bundle = new Bundle();
                    bundle.putString("green_id", mGreen_id);
                    fragment.setArguments(bundle);
                    return true;
                }
                else if(item.getItemId()==R.id.chat)
                {
                    fragment = new ChatFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.ifragment, fragment)
                            .commit();
                    return true;
                    //startActivity(new Intent(MainActivity.this,ChatActivity.class));
                }
                else if(item.getItemId()==R.id.about)
                {
                    fragment = new HomeFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.ifragment, fragment)
                            .commit();
                    return true;
                    //startActivity(new Intent(MainActivity.this,HelpActivity.class));
                }
                return false;
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
        checkUserExist();
    }

    private void checkUserExist()
    {
        final String user_id=firebaseAuth.getCurrentUser().getUid();
        databaseusers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(user_id))
                {
                    Intent mainIntent=new Intent(MainActivity.this,LoginActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
      return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_logout)
        {
            logout();
        }
        if(item.getItemId()==R.id.action_back)
        {
            Intent greenIntent=new Intent(MainActivity.this,GreenHouseActivity.class);
            greenIntent.putExtra("green_id",mGreen_id);
            greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(greenIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        firebaseAuth.signOut();
    }


}
