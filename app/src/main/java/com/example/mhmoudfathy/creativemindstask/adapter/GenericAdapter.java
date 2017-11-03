package com.example.mhmoudfathy.creativemindstask.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;




public class GenericAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    HolderInterface holderInterface;
    Context context;

    public GenericAdapter(HolderInterface holderInterface){
        this.context=context;
        this.holderInterface=holderInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return holderInterface.getHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       holderInterface.getViewData(holder,position);
    }

    @Override
    public int getItemCount() {
        return holderInterface.listsize();
    }
}
