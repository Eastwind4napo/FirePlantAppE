package com.example.kaumadi.fireplantapp.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eastwind.app.Greenhouse.GreenhouseActivity;
import com.example.eastwind.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText ete;
    EditText etp;
    Button lg;
    TextView rg;
    String email;
    String password;
    FirebaseDatabase mbd;
    DatabaseReference mdbr;
    FirebaseAuth mAuth;
    ProgressDialog mProgressDialog;
    String uid,post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        mbd = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mdbr = mbd.getReference();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, GreenhouseActivity.class));
            finish();
        }

        lg =(Button)findViewById(R.id.log);
        lg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                run();
            }
        });

        rg=(TextView)findViewById(R.id.reg);
        rg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.this.startActivity(new Intent(Login.this,ChooseRole.class));
            }
        });

    }

    private void selectUser() {
        uid = mAuth.getCurrentUser().getUid();

        mdbr.child("Users").child(uid).child("post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post = dataSnapshot.getValue(String.class);
                if(post.equals("Student")){
                    Login.this.startActivity(new Intent(Login.this,Student.class));
                }
                else if(post.equals("Researcher")){
                    Login.this.startActivity(new Intent(Login.this,GreenhouseActivity.class));
                }else{
                    Toast.makeText(Login.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void run() {
        ete=(EditText)findViewById(R.id.etemail);
        email=ete.getText().toString();
        etp=(EditText)findViewById(R.id.etpwd);
        password=etp.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            ete.setError("Required.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            etp.setError("Required.");
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    // there was an error
                    hideProgressDialog();
                    if (password.length() < 6) {
                        etp.setError("Password too short.");
                    } else {
                        Toast.makeText(Login.this, "Authentication failed, check your email and password or sign up", Toast.LENGTH_LONG).show();
                    }
                } else {
                    selectUser();
                    finish();
                }
            }
        });

    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
