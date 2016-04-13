package com.popularmoviesapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.popularmoviesapp.R;
import com.popularmoviesapp.fragment.DetailActivityFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Bundle arguments = new Bundle();
        arguments.putParcelable(DetailActivityFragment.DETAIL_URI, getIntent().getData());
        arguments.putBoolean(DetailActivityFragment.ACTIVITY_FRAGMENT_SELECTOR, getIntent().getBooleanExtra(DetailActivityFragment.ACTIVITY_FRAGMENT_SELECTOR , true));

        DetailActivityFragment detailFragment = new DetailActivityFragment();
        detailFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, detailFragment).commit();
    }
}
