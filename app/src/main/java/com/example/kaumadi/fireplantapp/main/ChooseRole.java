package com.example.kaumadi.fireplantapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.kaumadi.fireplantapp.R;


public class ChooseRole extends AppCompatActivity {


    RadioGroup post;
    Button next;
    String spost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);

        post=(RadioGroup)findViewById(R.id.position);
        post.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rb =(RadioButton)findViewById(checkedId);

                switch (rb.getId()){
                    case R.id.rbsp:
                        spost="Supervisor";
                        break;
                    case R.id.rbr:
                        spost="Researcher";
                        break;
                    case R.id.rbs:
                        spost="Student";
                        break;
                }
            }
        });

        next=(Button)findViewById(R.id.btnn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseRole.this,Register.class);
                i.putExtra("Role",spost);
                startActivity(i);
            }
        });

    }
}
