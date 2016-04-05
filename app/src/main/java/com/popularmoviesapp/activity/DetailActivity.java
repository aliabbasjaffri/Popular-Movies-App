package com.popularmoviesapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.popularmoviesapp.R;
import com.popularmoviesapp.fragment.DetailActivityFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = new Bundle();
        arguments.putParcelable(DetailActivityFragment.DETAIL_URI, getIntent().getData());
        DetailActivityFragment detailFragment = new DetailActivityFragment();
        detailFragment.setArguments(arguments);

        getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, detailFragment).commit();
    }

}
