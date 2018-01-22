package com.example.averma1212.popular_movies_2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.averma1212.popular_movies_2.R;
import com.example.averma1212.popular_movies_2.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by HP on 14-12-2017.
 */


public class favAdapter extends RecyclerView.Adapter<favAdapter.viewHolder>{
    private Cursor mCursor;
    private Context mContext;
    private String[] mThumbs;
    onFavItemClickListener mFavItemClickListener;

    private final String image_base_url = "http://image.tmdb.org/t/p/w185";

    public favAdapter(Context c, Cursor cursor,onFavItemClickListener listener) {
        mContext = c;
        mCursor = cursor;
        mFavItemClickListener = listener;
        if(cursor.getCount()>0)
            extractThumbs();
    }

    // parent activity will implement this method to respond to click events
    public interface onFavItemClickListener {
        void onFavItemClick(int position);
    }

    private void extractThumbs() {
        mThumbs = new String[mCursor.getCount()];
        int i=0;
        String thumb=null;
        mCursor.moveToFirst();
        do{
            thumb = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.POSTER_PATH));
            Log.e("thumb",thumb);
            mThumbs[i++] = thumb;
        }while(mCursor.moveToNext());
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        return new favAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(mCursor!=null && mCursor.getCount()>0)
            return mCursor.getCount();
        return 0;
    }

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;

        public viewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.list_item_view);
            imageView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            if(mThumbs!=null)
                Picasso.with(mContext).load(mThumbs[listIndex]).into(imageView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mFavItemClickListener.onFavItemClick(position);
        }
    }

}





/*
public class favAdapter extends BaseAdapter {
    private Cursor mCursor;
    private Context mContext;
    private String[] mThumbs;

    private final String image_base_url = "http://image.tmdb.org/t/p/w185";

    public favAdapter(Context c, Cursor cursor) {
        mContext = c;
        mCursor = cursor;
        if(cursor.getCount()>0)
            extractThumbs();
    }

    private void extractThumbs() {
        mThumbs = new String[mCursor.getCount()];
        int i=0;
        String thumb=null;
        mCursor.moveToFirst();
        do{
            thumb = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.POSTER_PATH));
            Log.e("thumb",thumb);
            mThumbs[i++] = thumb;
        }while(mCursor.moveToNext());
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
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
            Picasso.with(mContext).load(mThumbs[position]).into(imageView);
        return imageView;
    }
}*/
