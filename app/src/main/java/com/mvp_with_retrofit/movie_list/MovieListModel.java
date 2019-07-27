package com.mvp_with_retrofit.movie_list;

import com.mvp_with_retrofit.model.MovieListResponse;
import com.mvp_with_retrofit.network.MovieApiClient;
import com.mvp_with_retrofit.network.MovieApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.mvp_with_retrofit.network.MovieApiClient.getClient;

public class MovieListModel implements MovieListContract.Model{

    @Override
    public void getMovieList(final OnFinishedListener callback, int pageNo) {
        MovieApiInterface retrofitApi =  getClient().create(MovieApiInterface.class);
        Call<MovieListResponse> call = retrofitApi.getPopularMovies(MovieApiClient.API_KEY, pageNo);

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                callback.onFinished(response.body().getResults());
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
