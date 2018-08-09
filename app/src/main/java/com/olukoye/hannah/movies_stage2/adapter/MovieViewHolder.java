package com.olukoye.hannah.movies_stage2.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.olukoye.hannah.movies_stage2.R;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;

    public MovieViewHolder(View itemView)

    {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);

    }
}
