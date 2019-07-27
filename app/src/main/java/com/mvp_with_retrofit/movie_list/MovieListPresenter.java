package com.mvp_with_retrofit.movie_list;

import com.mvp_with_retrofit.model.Movie;

import java.util.List;

public class MovieListPresenter implements MovieListContract.Presenter, MovieListContract.Model.OnFinishedListener {
    MovieListContract.Model mModel;
    MovieListContract.View mView;

    public MovieListPresenter(MovieListContract.View view) {
        this.mView = view;
        mModel = new MovieListModel();
    }

    @Override
    public void requestDataFromServer() {
        if (mView != null) {
            mView.showProgress();
            mModel.getMovieList(this, 1);
        }
    }

    @Override
    public void onDestroy() {
        this.mView = null;
    }

    @Override
    public void getMoreData(int pageNo) {

        if (mView != null) {
            mView.showProgress();
        }
        mModel.getMovieList(this, pageNo);
    }


    @Override
    public void onFinished(List<Movie> movies) {
        mView.setDataToRecyclerView(movies);
        if (mView != null) {
            mView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        mView.onResponseFailure(t);
        if (mView != null) {
            mView.hideProgress();
        }
    }


}
