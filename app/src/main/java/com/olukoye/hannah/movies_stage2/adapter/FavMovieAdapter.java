package com.olukoye.hannah.movies_stage2.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olukoye.hannah.movies_stage2.database.FavMoviesTable;
import com.olukoye.hannah.movies_stage2.R;
import com.squareup.picasso.Picasso;


import java.util.List;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieViewHolder> {

    private List<FavMoviesTable> messageList;
    private Context context;

    public FavMovieAdapter(Context context, List<FavMoviesTable> messageList) {
        this.messageList = messageList;
        this.context = context;
    }

    @Override
    public FavMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_row_layout, parent, false);
        return new FavMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavMovieViewHolder holder, int position) {
        FavMoviesTable favMovie = messageList.get(position);

        //holder.content.setText(favMovie.getMovieId());
        final String imageUrl = favMovie.getPoster();
        // This is how we use Picasso to load images from the internet.
        Picasso.with(context)
                .load(imageUrl)
                .into(holder.favImageView);

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


}
