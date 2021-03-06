package com.example.kaumadi.fireplantapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class LogSinglePlantDiameterActivity extends AppCompatActivity {

    private LineGraphSeries<DataPoint> series;

    private int lastX=1;

    private String mPlantKey=null;
    private String mGreenKey=null;
    private DatabaseReference databasePlants,current_user_db,current_plant,currentGreen;

    Toolbar toolbar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_single_plant_diameter);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GraphView graphView=(GraphView)findViewById(R.id.graph);
        series=new LineGraphSeries<DataPoint>();
        graphView.addSeries(series);


        Viewport viewport=graphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(20);


        viewport.setScalable(true);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);

        graphView.setTitle("Shoot diamter via weeks");

        GridLabelRenderer gridLabel=graphView.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("weeks");
        gridLabel.setVerticalAxisTitle("Shoot diameter(cm)");



        mPlantKey=getIntent().getExtras().getString("plant_id");
        mGreenKey=getIntent().getExtras().getString("green_id");

        firebaseAuth= FirebaseAuth.getInstance();
        databasePlants= FirebaseDatabase.getInstance().getReference("plants");
        databasePlants.keepSynced(true);
        String id=firebaseAuth.getCurrentUser().getUid();
        current_user_db=databasePlants.child(id);

        currentGreen = current_user_db.child(mGreenKey);
        current_plant = currentGreen.child(mPlantKey);



        current_plant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snap: dataSnapshot.getChildren())
                {
                    if(snap.child("diameter").exists()) {
                        String y = snap.child("diameter").getValue() + "";
                        Log.i("=======diameter ",y);
                        double x_val = Double.parseDouble(y);
                        addEntry(x_val);

                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addEntry(double y_val)
    {
        series.appendData(new DataPoint(lastX++, y_val), true,50);

    }
}
