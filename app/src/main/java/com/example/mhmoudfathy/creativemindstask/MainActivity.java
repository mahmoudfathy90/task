package com.example.mhmoudfathy.creativemindstask;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mhmoudfathy.creativemindstask.adapter.GenericAdapter;
import com.example.mhmoudfathy.creativemindstask.adapter.GenericHolder;
import com.example.mhmoudfathy.creativemindstask.adapter.HolderInterface;
import com.example.mhmoudfathy.creativemindstask.databinding.ActivityMainBinding;
import com.example.mhmoudfathy.creativemindstask.databinding.RepoItemBinding;
import com.example.mhmoudfathy.creativemindstask.model.RepoModel;
import com.example.mhmoudfathy.creativemindstask.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity  implements HolderInterface{

    public static final String BASE_URL = "https://api.github.com/users/";
    Retrofit retrofit;
    List<RepoModel> repoModels;
    ActivityMainBinding binding;
    GenericAdapter genericAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.container.setLayoutManager(new LinearLayoutManager(this));
        genericAdapter= new GenericAdapter(this);
        buildRetrofit();
        getData();
    }

    public void buildRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getData(){
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<List<RepoModel>>  call= apiInterface.getRepoInfo();
        call.enqueue(new Callback<List<RepoModel>>() {
            @Override
            public void onResponse(Call<List<RepoModel>> call, Response<List<RepoModel>> response) {
                if (response.code()==200){
                    repoModels=response.body();
                    binding.container.setAdapter(genericAdapter);

                }

            }

            @Override
            public void onFailure(Call<List<RepoModel>> call, Throwable t) {

            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHolder(ViewGroup view) {
        View view1= LayoutInflater.from(this).inflate(R.layout.repo_item,view,false);
        return new GenericHolder(view1);
    }

    @Override
    public void getViewData(RecyclerView.ViewHolder holder, int postion) {
        RepoItemBinding repoItemBinding= DataBindingUtil.bind(holder.itemView);
        RepoModel repoModel=repoModels.get(postion);
        RepoModel.Owner owner=repoModels.get(postion).getOwner();
        repoItemBinding.setRepoModel(repoModel);
        repoItemBinding.setOwner(owner);
    }

    @Override
    public int listsize() {
        return repoModels.size();
    }
}
