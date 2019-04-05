package com.pixxo.breezil.pixxo.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.pixxo.breezil.pixxo.R;
import com.pixxo.breezil.pixxo.ui.main.MainActivity;

public class PixxoAppWidget extends AppWidgetProvider {
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int[] appWidgetIds) {

        for(int appWidgetId : appWidgetIds){
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), 0);
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pixxo_app_widget);
            views.setTextViewText(R.id.appwidget_text, WidgetPref.getTitle(context));

            Intent listviewIntent = new Intent(context, WidgetService.class);
            views.setRemoteAdapter(R.id.widget_images_list,
                    listviewIntent);


            views.setPendingIntentTemplate(R.id.widget_images_list,pendingIntent);
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);


            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        updateAppWidget(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
