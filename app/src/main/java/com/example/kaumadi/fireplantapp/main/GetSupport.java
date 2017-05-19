package com.example.kaumadi.fireplantapp.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class GetSupport extends FragmentActivity {
    public void FragmentHandler(int id, Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(id,fragment);
        transaction.commit();
    }
}
