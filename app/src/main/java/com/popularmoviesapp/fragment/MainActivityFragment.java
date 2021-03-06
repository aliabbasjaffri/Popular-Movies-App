package com.popularmoviesapp.fragment;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.popularmoviesapp.R;
import com.popularmoviesapp.adapter.MovieGridAdapter;
import com.popularmoviesapp.provider.MovieContract;
import com.popularmoviesapp.sync.MoviesSyncAdapter;
import com.popularmoviesapp.utils.Constants;
import com.popularmoviesapp.utils.Utility;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private boolean mainActivityFragment;
    private static final int GENERAL_MOVIE_LOADER = 0;
    private static final int FAVORITE_MOVIE_LOADER = 1;
    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    GridView gridView = null;
    public MovieGridAdapter mMovieGridAdapter = null;
    int mPosition = GridView.INVALID_POSITION;
    static final String SELECTED_KEY = "selectedPosition";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView)view.findViewById(R.id.mainMoviesGrid);
        mMovieGridAdapter = new MovieGridAdapter(getActivity() , null , 0 , true);
        gridView.setAdapter(mMovieGridAdapter);

        mainActivityFragment = getActivity().getIntent().getBooleanExtra( getResources().getString(R.string.mainActivityActivitySelector) , true);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                mPosition = position;
                if (cursor != null)
                {
                    if(mainActivityFragment)
                        ((MovieCallback) getActivity()).onMovieItemSelected(MovieContract.MovieEntry.buildMovieUri(cursor.getLong(Constants.MOVIE_ID)));
                    else
                        ((MovieCallback) getActivity()).onMovieItemSelected(MovieContract.FavouriteEntry.buildFavouriteUri(cursor.getLong(Constants.FAVORITE_MOVIE_ID)));
                }
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY))
            mPosition = savedInstanceState.getInt(SELECTED_KEY);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        if(mainActivityFragment)
            getLoaderManager().initLoader(GENERAL_MOVIE_LOADER, null, this);
        else
            getLoaderManager().initLoader(FAVORITE_MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        if(mainActivityFragment) {
            return new CursorLoader(getActivity(),
                    MovieContract.MovieEntry.CONTENT_URI,
                    Constants.MOVIE_COLUMNS,
                    MovieContract.MovieEntry.COLUMN_MOVIE_CATEGORY + " = ?",
                    new String[]{Utility.getPreferredCategory(getActivity())},
                    MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY + " ASC");
        }
        else
        {
            return new CursorLoader(getActivity(),
                    MovieContract.FavouriteEntry.CONTENT_URI,
                    Constants.FAVORITE_MOVIE_COLUMNS,
                    null,
                    null,
                    MovieContract.FavouriteEntry.COLUMN_MOVIE_POPULARITY + " ASC");
        }
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

    public void onCategoryChanged()
    {
        MoviesSyncAdapter.syncImmediately(getActivity());
        mMovieGridAdapter.notifyDataSetChanged();
        getLoaderManager().restartLoader(GENERAL_MOVIE_LOADER, null, this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mMovieGridAdapter.notifyDataSetChanged();

        if(mainActivityFragment)
            getLoaderManager().restartLoader(GENERAL_MOVIE_LOADER, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        if (mPosition != ListView.INVALID_POSITION)
            outState.putInt(SELECTED_KEY, mPosition);

        super.onSaveInstanceState(outState);
    }


    public interface MovieCallback
    {
        void onMovieItemSelected(Uri dataUri);
    }
}
