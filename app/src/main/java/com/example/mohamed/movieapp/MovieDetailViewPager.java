package com.example.mohamed.movieapp;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mohamed on 10/20/16.
 */
public class MovieDetailViewPager extends AppCompatActivity implements MovieDetailFragment.Callbacks {
    public static final String EXTRA_movie_ID = "com.example.mohamedmabrouk.movie_id";
    public static final String  MOVIES_OBJECT = "com.example.mohamedmabrouk.movies_object";
    public static ViewPager mViewPager;
    private List<Movie> movies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_view_pager);
        int movieid=(int)getIntent().getSerializableExtra(EXTRA_movie_ID);
        mViewPager=(ViewPager)findViewById(R.id.detail_view_pager);
        FragmentManager fragmentManager=getSupportFragmentManager();
        movies= (List<Movie>) getIntent().getSerializableExtra(MOVIES_OBJECT);
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Movie movie = movies.get(position);
                return MovieDetailFragment.newInstance(movie);
            }

            @Override
            public int getCount() {
                return movies.size();
            }
        });
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getID()==movieid) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    //******* for return intent ********////////
    public static Intent newIntent(Context context, int uuid,List<Movie> movies){
        Intent intent=new Intent(context,MovieDetailViewPager.class);
        intent.putExtra(EXTRA_movie_ID, uuid);
        intent.putExtra(MOVIES_OBJECT, (Serializable) movies);
        return intent;
    }

    @Override
    public void onCrimeUpdated(Movie movie) {
    }


}
