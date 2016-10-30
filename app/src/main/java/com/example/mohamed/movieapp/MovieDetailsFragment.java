package com.example.mohamed.movieapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mohamed on 10/10/16.
 */

public class MovieDetailsFragment extends Fragment {

    /************** instance from MovieDetailsFragment  **************/
    public static MovieDetailsFragment newIstance(){
        return new MovieDetailsFragment();
    }
    /********** create view **************/
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
    View view=inflater.inflate(R.layout.movie_details_fragment,container,false);

        return view;
    }
}