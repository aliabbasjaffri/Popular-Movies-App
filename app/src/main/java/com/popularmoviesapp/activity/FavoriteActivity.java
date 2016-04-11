package com.popularmoviesapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.popularmoviesapp.R;
import com.popularmoviesapp.fragment.FavoriteActivityFragment;

public class FavoriteActivity extends AppCompatActivity
{
    private static final String FAVORITEFRAGMENT_TAG = "FAVTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_favorite_container, new FavoriteActivityFragment(), FAVORITEFRAGMENT_TAG)
                .commit();
    }

}
