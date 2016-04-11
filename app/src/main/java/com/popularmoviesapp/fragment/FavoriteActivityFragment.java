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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.popularmoviesapp.R;
import com.popularmoviesapp.adapter.MovieGridAdapter;
import com.popularmoviesapp.provider.MovieContract;
import com.popularmoviesapp.utils.Constants;
import com.popularmoviesapp.utils.Utility;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavoriteActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int MOVIE_LOADER = 0;
    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    GridView gridView = null;
    public MovieGridAdapter mMovieGridAdapter = null;
    int mPosition = GridView.INVALID_POSITION;
    static final String SELECTED_KEY = "selectedPosition";

    public FavoriteActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        gridView = (GridView)view.findViewById(R.id.favoriteMoviesGrid);
        mMovieGridAdapter = new MovieGridAdapter(getActivity() , null , 0);
        gridView.setAdapter(mMovieGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                mPosition = position;
                if (cursor != null) {
                    ((FavoriteMovieCallback) getActivity())
                            .onMovieItemSelected(MovieContract.FavouriteEntry.buildFavouriteUri(cursor.getLong(Constants.MOVIE_ID)));
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
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieContract.FavouriteEntry.CONTENT_URI,
                Constants.FAVORITE_MOVIE_COLUMNS,
                null, null,
                MovieContract.FavouriteEntry.COLUMN_MOVIE_POPULARITY + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieGridAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION)
            gridView.smoothScrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieGridAdapter.swapCursor(null);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mMovieGridAdapter.notifyDataSetChanged();
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION)
            outState.putInt(SELECTED_KEY, mPosition);

        super.onSaveInstanceState(outState);
    }

    public interface FavoriteMovieCallback
    {
        void onMovieItemSelected(Uri dataUri);
    }
}