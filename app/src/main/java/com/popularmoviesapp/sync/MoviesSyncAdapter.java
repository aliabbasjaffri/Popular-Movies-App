package com.popularmoviesapp.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.popularmoviesapp.BuildConfig;
import com.popularmoviesapp.R;
import com.popularmoviesapp.activity.MainActivity;
import com.popularmoviesapp.provider.MovieContract;
import com.popularmoviesapp.utils.Constants;
import com.popularmoviesapp.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class MoviesSyncAdapter extends AbstractThreadedSyncAdapter
{
    public final String LOG_TAG = MoviesSyncAdapter.class.getSimpleName();
    
    public MoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        Log.d(LOG_TAG, "Starting sync");
        String category = Utility.getPreferredCategory(getContext());

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;

        try {
            final String MOVIE_BASE_URL =
                    Constants.MOVIE_URL;
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(category)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, url.toString());

            // Create the request to MovieApi, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null)
                return;

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null)
                buffer.append(line).append("\n");


            if (buffer.length() == 0)
                return;

            movieJsonStr = buffer.toString();
            getMovieDataFromJson(movieJsonStr , category);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private void getMovieDataFromJson(String movieJsonStr , String category) throws JSONException
    {
        final String OMA_ID = "id";
        final String OMA_TITLE = "title";
        final String OMA_OVERVIEW = "overview";
        final String OMA_POPULARITY = "popularity";
        final String OMA_VOTE = "vote_average";
        final String OMA_RELEASE_DATE = "release_date";
        final String OMA_POSTER_PATH = "poster_path";
        final String OMA_BACKDROP_PATH = "backdrop_path";

        final String OMA_RESULTS = "results";

        try {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OMA_RESULTS);

            Vector<ContentValues> cVVector = new Vector<>(movieArray.length());

            for(int i = 0; i < movieArray.length(); i++)
            {
                String id;
                String title;
                String overview;
                long popularity;
                long vote;
                String releaseDate;
                String posterPath;
                String backdropPath;

                // Get the JSON object representing the day
                JSONObject movieObject = movieArray.getJSONObject(i);

                id = movieObject.getString(OMA_ID);
                title = movieObject.getString(OMA_TITLE);
                overview = movieObject.getString(OMA_OVERVIEW);
                popularity = movieObject.getLong(OMA_POPULARITY);
                vote = movieObject.getLong(OMA_VOTE);
                releaseDate = movieObject.getString(OMA_RELEASE_DATE);
                posterPath = movieObject.getString(OMA_POSTER_PATH);
                backdropPath = movieObject.getString(OMA_BACKDROP_PATH);

                ContentValues movieValues = new ContentValues();

                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_API_ID, id);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, overview);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY, popularity);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, vote);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, releaseDate);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_CATEGORY, category);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, posterPath);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, backdropPath);

                cVVector.add(movieValues);
            }

            insertUniqueMovies(cVVector , category);

            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void insertUniqueMovies(Vector<ContentValues> cVVector , String category)
    {
        Vector<ContentValues> copyVector = new Vector<>(cVVector);
        Cursor moviesInMoviesTable = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI , Constants.MOVIE_COLUMNS , null , null , null);

        if( moviesInMoviesTable != null)
        {
            for (ContentValues value : cVVector)
                if( movieExistsInTable(moviesInMoviesTable,value.getAsString(MovieContract.MovieEntry.COLUMN_MOVIE_API_ID)))
                    copyVector.remove(value);
        }

        if ( copyVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[copyVector.size()];
            copyVector.toArray(cvArray);
            int inserted = getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);

            moviesInsertedNotification(inserted, category);
        }
    }

    private boolean movieExistsInTable(Cursor cursor , String ID)
    {
        cursor.moveToFirst();

        while( cursor.moveToNext() )
            if(cursor.getString(Constants.MOVIE_API_ID).equals(ID))
                return true;

        return false;
    }

    private void moviesInsertedNotification(int insertedItems, String category)
    {
        Context context = getContext();
        //checking the last update and notify if it' the first of the day
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);
        boolean displayNotifications = prefs.getBoolean(displayNotificationsKey,
                Boolean.parseBoolean(context.getString(R.string.pref_enable_notifications_default)));

        /*            int iconId = Utility.getIconResourceForWeatherCondition(weatherId);
                    Resources resources = context.getResources();
                    Bitmap largeIcon = BitmapFactory.decodeResource(resources,
                            Utility.getArtResourceForWeatherCondition(weatherId));
        */
        if(displayNotifications)
        {
            String title = context.getString(R.string.app_name);

            String contentText = context.getString(R.string.notification_content, insertedItems, category);

            // NotificationCompatBuilder is a very convenient way to build backward-compatible
            // notifications.  Just throw in some data.
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getContext())
                                    .setColor(context.getResources().getColor(R.color.colorPrimaryTransparent))
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    //.setLargeIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(contentText);

            // Make something interesting happen when the user clicks on the notification.
            // In this case, opening the app is sufficient.
            Intent resultIntent = new Intent(context, MainActivity.class);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
            mNotificationManager.notify(Constants.WEATHER_NOTIFICATION_ID, mBuilder.build());
        }
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.authourity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.authourity), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MoviesSyncAdapter.configurePeriodicSync(context, Constants.SYNC_INTERVAL, Constants.SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.authourity), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}