package com.popularmoviesapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.popularmoviesapp.R;
import com.popularmoviesapp.fragment.DetailActivityFragment;
import com.popularmoviesapp.fragment.MainActivityFragment;
import com.popularmoviesapp.provider.MovieContract;
import com.popularmoviesapp.sync.MoviesSyncAdapter;
import com.popularmoviesapp.utils.Utility;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.MovieCallback
{
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;
    String mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategory = Utility.getPreferredCategory(this);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_logo);
        getSupportActionBar().setTitle("");

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

            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG).commit();
        }
        else
            startActivity(new Intent(this, DetailActivity.class).setData(dataUri));

        Toast.makeText(MainActivity.this, "Item Clicked " + MovieContract.MovieEntry.getMovieIDFromUri(dataUri), Toast.LENGTH_SHORT).show();
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
