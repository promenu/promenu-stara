package sk.maras.promenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;

/**
 * Created by Lukas Knotek on 26. 3. 2016.
 */
public class JavaScriptInterface {

    private Activity activity;

    JavaScriptInterface(Activity activity)
    {
        this.activity = activity;
    }

    @JavascriptInterface
    public void onFieldBlur(String fieldId, String fieldValue){
        //Do something with value
        //Toast.makeText(getContext(), fieldId+"="+fieldValue, Toast.LENGTH_LONG).show();

        MessageBox messageBox = new MessageBox(this.activity, "test", "OK");
        messageBox.Show();
    }

    @JavascriptInterface
    public void test()
    {
        View view = this.activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)this.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        //MessageBox messageBox = new MessageBox(this.activity, "test", "OK");
        //messageBox.Show();
    }

    @JavascriptInterface
    public void detail(String restaurant_name, String slug, String mobile_id, String latitude, String longitude)
    {
        Intent restaurant_intent = new Intent(this.activity, RestaurantActivity.class);
        restaurant_intent.putExtra("restaurant_name", restaurant_name);
        restaurant_intent.putExtra("slug", slug);
        restaurant_intent.putExtra("mobile_id", mobile_id);
        restaurant_intent.putExtra("latitude", latitude);
        restaurant_intent.putExtra("longitude", longitude);
        restaurant_intent.putExtra("restaurant_latitude", "");
        restaurant_intent.putExtra("restaurant_longitude", "");

        this.activity.startActivity(restaurant_intent);
    }

    @JavascriptInterface
    public void search(String search_value)
    {
        MainActivity main_activity = (MainActivity)this.activity;
        main_activity.onSearch(search_value);
    }

    @JavascriptInterface
    public void food_list(String restaurant_name, String slug, String mobile_id, String latitude, String longitude)
    {
        Intent foodlist_intent = new Intent(this.activity, FoodlistActivity.class);
        foodlist_intent.putExtra("restaurant_name", restaurant_name);
        foodlist_intent.putExtra("slug", slug);
        foodlist_intent.putExtra("mobile_id", mobile_id);
        foodlist_intent.putExtra("latitude", latitude);
        foodlist_intent.putExtra("longitude", longitude);
        foodlist_intent.putExtra("restaurant_latitude", "");
        foodlist_intent.putExtra("restaurant_longitude", "");

        this.activity.startActivity(foodlist_intent);
    }

    @JavascriptInterface
    public void map_detail(String restaurant_name, String slug, String mobile_id, String latitude, String longitude)
    {
        Intent maps_intent = new Intent(this.activity, MapsActivity.class);
        maps_intent.putExtra("restaurant_name", restaurant_name);
        maps_intent.putExtra("slug", slug);
        maps_intent.putExtra("mobile_id", mobile_id);
        maps_intent.putExtra("latitude", latitude);
        maps_intent.putExtra("longitude", longitude);
        maps_intent.putExtra("restaurant_latitude", "");
        maps_intent.putExtra("restaurant_longitude", "");

        this.activity.startActivity(maps_intent);
    }

    @JavascriptInterface
    public void enable_gps()
    {
        MainActivity main_activity = (MainActivity)this.activity;
        main_activity.enableGPS();
    }

    @JavascriptInterface
    public void add_favorite(int id, String mobile_id)
    {
        RestaurantActivity restaurant_activity = (RestaurantActivity)this.activity;
        restaurant_activity.add_favorite(id, mobile_id);
    }

    @JavascriptInterface
    public void remove_favorite(int id, String mobile_id)
    {
        RestaurantActivity restaurant_activity = (RestaurantActivity)this.activity;
        restaurant_activity.remove_favorite(id, mobile_id);
    }
}
