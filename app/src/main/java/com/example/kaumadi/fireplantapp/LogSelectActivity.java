package com.example.kaumadi.fireplantapp;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LogSelectActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    Button buttonNext;
    String item="null";

    private String mPlantKey=null;
    private String mGreenKey=null;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_select);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPlantKey=getIntent().getExtras().getString("plant_id");
        mGreenKey=getIntent().getExtras().getString("green_id");

        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        buttonNext=(Button)findViewById(R.id.btnNext);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rb =(RadioButton)findViewById(checkedId);

                switch (rb.getId()){
                    case R.id.radioShootLength:
                        item="ShootLength";
                        break;
                    case R.id.radioShootDiameter:
                        item="ShootDiameter";
                        break;
                    case R.id.radioLeaves:
                        item="Leaves";
                        break;
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.equals("Leaves")) {
                    Intent i = new Intent(LogSelectActivity.this, LogSinglePlantLeavesActivity.class);
                    i.putExtra("green_id",mGreenKey);
                    i.putExtra("plant_id",mPlantKey);
                    startActivity(i);
                }
                if(item.equals("ShootLength")) {
                    Intent i = new Intent(LogSelectActivity.this, LogSinglePlantLengthActivity.class);
                    i.putExtra("green_id",mGreenKey);
                    i.putExtra("plant_id",mPlantKey);
                    startActivity(i);
                }
               if(item.equals("ShootDiameter")) {
                   Intent i = new Intent(LogSelectActivity.this, LogSinglePlantDiameterActivity.class);
                    i.putExtra("green_id",mGreenKey);
                    i.putExtra("plant_id",mPlantKey);
                   startActivity(i);
                }
                if(item.equals("null"))
                {
                    Toast.makeText(LogSelectActivity.this, "First you need to select on option...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
