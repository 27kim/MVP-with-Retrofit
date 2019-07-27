package com.mvp_with_retrofit.movie_details;

import com.mvp_with_retrofit.model.Movie;
import com.mvp_with_retrofit.network.MovieApiInterface;
import com.orhanobut.logger.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mvp_with_retrofit.network.MovieApiClient.API_KEY;
import static com.mvp_with_retrofit.network.MovieApiClient.getClient;
import static com.mvp_with_retrofit.utils.Constants.CREDITS;

public class MovieDetailsModel implements MovieDetailsContract.Model{

    @Override
    public void getMovieDetails(final OnFinishedListener onFinishedListener, int movieId) {
        MovieApiInterface retrofitApi = getClient().create(MovieApiInterface.class);
        Call<Movie> call =  retrofitApi.getMovieDetails(movieId, API_KEY, CREDITS);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();
                Logger.d(movie.toString());
                onFinishedListener.onFinished(movie);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });


    }
}
