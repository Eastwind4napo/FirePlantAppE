package com.example.kaumadi.fireplantapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail,editTextPassword;
    private Spinner s;
    private Button buttonLogin;
    private TextView textViewRegister;

    private DatabaseReference databaseusers,databasestudents,databasesuppervisors;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String userType;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseusers= FirebaseDatabase.getInstance().getReference().child("users");
        databaseusers.keepSynced(true);
        databasestudents= FirebaseDatabase.getInstance().getReference().child("students");
        databasestudents.keepSynced(true);
        databasesuppervisors= FirebaseDatabase.getInstance().getReference().child("suppervisors");
        databasesuppervisors.keepSynced(true);

        progressDialog=new ProgressDialog(this);

        editTextEmail=(EditText)findViewById(R.id.txtEmail);
        editTextPassword=(EditText)findViewById(R.id.txtPassword);
        buttonLogin=(Button)findViewById(R.id.btnLogin);
        textViewRegister=(TextView)findViewById(R.id.txtRegister);
        s=(Spinner)findViewById(R.id.spin);


        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userType=adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

    }


    private void checkLogin()
    {
        final String email=editTextEmail.getText().toString().trim();
        final String password=editTextPassword.getText().toString().trim();
        final String user_type=s.getSelectedItem().toString();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)&& !user_type.isEmpty() ) {

            progressDialog.setMessage("Checking login...");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        checkUserExist();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Error login...", Toast.LENGTH_LONG).show();
                        clearfields();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "You should fill all fields", Toast.LENGTH_LONG).show();
        }

    }


    private void checkUserExist()
    {


        if(userType.equals("Researcher")) {


            databaseusers.addValueEventListener(new ValueEventListener() {
                String user_id = firebaseAuth.getCurrentUser().getUid();
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {
                        Intent mainIntent = new Intent(LoginActivity.this, GreenHouseActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                    else {

                        Toast.makeText(LoginActivity.this, "First You need to register...", Toast.LENGTH_LONG).show();
                        clearfields();
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if(userType.equals("Student")) {
            databasestudents.addValueEventListener(new ValueEventListener() {
                String user_id = firebaseAuth.getCurrentUser().getUid();
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {
                        Intent mainIntent = new Intent(LoginActivity.this, StudentMainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                    else {

                        Toast.makeText(LoginActivity.this, "First You need to register...", Toast.LENGTH_LONG).show();
                        clearfields();
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if(userType.equals("Supperviser")) {
            databasesuppervisors.addValueEventListener(new ValueEventListener() {
                String user_id = firebaseAuth.getCurrentUser().getUid();
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {
                        Intent mainIntent = new Intent(LoginActivity.this, SuppervisorMainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                    else {

                        Toast.makeText(LoginActivity.this, "First You need to register...", Toast.LENGTH_LONG).show();
                        clearfields();
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void clearfields()
    {
        editTextEmail.setText(null);
        editTextPassword.setText(null);
    }
}
