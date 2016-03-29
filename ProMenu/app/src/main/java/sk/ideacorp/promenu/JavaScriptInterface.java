package sk.ideacorp.promenu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ProgressBar;

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
}
