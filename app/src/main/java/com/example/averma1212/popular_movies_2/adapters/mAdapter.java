package com.example.averma1212.popular_movies_2.adapters;

/**
 * Created by HP on 12-12-2017.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.averma1212.popular_movies_2.MainActivity;
import com.example.averma1212.popular_movies_2.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;



import static android.webkit.ConsoleMessage.MessageLevel.LOG;



public class mAdapter extends RecyclerView.Adapter<mAdapter.viewHolder>{
    private Context mContext;
    private String[] mThumbs;
    private final String image_base_url = "http://image.tmdb.org/t/p/w185";
    private onItemClickListener clickListener;

    public mAdapter(Context mContext, String[] mThumbs,onItemClickListener mClickListener) {
        this.mContext = mContext;
        this.mThumbs = mThumbs;
        this.clickListener = mClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface onItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        return new mAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mThumbs.length;
    }

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;

        public viewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.list_item_view);
           // imageView.setLayoutParams(new RecyclerView.LayoutParams(100,200));
            imageView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            if(mThumbs!=null)
                Picasso.with(mContext).load(image_base_url+mThumbs[listIndex]).into(imageView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            clickListener.onItemClick(position);
        }
    }

}








/*

public class mAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mThumbs;
    private final String image_base_url = "http://image.tmdb.org/t/p/w185";

    public mAdapter(Context c, String[] images) {
        mContext = c;
        mThumbs=images;
    }




    public int getCount() {
        return mThumbs.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(480,600));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }


        if(mThumbs!=null)
            Picasso.with(mContext).load(image_base_url+mThumbs[position]).into(imageView);
        return imageView;
    }

}*/

