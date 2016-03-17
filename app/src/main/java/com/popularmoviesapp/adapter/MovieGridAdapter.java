package com.popularmoviesapp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.popularmoviesapp.R;
import butterknife.Bind;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import butterknife.ButterKnife;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.support.v7.widget.RecyclerView;

/**
 * Created by aliabbasjaffri on 17/03/16.
 */
public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder>
{
    private List<String> movieList = new ArrayList<>();
    private boolean mFavoriteView;
    private Calendar mCalendar;

    public MovieGridAdapter()
    {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.gridMovieItemFrameLayout)
        FrameLayout mGridMovieItemContainer;

        @Bind(R.id.gridMovieItemImageView)
        ImageView mGridMovieItemImageView;

        @Bind(R.id.gridMovieItemLikeImageView)
        ImageView mGridMovieItemLikeImageView;

        @Bind(R.id.gridMovieItemAverageRating)
        TextView mGridMovieItemAverageRating;

        @Bind(R.id.gridMovieItemYear)
        TextView mGridMovieItemDate;

        @Bind(R.id.gridMovieItemTitleContainer)
        LinearLayout mGridMovieItemTitleContainer;

        public ViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
