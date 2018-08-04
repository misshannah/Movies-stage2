package com.olukoye.hannah.movies_stage2;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.olukoye.hannah.movies_stage2.databinding.ActivityPosterDetailsBinding;
import com.squareup.picasso.Picasso;

public class PosterDetails extends AppCompatActivity {
    private ActivityPosterDetailsBinding posterBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posterBinding = DataBindingUtil.setContentView(this, R.layout.activity_poster_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String title = bundle.getString("title");
            String description = bundle.getString("description");
            String rating = bundle.getString("rating");
            String posterUrl = bundle.getString("posterurl");
            posterBinding.tvTitle.setText(title);
            posterBinding.tvRating.setText(rating);
            Picasso.with(this).load(posterUrl).into(posterBinding.ivThumbnail);
            posterBinding.tvDescription.setText(description);
        }

    }
}
