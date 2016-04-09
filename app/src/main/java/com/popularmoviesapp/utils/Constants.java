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
    public final static String VIDEO = "videos";
    public final static String MOVIE_URL = "http://api.themoviedb.org/3/movie/";
    public final static String IMAGE_MOVIE_URL = "http://image.tmdb.org/t/p/";
    public final static String IMAGE_SIZE_W185 = "w185/";
    public final static String IMAGE_SIZE_W342 = "w342/";
    public final static String IMAGE_SIZE_W500 = "w500/";
    public final static String IMAGE_SIZE_W780 = "w780/";
    public static final String YOU_TUBE_VIDEO_URL = "http://www.youtube.com/watch?v=";
    public static final String YOU_TUBE_IMG_URL = "http://img.youtube.com/vi/%s/default.jpg";
    public static final String YOU_TUBE = "YouTube";
    public static final String TRAILER = "Trailer";
    public static final String CLIP = "Clip";
    public static final String MAIN_TRAILER = "main_trailer";
    public static final String SHOW_FAVORITES = "show_favorites";
    public static final String POPULAR_MOVIES = "popular";
    public static final String TOP_RATED_MOVIES = "top_rated";
    public static final String UPCOMING_MOVIES = "upcoming";

    public static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_API_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT,
            MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_CATEGORY,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_MOVIE_LIKED,
            MovieContract.MovieEntry.COLUMN_MOVIE_YOUTUBE_KEY
    };

    public static final String[] FAVORITE_MOVIE_COLUMNS = {
            MovieContract.FavouriteEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_API_ID,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_TITLE,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_POPULARITY,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_VOTE_COUNT,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_DATE,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_CATEGORY,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_POSTER_PATH,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_BACKDROP_PATH,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_YOUTUBE_KEY,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_BACKDROP_IMAGE_BLOB,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_POSTER_IMAGE_BLOB
    };

    public static final int MOVIE_ID = 0;
    public static final int MOVIE_API_ID = 1;
    public static final int MOVIE_TITLE = 2;
    public static final int MOVIE_OVERVIEW = 3;
    public static final int MOVIE_POPULARITY = 4;
    public static final int MOVIE_VOTE_COUNT = 5;
    public static final int MOVIE_RELEASE_DATE = 6;
    public static final int MOVIE_GENRE = 7;
    public static final int MOVIE_POSTER_PATH = 8;
    public static final int MOVIE_BACKDROP_PATH = 9;
    public static final int MOVIE_LIKED = 10;
    public static final int MOVIE_YOUTUBE_KEY = 11;

    public static final int FAVORITE_MOVIE_ID = 0;
    public static final int FAVORITE_MOVIE_API_ID = 1;
    public static final int FAVORITE_MOVIE_TITLE = 2;
    public static final int FAVORITE_MOVIE_OVERVIEW = 3;
    public static final int FAVORITE_MOVIE_POPULARITY = 4;
    public static final int FAVORITE_MOVIE_VOTE_COUNT = 5;
    public static final int FAVORITE_MOVIE_RELEASE_DATE = 6;
    public static final int FAVORITE_MOVIE_CATEGORY = 7;
    public static final int FAVORITE_MOVIE_POSTER_PATH = 8;
    public static final int FAVORITE_MOVIE_BACKDROP_PATH = 9;
    public static final int FAVORITE_MOVIE_YOUTUBE_KEY = 10;
    public static final int FAVORITE_MOVIE_BACKDROP_IMAGE_BLOB = 11;
    public static final int FAVORITE_MOVIE_POSTER_IMAGE_BLOB = 12;

    // Interval at which to sync with the movies, in seconds.
    // (60 seconds (1 minute) * 60 * 12 hours) seconds
    public static final int SYNC_INTERVAL = 60 * 60 * 12;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/2;
    public static final int WEATHER_NOTIFICATION_ID = 3004;
}
