package com.example.averma1212.popular_movies_2.Util;

/**
 * Created by HP on 08-12-2017.
 */

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static String TAG = "Error";
    private final static String apiBaseUrl = "http://api.themoviedb.org/3/movie";
    private final static String key ="Your Key";
    private final static String queryParameter = "api_key";




    public static URL buildUrl(String sortBy,String mode){
        Uri uri=null;
        if(mode==null)
            uri = Uri.parse(apiBaseUrl).buildUpon().appendPath(sortBy).appendQueryParameter(queryParameter,key).build();
        else
            uri = Uri.parse(apiBaseUrl).buildUpon().appendPath(sortBy).appendPath(mode).appendQueryParameter(queryParameter,key).build();
        String builtUri = String.valueOf(uri);

        Log.d("TAG",builtUri);

        URL url = null;
        try{
            url = new URL(builtUri);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}



