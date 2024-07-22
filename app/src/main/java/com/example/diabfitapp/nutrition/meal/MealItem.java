package com.example.diabfitapp.nutrition.meal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class MealItem implements Serializable {
    private String title;
    private String details;
    private String mealType;
    private float carbs;
    private int calories;
    private String ingredients;
    private String servingSize;
    private int imageResId;

    public MealItem(String title, String details, String mealType, float carbs, int calories, String ingredients, String servingSize, int imageResId) {
        this.title = title;
        this.details = details;
        this.mealType = mealType;
        this.carbs = carbs;
        this.calories = calories;
        this.ingredients = ingredients;
        this.servingSize = servingSize;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getMealType() {
        return mealType;
    }

    public float getCarbs() {
        return carbs;
    }

    public int getCalories() {
        return calories;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getServingSize() {
        return servingSize;
    }

    public int getImageResId() {
        return imageResId;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("details", details);
        jsonObject.put("mealType", mealType);
        jsonObject.put("carbs", carbs);
        jsonObject.put("calories", calories);
        jsonObject.put("ingredients", ingredients);
        jsonObject.put("servingSize", servingSize);
        jsonObject.put("imageResId", imageResId);
        return jsonObject;
    }

    public static MealItem fromJson(JSONObject jsonObject) throws JSONException {
        String title = jsonObject.getString("title");
        String details = jsonObject.getString("details");
        String mealType = jsonObject.getString("mealType");
        float carbs = (float) jsonObject.getDouble("carbs");
        int calories = jsonObject.getInt("calories");
        String ingredients = jsonObject.getString("ingredients");
        String servingSize = jsonObject.getString("servingSize");
        int imageResId = jsonObject.getInt("imageResId");

        return new MealItem(title, details, mealType, carbs, calories, ingredients, servingSize, imageResId);
    }

    public static JSONArray toJsonArray(List<MealItem> items) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (MealItem item : items) {
            jsonArray.put(item.toJson());
        }
        return jsonArray;
    }

    public static List<MealItem> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<MealItem> items = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            items.add(MealItem.fromJson(jsonArray.getJSONObject(i)));
        }
        return items;
    }
}
