package com.example.kaumadi.fireplantapp.mFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kaumadi.fireplantapp.R;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private Button bt,bl,bh,bs;
    private TextView statusUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        bt = (Button) rootView.findViewById(R.id.btnTemp);
        bl = (Button) rootView.findViewById(R.id.btnLight);
        bh = (Button) rootView.findViewById(R.id.btnHum);
        bs = (Button) rootView.findViewById(R.id.btnSoil);


        bt.setOnClickListener(this);
        bl.setOnClickListener(this);
        bh.setOnClickListener(this);
        bs.setOnClickListener(this);



        /*====Update Status=====*/
        statusUpdate = (TextView) rootView.findViewById(R.id.statustxt);




        return rootView;
    }
    private void onSelectedFragment(View view){
        Fragment newFragment;

        if(view == view.findViewById(R.id.btnTemp)){

            newFragment = new TempFragment();

        }else if(view == view.findViewById(R.id.btnLight)){
            newFragment = new LightFragment();

        }else if(view == view.findViewById(R.id.btnHum)){

            newFragment = new HumFragment();

        }else if (view == view.findViewById(R.id.btnSoil)){
            newFragment = new SoilFragment();

        }else{
            newFragment = null;

        }
        replaceFragment(newFragment);



    }



    private void replaceFragment(Fragment newFragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(!newFragment.isAdded()){
            try{
//                getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_placeholder,newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }catch (Exception e){

            }
        }else {
            transaction.show(newFragment);
        }
    }
    public void updateStatus(View view){
        //status bar
        Flag flag = new Flag();
        if(view == view.findViewById(R.id.btnTemp)){
            if(flag.isTempFlag()){
                statusUpdate.setText("Temperature is high");
            }else {
                statusUpdate.setText("Temperature is Low");
            }
        }


    }


    @Override
    public void onClick(View view) {
        onSelectedFragment(view);
        updateStatus(view);

    }


}
