package com.example.averma1212.popular_movies_2;

/**
 * Created by HP on 02-12-2017.
 */


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.averma1212.popular_movies_2.Util.DbImageUtil;
import com.example.averma1212.popular_movies_2.Util.JsonStrongUtil;
import com.example.averma1212.popular_movies_2.Util.NetworkUtils;
import com.example.averma1212.popular_movies_2.adapters.*;

import com.example.averma1212.popular_movies_2.data.MovieContract;
import com.example.averma1212.popular_movies_2.data.MoviesProvider;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class childActivity extends AppCompatActivity
    implements trailerAdapter.ListItemClickListener{
    RecyclerView reviewList;
    RecyclerView trailerList;
    ImageButton favButton;
    ImageView img;
    private int isFav=0;
    private Toast mToast;
    private String[][] trailerId;
    ProgressBar trailerProgress;
    ProgressBar reviewProgress;
    ScrollView mainScroll;
    private static int scrollY = -1,scrollX=0;

    private movieDetails movie;
    private final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        TextView desc = (TextView) findViewById(R.id.child_desc);
        TextView avg_votes = (TextView) findViewById(R.id.child_votes);
        TextView lang = (TextView) findViewById(R.id.child_lang);
        TextView date = (TextView) findViewById(R.id.child_date);
        TextView title = (TextView) findViewById(R.id.child_title);
        img = (ImageView) findViewById(R.id.child_img);
        favButton = (ImageButton) findViewById(R.id.favButton);
        mainScroll = (ScrollView) findViewById(R.id.mainScroll);

        reviewList = (RecyclerView) findViewById(R.id.rv_reviews);
        reviewProgress = (ProgressBar) findViewById(R.id.review_loading_indicator);
        trailerList = (RecyclerView) findViewById(R.id.rv_trailers);
        trailerProgress = (ProgressBar) findViewById(R.id.trailer_loading_indicator);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager_1 = new LinearLayoutManager(this);
        reviewList.setLayoutManager(layoutManager);
        trailerList.setLayoutManager(layoutManager_1);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                reviewList.getContext(),
                layoutManager.getOrientation()
        );

        reviewList.addItemDecoration(mDividerItemDecoration);
        trailerList.addItemDecoration(mDividerItemDecoration);


        reviewList.setHasFixedSize(true);
        trailerList.setHasFixedSize(true);


        Intent StartingIntent = getIntent();
        int position;

        if(StartingIntent.hasExtra("movie_id")){
            movie = StartingIntent.getParcelableExtra("movie_id");
        }
        else {
            try {
                position = StartingIntent.getIntExtra("movie_class", 0);
                movie = movieDetails.get_movie(position);
            } catch (Exception e) {
                String TAG = "In childActivity";
                Log.e(TAG, "error receiving position");
            }
        }

        new FetchReview().execute(movie.getId());
        desc.setText(movie.getDesc());
        avg_votes.setText(movie.getVotes());
        date.setText(movie.getDate());
        title.setText(movie.getTitle());
        // Log.e(TAG,movie.getPoster_path());
        Picasso.with(childActivity.this).load(movie.getPosterPath()).into(img);
        isFav = isFavorite();
        updateButton(isFav);

        switch (movie.getLang()){
            case "hi":
                lang.setText(R.string.hindi);
                break;

            case "ja":
                lang.setText(R.string.japanese);
                break;

            default:
                lang.setText(R.string.english);
                break;
        }

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFav==0)
                    addToFav();
                else
                    remFromFav();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void updateButton(int isFav){
        if(isFav==1)
            favButton.setImageResource(R.drawable.star_on);
        else
            favButton.setImageResource(R.drawable.star_off);
    }

    private int isFavorite() {
        int isFavorite = 0;

        Cursor movieCursor = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{
                        MovieContract.MovieEntry._ID,
                        MovieContract.MovieEntry.MOVIE_ID
                },
                MovieContract.MovieEntry.MOVIE_ID + " = ?",
                new String[]{movie.getId()},
                null
        );

        if (movieCursor.moveToFirst()) {
            isFavorite =1;
        }

        movieCursor.close();
        return isFavorite;
    }


    public View getLineView()
    {
        View view=new View(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setBackgroundColor(Color.GRAY);
        view.setMinimumHeight(2);
        return  view;
    }

    public void noReview(){
        reviewList.setVisibility(View.INVISIBLE);
        TextView no_review = (TextView) findViewById(R.id.noReview);
        no_review.setVisibility(View.VISIBLE);
    }

    public void noTrailer(){
        trailerList.setVisibility(View.INVISIBLE);
        TextView no_trailer = (TextView) findViewById(R.id.noTrailer);
        no_trailer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        String key = trailerId[clickedItemIndex][0];

        startActivity(new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(YOUTUBE_URL+key)));
    }


    public class FetchReview extends AsyncTask<String, Void, String[][][]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reviewList.setVisibility(View.INVISIBLE);
            reviewProgress.setVisibility(View.VISIBLE);
            trailerProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[][][] doInBackground(String... reviewStrings) {
            if (reviewStrings.length == 0)
                return null;
            String Url = reviewStrings[0];
            //  Log.e("sortBy: ",Url);
            URL reviewUrl = NetworkUtils.buildUrl(Url,"reviews");
            URL trailerUrl = NetworkUtils.buildUrl(Url,"videos");
            String[][][] result_array = new String[2][][];
            String[][] review_array = null;
            String[][] trailer_array=null;
           // Log.e("sortBy: ", reviewUrl.toString());
            try {
                String reviewString = NetworkUtils.getResponseFromHttpUrl(reviewUrl);
                review_array = JsonStrongUtil.getReviewFromJson(reviewString);
                String trailerString = NetworkUtils.getResponseFromHttpUrl(trailerUrl);
                trailer_array = JsonStrongUtil.getTrailerFromJson(trailerString);
                result_array[0] = review_array;
                result_array[1] = trailer_array;
               // Log.e("review1: ", review_array[0][0]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result_array;
        }

        @Override
        protected void onPostExecute(String[][][] strings) {
            if(strings[0].length==0)
                noReview();
            if(strings[1].length==0)
                noTrailer();
            trailerId = strings[1];
            reviewAdapter rAdapter = new reviewAdapter(strings[0].length,strings[0]);
            reviewList.setAdapter(rAdapter);
            trailerAdapter tAdapter = new trailerAdapter(childActivity.this,strings[1].length,strings[1],childActivity.this);
            trailerList.setAdapter(tAdapter);
            reviewList.setVisibility(View.VISIBLE);
            reviewProgress.setVisibility(View.INVISIBLE);
            trailerProgress.setVisibility(View.INVISIBLE);
            mainScroll.post(new Runnable() {
                @Override
                public void run() {
                    mainScroll.scrollTo(scrollX,scrollY);
                }
            });
        }
    }

    public void addToFav()
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(MovieContract.MovieEntry.TITLE,movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.OVERVIEW,movie.getDesc());
        contentValues.put(MovieContract.MovieEntry.VOTE_AVERAGE,String.valueOf(movie.getVotes()));
        contentValues.put(MovieContract.MovieEntry.POSTER_PATH, movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.RELEASE_DATE, movie.getDate());
        contentValues.put(MovieContract.MovieEntry.MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.LANGUAGE, movie.getLang());
        this.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        String added = getString(R.string.addedToFav);
        Toast.makeText(this,added,Toast.LENGTH_SHORT).show();
        isFav = 1;
        updateButton(isFav);
    }

    public void remFromFav(){
        getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,MovieContract.MovieEntry.MOVIE_ID+"=?",new String[]{String.valueOf(movie.getId())});
        String removed = getString(R.string.removedFromFav);
        Toast.makeText(this,removed,Toast.LENGTH_SHORT).show();
        isFav=0;
        updateButton(isFav);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();
        scrollY = mainScroll.getScrollY();
        scrollX = mainScroll.getScrollX();
    }



    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
