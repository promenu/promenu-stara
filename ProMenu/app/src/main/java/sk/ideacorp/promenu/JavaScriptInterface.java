package sk.ideacorp.promenu;

import android.app.Activity;
import android.content.Context;
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
}
