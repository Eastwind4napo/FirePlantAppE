package com.example.kaumadi.fireplantapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SuppervisorMainActivity extends AppCompatActivity {

    private  FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference databasesuppervisors;
    private BottomNavigationView bottomNavigationView;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppervisor_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databasesuppervisors= FirebaseDatabase.getInstance().getReference().child("suppervisors");
        databasesuppervisors.keepSynced(true);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {

                    Intent mainIntent=new Intent(SuppervisorMainActivity.this,LoginActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }
        };

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.NavBot);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if(item.getItemId()==R.id.myGarden)
                {
                    Intent greenIntent=new Intent(SuppervisorMainActivity.this,SuppervisorGreenHouseActivity.class);
                    greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(greenIntent);

                }
                else if(item.getItemId()==R.id.log)
                {
                    Intent greenIntent=new Intent(SuppervisorMainActivity.this,SuppervisorLogActivity.class);
                    greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(greenIntent);
                }
                else if(item.getItemId()==R.id.chat)
                {
                    Intent greenIntent=new Intent(SuppervisorMainActivity.this,SuppervisorChatActivity.class);
                    greenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(greenIntent);
                }

                return false;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);

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
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        firebaseAuth.signOut();
    }

}
