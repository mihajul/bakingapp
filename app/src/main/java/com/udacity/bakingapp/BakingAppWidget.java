package com.udacity.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.widget.WidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    public static final String RECIPE_SELECTED = "com.udacity.bakingapp.action.APPWIDGET_RECIPE_SELECTED";
    public static final String RECIPE_KEY = "recipe";
    public static final String WIDGET_IDS_KEY = "widgetIds";
    public static final String LOG_TAG = BakingAppWidget.class.getName();
    public static Recipe currentRecipe;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
       Log.d(LOG_TAG, "onUpdate " + appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateAppWidget(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateAppWidget(Context context, int appWidgetId) {


        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),  R.layout.baking_app_widget);
        Intent svcIntent = new Intent(context, WidgetService.class);
        svcIntent.setExtrasClassLoader(Recipe.class.getClassLoader());
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        remoteViews.setRemoteAdapter(appWidgetId, R.id.appwidget_recipelist, svcIntent);
        remoteViews.setEmptyView(R.id.appwidget_recipelist, R.id.appwidget_empty);

        Intent intent = new Intent(context, RecipeListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_empty, pendingIntent);

        Log.d(LOG_TAG, "updateWidgetListView " + appWidgetId);
        return remoteViews;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "onReceive " + intent.getAction());
        if (intent.getAction().equals(RECIPE_SELECTED)) {
            currentRecipe = intent.getParcelableExtra(RECIPE_KEY);
            Log.d(LOG_TAG, "currentRecipe " + currentRecipe.getName());

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] ids = intent.getExtras().getIntArray(WIDGET_IDS_KEY);

            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.appwidget_recipelist);
        }
        Log.d(LOG_TAG, "onReceive finished " + intent.getAction());
    }


}

