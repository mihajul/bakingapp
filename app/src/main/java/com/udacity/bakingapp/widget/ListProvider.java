package com.udacity.bakingapp.widget;

import java.util.ArrayList;
import java.util.List;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.udacity.bakingapp.BakingAppWidget;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.model.Ingredient;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;

public class ListProvider implements RemoteViewsFactory {
    private List<Ingredient> listItemList = new ArrayList<Ingredient>();
    private Context context = null;
    private int appWidgetId;
    public static final String LOG_TAG = ListProvider.class.getName();

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        Recipe recipe = BakingAppWidget.currentRecipe; //intent.getParcelableExtra(BakingAppWidget.RECIPE_KEY);

        Log.d(LOG_TAG, "ListProvider" + appWidgetId + " extras: " + intent.getExtras().size());
        Log.d(LOG_TAG, "ListProvider" + appWidgetId + " recipe: " +  recipe);

        if(recipe!=null) {
            listItemList.clear();
            listItemList.addAll(recipe.getIngredients());
        }

    }


    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {

        Log.d(LOG_TAG, "ListProvider" + " getViewAt: " +  position);


        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.widget_list_row);
        Ingredient listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.content, listItem.toString());

        return remoteView;
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
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        Recipe recipe = BakingAppWidget.currentRecipe;
        if(recipe!=null) {
            listItemList.clear();
            listItemList.addAll(recipe.getIngredients());
        }
        Log.d(LOG_TAG, "onDataSetChanged " + listItemList.size());
    }

    @Override
    public void onDestroy() {
    }

}