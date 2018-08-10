package com.udacity.bakingapp;

/**
 * Created by Mihai on 8/9/2018.
 */

import android.content.Intent;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    private final static String RECIPE_NAME = "Test recipe";
    private final static String RECIPE_STEP1 = "step1 description";
    private final static String RECIPE_STEP2 = "step2 description";

    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeDetailActivity.class, false, false);

    @Test
    public void checkCorrectRecipeInformation() {

        Intent i = new Intent();
        Recipe recipe = getDummyRecipe();
        i.putExtra(StepDetailFragment.ARG_ITEM, recipe);
        mActivityTestRule.launchActivity(i);

        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(RECIPE_NAME)));

        RecyclerView recyclerView = (RecyclerView) mActivityTestRule.getActivity().findViewById(R.id.step_list);
        Assert.assertEquals(recyclerView.getAdapter().getItemCount(), 2);

        onView(withRecyclerView(R.id.step_list).atPosition(0)).check(matches(isDisplayed()));

    }

    @Test
    public void clickRecyclerViewItem_OpensStepDetailFragment() {

        Intent i = new Intent();
        Recipe recipe = getDummyRecipe();
        i.putExtra(StepDetailFragment.ARG_ITEM, recipe);
        mActivityTestRule.launchActivity(i);

        onView(withId(R.id.step_list)).perform( RecyclerViewActions.actionOnItemAtPosition(0, click()) );
        onView(withId(R.id.recipe_detail)).check(matches(withText(RECIPE_STEP1)));
    }

    @Test
    public void checkFlowInRecipeSteps() {

        Intent i = new Intent();
        Recipe recipe = getDummyRecipe();
        i.putExtra(StepDetailFragment.ARG_ITEM, recipe);
        mActivityTestRule.launchActivity(i);

        onView(withId(R.id.step_list)).perform( RecyclerViewActions.actionOnItemAtPosition(0, click()) );
        onView(withId(R.id.recipe_detail)).check(matches(withText(RECIPE_STEP1)));
        onView(withId(R.id.next_step)).perform(click());
        onView(withId(R.id.recipe_detail)).check(matches(withText(RECIPE_STEP2)));
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(withId(R.id.recipe_detail)).check(matches(withText(RECIPE_STEP1)));
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(withId(R.id.step_list)).check(matches(isDisplayed()));

    }

    private Recipe getDummyRecipe() {
        Recipe recipe = new Recipe();
        recipe.setName(RECIPE_NAME);
        Step step1 = new Step();
        step1.setShortDescription("step1");
        step1.setDescription(RECIPE_STEP1);
        step1.setVideoURL("");
        step1.setThumbnailURL("");

        Step step2 = new Step();
        step2.setShortDescription("step2");
        step2.setDescription(RECIPE_STEP2);
        step2.setVideoURL("");
        step2.setThumbnailURL("");

        recipe.getSteps().add(step1);
        recipe.getSteps().add(step2);
        return recipe;
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

}