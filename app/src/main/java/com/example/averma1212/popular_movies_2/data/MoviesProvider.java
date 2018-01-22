package com.example.averma1212.popular_movies_2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by HP on 08-12-2017.
 */

public class MoviesProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES,MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES+"/#",MOVIES_WITH_ID);
        return uriMatcher;
    }





    private MovieDbHelper DbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = DbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch(match){
            case MOVIES:
                cursor =  db.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return cursor;
        }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        String type;
        switch (match){
            case MOVIES:
                type = MovieContract.MovieEntry.CONTENT_DIR_TYPE;
                break;
            case MOVIES_WITH_ID:
                type = MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
                break;
            default:
                throw new SQLException("Unknown Uri: " + uri);
        }
        return type;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = DbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIES:
                long id = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,contentValues);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert rows into "+uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = DbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int moviesDeleted;

        switch (match) {
            case MOVIES:
                moviesDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.MovieEntry.TABLE_NAME + "'");
                break;

            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                moviesDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,"_id=?", new String[]{id});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (moviesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection,String[] selectionArgs) {
            final SQLiteDatabase db = DbHelper.getWritableDatabase();
            int moviesUpdated = 0;

            if (contentValues == null) {
                throw new IllegalArgumentException("Cannot have null content values");
            }

            switch (sUriMatcher.match(uri)) {
                case MOVIES: {
                    moviesUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME,
                            contentValues,
                            selection,
                            selectionArgs);
                    break;
                }
                case MOVIES_WITH_ID: {
                    String id = uri.getPathSegments().get(1);
                    moviesUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME,
                            contentValues,
                            MovieContract.MovieEntry._ID + " = ?",
                            new String[]{id});
                    break;
                }
                default: {
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
                }
            }

            if (moviesUpdated > 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }

            return moviesUpdated;
        }

}
