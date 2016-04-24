package com.popularmoviesapp.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewGroup;
import com.popularmoviesapp.R;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;
import android.widget.Toast;

import com.popularmoviesapp.helper.GridViewHolder;
import com.popularmoviesapp.utils.Constants;
import com.popularmoviesapp.utils.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by aliabbasjaffri on 17/03/16.
 */
public class MovieGridAdapter extends CursorAdapter
{
    Context context;
    boolean mainAdapter;

    public MovieGridAdapter(Context context, Cursor c, int flags , boolean mainOrFavorite) {        //Distinguishes between Main Activity and Favorite activity: True for main, false for favorite
        super(context, c, flags);
        this.context = context;
        this.mainAdapter = mainOrFavorite;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_movie_item, parent, false);

        GridViewHolder viewHolder = new GridViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor)
    {
        final GridViewHolder viewHolder = (GridViewHolder) view.getTag();

        if(mainAdapter) {
            String moviePosterPath = cursor.getString(Constants.MOVIE_POSTER_PATH);

            final int colorPrimaryLight = ContextCompat.getColor(context, (R.color.colorPrimaryTransparent));
            String imageUrl = Constants.IMAGE_MOVIE_URL + Constants.IMAGE_SIZE_W185 + moviePosterPath;
            Picasso.with(viewHolder.mGridMovieItemImageView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .into(viewHolder.mGridMovieItemImageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap posterBitmap = ((BitmapDrawable) viewHolder.mGridMovieItemImageView.getDrawable()).getBitmap();
                                    Palette.from(posterBitmap).generate(new Palette.PaletteAsyncListener() {
                                        @Override
                                        public void onGenerated(Palette palette) {
                                            viewHolder.mGridMovieItemTitleContainer.setBackgroundColor(ColorUtils.setAlphaComponent(palette.getMutedColor(colorPrimaryLight), 190)); //Opacity
                                        }
                                    });
                                }

                                @Override
                                public void onError() {
                                    Toast.makeText(context , context.getResources().getString(R.string.errorFetchingFromServer) , Toast.LENGTH_SHORT).show();
                                }
                            }
                    );

            viewHolder.mGridMovieItemLikeImageView.setImageResource(cursor.getString(Constants.MOVIE_LIKED).equals("0") ? R.drawable.ic_favorite_border : R.drawable.ic_favorite);

            String movieAverageRating = cursor.getString(Constants.MOVIE_POPULARITY);
            viewHolder.mGridMovieItemAverageRating.setText(movieAverageRating);

            String movieReleaseYear = cursor.getString(Constants.MOVIE_RELEASE_DATE);
            viewHolder.mGridMovieItemDate.setText(Utility.getYearFromDate(movieReleaseYear));
        }
        else
        {
            viewHolder.mGridMovieItemImageView.setImageBitmap(Utility.getImage(cursor.getBlob(Constants.FAVORITE_MOVIE_POSTER_IMAGE_BLOB)));

            final int colorPrimaryLight = ContextCompat.getColor(context, (R.color.colorPrimaryTransparent));
            Bitmap posterBitmap = ((BitmapDrawable) viewHolder.mGridMovieItemImageView.getDrawable()).getBitmap();
            Palette.from(posterBitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    viewHolder.mGridMovieItemTitleContainer.setBackgroundColor(ColorUtils.setAlphaComponent(palette.getMutedColor(colorPrimaryLight), 190));
                }
            });

            viewHolder.mGridMovieItemLikeImageView.setImageResource(R.drawable.ic_favorite);

            String movieAverageRating = cursor.getString(Constants.FAVORITE_MOVIE_POPULARITY);
            viewHolder.mGridMovieItemAverageRating.setText(movieAverageRating);

            String movieReleaseYear = cursor.getString(Constants.FAVORITE_MOVIE_RELEASE_DATE);
            viewHolder.mGridMovieItemDate.setText(Utility.getYearFromDate(movieReleaseYear));
        }
    }
}
