package com.olukoye.hannah.movies_stage2.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.olukoye.hannah.movies_stage2.R;


public class FavMovieViewHolder extends RecyclerView.ViewHolder{

    public ImageView imageView;

    public FavMovieViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageView);
    }
}
