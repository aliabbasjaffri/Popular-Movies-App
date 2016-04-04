package com.popularmoviesapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.popularmoviesapp.R;
import com.popularmoviesapp.fragment.DetailActivityFragment;
import com.popularmoviesapp.fragment.MainActivityFragment;
import com.popularmoviesapp.sync.MoviesSyncAdapter;
import com.popularmoviesapp.utils.Utility;

import android.support.v7.widget.Toolbar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.MovieCallback
{
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;
    String mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.ic_logo);
        getSupportActionBar().setTitle("");

        mCategory = Utility.getPreferredCategory(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own bloody action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        MoviesSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent( this , SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieItemSelected(Uri dataUri)
    {
        if (mTwoPane)
        {
            Bundle args = new Bundle();
            args.putParcelable(DetailActivityFragment.DETAIL_URI, dataUri);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG).commit();
        }
        else
            startActivity(new Intent(this, DetailActivity.class).setData(dataUri));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String category = Utility.getPreferredCategory(this);
        MainActivityFragment mainActivityFragment = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        DetailActivityFragment detailFragment = (DetailActivityFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);

        if (category!= null && !category.equals(mCategory))
        {
            if ( null != mainActivityFragment )
            {
                mainActivityFragment.onCategoryChanged();
            }
            if ( null != detailFragment )
            {
            //    detailFragment.onLocationChanged(location);
            }
            mCategory = category;
        }
        mainActivityFragment.mMovieGridAdapter.notifyDataSetChanged();
    }
}
