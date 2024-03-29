package com.pixxo.breezil.pixxo.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.callbacks.SavedImageClickListener;
import com.pixxo.breezil.pixxo.callbacks.SavedImageLongClickListener;
import com.pixxo.breezil.pixxo.databinding.GridImageItemBinding;
import com.pixxo.breezil.pixxo.databinding.ImageItemBinding;
import com.pixxo.breezil.pixxo.model.SavedImageModel;

public class SavedImageRecyclerAdapter extends ListAdapter<SavedImageModel, SavedImageRecyclerAdapter.SavedImageHolder> {
    GridImageItemBinding binding;
    private SavedImageLongClickListener imageLongClickListener;
    private SavedImageClickListener imageClickListener;
    Context context;
    CircularProgressDrawable circularProgressDrawable;

    public SavedImageRecyclerAdapter(Context context, SavedImageLongClickListener imageLongClickListener,
                                     SavedImageClickListener imageClickListener){
        super(DIFF_CALLBACK);
        this.context = context;
        this.imageClickListener = imageClickListener;
        this.imageLongClickListener = imageLongClickListener;
    }

    private static final DiffUtil.ItemCallback<SavedImageModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<SavedImageModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull SavedImageModel oldItem, @NonNull SavedImageModel newItem) {
            return oldItem.getSaved_id() == newItem.getSaved_id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull SavedImageModel oldItem, @NonNull SavedImageModel newItem) {
            return oldItem.getLargeImageURL().equals(newItem.getLargeImageURL()) &&
                    oldItem.getPreviewURL().equals(newItem.getPreviewURL());
        }
    };

    @NonNull
    @Override
    public SavedImageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        binding = GridImageItemBinding.inflate(layoutInflater,viewGroup,false);
        return new SavedImageHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull SavedImageHolder savedImageHolder, int i) {
        SavedImageModel savedImageModel = getItem(i);
        savedImageHolder.bind(savedImageModel,imageClickListener,imageLongClickListener);
    }


    public SavedImageModel getSavedImageAt(int position){
        return  getItem(position);
    }



    class SavedImageHolder extends RecyclerView.ViewHolder{
        GridImageItemBinding binding;
        public SavedImageHolder( GridImageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SavedImageModel imagesModel,
                         SavedImageClickListener imageClickListener,
                         SavedImageLongClickListener imageLongClickListener
        ){
            itemView.setOnClickListener(v -> imageClickListener.showFullImage(imagesModel));
            itemView.setOnLongClickListener(v -> {
                imageLongClickListener.doSomethingWithImage(imagesModel);
                return true;
            });
            circularProgressDrawable = new CircularProgressDrawable(context);
            circularProgressDrawable.setStrokeWidth(10f);
            circularProgressDrawable.setCenterRadius(40f);
            circularProgressDrawable.setColorSchemeColors(R.color.colorAccent,R.color.colorPrimary,
                    R.color.colorblue,R.color.hotPink);
            circularProgressDrawable.start();
            Glide.with(context)
                    .load(imagesModel.getWebformatURL())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder))
                    .into(binding.image);
        }
    }
}
