package com.udacity.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.udacity.bakingapp.adapter.RecipeRecyclerViewAdapter;
import com.udacity.bakingapp.loader.RecipeListLoader;
import com.udacity.bakingapp.model.Recipe;

import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>> {


    private boolean mTwoPane;
    private RecyclerView recyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private RecipeRecyclerViewAdapter recipeRecyclerViewAdapter;

    private static final int LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        recyclerView = (RecyclerView) findViewById(R.id.recipe_list);

        setupRecyclerView((RecyclerView) recyclerView);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(this);
        recyclerView.setAdapter(recipeRecyclerViewAdapter);
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, final Bundle loaderArgs) {
        return new RecipeListLoader(this, mLoadingIndicator);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        ((RecipeRecyclerViewAdapter) recyclerView.getAdapter()).setData(data);

        if (null == data) {
            showErrorMessage();
        } else {
            showGridView();
        }
    }

    private void showGridView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

    }

}
