package com.popularmoviesapp.provider;

import android.content.Context;
import android.net.Uri;

import com.popularmoviesapp.model.Movie;

import java.util.List;

import nl.littlerobots.cupboard.tools.provider.CupboardContentProvider;
import nl.littlerobots.cupboard.tools.provider.UriHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by aliabbasjaffri on 01/03/16.
 */
public class MovieProvider extends CupboardContentProvider
{
    private static final String DATABASE_NAME = "crossOverDb.db";
    private static final int DATABASE_VERSION = 1;
    public static final String CONTENT_AUTHORITY = "com.udacity.popularmoviesapp";
    public static final String BASE_CONTENT_URI = "content://" + CONTENT_AUTHORITY + "/movies";

    protected MovieProvider()
    {
        super(BASE_CONTENT_URI, DATABASE_NAME, DATABASE_VERSION);
    }

    public static Movie getMovieData(Context context, long id) {
        UriHelper uriHelper = UriHelper.with(MovieProvider.BASE_CONTENT_URI);
        Uri moviesUri = uriHelper.getUri(Movie.class);
        return cupboard().withContext(context).query(moviesUri, Movie.class).withSelection("id = ?", "" + id).get();
    }

    public static void deleteMovieData(Context context, long id) {
        UriHelper uriHelper = UriHelper.with(MovieProvider.BASE_CONTENT_URI);
        Uri moviesUri = uriHelper.getUri(Movie.class);
        cupboard().withContext(context).delete(moviesUri, "id = ?", id + "");
    }

    public static void putMovieData(Context context, Movie mData) {
        UriHelper uriHelper = UriHelper.with(MovieProvider.BASE_CONTENT_URI);
        Uri movieUri = uriHelper.getUri(Movie.class);
        cupboard().withContext(context).put(movieUri, mData);
    }

    public static List<Movie> getFavorites(Context context){
        UriHelper uriHelper = UriHelper.with(MovieProvider.BASE_CONTENT_URI);
        Uri movieUri = uriHelper.getUri(Movie.class);
        return cupboard().withContext(context).query(movieUri, Movie.class).list();
    }
}
