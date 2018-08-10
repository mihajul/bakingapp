package com.udacity.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.udacity.bakingapp.adapter.RecipeStepsRecyclerViewAdapter;
import com.udacity.bakingapp.model.Ingredient;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;

import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailActivity extends AppCompatActivity {


    private static final String LOG_TAG = RecipeDetailActivity.class.getName();
    private boolean mTwoPane;
    private static final int LOADER_ID = 0;
    private RecipeStepsRecyclerViewAdapter recipeStepsRecyclerViewAdapter;
    private Recipe recipe;


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ingredientsList) TextView ingredientsTextView;
    @BindView(R.id.step_list) RecyclerView recyclerView;
    @BindView(R.id.frameLayout) NestedScrollView nestedScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        recipe = getIntent().getParcelableExtra(StepDetailFragment.ARG_ITEM);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(recipe.getName());
        toolbar.setTitle(recipe.getName());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.step_detail_container) != null) {
            mTwoPane = true;
        }


        StringBuilder ingredientsBuilder = new StringBuilder();
        for(Ingredient ingredient : recipe.getIngredients()) {
            ingredientsBuilder.append(ingredient.toString());
            ingredientsBuilder.append("\n");
        }


        ingredientsTextView.setText(ingredientsBuilder.toString());

        setupRecyclerView((RecyclerView) recyclerView, recipe.getSteps());
        populateWidgets();

        nestedScrollView.scrollTo(0,0);

    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Step> steps) {

        recipeStepsRecyclerViewAdapter = new RecipeStepsRecyclerViewAdapter(this, recipe, mTwoPane);
        recipeStepsRecyclerViewAdapter.setData(steps);
        recyclerView.setAdapter(recipeStepsRecyclerViewAdapter);
    }

    private void populateWidgets() {
        Log.d(LOG_TAG, "Setting widget recipe: " + String.valueOf(recipe));
        BakingAppWidget.currentRecipe = recipe;
        AppWidgetManager man = AppWidgetManager.getInstance(getApplicationContext());
        int[] ids = man.getAppWidgetIds(new ComponentName(getApplicationContext(),BakingAppWidget.class));

        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setExtrasClassLoader(Recipe.class.getClassLoader());
        widgetUpdateIntent.setAction(BakingAppWidget.RECIPE_SELECTED);
        widgetUpdateIntent.putExtra(BakingAppWidget.RECIPE_KEY, recipe);
        widgetUpdateIntent.putExtra(BakingAppWidget.WIDGET_IDS_KEY, ids);
        sendBroadcastCompat(this, widgetUpdateIntent);

    }
    public static void sendBroadcastCompat(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            context.sendBroadcast(intent);
            return;
        }

        Intent broadcastIntent = new Intent(intent);
        PackageManager pm = context.getPackageManager();

        List<ResolveInfo> broadcastReceivers  = pm.queryBroadcastReceivers(broadcastIntent, 0);
        for(ResolveInfo info : broadcastReceivers) {
            broadcastIntent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
            context.sendBroadcast(broadcastIntent);
        }
    }
}
