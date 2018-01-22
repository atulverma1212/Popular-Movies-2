package com.example.averma1212.popular_movies_2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import com.example.averma1212.popular_movies_2.adapters.*;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.averma1212.popular_movies_2.Util.*;

import java.net.URL;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
    mAdapter.onItemClickListener{

    //private GridView gv;
    private RecyclerView gv;
    private mAdapter imageAdapter;
    private String[] images;
    private String sortBy;
    private TextView tv_error;
    private TextView tv_connecting;
    private ProgressBar progressBar;
    private Menu my_menu;
    private int flag = 0;
    private static final String SORTBY = "sortBy_url";
    private static final String SCROLL_POSITION = "scrollPosition";
    private int scrollIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gv = (RecyclerView) findViewById(R.id.grid_view);
        tv_error = (TextView) findViewById(R.id.tv_error_message_display);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /*if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            gv.setLayoutManager(new GridLayoutManager(this,4));
        } else {
            gv.setLayoutManager(new GridLayoutManager(this,2));
        }*/

        gv.setLayoutManager(new GridLayoutManager(this,2));


        tv_connecting = (TextView) findViewById(R.id.connecting);
        if (savedInstanceState != null) {
            sortBy = savedInstanceState.getString(SORTBY);
            scrollIndex = savedInstanceState.getInt(SCROLL_POSITION);
        }
        setupSharedPreferences();

     /*   AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent startChild = new Intent(MainActivity.this, childActivity.class);
                startChild.putExtra("movie_class", position);
                startActivity(startChild);


                //   Toast.makeText(MainActivity.this,"#"+position,Toast.LENGTH_LONG).show();
            }
        };    */


     //   gv.setSmoothScrollbarEnabled(true);
    }


    public void showDataView() {
        tv_error.setVisibility(View.INVISIBLE);
        gv.setVisibility(View.VISIBLE);
    }

    public void showErrormsg() {
        tv_error.setVisibility(View.VISIBLE);
        //gv.invalidateViews();
        gv.setVisibility(View.INVISIBLE);
        flag = 1;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        my_menu = menu;
        if (flag == 1)
            my_menu.clear();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        if(id == R.id.favorites) {
            Intent startFavActivity = new Intent(this, favActivity.class);
            startActivity(startFavActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sortBy = sharedPreferences.getString(getString(R.string.pref_sortBy_key),
                getString(R.string.pref_sortBy_ratings_value));
        updateData(sortBy);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void updateData(String url){
        // url is sortBy string
        if(url.length()==0){
            Log.e("Error in update data","No string");
            return;
        }
      //  gv.invalidateViews();
        new FetchUrl().execute(sortBy);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_sortBy_key))){
          //  gv.invalidateViews();
            sortBy = sharedPreferences.getString(getString(R.string.pref_sortBy_key),
                    getString(R.string.pref_sortBy_ratings_value));
            updateData(sortBy);
            Intent startSettingsActivity = new Intent(this, MainActivity.class);
            startActivity(startSettingsActivity);
        }

    }

    @Override
    public void onItemClick(int position) {
        Intent startChild = new Intent(MainActivity.this, childActivity.class);
        startChild.putExtra("movie_class", position);
        startActivity(startChild);
    }

    public class FetchUrl extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            tv_connecting.setVisibility(View.VISIBLE);
            gv.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String[] doInBackground(String... sortBys) {
            if (sortBys.length == 0)
                return null;
            String Url = sortBys[0];
            //  Log.e("sortBy: ",Url);
            URL imageUrl = NetworkUtils.buildUrl(Url,null);
            String[] image_array = null;
            Log.e("sortBy: ", imageUrl.toString());
            ArrayList<movieDetails> details = new ArrayList<movieDetails>();
            try {
                String jsonString = NetworkUtils.getResponseFromHttpUrl(imageUrl);
                image_array = JsonStrongUtil.getImgUrlFromJson(jsonString);
                details = JsonStrongUtil.getDetails();
                movieDetails.set_movies(details);

                Log.e("image1: ", jsonString);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return image_array;
        }


        @Override
        protected void onPostExecute(String[] image_array) {
            progressBar.setVisibility(View.INVISIBLE);
            tv_connecting.setVisibility(View.INVISIBLE);
            gv.setVisibility(View.VISIBLE);
            if (image_array != null) {
                showDataView();
                imageAdapter = new mAdapter(MainActivity.this, image_array,MainActivity.this);
                gv.setAdapter(imageAdapter);
                //gv.smoothScrollToPosition(scrollIndex);

            } else {
                showErrormsg();
                if (my_menu != null)
                    my_menu.clear();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
     //   int index = gv.getFirstVisiblePosition();
        outState.putString(SORTBY,sortBy);
     //   outState.putInt(SCROLL_POSITION,index);
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  scrollIndex = gv.getFirstVisiblePosition();
      //  Log.e("index",String.valueOf(scrollIndex));
    }

    @Override
    protected void onResume() {
        super.onResume();
     //   gv.smoothScrollToPosition(scrollIndex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
