package com.example.averma1212.popular_movies_2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.averma1212.popular_movies_2.R;
import com.squareup.picasso.Picasso;

/**
 * Created by HP on 12-12-2017.
 */

public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.reviewHolder>{
    private int mNumberItems;
    private String[][] reviews;

    public reviewAdapter(int mNumberItems,String[][] sReview) {
        reviews = sReview;
        this.mNumberItems = mNumberItems;
    }

    @Override
    public reviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        return new reviewHolder(view);
    }

    @Override
    public void onBindViewHolder(reviewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class reviewHolder extends RecyclerView.ViewHolder {
        TextView review_desc;
        TextView review_writer;
        public reviewHolder(View itemView) {
            super(itemView);
            review_desc = (TextView) itemView.findViewById(R.id.review_desc);
            review_writer = (TextView) itemView.findViewById(R.id.review_writer);
        }

        void bind(int listIndex){
            review_desc.setText(reviews[listIndex][0]);
            review_writer.setText("-".concat(reviews[listIndex][1]));
        }
    }
}
