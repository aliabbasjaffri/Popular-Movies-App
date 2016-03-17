package com.popularmoviesapp.fragment;

import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.popularmoviesapp.R;
import com.popularmoviesapp.provider.DatabaseHelper;
import com.popularmoviesapp.provider.MovieProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
    //@Bind(R.id.fragmentMainID) TextView textView;
    TextView textView;


//    @Override
//    public void onCreate(Bundle savedInstanceState)
//    {
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView)view.findViewById(R.id.mainMoviesGrid);

        return view;
    }
}
