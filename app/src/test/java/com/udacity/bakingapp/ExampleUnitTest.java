package com.udacity.bakingapp;

import com.udacity.bakingapp.model.Ingredient;


import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testIngredientToStringMethod() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setMeasure("TBSP");
        ingredient.setQuantity(1.0);
        ingredient.setIngredient("test");
        Assert.assertEquals("- 1 TBSP x test", ingredient.toString());
    }
}