package com.popularmoviesapp.utils;

import com.popularmoviesapp.provider.MovieContract;

/**
 * Created by aliabbasjaffri on 23/03/16.
 */
public class Constants {
    public final static String MOVIE_DATE_FORMAT = "yyyy-MM-dd";
    public final static String MOVIES_KEY = "movies";
    public static final String POSTER_IMAGE_KEY = "poster";
    public final static String TWO_PANE_KEY = "two_pane";
    public final static String MOVIE_DETAIL_KEY = "movie_detail";
    public static final String POSITION_KEY = "position";
    public final static String MODE_VIEW = "mode_view";
    public final static String MOVIE_URL = "http://api.themoviedb.org";
    public final static String IMAGE_MOVIE_URL = "http://image.tmdb.org/t/p/";
    public final static String IMAGE_SIZE_W185 = "w185/";
    public final static String IMAGE_SIZE_W342 = "w342/";
    public final static String IMAGE_SIZE_W500 = "w500/";
    public final static String IMAGE_SIZE_W780 = "w780/";
    public static final String SORT_BY_POPULARITY_DESC = "popularity.desc";
    public static final String SORT_BY_RATING_DESC = "vote_average.desc";
    public static final String POSTER_IMAGE_VIEW_KEY = "posterImageView";
    public static final String FAVORITE = "favorite";
    public static final String YOU_TUBE_VIDEO_URL = "http://www.youtube.com/watch?v=";
    public static final String YOU_TUBE_IMG_URL = "http://img.youtube.com/vi/%s/default.jpg";
    public static final String YOU_TUBE = "YouTube";
    public static final String TRAILERS = "trailers";
    public static final String REVIEWS = "reviews";
    public static final String MAIN_TRAILER = "main_trailer";
    public static final String SHOW_FAVORITES = "show_favorites";

    public static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT,
            MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_MOVIE_LIKED
    };

    public static final int MOVIE_ID = 0;
    public static final int MOVIE_TITLE = 1;
    public static final int MOVIE_OVERVIEW = 2;
    public static final int MOVIE_POPULARITY = 3;
    public static final int MOVIE_VOTE_COUNT = 4;
    public static final int MOVIE_RELEASE_DATE = 5;
    public static final int MOVIE_POSTER_PATH = 6;
    public static final int MOVIE_BACKDROP_PATH =7;
    public static final int MOVIE_LIKED = 8;

    // Interval at which to sync with the weather, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int WEATHER_NOTIFICATION_ID = 3004;
}
