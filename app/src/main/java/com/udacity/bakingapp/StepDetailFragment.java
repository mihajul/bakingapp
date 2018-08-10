package com.udacity.bakingapp;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.adapter.RecipeStepsRecyclerViewAdapter;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link RecipeDetailActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {

    public static final String ARG_ITEM = "item";
    public static final String ARG_STEP_INDEX = "index";
    public static final String CURRENT_TIME = "current_time";

    private Recipe recipe;
    private Step step;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    long currentTime = 0;

    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            int index = getArguments().getInt(ARG_STEP_INDEX);
            recipe = getArguments().getParcelable(ARG_ITEM);
            step = recipe.getSteps().get(index);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null) {
            currentTime = mExoPlayer.getCurrentPosition();
        }
        outState.putLong(CURRENT_TIME, currentTime);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setToolbarTitle();

        final View rootView = inflater.inflate(R.layout.step_detail, container, false);

        ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(step.getDescription());

        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);

        if (!step.getVideoURL().isEmpty()) {
            if (savedInstanceState != null) {
                currentTime = savedInstanceState.getLong(CURRENT_TIME, 0);
            }
            initializePlayer(Uri.parse(step.getVideoURL()), currentTime);
            mPlayerView.setVisibility(View.VISIBLE);
        }else {
            mPlayerView.setVisibility(View.GONE);
        }

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
        if (!step.getThumbnailURL().isEmpty()) {
            Picasso.with(this.getContext())
                    .load(step.getThumbnailURL())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .fit()
                    .centerCrop()
                    .into(imageView);
            imageView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.GONE);
        }

        final boolean mTwoPane = getActivity().findViewById(R.id.step_detail_container) != null;

        Button next = (Button) rootView.findViewById(R.id.next_step);
        if(step.getId() < recipe.getSteps().size() - 1) {
            next.setVisibility(View.VISIBLE);
        }else {
            next.setVisibility(View.GONE);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeStepsRecyclerViewAdapter.showStepDetailActivity(getActivity(), mTwoPane, recipe, step.getId() + 1);
            }
        });

        Button previous = (Button) rootView.findViewById(R.id.previous_step);
        if(step.getId() > 0) {
            previous.setVisibility(View.VISIBLE);
        }else {
            previous.setVisibility(View.GONE);
        }

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeStepsRecyclerViewAdapter.showStepDetailActivity(getActivity(), mTwoPane, recipe, step.getId() - 1);
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer(Uri.parse(step.getVideoURL()), currentTime);
    }

    private void setToolbarTitle() {
        Activity activity = this.getActivity();
        android.support.v7.widget.Toolbar appBarLayout = (android.support.v7.widget.Toolbar) activity.findViewById(R.id.detail_toolbar);
        String title = step.getShortDescription();
        if (step.getId() != 0) {
            title = step.getId() + ". " + title;
        }
        if (appBarLayout != null) {
            appBarLayout.setTitle(title);
        }
        if (((AppCompatActivity) activity).getSupportActionBar() != null) {
            ((AppCompatActivity) activity).getSupportActionBar().setTitle(title);
        }
    }

    private void initializePlayer(Uri mediaUri, long currentTime) {
        if (mExoPlayer == null &&  !step.getVideoURL().isEmpty()) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(this.getContext(), "RecipePlayer");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this.getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(currentTime);
        }
    }


    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            currentTime = mExoPlayer.getCurrentPosition();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
