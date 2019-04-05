package com.pixxo.breezil.pixxo.ui.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.callbacks.ImageClickListener;
import com.pixxo.breezil.pixxo.callbacks.ImageLongClickListener;
import com.pixxo.breezil.pixxo.databinding.ImageItemBinding;
import com.pixxo.breezil.pixxo.databinding.ItemNetworkStateBinding;
import com.pixxo.breezil.pixxo.model.ImagesModel;
import com.pixxo.breezil.pixxo.repository.NetworkState;

public class ImagesRecyclerViewAdapter extends PagedListAdapter<ImagesModel, RecyclerView.ViewHolder> {

    private ImageItemBinding binding;
    private ItemNetworkStateBinding networkStateBinding;
    private ImageLongClickListener imageLongClickListener;
    private ImageClickListener imageClickListener;
    Context context;

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;
    private NetworkState networkState;



    public ImagesRecyclerViewAdapter(Context context, ImageClickListener imageClickListener ,
                                     ImageLongClickListener imageLongClickListener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.imageClickListener = imageClickListener;
        this.imageLongClickListener = imageLongClickListener;
    }

    private static final DiffUtil.ItemCallback<ImagesModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<ImagesModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull ImagesModel oldItem, @NonNull ImagesModel newItem) {
            return oldItem.getId() == newItem.getId() ;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ImagesModel oldItem, @NonNull ImagesModel newItem) {
            return oldItem.getLargeImageURL().equals(newItem.getLargeImageURL()) &&
                    oldItem.getPreviewURL().equals(newItem.getPreviewURL());
        }
    };

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_PROGRESS) {
            networkStateBinding = ItemNetworkStateBinding.inflate(layoutInflater,parent,false);
            return new NetworkStateItemViewHolder(networkStateBinding);
        }else {
            binding = ImageItemBinding.inflate(layoutInflater,parent,false);
            return new ImageHolder(binding);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof ImageHolder){
            ImagesModel imagesModel = getItem(position);
            ((ImageHolder)viewHolder).bind(imagesModel, imageClickListener,imageLongClickListener);
        }else{
            ((NetworkStateItemViewHolder) viewHolder).bindView(networkState);
        }
    }



    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    public ImagesModel getImageAt(int position){
        return getItem(position);
    }

    class ImageHolder extends RecyclerView.ViewHolder{
        ImageItemBinding binding;
        public ImageHolder( ImageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ImagesModel imagesModel,
                         ImageClickListener imageClickListener,
                         ImageLongClickListener imageLongClickListener
                         ){
            itemView.setOnClickListener(v -> {
                imageClickListener.showFullImage(imagesModel);

            });
            itemView.setOnLongClickListener(v -> {
                imageLongClickListener.doSomethingWithImage(imagesModel);
                return true;
            });

            Glide.with(context)
                    .load(imagesModel.getWebformatURL())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder))
                    .into(binding.image);
        }
    }


    public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

        private ItemNetworkStateBinding binding;
        public NetworkStateItemViewHolder(ItemNetworkStateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(NetworkState networkState) {
            if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
                binding.errorMsg.setVisibility(View.VISIBLE);
            } else {
                binding.errorMsg.setVisibility(View.GONE);
            }
        }
    }
}
