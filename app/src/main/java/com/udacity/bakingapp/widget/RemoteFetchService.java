package com.udacity.bakingapp.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.udacity.bakingapp.BakingAppWidget;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.utils.JsonUtils;
import com.udacity.bakingapp.utils.NetworkUtils;

import java.util.List;

public class RemoteFetchService extends Service {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public static List<Recipe> listItemList;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * Retrieve appwidget id from intent it is needed to update widget later
     * initialize our AQuery class
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
            appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

        new DownloadRecipesTask().execute();

        return super.onStartCommand(intent, flags, startId);
    }
    /**
     * Method which sends broadcast to WidgetProvider
     * so that widget is notified to do necessary action
     * and here action == WidgetProvider.DATA_FETCHED
     */
    private void populateWidget() {

        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(BakingAppWidget.RECIPES_LOADED);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        sendBroadcast(widgetUpdateIntent);
        this.stopSelf();
    }


    private class DownloadRecipesTask extends AsyncTask<Void, Integer, List<Recipe>> {
        protected List<Recipe> doInBackground(Void... voids) {
            try {
                String json = NetworkUtils.getRecipesJson();
                List<Recipe> recipes = JsonUtils.parseRecipesJson(json);

                return recipes;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(List<Recipe> result) {
            listItemList = result;
            populateWidget();
        }
    }
}

