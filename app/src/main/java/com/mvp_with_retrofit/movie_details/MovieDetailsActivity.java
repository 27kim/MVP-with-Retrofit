package com.mvp_with_retrofit.movie_details;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.mvp_with_retrofit.R;
import com.mvp_with_retrofit.adapter.CastAdapter;
import com.mvp_with_retrofit.model.Cast;
import com.mvp_with_retrofit.model.Movie;
import com.mvp_with_retrofit.network.MovieApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mvp_with_retrofit.utils.Constants.KEY_MOVIE_ID;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsContract.View {


    private ImageView mImageView;
    private TextView tvMovieTitle;
    private TextView tvMovieReleaseDate;
    private TextView tvMovieRatings;
    private TextView tvOverview;
    private CastAdapter castAdapter;
    private List<Cast> castList;
    private ProgressBar pbLoadCast;
    private ProgressBar pbLoadBackdrop;
    private TextView tvHomepageValue;
    private TextView tvTaglineValue;
    private TextView tvRuntimeValue;

    private String movieName;

    private MovieDetailsPresenter movieDetailsPresenter;

    public static Intent newIntent(Context context, int movieId) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(KEY_MOVIE_ID, movieId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

//        <androidx.appcompat.widget.Toolbar
//        android:id="@+id/toolbar"
//        android:layout_width="match_parent"
//        android:layout_height="?attr/actionBarSize"
//        app:layout_collapseMode="pin"
//        app:popupTheme="@style/ThemeOverlay.AppCompat.Light" />

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        initCollapsingToolbar();

        initUI();

        Intent mIntent = getIntent();
        int movieId = mIntent.getIntExtra(KEY_MOVIE_ID, 0);

        movieDetailsPresenter = new MovieDetailsPresenter(this);
        movieDetailsPresenter.requestMovieData(movieId);
    }

    /**
     * Initializing UI components
     */
    private void initUI() {

        mImageView = findViewById(R.id.iv_backdrop);
        tvMovieTitle = findViewById(R.id.tv_movie_title);
        tvMovieReleaseDate = findViewById(R.id.tv_release_date);
        tvMovieRatings = findViewById(R.id.tv_movie_ratings);
        tvOverview = findViewById(R.id.tv_movie_overview);

        castList = new ArrayList<>();
        RecyclerView mRecyclerView = findViewById(R.id.rv_cast);
        castAdapter = new CastAdapter(this, castList);
        mRecyclerView.setAdapter(castAdapter);
        pbLoadCast = findViewById(R.id.pb_cast_loading);
        pbLoadBackdrop = findViewById(R.id.pb_load_backdrop);
        tvHomepageValue = findViewById(R.id.tv_homepage_value);
        tvTaglineValue = findViewById(R.id.tv_tagline_value);
        tvRuntimeValue = findViewById(R.id.tv_runtime_value);
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");

        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(movieName);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void showProgress() {
        pbLoadBackdrop.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbLoadCast.setVisibility(View.GONE);
    }

    @Override
    public void setDataToViews(Movie movie) {
        if (movie != null) {

            movieName = movie.getTitle();
            tvMovieTitle.setText(movie.getTitle());
            tvMovieReleaseDate.setText(movie.getReleaseDate());
            tvMovieRatings.setText(String.valueOf(movie.getRating()));
            tvOverview.setText(movie.getOverview());

            // loading album cover using Glide library
            Glide.with(this)
                    .load(MovieApiClient.BACKDROP_BASE_URL + movie.getBackdropPath())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            pbLoadBackdrop.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pbLoadBackdrop.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                    .into(mImageView);

            castList.clear();
            castList.addAll(movie.getCredits().getCast());
            castAdapter.notifyDataSetChanged();

            tvTaglineValue.setText(movie.getTagline() != null ? movie.getTagline() : "N/A");
            tvHomepageValue.setText(movie.getHomepage() != null ? movie.getHomepage() : "N/A");
            tvRuntimeValue.setText(movie.getRunTime() != null ? movie.getRunTime() : "N/A");
        }
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Snackbar.make(findViewById(R.id.main_content), getString(R.string.error_data), Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        movieDetailsPresenter.onDestroy();
    }
}
