package com.mvp_with_retrofit.movie_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.mvp_with_retrofit.R;
import com.mvp_with_retrofit.adapter.MoviesAdapter;
import com.mvp_with_retrofit.model.Movie;
import com.mvp_with_retrofit.movie_details.MovieDetailsActivity;
import com.mvp_with_retrofit.utils.GridSpacingItemDecoration;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.nio.channels.ScatteringByteChannel;
import java.util.ArrayList;
import java.util.List;

import static com.mvp_with_retrofit.utils.Constants.ACTION_MOVIE_FILTER;
import static com.mvp_with_retrofit.utils.Constants.KEY_RELEASE_FROM;
import static com.mvp_with_retrofit.utils.Constants.KEY_RELEASE_TO;
import static com.mvp_with_retrofit.utils.GridSpacingItemDecoration.dpToPx;
import static com.mvp_with_retrofit.utils.Logger.LOGD;

public class MovieListActivity extends AppCompatActivity implements MovieListContract.View {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private MoviesAdapter mMoviesAdapter;
    private ProgressBar mProgressBar;
    List<Movie> mMovieList;
    MovieListPresenter mListPresenter;

    // Constants for filter functionality
    private String fromReleaseFilter = "";
    private String toReleaseFilter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(getString(R.string.most_popular_movies));
        //Log test
        Logger.addLogAdapter(new AndroidLogAdapter());
        LOGD("Log Test");
        Logger.d("Log Test");

        initUI();

        mListPresenter = new MovieListPresenter(this);
        mListPresenter.requestDataFromServer();
    }

    private void initUI() {
        mRecyclerView = findViewById(R.id.rv_movie_list);
        mProgressBar = findViewById(R.id.pb_loading);
        mLayoutManager = new GridLayoutManager(this, 2);
        mMovieList = new ArrayList<>();
        mMoviesAdapter = new MoviesAdapter(this, mMovieList);

        mRecyclerView.setAdapter(mMoviesAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(this, 10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }


    public void setDataToRecyclerView(List<Movie> movies) {
        Logger.d(movies);
        mMovieList.addAll(movies);
        mMoviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        LOGD("onResponseFailure" + throwable.getMessage());
        Toast.makeText(this, getString(R.string.communication_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMovieItemClick(int position) {

        if (position == -1) {
            return;
        }

        Intent detailIntent = MovieDetailsActivity.newIntent(getApplicationContext(), mMovieList.get(position).getId());
        startActivity(detailIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ACTION_MOVIE_FILTER) {
            if (resultCode == Activity.RESULT_OK) {
                // Checking if there is any data to filter
                fromReleaseFilter = data.getStringExtra(KEY_RELEASE_FROM);
                toReleaseFilter = data.getStringExtra(KEY_RELEASE_TO);

                mMoviesAdapter.setFilterParameter(fromReleaseFilter, toReleaseFilter);
                mMoviesAdapter.getFilter().filter("");
            }
        }
    }
}
