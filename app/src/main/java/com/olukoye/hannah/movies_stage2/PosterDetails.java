package com.olukoye.hannah.movies_stage2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

public class PosterDetails extends AppCompatActivity {
    private ActivityPosterDetailsBinding posterBinding;
    String title,description,rating,posterUrl,id,video_key,reviewText,reviewAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posterBinding = DataBindingUtil.setContentView(this, R.layout.activity_poster_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            description = bundle.getString("description");
            rating = bundle.getString("rating");
            posterUrl = bundle.getString("posterurl");
            id = bundle.getString("movie_id");

            posterBinding.tvTitle.setText(title);
            posterBinding.tvRating.setText(getString(R.string.rating_title)+" "+rating);
            posterBinding.tvDescription.setText(description);
            Picasso.with(this).load(posterUrl).into(posterBinding.ivThumbnail);

            posterBinding.favouriteButton.setOnClickListener(new View.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(View v) {

                                                                 }
                                                             }
                );
            new showTrailer().execute();
            new getReviews().execute();
        }

    }

    public class showTrailer extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            Log.i("Background Task","Loading Trailer details");
        }

        protected String doInBackground(Void... urls) {
            try {
                ///movie/{movie_id}/videos"
                URL url = new URL(getString(R.string.movie_url_base) + "/movie/" +
                        id + "/videos?"+ "api_key=" + getString(R.string.movie_api_key));
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
            Log.i("Background Task","Loading Reviews");
        }

        protected String doInBackground(Void... urls) {
            try {
                ///movie/{movie_id}/videos"
                URL url = new URL(getString(R.string.movie_url_base) + "/movie/" +
                        id + "/reviews?"+ "api_key=" + getString(R.string.movie_api_key));
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
                posterBinding.tvReviewAuthor.setText(getString(R.string.author_title)+": "+reviewAuthor);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}