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

public class RegisterActivity extends AppCompatActivity {

    private Button buttonSave;
    private EditText editTextFirstName,editTextLastName,editTextEmail,editTextUser1,editTextPassword,editTextConfirmPassword,editTextUser2;
    private Spinner s;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private String userType;

    Toolbar toolbar;

    private DatabaseReference databaseusers,databasestudents,databasesuppervisors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();
       // databaseusers= FirebaseDatabase.getInstance().getReference().child("users");
        databaseusers= FirebaseDatabase.getInstance().getReference("users");
        databaseusers.keepSynced(true);
        databasestudents= FirebaseDatabase.getInstance().getReference().child("students");
        databasestudents.keepSynced(true);
        databasesuppervisors= FirebaseDatabase.getInstance().getReference().child("suppervisors");
        databasesuppervisors.keepSynced(true);
        progressDialog=new ProgressDialog(this);

        buttonSave=(Button)findViewById(R.id.btnSave);
        editTextFirstName=(EditText)findViewById(R.id.txtFirstname);
        editTextLastName=(EditText)findViewById(R.id.txtLastname);
        editTextEmail=(EditText)findViewById(R.id.txtEmail);
        editTextUser1=(EditText)findViewById(R.id.txtUser1);
        editTextUser2=(EditText)findViewById(R.id.txtUser2);
        editTextPassword=(EditText)findViewById(R.id.txtPassword);
        editTextConfirmPassword=(EditText)findViewById(R.id.txtConfirmpassword);
        s=(Spinner)findViewById(R.id.spin);


        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userType=adapterView.getSelectedItem().toString();
                if((userType.equals("Student")) || (userType.equals("Supperviser")))
                {
                    editTextUser2.setVisibility(View.GONE);
                    editTextUser1.setHint("Resercher email");
                }
                else if(userType.equals("Researcher"))
                {
                    editTextUser2.setVisibility(View.VISIBLE);
                    //editTextUser1.setHint("Resercher email");
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerUser();


            }
        });

    }



    private void registerUser() {
        final String firstname = editTextFirstName.getText().toString().trim();
        final String lastname = editTextLastName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String user1 = editTextUser1.getText().toString().trim();
        final String user2 = editTextUser2.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String confirmpassword = editTextConfirmPassword.getText().toString().trim();
        final String user_type=s.getSelectedItem().toString();



        if (!TextUtils.isEmpty(firstname) && !TextUtils.isEmpty(lastname) && !TextUtils.isEmpty(email)  && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmpassword)&& !user_type.isEmpty()) {

            if(password.equals(confirmpassword))
            {
                if(user_type.equals("Researcher")) {
                    if (!TextUtils.isEmpty(user1) && !TextUtils.isEmpty(user2)) {
                        progressDialog.setMessage("Register User...");
                        progressDialog.show();

                        (firebaseAuth.createUserWithEmailAndPassword(email, password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {


                                    String id = firebaseAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db = databaseusers.child(id);
                                    current_user_db.child("userFirstName").setValue(firstname);
                                    current_user_db.child("userLastName").setValue(lastname);
                                    current_user_db.child("userEmail").setValue(email);
                                    current_user_db.child("userUser1").setValue(user1);
                                    current_user_db.child("userUser2").setValue(user2);
                                    current_user_db.child("userPassword").setValue(password);

                                    //progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Saved successfully...", Toast.LENGTH_LONG).show();

                                    Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);

                                    //startActivity(new Intent(RegisterActivity.this,LoginActivity.class));


                                }


                            }
                        });
                    }
                }
                else if(user_type.equals("Supperviser")) {
                    if (!TextUtils.isEmpty(user1)) {
                        progressDialog.setMessage("Register User...");
                        progressDialog.show();

                        (firebaseAuth.createUserWithEmailAndPassword(email, password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {


                                    String id = firebaseAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db = databasesuppervisors.child(id);
                                    current_user_db.child("suppervisorFirstName").setValue(firstname);
                                    current_user_db.child("suppervisorLastName").setValue(lastname);
                                    current_user_db.child("suppervisorEmail").setValue(email);
                                    current_user_db.child("suppervisorUser1").setValue(user1);
                                    //current_user_db.child("userUser2").setValue(user2);
                                    current_user_db.child("suppervisorPassword").setValue(password);

                                    //progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Saved successfully...", Toast.LENGTH_LONG).show();

                                    Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);

                                    //startActivity(new Intent(RegisterActivity.this,LoginActivity.class));


                                }


                            }
                        });
                    }
                }
                else if(user_type.equals("Student")) {
                    if (!TextUtils.isEmpty(user1)) {
                        progressDialog.setMessage("Register User...");
                        progressDialog.show();

                        (firebaseAuth.createUserWithEmailAndPassword(email, password)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {


                                    String id = firebaseAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db = databasestudents.child(id);
                                    current_user_db.child("studentFirstName").setValue(firstname);
                                    current_user_db.child("studentLastName").setValue(lastname);
                                    current_user_db.child("studentEmail").setValue(email);
                                    current_user_db.child("studentUser1").setValue(user1);
                                    // current_user_db.child("userUser2").setValue(user2);
                                    current_user_db.child("studentPassword").setValue(password);

                                    //progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Saved successfully...", Toast.LENGTH_LONG).show();

                                    Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);

                                    //startActivity(new Intent(RegisterActivity.this,LoginActivity.class));


                                }


                            }
                        });
                    }
                }

            }
            else
            {
                Toast.makeText(RegisterActivity.this, "Enter password fields again...", Toast.LENGTH_LONG).show();
                editTextPassword.setText(null);
                editTextConfirmPassword.setText(null);
            }

        }
        else
        {
            Toast.makeText(this, "You should fill all fields", Toast.LENGTH_LONG).show();
        }
    }



}
