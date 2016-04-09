package com.popularmoviesapp.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmoviesapp.BuildConfig;
import com.popularmoviesapp.R;
import com.popularmoviesapp.provider.MovieContract;
import com.popularmoviesapp.utils.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int DETAIL_LOADER = 0;
    public static final String DETAIL_URI = "URI";
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    ImageView backDropImage;
    ImageView posterImage;
    ImageView trailerPlayButtonImage;
    ImageView favoriteButtonImage;
    TextView movieTitle;
    TextView movieReleaseDate;
    TextView movieRating;
    TextView movieOverview;

    Uri mUri;
    String youtubeKey;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle arguments = getArguments();
        if (arguments != null)
            mUri = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);

        backDropImage = (ImageView) view.findViewById(R.id.fragmentDetailsMovieBackDropImage);
        posterImage = (ImageView) view.findViewById(R.id.fragmentDetailsMoviePosterImage);
        trailerPlayButtonImage = (ImageView) view.findViewById(R.id.fragmentDetailsMovieTrailerPlayButton);
        favoriteButtonImage = (ImageView) view.findViewById(R.id.fragmentDetailsMovieFavoriteImage);
        movieTitle = (TextView) view.findViewById(R.id.fragmentDetailsMovieTitleText);
        movieReleaseDate = (TextView) view.findViewById(R.id.fragmentDetailsMovieReleaseDateText);
        movieRating = (TextView) view.findViewById(R.id.fragmentDetailsMovieRatingText);
        movieOverview = (TextView) view.findViewById(R.id.fragmentDetailsMovieOverviewText);

        trailerPlayButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        favoriteButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: on click to add in database. Also persist the movie liked field.
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        if( null != mUri )
        {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    Constants.MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");

        if (data != null && data.moveToFirst())
        {
            Picasso.with(getActivity())
                   .load(Constants.IMAGE_MOVIE_URL + Constants.IMAGE_SIZE_W500 + data.getString(Constants.MOVIE_BACKDROP_PATH))
                   .placeholder(R.drawable.ic_movie_placeholder)
                   .error(R.drawable.ic_movie_placeholder)
                   .into(backDropImage);

            Picasso.with(getActivity())
                    .load(Constants.IMAGE_MOVIE_URL + Constants.IMAGE_SIZE_W185 + data.getString(Constants.MOVIE_POSTER_PATH))
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_movie_placeholder)
                    .into(posterImage);

            favoriteButtonImage.setImageResource(data.getString(Constants.MOVIE_LIKED).equals("0") ? R.drawable.ic_favorite_border : R.drawable.ic_favorite);

            movieTitle.setText(data.getString(Constants.MOVIE_TITLE));
            movieReleaseDate.setText(data.getString(Constants.MOVIE_RELEASE_DATE));
            movieRating.setText(data.getString(Constants.MOVIE_POPULARITY));
            movieOverview.setText(data.getString(Constants.MOVIE_OVERVIEW));

            if(data.getString(Constants.MOVIE_YOUTUBE_KEY).equals(""))
                new GetYoutubeKeyAsyncTask().execute(data.getString(Constants.MOVIE_API_ID));
            else
                youtubeKey = data.getString(Constants.MOVIE_YOUTUBE_KEY);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private String getMovieYoutubeID(String id)
    {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuilder buffer = new StringBuilder();

        try {
            final String MOVIE_BASE_URL =
                    Constants.MOVIE_URL;
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(id)
                    .appendPath(Constants.VIDEO)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null)
                buffer.append(line).append("\n");

            if (buffer.length() == 0)
                return null;

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        }
        finally
        {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return buffer.toString();
    }

    private String getMovieYouTubeKeyFromJSON(String JSON)
    {
        final String OMA_RESULTS = "results";
        final String OMA_TYPE = "type";
        final String OMA_KEY = "key";
        String youtubeKey = "";

        try {
            JSONObject movieVideoJson = new JSONObject(JSON);
            JSONArray movieVideoArray = movieVideoJson.getJSONArray(OMA_RESULTS);

            for (int i = 0; i < movieVideoArray.length(); i++)
            {
                JSONObject movieObject = movieVideoArray.getJSONObject(i);

                if(movieObject.getString(OMA_TYPE).equals(Constants.TRAILER))
                    youtubeKey = movieObject.getString(OMA_KEY);
            }
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return youtubeKey;
    }

    private class GetYoutubeKeyAsyncTask extends AsyncTask<String , Void , String>
    {
        private String movieID;

        @Override
        protected String doInBackground(String... params)
        {
            movieID = params[0];
            return getMovieYoutubeID(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            youtubeKey = getMovieYouTubeKeyFromJSON(s);
            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_YOUTUBE_KEY, youtubeKey);
            getContext().getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, movieValues, MovieContract.MovieEntry.COLUMN_MOVIE_API_ID + " = ?", new String[]{movieID});
        }
    }
}
