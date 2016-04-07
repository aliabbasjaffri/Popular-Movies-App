package com.popularmoviesapp.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmoviesapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment
{
    public static final String DETAIL_URI = "URI";

    ImageView backDropImage;
    ImageView posterImage;
    ImageView trailerPlayButtonImage;
    ImageView favoriteButtonImage;
    TextView movieTitle;
    TextView movieReleaseDate;
    TextView movieRating;
    TextView movieOverview;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        backDropImage = (ImageView) view.findViewById(R.id.fragmentDetailsMovieBackDropImage);
        posterImage = (ImageView) view.findViewById(R.id.fragmentDetailsMoviePosterImage);
        trailerPlayButtonImage = (ImageView) view.findViewById(R.id.fragmentDetailsMovieTrailerPlayButton);
        favoriteButtonImage = (ImageView) view.findViewById(R.id.fragmentDetailsMovieFavoriteImage);
        movieTitle = (TextView) view.findViewById(R.id.fragmentDetailsMovieTitleText);
        movieReleaseDate = (TextView) view.findViewById(R.id.fragmentDetailsMovieReleaseDateText);
        movieRating = (TextView) view.findViewById(R.id.fragmentDetailsMovieRatingText);
        movieOverview = (TextView) view.findViewById(R.id.fragmentDetailsMovieOverviewText);

        return view;
    }
}
