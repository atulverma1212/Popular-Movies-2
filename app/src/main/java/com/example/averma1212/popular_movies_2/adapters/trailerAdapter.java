package com.example.averma1212.popular_movies_2.adapters;

/**
 * Created by HP on 12-12-2017.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.averma1212.popular_movies_2.R;
import com.squareup.picasso.Picasso;


public class trailerAdapter extends RecyclerView.Adapter<trailerAdapter.trailerHolder>{
    private static final String TAG = trailerHolder.class.getSimpleName();
    private int mNumberItems;
    private Context mContext;
    private String[][] trailer;
    final private ListItemClickListener mOnClickListener;
    private final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    public trailerAdapter(Context c, int mNumberItems,String[][] sTrailer, ListItemClickListener listener) {
        mContext = c;
        this.mNumberItems = mNumberItems;
        mOnClickListener = listener;
        trailer = sTrailer;
    }

    @Override
    public trailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        return new trailerHolder(view);
    }

    @Override
    public void onBindViewHolder(trailerHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class trailerHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        ImageView trailer_img;
        TextView trailer_desc;
        private final String image_base_url = "http://img.youtube.com/vi/";
        private trailerHolder(View itemView) {
            super(itemView);
            trailer_img = (ImageView) itemView.findViewById(R.id.trailer_img);
            trailer_desc = (TextView) itemView.findViewById(R.id.trailer_desc);
            itemView.setOnClickListener(this);
        }
        void bind(int listIndex){
            String image_uri = image_base_url + trailer[listIndex][0] + "/0.jpg";
            Picasso.with(mContext).load(image_uri).into(trailer_img);
            trailer_desc.setText(trailer[listIndex][1]);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
