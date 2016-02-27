package com.popularmoviesapp.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by aliabbasjaffri on 28/02/16.
 */
public final class MoviesContract
{
    public interface GenresColumns
    {
        String GENRE_ID = "genre_id";
        String GENRE_NAME = "genre_name";
    }

    public interface MoviesColumns
    {
        String MOVIE_ID = "movie_id";
        String MOVIE_TITLE = "movie_title";
        String MOVIE_OVERVIEW = "movie_overview";
        String MOVIE_POPULARITY = "movie_popularity";
        String MOVIE_GENRE_IDS = "movie_genre_ids"; //This is a comma-separated list of genre ids.
        String MOVIE_VOTE_COUNT = "movie_vote_count";
        String MOVIE_VOTE_AVERAGE = "movie_vote_average";
        String MOVIE_RELEASE_DATE = "movie_release_date";
        String MOVIE_FAVORED = "movie_favored";
        String MOVIE_POSTER_PATH = "movie_poster_path";
        String MOVIE_BACKDROP_PATH = "movie_backdrop_path";
    }

    public static final String CONTENT_AUTHORITY = "com.popularmoviesapp";
    public static final Uri BASE_CONTENT_URI =  Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_GENRES = "genres";

    public static class Genres implements GenresColumns , BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRES).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.popularmoviesapp.genre";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.popularmoviesapp.genre";

        public static Uri buildGenresUri()
        {
            return CONTENT_URI;
        }

        public static Uri buildGenreUri(String genreID)
        {
            return CONTENT_URI.buildUpon().appendPath(genreID).build();
        }
    }

    public static class Movies implements MoviesColumns , BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.popularmoviesapp.movie";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.popularmoviesapp.movie";

        public static Uri buildMoviesUri()
        {
            return CONTENT_URI;
        }

        public static Uri buildMovieUri(String movieID)
        {
            return CONTENT_URI.buildUpon().appendPath(movieID).build();
        }
    }
    private MoviesContract() {
        throw new AssertionError("No instances.");
    }
}
