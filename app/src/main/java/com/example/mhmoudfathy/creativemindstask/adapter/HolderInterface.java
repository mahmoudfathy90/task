package com.example.mhmoudfathy.creativemindstask.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public interface HolderInterface {

    public RecyclerView.ViewHolder getHolder(ViewGroup view);
    public void getViewData(RecyclerView.ViewHolder holder, int postion);
    public  int listsize();
}
