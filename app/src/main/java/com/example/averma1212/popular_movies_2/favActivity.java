package com.example.averma1212.popular_movies_2;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.averma1212.popular_movies_2.adapters.favAdapter;
import com.example.averma1212.popular_movies_2.data.MovieContract;

import java.util.ArrayList;

public class favActivity extends AppCompatActivity implements favAdapter.onFavItemClickListener{

    private static final int CURSOR_LOADER = 223;
    private RecyclerView fav_list;
    TextView noFavMovie;
    private Cursor sCursor;
    private ArrayList<movieDetails> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        fav_list = (RecyclerView) findViewById(R.id.gv_favorites);
        fav_list.setLayoutManager(new GridLayoutManager(this,2));
        noFavMovie = (TextView) findViewById(R.id.tv_NoFavMovie);
        //getSupportLoaderManager().initLoader(CURSOR_LOADER,null,this);
        new DataFetchTask().execute();
    }

    private void extractTitles() {
        sCursor.moveToFirst();
        do{
            String id = sCursor.getString(sCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_ID));
            String title = sCursor.getString(sCursor.getColumnIndex(MovieContract.MovieEntry.TITLE));
            String desc = sCursor.getString(sCursor.getColumnIndex(MovieContract.MovieEntry.OVERVIEW));
            String posterPath = sCursor.getString(sCursor.getColumnIndex(MovieContract.MovieEntry.POSTER_PATH));
            String lang = sCursor.getString(sCursor.getColumnIndex(MovieContract.MovieEntry.LANGUAGE));
            String votes = sCursor.getString(sCursor.getColumnIndex(MovieContract.MovieEntry.VOTE_AVERAGE));
            String date = sCursor.getString(sCursor.getColumnIndex(MovieContract.MovieEntry.RELEASE_DATE));
            movieDetails movie = new movieDetails(id,title,desc,posterPath,lang,votes,date);
            movies.add(movie);
        }while(sCursor.moveToNext());
    }


    private void displayNoFavMovie() {
        noFavMovie.setVisibility(View.VISIBLE);
        fav_list.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFavItemClick(int position) {
        Intent startChild = new Intent(favActivity.this, childActivity.class);
        startChild.putExtra("movie_id",movies.get(position));
        startActivity(startChild);
    }

 /*   @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            private Cursor mData;

            @Override
            protected void onStartLoading() {
                if (mData != null) {
                    deliverResult(mData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        null,null,null,null);
            }

            @Override
            public void deliverResult(Cursor data) {
                mData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor.getCount()==0){
            displayNoFavMovie();
        } else {
            sCursor=cursor;
            extractTitles();
        }
        favAdapter fAdapter = new favAdapter(favActivity.this, cursor, favActivity.this);
        fav_list.setAdapter(fAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }*/


    public class DataFetchTask extends AsyncTask<Void, Void, Cursor> {

        // Invoked on a background thread
        @Override
        protected Cursor doInBackground(Void... params) {
            return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,null,null,null);
        }


        // Invoked on UI thread
        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if(cursor.getCount()==0){
                displayNoFavMovie();
            } else {
                sCursor=cursor;
                extractTitles();
            }
            favAdapter fAdapter = new favAdapter(favActivity.this,cursor,favActivity.this);
            fav_list.setAdapter(fAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new DataFetchTask().execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("extra",1);
    }
}