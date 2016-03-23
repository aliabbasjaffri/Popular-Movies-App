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
import android.widget.LinearLayout;

import com.popularmoviesapp.fragment.MainActivityFragment;
import com.popularmoviesapp.helper.GridViewHolder;
import com.popularmoviesapp.utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by aliabbasjaffri on 17/03/16.
 */
public class MovieGridAdapter extends CursorAdapter
{
    Context context;

    public MovieGridAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
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
    public void bindView(View view, Context context, Cursor cursor)
    {
        final GridViewHolder viewHolder = (GridViewHolder) view.getTag();
        String moviePosterPath = cursor.getString(MainActivityFragment.MOVIE_POSTER_PATH);

        final int colorPrimaryLight = ContextCompat.getColor(context, (R.color.colorPrimaryTransparent));
        String imageUrl = Constants.IMAGE_MOVIE_URL + Constants.IMAGE_SIZE_W185 + moviePosterPath;
        Picasso.with(viewHolder.mGridMovieItemImageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_movie)
                .into(viewHolder.mGridMovieItemImageView, new Callback()
                {
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
                    }
                }
        );


        String movieLikedIndicator = cursor.getString(MainActivityFragment.MOVIE_LIKED);
        setIconForLiked(viewHolder, movieLikedIndicator);

        String movieAverageRating = cursor.getString(MainActivityFragment.MOVIE_VOTE_COUNT);
        viewHolder.mGridMovieItemAverageRating.setText(movieAverageRating);

        String movieReleaseYear = cursor.getString(MainActivityFragment.MOVIE_VOTE_COUNT );
        viewHolder.mGridMovieItemDate.setText(movieReleaseYear);
    }

    private void  setIconForLiked(GridViewHolder holder, String value) {
        boolean addedInFavorite = Boolean.parseBoolean(value);
        holder.mGridMovieItemLikeImageView.setImageResource(addedInFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
    }
}
