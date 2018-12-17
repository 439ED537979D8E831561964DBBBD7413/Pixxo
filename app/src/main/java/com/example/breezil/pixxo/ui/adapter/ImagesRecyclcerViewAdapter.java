package com.example.breezil.pixxo.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.callbacks.ImageClickListener;
import com.example.breezil.pixxo.callbacks.ImageLongClickListener;
import com.example.breezil.pixxo.databinding.ImageItemBinding;
import com.example.breezil.pixxo.model.ImagesModel;

public class ImagesRecyclcerViewAdapter extends ListAdapter<ImagesModel, ImagesRecyclcerViewAdapter.ImageHolder> {

    ImageItemBinding binding;
    private ImageLongClickListener imageLongClickListener;
    private ImageClickListener imageClickListener;
    Context context;



    public ImagesRecyclcerViewAdapter(Context context, ImageClickListener imageClickListener ,
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
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = ImageItemBinding.inflate(layoutInflater,parent,false);

        return new ImageHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder imageHolder, int position) {
        ImagesModel imagesModel = getItem(position);
        imageHolder.bind(imagesModel, imageClickListener,imageLongClickListener);
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
}
