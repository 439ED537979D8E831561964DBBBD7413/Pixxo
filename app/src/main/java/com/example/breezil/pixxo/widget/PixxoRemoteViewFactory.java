package com.example.breezil.pixxo.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.breezil.pixxo.R;
import com.example.breezil.pixxo.db.AppDatabase;
import com.example.breezil.pixxo.model.ImagesModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PixxoRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory{

    private List<String> imagesList;
    private Context context;
    private AppDatabase appDatabase;

    public PixxoRemoteViewFactory(Context context) {
        this.context = context;
        appDatabase = AppDatabase.getAppDatabase(context);
    }

    @Override
    public void onCreate() {
        imagesList = new ArrayList<>();

    }

    @Override
    public void onDataSetChanged() {

        List<ImagesModel> imagesModels = appDatabase.imagesDao().getImagesss();

        for (ImagesModel imagesModel : imagesModels){
            imagesList.add(String.format(Locale.getDefault(),
                    imagesModel.getTags(), imagesModel.getType(),imagesModel.getUser()));
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return imagesList == null? 0 : imagesList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);

        Intent intent = new Intent();
        remoteViews.setTextViewText(R.id.widget_item_textview, imagesList.get(position));
        remoteViews.setOnClickFillInIntent(R.id.widget_item_textview, intent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
