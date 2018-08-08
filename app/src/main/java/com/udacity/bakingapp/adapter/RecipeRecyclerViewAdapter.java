package com.udacity.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.RecipeDetailActivity;
import com.udacity.bakingapp.StepDetailActivity;
import com.udacity.bakingapp.StepDetailFragment;
import com.udacity.bakingapp.RecipeListActivity;
import com.udacity.bakingapp.model.Recipe;

import java.util.List;

/**
 * Created by Mihai
 */

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

    private final RecipeListActivity mParentActivity;
    private List<Recipe> mValues;

    public RecipeRecyclerViewAdapter(RecipeListActivity parent) {
        mParentActivity = parent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Recipe recipe = mValues.get(position);

        if(!recipe.getImage().isEmpty()) {
            Picasso.with(mParentActivity)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .resize(100, 100)
                    .centerCrop()
                    .into(holder.mContentView);
        };
        holder.mTextView.setText(recipe.getName());
        holder.mServingsView.setText("Servings: " + recipe.getServings());
        holder.itemView.setTag(mValues.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   showDetailActivity(view, recipe);
                                               }
                                           }
        );
    }

    private void showDetailActivity(View view, Recipe recipe) {
       Context context = view.getContext();
       Intent intent = new Intent(context, RecipeDetailActivity.class);
       intent.putExtra(StepDetailFragment.ARG_ITEM, recipe);
       mParentActivity.startActivityForResult(intent, 0);
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public List<Recipe> getData() {
        return mValues;
    }

    public void setData(List<Recipe> data) {
        mValues = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView mContentView;
        final TextView mTextView;
        final TextView mServingsView;
        ViewHolder(View view) {
            super(view);
            mContentView = (ImageView) view.findViewById(R.id.content);
            mTextView = (TextView) view.findViewById(R.id.title);
            mServingsView = (TextView) view.findViewById(R.id.servings);
        }
    }


}
