package com.udacity.bakingapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.BulletSpan;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.bakingapp.adapter.RecipeRecyclerViewAdapter;
import com.udacity.bakingapp.adapter.RecipeStepsRecyclerViewAdapter;
import com.udacity.bakingapp.loader.RecipeListLoader;
import com.udacity.bakingapp.model.Ingredient;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;

import java.text.NumberFormat;
import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailActivity extends AppCompatActivity {


    private boolean mTwoPane;
    private RecyclerView recyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private RecipeStepsRecyclerViewAdapter recipeStepsRecyclerViewAdapter;
    private Recipe recipe;
    private static final int LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipe = getIntent().getParcelableExtra(StepDetailFragment.ARG_ITEM);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(recipe.getName());
        toolbar.setTitle(recipe.getName());


        if (findViewById(R.id.step_detail_container) != null) {
            mTwoPane = true;
        }


        StringBuilder ingredientsBuilder = new StringBuilder();
        for(Ingredient ingredient : recipe.getIngredients()) {
            String quantity = NumberFormat.getInstance().format(ingredient.getQuantity());
            ingredientsBuilder.append("- ");
            ingredientsBuilder.append(quantity);
            ingredientsBuilder.append(" ");
            ingredientsBuilder.append(ingredient.getMeasure());
            ingredientsBuilder.append(" x ");
            ingredientsBuilder.append(ingredient.getIngredient());
            ingredientsBuilder.append("\n");
        }


        TextView ingredientsTextView = (TextView) findViewById(R.id.ingredientsList);
        ingredientsTextView.setText(ingredientsBuilder.toString());

        recyclerView = (RecyclerView) findViewById(R.id.step_list);
        setupRecyclerView((RecyclerView) recyclerView, recipe.getSteps());
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Step> steps) {

        recipeStepsRecyclerViewAdapter = new RecipeStepsRecyclerViewAdapter(this, recipe, mTwoPane);
        recipeStepsRecyclerViewAdapter.setData(steps);
        recyclerView.setAdapter(recipeStepsRecyclerViewAdapter);
    }


}
