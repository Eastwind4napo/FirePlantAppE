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
import android.widget.Toast;

import com.example.kaumadi.fireplantapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private static final String TAG = "AndroidBash";
    EditText etfn,etln,etun,etm,etpwd,etcpwd;
// RadioButton ai,rsc,frm;
    Button rgs;
    String sfn,sln,sun,sm,spwd,scpwd,spost;
    User user;
    FirebaseDatabase mbd;
    DatabaseReference mdbr;
    FirebaseAuth mAuth;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mbd = FirebaseDatabase.getInstance();
        mdbr = mbd.getReference();

       //Bundle b = getIntent().getExtras();
        spost = getIntent().getStringExtra("Role");


        rgs=(Button)findViewById(R.id.btnr);
        rgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go();
            }
        });
    }


    private void go() {

        etfn=(EditText)findViewById(R.id.fname);
        sfn=etfn.getText().toString().trim();

        etln=(EditText)findViewById(R.id.lname);
        sln=etln.getText().toString().trim();

        etun=(EditText)findViewById(R.id.uname);
        sun=etun.getText().toString().trim();

        etm=(EditText)findViewById(R.id.mail);
        sm=etm.getText().toString().trim();

        etpwd=(EditText)findViewById(R.id.pwd);
        spwd=etpwd.getText().toString().trim();

        etcpwd=(EditText)findViewById(R.id.cpwd);
        scpwd=etcpwd.getText().toString().trim();


        if(checkText()==false){
            Toast.makeText(this,"Enter All Data",Toast.LENGTH_SHORT).show();
        }

        if(spwd.equals(scpwd)==false) {
            Toast.makeText(this, "Password Not Match", Toast.LENGTH_LONG).show();
        }

        if(TextUtils.isEmpty(sm)) {
                etm.setError("Required.");
        }

        if (TextUtils.isEmpty(spwd)) {
            etpwd.setError("Required.");
        }

        if (spwd.length()<6) {
            etpwd.setError("Password too short.");
        }

        else{
            showProgressDialog();
            createNewAccount(sm,spwd);
        }
    }

    private void createNewAccount(final String sm, final String spwd) {

        mAuth.createUserWithEmailAndPassword(sm, spwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, "Authentication failed."+ task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                           String id= mAuth.getCurrentUser().getUid();
                            createUser(id,sfn,sln,sun,sm,spwd,spost);
                            mAuth.signOut();
                            startActivity(new Intent(Register.this, Login.class));
                            finish();
                        }


                    }
                });

    }

    private void createUser(String userId,String sfn,String sln,String sun,String sm,String spwd,String spost) {
        user = new User(userId,sfn,sln,sun,sm,spwd,spost);
        mdbr.child("Users").child(userId).setValue(user);
    }

    private boolean checkText(){
        if(sfn.matches("")||sln.matches("")||sun.matches("")||sm.matches("")||spwd.matches("")||scpwd.matches("")||spost.matches("")){
            return false;
        }else
            return true;
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
