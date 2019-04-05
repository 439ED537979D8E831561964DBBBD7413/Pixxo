package com.pixxo.photoeditor.filters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixxo.photoeditor.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ja.burhanrashid52.photoeditor.PhotoFilter;

import static com.pixxo.photoeditor.Constant.AUT0_FIX;
import static com.pixxo.photoeditor.Constant.BLACK_WHITE;
import static com.pixxo.photoeditor.Constant.BRIGHTNESS;
import static com.pixxo.photoeditor.Constant.CONTRAST;
import static com.pixxo.photoeditor.Constant.CROSS_PROCESS;
import static com.pixxo.photoeditor.Constant.DOCUMENTARY;
import static com.pixxo.photoeditor.Constant.DUAL_TONE;
import static com.pixxo.photoeditor.Constant.FILL_LIGHT;
import static com.pixxo.photoeditor.Constant.FISH_EYE;
import static com.pixxo.photoeditor.Constant.FLIP_HORIZONTAL;
import static com.pixxo.photoeditor.Constant.FLIP_VERTICAL;
import static com.pixxo.photoeditor.Constant.GRAIN;
import static com.pixxo.photoeditor.Constant.GRAY_SCALE;
import static com.pixxo.photoeditor.Constant.LOMISH;
import static com.pixxo.photoeditor.Constant.NEGATIVE;
import static com.pixxo.photoeditor.Constant.ORIGINAL;
import static com.pixxo.photoeditor.Constant.POSTERIZE;
import static com.pixxo.photoeditor.Constant.ROTATE;
import static com.pixxo.photoeditor.Constant.SATURATE;
import static com.pixxo.photoeditor.Constant.SEPIA;
import static com.pixxo.photoeditor.Constant.SHARPEN;
import static com.pixxo.photoeditor.Constant.TEMPERATURE;
import static com.pixxo.photoeditor.Constant.TINT;
import static com.pixxo.photoeditor.Constant.VIGNETTE;

/**
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.2
 * @since 5/23/2018
 */


public class FilterViewAdapter extends RecyclerView.Adapter<FilterViewAdapter.ViewHolder> {

    private FilterListener mFilterListener;
    private List<Pair<String, PhotoFilter>> mPairList = new ArrayList<>();


    public FilterViewAdapter( FilterListener filterListener) {
        mFilterListener = filterListener;
        setupFilters();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<String, PhotoFilter> filterPair = mPairList.get(position);
        Bitmap fromAsset = getBitmapFromAsset(holder.itemView.getContext(), filterPair.first);
        holder.mImageFilterView.setImageBitmap(fromAsset);
        holder.mTxtFilterName.setText(filterPair.second.name().replace("_", " "));
    }

    @Override
    public int getItemCount() {
        return mPairList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageFilterView;
        TextView mTxtFilterName;

        ViewHolder(View itemView) {
            super(itemView);
            mImageFilterView = itemView.findViewById(R.id.imgFilterView);
            mTxtFilterName = itemView.findViewById(R.id.txtFilterName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFilterListener.onFilterSelected(mPairList.get(getLayoutPosition()).second);
                }
            });
        }
    }

    private Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
            return BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    private void setupFilters() {
        mPairList.add(new Pair<>(ORIGINAL, PhotoFilter.NONE));
        mPairList.add(new Pair<>(AUT0_FIX, PhotoFilter.AUTO_FIX));
        mPairList.add(new Pair<>(BRIGHTNESS, PhotoFilter.BRIGHTNESS));
        mPairList.add(new Pair<>(CONTRAST, PhotoFilter.CONTRAST));
        mPairList.add(new Pair<>(DOCUMENTARY, PhotoFilter.DOCUMENTARY));
        mPairList.add(new Pair<>(DUAL_TONE, PhotoFilter.DUE_TONE));
        mPairList.add(new Pair<>(FILL_LIGHT, PhotoFilter.FILL_LIGHT));
        mPairList.add(new Pair<>(FISH_EYE, PhotoFilter.FISH_EYE));
        mPairList.add(new Pair<>(GRAIN, PhotoFilter.GRAIN));
        mPairList.add(new Pair<>(GRAY_SCALE, PhotoFilter.GRAY_SCALE));
        mPairList.add(new Pair<>(LOMISH, PhotoFilter.LOMISH));
        mPairList.add(new Pair<>(NEGATIVE, PhotoFilter.NEGATIVE));
        mPairList.add(new Pair<>(POSTERIZE, PhotoFilter.POSTERIZE));
        mPairList.add(new Pair<>(SATURATE, PhotoFilter.SATURATE));
        mPairList.add(new Pair<>(SEPIA, PhotoFilter.SEPIA));
        mPairList.add(new Pair<>(SHARPEN, PhotoFilter.SHARPEN));
        mPairList.add(new Pair<>(TEMPERATURE, PhotoFilter.TEMPERATURE));
        mPairList.add(new Pair<>(TINT, PhotoFilter.TINT));
        mPairList.add(new Pair<>(VIGNETTE, PhotoFilter.VIGNETTE));
        mPairList.add(new Pair<>(CROSS_PROCESS, PhotoFilter.CROSS_PROCESS));
        mPairList.add(new Pair<>(BLACK_WHITE, PhotoFilter.BLACK_WHITE));
        mPairList.add(new Pair<>(FLIP_HORIZONTAL, PhotoFilter.FLIP_HORIZONTAL));
        mPairList.add(new Pair<>(FLIP_VERTICAL, PhotoFilter.FLIP_VERTICAL));
        mPairList.add(new Pair<>(ROTATE, PhotoFilter.ROTATE));
    }
}
