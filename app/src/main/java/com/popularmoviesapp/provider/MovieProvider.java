package com.popularmoviesapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by aliabbasjaffri on 01/03/16.
 */
public class MovieProvider extends ContentProvider
{
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DatabaseHelper mDatabaseHelper;

    static final int MOVIE = 200;
    static final int MOVIE_WITH_ID = 201;

    static final int FAVOURITE = 300;
    static final int FAVOURITE_WITH_ID = 301;

    private static final SQLiteQueryBuilder sMovies;
    private static final SQLiteQueryBuilder sFavourites;

    static
    {
        sMovies = new SQLiteQueryBuilder();
        sFavourites = new SQLiteQueryBuilder();

        sMovies.setTables(
                MovieContract.MovieEntry.TABLE_NAME
        );

        sFavourites.setTables(
                MovieContract.FavouriteEntry.TABLE_NAME
        );
    }

    private static final String sMovieIDSelection =
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID + " = ? ";

    private static final String sFavouriteIDSelection =
            MovieContract.FavouriteEntry.TABLE_NAME + "." + MovieContract.FavouriteEntry._ID + " = ? ";

    private Cursor getMovieByID(Uri uri, String[] projection, String sortOrder)
    {
        long id = MovieContract.MovieEntry.getMovieIDFromUri(uri);
        return sMovies.query(mDatabaseHelper.getReadableDatabase(),
                projection,
                sMovieIDSelection,
                new String[]{Long.toString(id)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getFavouriteByID(Uri uri, String[] projection, String sortOrder)
    {
        long id = MovieContract.FavouriteEntry.getFavouriteIDFromUri(uri);
        return sFavourites.query(mDatabaseHelper.getReadableDatabase(),
                projection,
                sFavouriteIDSelection,
                new String[]{Long.toString(id)},
                null,
                null,
                sortOrder
        );
    }

    @Override
    public boolean onCreate()
    {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri))
        {
            case MOVIE:{
                retCursor = sMovies.query(
                        mDatabaseHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case MOVIE_WITH_ID: {
                retCursor = getMovieByID(uri, projection, sortOrder);
                break;
            }

            case FAVOURITE:{
                retCursor = sFavourites.query(
                        mDatabaseHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case FAVOURITE_WITH_ID: {
                retCursor = getFavouriteByID(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        switch (sUriMatcher.match(uri))
        {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case FAVOURITE:
                return MovieContract.FavouriteEntry.CONTENT_TYPE;
            case FAVOURITE_WITH_ID:
                return MovieContract.FavouriteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri))
        {
            case MOVIE:
            {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVOURITE:
            {
                long _id = db.insert(MovieContract.FavouriteEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.FavouriteEntry.buildFavouriteUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (sUriMatcher.match(uri))
        {
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVOURITE:
                rowsDeleted = db.delete(
                        MovieContract.FavouriteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int rowsUpdated;

        switch (sUriMatcher.match(uri))
        {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case FAVOURITE:
                rowsUpdated = db.update(MovieContract.FavouriteEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_FAVOURITE, FAVOURITE);
        matcher.addURI(authority, MovieContract.PATH_FAVOURITE + "/*", FAVOURITE_WITH_ID);
        return matcher;
    }
}
