package com.example.mohamed.movieapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
/**
 * Created by mohamed on 8/28/16.
 */
public class MainActivity extends SingleFragmentActivity implements MoviesListFragment.Callbacks{


    @Override
    public Fragment CreateFragment() {
        return new MoviesListFragment();
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onMovieSelected(List<Movie> movies, Movie movie) {
            if (findViewById(R.id.detail_fragment_container) == null) {
                Intent intent = MovieDetailViewPager.newIntent(this,movie.getID(),movies);
                startActivity(intent);
            } else {
                Fragment newDetail = MovieDetailFragment.newInstance(movie);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment_container, newDetail)
                        .commit();
            }
    }
}