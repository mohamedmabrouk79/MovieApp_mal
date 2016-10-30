package com.example.mohamed.movieapp;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mohamed on 8/28/16.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {
  public abstract Fragment CreateFragment();


    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        FragmentManager fragmentManager=getSupportFragmentManager();
        Fragment fragment=fragmentManager.findFragmentById(R.id.Fragment_Container);

        if (fragment==null){
            fragment=CreateFragment();
            fragmentManager.beginTransaction().add(R.id.Fragment_Container,fragment).commit();
        }

    }
}
