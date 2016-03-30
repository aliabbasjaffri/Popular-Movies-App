package com.popularmoviesapp.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by aliabbasjaffri on 16/03/16.
 */
public class MovieContract
{
    public static final String CONTENT_AUTHORITY = "com.udacity.popularmoviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movies";
    public static final String PATH_FAVOURITE = "favourites";

    public interface MoviesColumns
    {
        String COLUMN_MOVIE_TITLE = "movie_title";
        String COLUMN_MOVIE_OVERVIEW = "movie_overview";
        String COLUMN_MOVIE_POPULARITY = "movie_popularity";
        String COLUMN_MOVIE_VOTE_COUNT = "movie_vote_count";
        String COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";
        String COLUMN_MOVIE_GENRE = "movie_genre";
        String COLUMN_MOVIE_POSTER_PATH = "movie_poster_path";
        String COLUMN_MOVIE_BACKDROP_PATH = "movie_backdrop_path";
    }

    public static final class MovieEntry implements BaseColumns , MoviesColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "Movies";

        public static final String COLUMN_MOVIE_LIKED = "movie_liked";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getMovieIDFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }

    public static final class FavouriteEntry implements BaseColumns , MoviesColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;

        public static final String TABLE_NAME = "Favourites";

        public static Uri buildFavouriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getFavouriteIDFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
    }
}
