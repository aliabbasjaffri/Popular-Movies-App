package com.popularmoviesapp.fragment;

import android.database.Cursor;
import android.net.Uri;
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

import com.popularmoviesapp.R;
import com.popularmoviesapp.utils.Constants;
import com.squareup.picasso.Picasso;

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

            trailerPlayButtonImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            favoriteButtonImage.setImageResource(data.getString(Constants.MOVIE_LIKED).equals("0") ? R.drawable.ic_favorite_border : R.drawable.ic_favorite );

            favoriteButtonImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: on click to add in database. Also persist the movie liked field.
                }
            });

            movieTitle.setText(data.getString(Constants.MOVIE_TITLE));
            movieReleaseDate.setText(data.getString(Constants.MOVIE_RELEASE_DATE));
            movieRating.setText(data.getString(Constants.MOVIE_POPULARITY));
            movieOverview.setText(data.getString(Constants.MOVIE_OVERVIEW));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
