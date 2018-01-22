package com.example.averma1212.popular_movies_2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by HP on 08-12-2017.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.example.averma1212.popular_movies_2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movie";



    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;

        public static final String _ID = "_id";
        public static final String MOVIE_ID = "movie_id";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String TITLE = "title";
        public static final String POSTER_PATH = "poster_path";
     //   public static final String POSTER_BLOB = "poster_blob";
        public static final String LANGUAGE = "language";
        public static final String RELEASE_DATE = "release_date";
        public static final String OVERVIEW = "overview";
    }

}
