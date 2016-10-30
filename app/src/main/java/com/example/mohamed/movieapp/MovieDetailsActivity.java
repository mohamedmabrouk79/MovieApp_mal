package com.example.mohamed.movieapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by mohamed on 10/10/16.
 */

public class MovieDetailsActivity extends SingleFragmentActivity{
    /*************** get Intent from MovieDetailsActivity  ****************/
    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,MovieDetailsActivity.class);

        return intent;
    }
    @Override
    public Fragment CreateFragment() {
        return MovieDetailsFragment.newIstance();
    }
}
