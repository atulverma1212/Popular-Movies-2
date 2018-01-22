package com.example.averma1212.popular_movies_2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP on 08-12-2017.
 */



public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 3;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_TABLE = "CREATE TABLE "+ MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.MOVIE_ID + " INTEGER NOT NULL UNIQUE, " +
                MovieContract.MovieEntry.TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.POSTER_PATH + " TEXT NOT NULL, " +
            //    MovieContract.MovieEntry.POSTER_BLOB + "BLOB, " +
                MovieContract.MovieEntry.LANGUAGE + " TEXT NOT NULL, " +
             //   MovieContract.MovieEntry.POPULARITY + "TEXT NOT NULL, " +
                MovieContract.MovieEntry.VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.RELEASE_DATE + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
