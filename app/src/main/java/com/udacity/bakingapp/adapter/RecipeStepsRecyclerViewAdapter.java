package com.udacity.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.RecipeDetailActivity;
import com.udacity.bakingapp.RecipeListActivity;
import com.udacity.bakingapp.StepDetailActivity;
import com.udacity.bakingapp.StepDetailFragment;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;

import java.util.List;

/**
 * Created by Mihai
 */

public class RecipeStepsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeStepsRecyclerViewAdapter.ViewHolder> {

    private final RecipeDetailActivity mParentActivity;
    private Recipe recipe;
    private List<Step> mValues;
    private final boolean mTwoPane;
    public static int currentRow = -1;

    public RecipeStepsRecyclerViewAdapter(RecipeDetailActivity parent, Recipe recipe,
                                          boolean twoPane) {
        mParentActivity = parent;
        this.recipe = recipe;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Step step = mValues.get(position);

        holder.mTextView.setText(step.getShortDescription());
        if(step.getId()!=0) {
            holder.mStepIdTextView.setText(step.getId() + ". ");
        }else {
            holder.mStepIdTextView.setText("   ");
        }
        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   showStepDetailActivity(mParentActivity, mTwoPane, recipe, position);
                                               }
                                           }
        );

        colorSelectedItem(holder, position);
    }

    public static void showStepDetailActivity(FragmentActivity currentActivity, boolean mTwoPane, Recipe recipe, int stepIndex) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(StepDetailFragment.ARG_ITEM, recipe);
            arguments.putInt(StepDetailFragment.ARG_STEP_INDEX, stepIndex);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            currentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(currentActivity, StepDetailActivity.class);
            intent.putExtra(StepDetailFragment.ARG_ITEM, recipe);
            intent.putExtra(StepDetailFragment.ARG_STEP_INDEX, stepIndex);
            currentActivity.startActivityForResult(intent, 0);
        }
        if(currentActivity.findViewById(R.id.frameLayout)!=null) {
            NestedScrollView scrollView = (NestedScrollView) currentActivity.findViewById(R.id.frameLayout);
            scrollView.scrollTo(0,0);
        }
        if(currentActivity.findViewById(R.id.step_list)!=null) {
            currentRow = stepIndex;
            RecyclerView recyclerview = currentActivity.findViewById(R.id.step_list);
            recyclerview.getAdapter().notifyDataSetChanged();
        }
    }

    private void colorSelectedItem(final ViewHolder holder, final int position) {
        if(currentRow == position){
            holder.cardView.setBackgroundColor(Color.parseColor("#eeeeee"));
        }
        else {
            holder.cardView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public List<Step> getData() {
        return mValues;
    }

    public void setData(List<Step> data) {
        mValues = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTextView;
        final TextView mStepIdTextView;
        final CardView cardView;
        ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.shortDescription);
            mStepIdTextView = (TextView) view.findViewById(R.id.stepId);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }


}
