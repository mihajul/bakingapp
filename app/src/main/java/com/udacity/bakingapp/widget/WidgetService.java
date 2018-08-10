package com.udacity.bakingapp.widget;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.udacity.bakingapp.BakingAppWidget;

public class WidgetService extends RemoteViewsService {
    public static final String LOG_TAG = WidgetService.class.getName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d(LOG_TAG, "onGetViewFactory" + appWidgetId + " extras: " + intent.getExtras().size());
        return (new ListProvider(this.getApplicationContext(), intent));
    }

}