package com.popularmoviesapp.helper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.popularmoviesapp.R;

/**
 * Created by aliabbasjaffri on 23/03/16.
 */
public class GridViewHolder
{
    public FrameLayout mGridMovieItemContainer;
    public LinearLayout mGridMovieItemTitleContainer;
    public ImageView mGridMovieItemImageView;
    public ImageView mGridMovieItemLikeImageView;
    public TextView mGridMovieItemAverageRating;
    public TextView mGridMovieItemDate;

    public GridViewHolder(View view)
    {
        mGridMovieItemContainer = (FrameLayout) view.findViewById(R.id.gridMovieItemFrameLayout);
        mGridMovieItemTitleContainer = (LinearLayout) view.findViewById(R.id.gridMovieItemTitleContainer);
        mGridMovieItemImageView = (ImageView) view.findViewById(R.id.gridMovieItemImageView);
        mGridMovieItemLikeImageView = (ImageView) view.findViewById(R.id.gridMovieItemLikeImageView);
        mGridMovieItemAverageRating = (TextView) view.findViewById(R.id.gridMovieItemAverageRating);
        mGridMovieItemDate = (TextView) view.findViewById(R.id.gridMovieItemYear);
    }
}
