package com.example.mhmoudfathy.creativemindstask.retrofit;

import com.example.mhmoudfathy.creativemindstask.model.RepoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by mhmoud fathy on 11/3/2017.
 */

public interface ApiInterface {
    @GET("square/repos")
    Call<List<RepoModel>> getRepoInfo ();
}
