package com.popularmoviesapp.provider;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aliabbasjaffri on 01/03/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "popularMovies.db";
    private static final int DATABASE_VERSION = 5;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT UNIQUE NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_CATEGORY + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_LIKED + " TEXT DEFAULT 0" +
                        " ); ";

        final String SQL_CREATE_FAVOURITES_TABLE =
                "CREATE TABLE " + MovieContract.FavouriteEntry.TABLE_NAME + " (" +
                        MovieContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_TITLE + " TEXT UNIQUE NOT NULL, " +
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_POPULARITY + " TEXT NOT NULL, " +
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_VOTE_COUNT + " TEXT NOT NULL, " +
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_CATEGORY + " TEXT NOT NULL, " +
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL " +
                        " ); ";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavouriteEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
