package com.example.mohamed.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
/**
 * Created by mohamed on 10/10/16.
 */
public class DetailActivity extends SingleFragmentActivity {

    public static final String MOVIE = "movie";
    private Movie movie;

    @Override
    public Fragment CreateFragment() {
        movie=getIntent().getParcelableExtra(MOVIE);
        return MovieDetailFragment.newInstance(movie);
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, DetailActivity.class);

        intent.putExtra(MOVIE, movie);
        return intent;
    }


}
