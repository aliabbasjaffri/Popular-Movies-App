package com.popularmoviesapp.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.popularmoviesapp.BuildConfig;
import com.popularmoviesapp.R;
import com.popularmoviesapp.provider.MovieContract;
import com.popularmoviesapp.utils.Constants;
import com.popularmoviesapp.utils.Utility;
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
    public static final String ACTIVITY_FRAGMENT_SELECTOR = "ACTIVITY_FRAGMENT_SELECTOR";

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
    boolean mainAcitivtyFragment;

    String youtubeKey;
    String movieAPIID = "";
    String movieName = "";
    boolean enableVideo;

    Cursor favoriteCursor = null;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        enableVideo = false;

        Bundle arguments = getArguments();

        if (arguments != null) {
            mUri = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
            mainAcitivtyFragment = arguments.getBoolean(DetailActivityFragment.ACTIVITY_FRAGMENT_SELECTOR);
        }

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
                if (enableVideo)
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOU_TUBE_VIDEO_URL + youtubeKey)).putExtra("force_fullscreen", true));
            }
        });


        favoriteButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enableVideo && mainAcitivtyFragment)
                    new CreateFavoriteItem().execute(favoriteCursor);
                //else if (enableVideo && !mainAcitivtyFragment)
                //    new CreateFavoriteItem().execute(favoriteCursor);
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
            if(mainAcitivtyFragment) {
                return new CursorLoader(
                        getActivity(),
                        mUri,
                        Constants.MOVIE_COLUMNS,
                        null,
                        null,
                        null
                );
            }
            else
            {
                return new CursorLoader(
                        getActivity(),
                        mUri,
                        Constants.FAVORITE_MOVIE_COLUMNS,
                        null,
                        null,
                        null
                );
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if (data != null && data.moveToFirst())
        {
            if(mainAcitivtyFragment) {
                favoriteCursor = data;

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

                movieTitle.setText(movieName = data.getString(Constants.MOVIE_TITLE));
                movieReleaseDate.setText(data.getString(Constants.MOVIE_RELEASE_DATE));
                movieRating.setText(data.getString(Constants.MOVIE_POPULARITY));
                movieOverview.setText(data.getString(Constants.MOVIE_OVERVIEW));
                movieAPIID = data.getString(Constants.MOVIE_API_ID);

                if (data.getString(Constants.MOVIE_YOUTUBE_KEY).equals(""))
                    new GetYoutubeKeyAsyncTask().execute(movieAPIID);
                else {
                    youtubeKey = data.getString(Constants.MOVIE_YOUTUBE_KEY);
                    enableVideo = true;
                }
            }
            else
            {
                backDropImage.setImageBitmap(Utility.getImage(data.getBlob(Constants.FAVORITE_MOVIE_BACKDROP_IMAGE_BLOB)));
                posterImage.setImageBitmap(Utility.getImage(data.getBlob(Constants.FAVORITE_MOVIE_POSTER_IMAGE_BLOB)));
                favoriteButtonImage.setImageResource(R.drawable.ic_favorite);
                movieTitle.setText(movieName = data.getString(Constants.FAVORITE_MOVIE_TITLE));
                movieReleaseDate.setText(data.getString(Constants.FAVORITE_MOVIE_RELEASE_DATE));
                movieRating.setText(data.getString(Constants.FAVORITE_MOVIE_POPULARITY));
                movieOverview.setText(data.getString(Constants.FAVORITE_MOVIE_OVERVIEW));
                movieAPIID = data.getString(Constants.FAVORITE_MOVIE_API_ID);
                enableVideo = true;
            }
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
            Log.v("Video Wali JSON" , JSON);
            JSONObject movieVideoJson = new JSONObject(JSON);
            JSONArray movieVideoArray = movieVideoJson.getJSONArray(OMA_RESULTS);

            if(movieVideoArray.isNull(0))
                return "";

            for (int i = 0; i < movieVideoArray.length(); i++)
            {
                JSONObject movieObject = movieVideoArray.getJSONObject(i);

                if(movieObject.getString(OMA_TYPE).equals(Constants.TRAILER)) {
                    youtubeKey = movieObject.getString(OMA_KEY);
                    break;
                }
                if(movieObject.getString(OMA_TYPE).equals(Constants.CLIP))
                    youtubeKey = movieObject.getString(OMA_KEY);
            }
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return youtubeKey;
    }

    private String insertInFavorite(Cursor data)
    {
        data.moveToFirst();
        Cursor cursor = getActivity().getContentResolver().query(MovieContract.FavouriteEntry.CONTENT_URI, Constants.FAVORITE_MOVIE_COLUMNS , MovieContract.FavouriteEntry.COLUMN_MOVIE_API_ID + " = ? " , new String [] {movieAPIID} , null);
         if( cursor == null || cursor.getCount() == 0  )
         {
             ContentValues movieValues = new ContentValues();

             movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_API_ID, data.getString(Constants.MOVIE_API_ID));
             movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, data.getString(Constants.MOVIE_TITLE));
             movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, data.getString(Constants.MOVIE_OVERVIEW));
             movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY, data.getString(Constants.MOVIE_POPULARITY));
             movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, data.getString(Constants.MOVIE_VOTE_COUNT));
             movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, data.getString(Constants.MOVIE_RELEASE_DATE));
             movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_CATEGORY, data.getString(Constants.MOVIE_CATEGORY));
             movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, data.getString(Constants.MOVIE_POSTER_PATH));
             movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, data.getString(Constants.MOVIE_BACKDROP_PATH));
             movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_YOUTUBE_KEY, youtubeKey);
             movieValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_POSTER_IMAGE_BLOB, Utility.getBytesFromBitmap(((BitmapDrawable) posterImage.getDrawable()).getBitmap()));
             movieValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_BACKDROP_IMAGE_BLOB, Utility.getBytesFromBitmap(((BitmapDrawable) backDropImage.getDrawable()).getBitmap()));

             getActivity().getContentResolver().insert(MovieContract.FavouriteEntry.CONTENT_URI, movieValues);
             return movieAPIID;
         }
        else return "none";
    }

    private class GetYoutubeKeyAsyncTask extends AsyncTask<String,Void,String>
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

            if(!youtubeKey.equals("")) {
                ContentValues movieValues = new ContentValues();
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_YOUTUBE_KEY, youtubeKey);
                getActivity().getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, movieValues, MovieContract.MovieEntry.COLUMN_MOVIE_API_ID + " = ?", new String[]{movieID});
                enableVideo = true;
            }
            else
                Toast.makeText(getActivity(), "No Video URL For " + ( !movieName.equals("") ? movieName : "movie"), Toast.LENGTH_LONG).show();
        }
    }

    private class CreateFavoriteItem extends AsyncTask<Cursor , Void , String >
    {
        @Override
        protected String doInBackground(Cursor... params)
        {
            return insertInFavorite(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(!s.equals("none")) {
                ContentValues movieValues = new ContentValues();
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_LIKED, "1");
                getActivity().getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, movieValues, MovieContract.MovieEntry.COLUMN_MOVIE_API_ID + " = ?", new String[]{s});
                synchronized (getActivity().getContentResolver())
                {
                    getActivity().getContentResolver().notifyAll();
                }
                favoriteButtonImage.setImageResource(R.drawable.ic_favorite);
                Toast.makeText(getActivity(), movieName + " added in Favorite List", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getActivity(), movieName + " already in Favorite List", Toast.LENGTH_SHORT).show();
        }
    }
}
