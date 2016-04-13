package com.popularmoviesapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.popularmoviesapp.R;
import com.popularmoviesapp.utils.Utility;
import android.support.v7.app.AppCompatActivity;
import com.popularmoviesapp.sync.MoviesSyncAdapter;
import com.popularmoviesapp.fragment.MainActivityFragment;
import com.popularmoviesapp.fragment.DetailActivityFragment;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.MovieCallback
{
    private boolean mainActivity;       //Distinguishes whether main activity of favorite activity; True for main, false for favorite
    private String movieName;

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;
    String mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = Utility.getPreferredCategory(this);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent != null)
        {
            mainActivity = intent.getBooleanExtra("ActivitySelector", true);
        }

        if (mainActivity) {
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.drawable.ic_logo);
            getSupportActionBar().setTitle("");
        }
        else {
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(" Favorite Movies");
        }

        if (findViewById(R.id.movie_detail_container) != null)
        {
            mTwoPane = true;
            if (savedInstanceState == null)
            {
                Bundle bundle = new Bundle();
                bundle.putBoolean(DetailActivityFragment.ACTIVITY_FRAGMENT_SELECTOR, mainActivity);

                DetailActivityFragment fragment = new DetailActivityFragment();
                fragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment , DETAILFRAGMENT_TAG)
                        .commit();
            }
        }
        else
            mTwoPane = false;

        if(mainActivity)
            MoviesSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(mainActivity) {
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }
        else return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mainActivity) {
            int id = item.getItemId();

            if (id == R.id.action_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            } else if (id == R.id.action_favorites) {
                startActivity(new Intent(this, MainActivity.class).putExtra("ActivitySelector", !mainActivity).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
        else return false;
    }

    @Override
    public void onMovieItemSelected(Uri dataUri)
    {
        if (mTwoPane)
        {
            Bundle args = new Bundle();
            args.putParcelable(DetailActivityFragment.DETAIL_URI, dataUri);
            args.putBoolean(DetailActivityFragment.ACTIVITY_FRAGMENT_SELECTOR, mainActivity);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG).commit();
        }
        else
            startActivity(new Intent(this, DetailActivity.class).setData(dataUri).putExtra(DetailActivityFragment.ACTIVITY_FRAGMENT_SELECTOR , mainActivity));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String category = Utility.getPreferredCategory(this);
        MainActivityFragment mainActivityFragment = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.movies_list);
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
