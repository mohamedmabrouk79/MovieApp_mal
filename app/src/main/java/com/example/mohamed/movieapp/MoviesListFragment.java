package com.example.mohamed.movieapp;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by mohamed on 10/12/16.
 */
public class MoviesListFragment  extends Fragment{
    public static final String TAG="MainFragment";
    public static Callbacks mCallbacks;
    public static List<Movie> Mmovies=new ArrayList<>();

    /****************** Instance from MoviesListFragment ****************/
    public static MoviesListFragment newInstance (){
        return new MoviesListFragment();
    }

    public interface Callbacks {
        void onMovieSelected(List<Movie> movie,Movie Mmovie);
    }
    private RecyclerView mRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view=inflater.inflate(R.layout.list_movie,container,false);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.movie_app_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        taskFactory("popular");
        return view;
    }

    /******************* movie Holder ********************/
    private class MovieHolder extends RecyclerView.ViewHolder{
        private Movie movie;
        private List<Movie> mMovies;
        String baseUrl = "http://image.tmdb.org/t/p/w185/";
     private ImageView mImageView;
        public MovieHolder(View itemView) {
            super(itemView);
            mImageView= (ImageView) itemView.findViewById(R.id.image_view);

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //startActivity(MovieDetailViewPager.newIntent(getActivity(),movie.getID(),mMovies));
                    mCallbacks.onMovieSelected(mMovies,movie);

                }
            });
        }

        public void bind(Movie mMovie,List<Movie> movies){
            mMovies=movies;
            movie=mMovie;
            Picasso.with(getActivity())
                    .load(baseUrl+mMovie.getPoster_path())
                    .placeholder(R.drawable.movie_poster)
                    .into(mImageView);
        }
    }


    /*********************  movie Adapter **************/
    private class  MovieAdapter extends RecyclerView.Adapter<MovieHolder>{
        private List<Movie> movies;
        public MovieAdapter(List<Movie> mMovies){
            movies=mMovies;
        }
        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.film_item,parent,false);
            return  new MovieHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position) {
         Movie movie=movies.get(position);
            holder.bind(movie,movies);
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.main_menu, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                taskFactory("popular");
                break;
            case R.id.top_rated:
                taskFactory("top_rated");
                break;
            case R.id.favourite:
                taskFactory("favourite");
                break;
            case R.id.menu_item_share:
                setShareIntent();
                break;
            default:
                taskFactory("popular");
        }
        return true;
    }

    public  List<Movie> taskFactory(String type) {
        ArrayList<Movie> movies = null;



        ItemsTask itemsTask = new ItemsTask();
        itemsTask.setContext(getActivity());


        itemsTask.execute(type, "");

        try {
            movies = itemsTask.get();
        } catch (InterruptedException e) {
            Log.v(TAG,"no items is get from json");
        } catch (ExecutionException e) {
        }
         UpdateUi(movies);
        Mmovies=movies;
        return movies;

    }

    private void UpdateUi(List<Movie> movies){
        MovieAdapter movieAdapter=new MovieAdapter(movies);
        mRecyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }


    private void setShareIntent() {
        Cursor cursor = new MovieDB(getActivity()).selectMovie(-2);
        cursor.moveToFirst();
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        /*
         "This A " + cursor.getString(cursor.getColumnIndex("title")) + " Movie Details \n" +
                "Overview :  " + cursor.getString(cursor.getColumnIndex("overview"));
         */
        String shareBody = "https://wwww.youtube.com/?v=" + cursor.getString(cursor.getColumnIndex("trailer"));
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Movie Url");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }
}
