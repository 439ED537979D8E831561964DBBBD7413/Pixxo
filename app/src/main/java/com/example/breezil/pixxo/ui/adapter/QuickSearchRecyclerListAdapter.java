package com.example.breezil.pixxo.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.callbacks.QuickSearchListener;
import com.example.breezil.pixxo.databinding.ImageItemBinding;
import com.example.breezil.pixxo.databinding.SingleQuckSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class QuickSearchRecyclerListAdapter extends
        RecyclerView.Adapter<QuickSearchRecyclerListAdapter.QuickSearchHolder>{
    SingleQuckSearchBinding binding;
    List<String> searchList;
    QuickSearchListener quickSearchListener;

    public QuickSearchRecyclerListAdapter(List<String> searchList, QuickSearchListener quickSearchListener) {
        this.searchList = searchList;
        this.quickSearchListener = quickSearchListener;
    }

    @NonNull
    @Override
    public QuickSearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = SingleQuckSearchBinding.inflate(layoutInflater,parent,false);

        return new QuickSearchHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuickSearchHolder quickSearchHolder, int position) {
        String string = searchList.get(position);
        quickSearchHolder.bind(string,quickSearchListener);
    }

    public void setList(List<String> search ){
        searchList = search;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(searchList == null){
            return 0;
        }
        return searchList.size();
    }

    public class QuickSearchHolder extends RecyclerView.ViewHolder{

        SingleQuckSearchBinding binding;
        public QuickSearchHolder(SingleQuckSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String string, QuickSearchListener quickSearchListener){
            itemView.setOnClickListener(v -> {
                quickSearchListener.getSTring(string);
            });

            binding.searchText.setText(string);
        }
    }
}
