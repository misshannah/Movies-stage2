package com.olukoye.hannah.movies_stage2.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.olukoye.hannah.movies_stage2.R;


public class FavMovieViewHolder extends RecyclerView.ViewHolder{

    public ImageView favImageView;

    public FavMovieViewHolder(View itemView) {
        super(itemView);

        favImageView = (ImageView)itemView.findViewById(R.id.favImageView);
    }
}
