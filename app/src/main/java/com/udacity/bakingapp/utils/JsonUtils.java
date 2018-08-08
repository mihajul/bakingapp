package com.udacity.bakingapp.utils;

import android.util.Log;

import com.udacity.bakingapp.model.Ingredient;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String INGREDIENTS = "ingredients";
    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String INGREDIENT = "ingredient";

    private static final String STEPS = "steps";
    private static final String SHORT_DESCRIPTION = "shortDescription";
    private static final String DESCRIPTION = "description";
    private static final String VIDEO_URL = "videoURL";
    private static final String THUMBNAIL_URL = "thumbnailURL";
    private static final String SERVINGS = "servings";
    private static final String IMAGE = "image";


   public static List<Recipe> parseRecipesJson(String json) {

       if (json != null) {
            List<Recipe> recipes = new ArrayList<>();
            try {

                JSONArray results = new JSONArray(json);

                for (int i = 0; i < results.length(); i++) {
                    JSONObject recipeObj = results.getJSONObject(i);
                    Recipe recipe = new Recipe();
                    recipe.setId(recipeObj.getInt(ID));
                    recipe.setName(recipeObj.getString(NAME));
                    recipe.setServings(recipeObj.getInt(SERVINGS));
                    recipe.setImage(recipeObj.getString(IMAGE));

                    JSONArray ingredientsArray = recipeObj.getJSONArray(INGREDIENTS);
                    List<Ingredient> ingredients = new ArrayList<>();
                    for (int j = 0; j < ingredientsArray.length(); j++) {
                        JSONObject ingredientsObj = ingredientsArray.getJSONObject(j);
                        Ingredient ingredient = new Ingredient();

                        ingredient.setIngredient(ingredientsObj.getString(INGREDIENT));
                        ingredient.setQuantity(ingredientsObj.getDouble(QUANTITY));
                        ingredient.setMeasure(ingredientsObj.getString(MEASURE));
                        ingredients.add(ingredient);
                    }
                    recipe.setIngredients(ingredients);

                    JSONArray stepsArray = recipeObj.getJSONArray(STEPS);
                    List<Step> steps = new ArrayList<>();
                    for (int j = 0; j < stepsArray.length(); j++) {
                        JSONObject stepsObj = stepsArray.getJSONObject(j);
                        Step step = new Step();
                        step.setId(stepsObj.getInt(ID));
                        step.setShortDescription(stepsObj.getString(SHORT_DESCRIPTION));
                        step.setDescription(stepsObj.getString(DESCRIPTION));
                        step.setVideoURL(stepsObj.getString(VIDEO_URL));
                        step.setThumbnailURL(stepsObj.getString(THUMBNAIL_URL));
                        steps.add(step);
                    }
                    recipe.setSteps(steps);

                    recipes.add(recipe);
                }
                return  recipes;
            }catch (Exception e) {
                Log.e( LOG_TAG , e.getMessage());
            }
        }

        return null;
    }

}
