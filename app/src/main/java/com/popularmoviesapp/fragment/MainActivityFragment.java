package com.popularmoviesapp.fragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.popularmoviesapp.R;
import com.popularmoviesapp.adapter.MovieGridAdapter;
import com.popularmoviesapp.provider.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int FORECAST_LOADER = 0;
    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    GridView gridView = null;
    private SharedPreferences sharedPref = null;
    MovieGridAdapter mMovieGridAdapter = null;
    int mPosition = GridView.INVALID_POSITION;
    static final String SELECTED_KEY = "selectedPosition";

    static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT,
            MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_MOVIE_LIKED
    };

    public static final int MOVIE_ID = 0;
    public static final int MOVIE_TITLE = 0;
    public static final int MOVIE_OVERVIEW = 0;
    public static final int MOVIE_VOTE_COUNT = 0;
    public static final int MOVIE_RELEASE_DATE = 0;
    public static final int MOVIE_POSTER_PATH = 0;
    public static final int MOVIE_BACKDROP_PATH = 0;
    public static final int MOVIE_LIKED = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView)view.findViewById(R.id.mainMoviesGrid);
        mMovieGridAdapter = new MovieGridAdapter(getActivity() , null , 0);
        gridView.setAdapter(mMovieGridAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT + " ASC";
        Uri movieUri = MovieContract.MovieEntry.CONTENT_URI;
        return new CursorLoader(getActivity(), movieUri, MOVIE_COLUMNS , null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mMovieGridAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION)
            gridView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        mMovieGridAdapter.swapCursor(null);
    }


    public interface MovieCallback
    {
        void onMovieItemSelected(Uri dataUri);
    }
}
