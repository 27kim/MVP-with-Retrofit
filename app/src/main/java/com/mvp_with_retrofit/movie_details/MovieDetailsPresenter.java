package com.mvp_with_retrofit.movie_details;

import com.mvp_with_retrofit.model.Movie;

public class MovieDetailsPresenter implements MovieDetailsContract.Presenter, MovieDetailsContract.Model.OnFinishedListener {
    MovieDetailsContract.View mView;
    MovieDetailsContract.Model mModel;
    public MovieDetailsPresenter(MovieDetailsContract.View mView) {
        this.mView = mView;
        mModel = new MovieDetailsModel();
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void requestMovieData(int movieId) {
        mModel.getMovieDetails(this, movieId);
    }

    @Override
    public void onFinished(Movie movie) {
        if(mView!=null){
            mView.hideProgress();
        }

        mView.setDataToViews(movie);

    }

    @Override
    public void onFailure(Throwable t) {
        if(mView!=null){
            mView.hideProgress();
        }

        mView.onResponseFailure(t);
    }
}
