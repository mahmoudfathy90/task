package com.example.mhmoudfathy.creativemindstask;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mhmoudfathy.creativemindstask.adapter.GenericAdapter;
import com.example.mhmoudfathy.creativemindstask.adapter.GenericHolder;
import com.example.mhmoudfathy.creativemindstask.adapter.HolderInterface;
import com.example.mhmoudfathy.creativemindstask.databinding.ActivityMainBinding;
import com.example.mhmoudfathy.creativemindstask.databinding.RepoItemBinding;
import com.example.mhmoudfathy.creativemindstask.dialog.MyDialog;
import com.example.mhmoudfathy.creativemindstask.model.RepoModel;
import com.example.mhmoudfathy.creativemindstask.retrofit.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
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
        setRefreshLayout();
        setPagenation();
    }



    public void buildRetrofit() {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(getCacheDir(), cacheSize);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void setRefreshLayout(){
        binding.swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this,android.R.color.holo_red_dark),
                ContextCompat.getColor(this,android.R.color.darker_gray),
                ContextCompat.getColor(this,android.R.color.holo_green_light));

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();

            }
        });
    }


    public void getData(){
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
        Call<List<RepoModel>>  call= apiInterface.getRepoInfo();
       // loading();
        binding.swipeRefreshLayout.setRefreshing(true);
        call.enqueue(new Callback<List<RepoModel>>() {
            @Override
            public void onResponse(Call<List<RepoModel>> call, Response<List<RepoModel>> response) {
                if (response.code()==200){
                    repoModels=response.body();
                    binding.container.setAdapter(genericAdapter);
                    hideLoading();
                    binding.swipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<List<RepoModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "PleaseCheck Your network", Toast.LENGTH_SHORT).show();
                hideLoading();
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setPagenation(){
        binding.container.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int x=10;
                if (dy > x ) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.swipeRefreshLayout.setRefreshing(true);
                        }
                    },3000);


                }
                x=x+10;
                binding.swipeRefreshLayout.setRefreshing(false);
                super.onScrolled(recyclerView, dx, dy);
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
        repoItemBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MyDialog dialog=new MyDialog(MainActivity.this);
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public int listsize() {
        return repoModels.size();
    }


    boolean isloading = false;
    ProgressBar image;

    public void loading() {
        if (!isloading) {
            isloading = true;
            image = new ProgressBar(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            image.setLayoutParams(params);
            LinearLayout loadingContainer = new LinearLayout(this);
            loadingContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            loadingContainer.setId(R.id.loader);
            loadingContainer.setBackgroundColor(Color.parseColor("#80000000"));
            loadingContainer.setOrientation(LinearLayout.HORIZONTAL);
            loadingContainer.addView(image);
            loadingContainer.setGravity(Gravity.CENTER);
            ((ViewGroup) getWindow().getDecorView()).addView(loadingContainer);
        }

    }

    public void hideLoading(){
        if(isloading) {
            isloading = false;
            ((ViewGroup) this.getWindow().getDecorView().getRootView()).removeView(findViewById(R.id.loader));
        }
    }

}
