package com.popularmoviesapp.provider;

import android.content.Context;
import com.popularmoviesapp.model.Movie;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by aliabbasjaffri on 01/03/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "popularMovies.db";
    private static final int DATABASE_VERSION = 1;

    static
    {
        cupboard().register(Movie.class);
    }

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        cupboard().withDatabase(db).createTables();
        db.execSQL("CREATE INDEX IDIndex ON " + Movie.class.getName() + "(id)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        cupboard().withDatabase(db).upgradeTables();
    }
}
