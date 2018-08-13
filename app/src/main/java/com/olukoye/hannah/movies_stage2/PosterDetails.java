package com.olukoye.hannah.movies_stage2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.olukoye.hannah.movies_stage2.database.AppDatabase;
import com.olukoye.hannah.movies_stage2.database.DaoAccess;
import com.olukoye.hannah.movies_stage2.database.FavMoviesTable;
import com.olukoye.hannah.movies_stage2.databinding.ActivityPosterDetailsBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class PosterDetails extends AppCompatActivity {
    private ActivityPosterDetailsBinding posterBinding;
    private String title, description, rating, posterUrl, id, video_key, reviewText, reviewAuthor;
    DaoAccess favDao;
    FavMoviesTable favmoviestable;
    SharedPreferences pref;
    String currentFav;
    String movieApiKey = BuildConfig.MOVIE_API_KEY;
    boolean mFav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posterBinding = DataBindingUtil.setContentView(this, R.layout.activity_poster_details);

        pref = getSharedPreferences("FavouriteMovies", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        Bundle bundle = getIntent().getExtras();

        favDao = AppDatabase.getInstance(getApplicationContext()).message();
        favDao.fetchAllMoviesIds().observe(PosterDetails.this, (List<FavMoviesTable> favmovie) -> {

            for (int i = 0; i < favmovie.size(); i++) {
                currentFav = favmovie.get(i).getMovieId().toString();
                if (currentFav.contains(id)) {
                    Log.d("Present ID:", id);
                    posterBinding.favouriteButton.setText(getString(R.string.fav_text_selected));
                    mFav = true;
                } else {
                    mFav = false;
                }
            }
        });
        title = bundle.getString("title");
        description = bundle.getString("description");
        rating = bundle.getString("rating");
        posterUrl = bundle.getString("posterurl");
        id = bundle.getString("movie_id");

        posterBinding.tvTitle.setText(title);
        if (rating != null) {
            posterBinding.tvRating.setText(getString(R.string.rating_title) + " " + rating);
        } else {
            posterBinding.tvRating.setVisibility(View.INVISIBLE);
        }
        if (description != null) {
            posterBinding.tvDescription.setText(description);
        } else {
            posterBinding.tvDescription.setVisibility(View.INVISIBLE);
        }
        Picasso.with(this).load(posterUrl).into(posterBinding.ivThumbnail);

        posterBinding.favouriteButton.setOnClickListener(v -> {

            if (mFav) {
                favmoviestable = new FavMoviesTable(id, title, posterUrl);

                favmoviestable.setMovieId(id);
                favmoviestable.setMovieName(title);
                favmoviestable.setMovieName(posterUrl);
                favDao.deleteFavMovies(favmoviestable);

                posterBinding.favouriteButton.setText(getString(R.string.fav_text));


            } else {
                favmoviestable = new FavMoviesTable(id, title, posterUrl);

                favmoviestable.setMovieId(id);
                favmoviestable.setMovieName(title);
                favmoviestable.setMovieName(posterUrl);
                favDao.insertOnlySingleMovie(favmoviestable);

                posterBinding.favouriteButton.setText(getString(R.string.fav_text_selected));

                edit.putString("FavouriteMovies", id);
                edit.apply();
            }
        });

        new showTrailer().execute();
        new getReviews().execute();

    }

    public class showTrailer extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            Log.i("Background Task", "Loading Trailer details");
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(getString(R.string.movie_url_base) + "/movie/" +
                        id + "/videos?" + "api_key=" + movieApiKey);
                Log.i("Url passed", String.valueOf(url));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);

                if (e.getMessage().contains("Unable to resolve host")) {
                    return e.getMessage();
                } else if (e.getMessage().contains("DOCTYPE HTML")) {
                    return e.getMessage();
                } else if (e.getMessage().contains("unreachable")) {
                    return e.getMessage();
                } else {
                    return null;
                }
            }
        }

        protected void onPostExecute(String response) {

            try {
                JSONObject details = (JSONObject) new JSONTokener(response).nextValue();

                JSONArray videoResults = details.getJSONArray("results");
                JSONObject videoData = videoResults.getJSONObject(0);
                video_key = videoData.getString("key");

                posterBinding.trailerButton.setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View v) {
                                                                       Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video_key));
                                                                       Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                                                               Uri.parse("http://www.youtube.com/watch?v=" + video_key));
                                                                       try {
                                                                           startActivity(appIntent);
                                                                       } catch (ActivityNotFoundException ex) {
                                                                           startActivity(webIntent);
                                                                       }
                                                                   }
                                                               }
                );

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class getReviews extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            Log.i("Background Task", "Loading Reviews");
        }

        protected String doInBackground(Void... urls) {
            try {
                ///movie/{movie_id}/videos"
                URL url = new URL(getString(R.string.movie_url_base) + "/movie/" +
                        id + "/reviews?" + "api_key=" + movieApiKey);
                Log.i("Url passed", String.valueOf(url));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);

                if (e.getMessage().contains("Unable to resolve host")) {
                    return e.getMessage();
                } else if (e.getMessage().contains("DOCTYPE HTML")) {
                    return e.getMessage();
                } else if (e.getMessage().contains("unreachable")) {
                    return e.getMessage();
                } else {
                    return null;
                }
            }
        }

        protected void onPostExecute(String response) {

            try {
                JSONObject details = (JSONObject) new JSONTokener(response).nextValue();

                JSONArray reviewResults = details.getJSONArray("results");
                JSONObject reviewData = reviewResults.getJSONObject(0);
                reviewText = reviewData.getString("content");
                reviewAuthor = reviewData.getString("author");

                posterBinding.tvReview.setText(reviewText);
                posterBinding.tvReviewAuthor.setText(getString(R.string.author_title) + ": " + reviewAuthor);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
