package com.mvp_with_retrofit.movie_list;

import com.mvp_with_retrofit.model.Movie;

import java.util.List;

public interface MovieListContract {
    interface View {
        void setDataToRecyclerView(List<Movie> movieArrayList);
        void onResponseFailure(Throwable throwable);
        void showProgress();
        void hideProgress();
        void onMovieItemClick(int position);
    }

    interface Presenter {
        void requestDataFromServer();
        void onDestroy();
        void getMoreData(int pageNo);
    }

    interface Model{
        interface OnFinishedListener {
            void onFinished(List<Movie> movieArrayList);

            void onFailure(Throwable t);
        }
        void getMovieList(OnFinishedListener callback, int pageNo);
    }
}
