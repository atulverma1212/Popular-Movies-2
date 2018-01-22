package com.example.averma1212.popular_movies_2;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.averma1212.popular_movies_2.data.MovieContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HP on 02-12-2017.
 */


public class movieDetails implements Parcelable{

    private static ArrayList <movieDetails> movies = new ArrayList<movieDetails>();

    private final String DATE = "release_date";
    private final String TITLE = "original_title";
    private final String VOTE = "vote_average";
    private final String PATH = "poster_path";
    private  final String LANG = "original_language";
    private  final String DESC = "overview";
    private final String ID = "id";
    private final String image_base_url = "http://image.tmdb.org/t/p/w185";


    private String date;
    private String votes;
    private String id;
    private String title;
    private String desc;
    private String lang;
    private String posterPath;

    public movieDetails() {
    }

    public movieDetails(Parcel in){
        id = in.readString();
        title = in.readString();
        desc = in.readString();
        posterPath = in.readString();
        lang = in.readString();
        votes = in.readString();
        date = in.readString();
    }

    public movieDetails(String id, String title, String desc, String posterPath, String lang, String votes, String date) {
        this.date = date;
        this.votes = votes;
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.lang = lang;
        this.posterPath = posterPath;
    }

    public movieDetails(JSONObject json) {
        try{
            date = json.getString(DATE);
            votes = json.getString(VOTE);
            desc = json.getString(DESC);
            lang = json.getString(LANG);
            posterPath = image_base_url+json.getString(PATH);
            title = json.getString(TITLE);
            id = json.getString(ID);

        } catch (JSONException e){
            Log.e("Const","JSon error");
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public movieDetails createFromParcel(Parcel in) {
            return new movieDetails(in);
        }

        public movieDetails[] newArray(int size) {
            return new movieDetails[size];
        }
    };

    public String getDate() {
        return date;
    }

    public String getVotes() {
        return votes;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getLang() {
        return lang;
    }

    public String getId() { return id; }

    public String getPosterPath() {
        return posterPath;
    }

    public static void set_movies(ArrayList<movieDetails> e){
        movies = new ArrayList<movieDetails>();
        movies = e;
    }

    public static movieDetails get_movie(int position){
        return movies.get(position);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(posterPath);
        dest.writeString(lang);
        dest.writeString(votes);
        dest.writeString(date);
    }
}
