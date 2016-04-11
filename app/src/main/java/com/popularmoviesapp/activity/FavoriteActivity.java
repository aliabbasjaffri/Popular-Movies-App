package com.popularmoviesapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.popularmoviesapp.R;
import com.popularmoviesapp.fragment.DetailActivityFragment;
import com.popularmoviesapp.fragment.FavoriteActivityFragment;

public class FavoriteActivity extends AppCompatActivity implements FavoriteActivityFragment.FavoriteMovieCallback
{
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        if (findViewById(R.id.movie_detail_container) != null)
        {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null)
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        }
        else
            mTwoPane = false;
    }

    @Override
    public void onMovieItemSelected(Uri dataUri) {
        
    }
}
