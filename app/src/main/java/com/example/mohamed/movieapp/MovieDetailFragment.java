package com.example.mohamed.movieapp;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
/**
 * Created by mohamed on 10/10/16.
 */
public class MovieDetailFragment extends Fragment {
    private RecyclerView mTarailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    public static final  String MOVE_M="MOVE_M";
    private TextView movieTitle, movieDate, movieDeuration, movieRate, overView;
    private Movie movie;
    private ArrayList<Movie> movies;
    private ArrayList<Review> reviews;
    private Callbacks mCallbacks;
    private Button favourite;
    private static final String MOVIE = "movie";
    public abstract interface Callbacks {
        abstract void onCrimeUpdated(Movie movie);
    }

    /*********************** return Instance from DetailFragment *****************/
    public static  MovieDetailFragment newInstance(Movie movie){
        Bundle bundle=new Bundle();
        bundle.putParcelable(MOVE_M,movie);
        MovieDetailFragment fragment=new MovieDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie=getArguments().getParcelable(MOVE_M);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.detail_fragment,container,false);
        mReviewRecyclerView= (RecyclerView) view.findViewById(R.id.Reviews_recyler_view);
        mTarailerRecyclerView= (RecyclerView) view.findViewById(R.id.trailer_recyler_view);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTarailerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        String baseUrl = "http://image.tmdb.org/t/p/w185/";

        ImageView moviePoster = (ImageView) view.findViewById(R.id.movie_poster);
        movieTitle = (TextView) view.findViewById(R.id.movie_title);
        movieDate = (TextView) view.findViewById(R.id.movie_date);
        movieDeuration = (TextView) view.findViewById(R.id.movie_minuts);
        movieRate = (TextView) view.findViewById(R.id.movie_rate);
        overView = (TextView) view.findViewById(R.id.overview);
        favourite = (Button) view.findViewById(R.id.fav_btn);


        if (movie != null) {

            Picasso.with(getActivity()).load(baseUrl + movie.getPoster_path()).into(moviePoster);
            movieTitle.setText(movie.getTitle());
            movieDate.setText(movie.formatDate());
            movieDeuration.setText(movie.getMinutes() + " Min");
            movieRate.setText(movie.getVote_average() + "/10");
            overView.setText(movie.getOverview());

            ItemsTask trailerTask = new ItemsTask();
            trailerTask.setContext(getActivity());
            trailerTask.execute(movie.getID() + "", "trailer");

            ItemsTask reviewsTask = new ItemsTask();
            reviewsTask.setContext(getActivity());
            reviewsTask.execute(movie.getID() + "", "review");
            try {
                movies = trailerTask.get();
                reviews = reviewsTask.get();
                movie.setTrailer(movies.get(0).getTrailer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            TrailerAdapter trailerAdapter=new TrailerAdapter(movies);
            ReviewAdapter reviewAdapter=new ReviewAdapter(reviews);
            mTarailerRecyclerView.setAdapter(trailerAdapter);
            mReviewRecyclerView.setAdapter(reviewAdapter);
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MovieDB movieDB = new MovieDB(getActivity());
                    int updated = movieDB.updateFavourite(movie.getID());
                    if (updated == 0) {
                        Toast.makeText(getActivity(), "Failed Adding To Favourite :(", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Successfully Adding To Favourite :)", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }

        return view;
    }

    /************* mReview Holder ***********************/
    private class ReviewHolder extends RecyclerView.ViewHolder{
        private TextView mAthouer;
        private TextView mContent;

        public ReviewHolder(View itemView) {
            super(itemView);
            mAthouer= (TextView) itemView.findViewById(R.id.Review_author);
            mContent= (TextView) itemView.findViewById(R.id.Review_content);

        }

        public void binddata(Review review){
            mAthouer.setText(review.getAuthor());
            mContent.setText(review.getContent());
        }
    }

/************************* mReview Adapter **************************/
private class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder>{
    private List<Review> mReviews;
    public ReviewAdapter(List<Review> reviews){
    mReviews=reviews;
    }


    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.reviw_item,parent,false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review=mReviews.get(position);
        holder.binddata(review);
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }
}

    /*********************     mTrailer Holder   ********************************/
    private class  TrailerHolder extends RecyclerView.ViewHolder{
        private TextView mTrailerText;
        public TrailerHolder(View itemView) {
            super(itemView);
            mTrailerText= (TextView) itemView.findViewById(R.id.trailer_txt);
            mTrailerText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("vnd.youtube:"
                                        + movie.getTrailer()));
                       startActivity(intent);

                    } catch (Exception e) {

                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/watch?v="
                                        + movie.getTrailer()));
                        startActivity(intent);

                    }
                }
            });
        }

        public void bind(int postion){
            Toast.makeText(getActivity(), postion+"", Toast.LENGTH_SHORT).show();
            mTrailerText.setText("Trailer " + (postion+ 1));
        }
    }

    /************************ mTrailer Adapter  ***********************/
    private class TrailerAdapter extends RecyclerView.Adapter<TrailerHolder>{
        private List<Movie> movies;
        public TrailerAdapter(List<Movie> mMovies){
            movies=mMovies;
        }
        @Override
        public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.trailer_item,parent,false);
            return new TrailerHolder(view);
        }

        @Override
        public void onBindViewHolder(TrailerHolder holder, int position) {
         holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       // mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }
}
