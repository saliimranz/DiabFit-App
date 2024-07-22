package com.example.diabfitapp.nutrition.meal;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONException;

public class MealData {
    public static JSONArray loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("meals.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            return new JSONArray(json);
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
