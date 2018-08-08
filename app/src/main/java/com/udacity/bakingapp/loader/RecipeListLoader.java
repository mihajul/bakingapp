package com.udacity.bakingapp.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.utils.JsonUtils;
import com.udacity.bakingapp.utils.NetworkUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mihai
 */

public class RecipeListLoader extends AsyncTaskLoader<List<Recipe>> {
    List<Recipe> recipes = null;
    ProgressBar mLoadingIndicator;

    public RecipeListLoader(Context context, ProgressBar mLoadingIndicator) {
        super(context);
        this.mLoadingIndicator = mLoadingIndicator;
    }

    @Override
    protected void onStartLoading() {
        if (recipes != null) {
            deliverResult(recipes);
        } else {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            forceLoad();
        }
    }


    @Override
    public List<Recipe> loadInBackground() {

        try {
            String json = NetworkUtils.getRecipesJson();
            List<Recipe> recipes = JsonUtils.parseRecipesJson(json);

            return recipes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void deliverResult(List<Recipe> data) {
        recipes = data;
        super.deliverResult(recipes);
    }
}
