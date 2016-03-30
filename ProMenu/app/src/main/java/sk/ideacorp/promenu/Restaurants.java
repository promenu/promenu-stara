package sk.ideacorp.promenu;

import android.app.Activity;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Lukas Knotek on 26. 3. 2016.
 */
public class Restaurants {

    private Activity activity;

    public Restaurants(Activity activity)
    {
        this.activity = activity;
    }

    public String getHeader()
    {
        String head = "<!DOCTYPE HTML><html dir=\"ltr\" lang=\"sk-SK\"><head><meta charset=\"UTF-8\">";
        head += "<meta charset=\"utf-8\"><title>ProMenu.sk</title><meta name=\"author\" content=\"ProMenu.sk\" />";
        head += "<meta name=\"viewport\" content=\"width=device-width\"><meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\">";
        head += "<link href=\"styles.css\" rel=\"stylesheet\" type=\"text/css\">";
        head += "<script src=\"scripts.js\"></script>";

        head += "</head><body><div class=\"mobile-wrapper\">";

        return head;
    }

    public String getHeader2()
    {
        String head = "<!DOCTYPE HTML><html dir=\"ltr\" lang=\"sk-SK\"><head><meta charset=\"UTF-8\">";
        head += "<meta charset=\"utf-8\"><title>ProMenu.sk</title><meta name=\"author\" content=\"ProMenu.sk\" />";
        head += "<meta name=\"viewport\" content=\"width=device-width\"><meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\">";
        head += "<link href=\"fancybox.css\" rel=\"stylesheet\" type=\"text/css\">";
        head += "<link href=\"styles.css\" rel=\"stylesheet\" type=\"text/css\">";
        head += "<script src=\"detail.js\"></script>";
        head += "<script src=\"jquery.fancybox.js\"></script>";

        head += "</head><body><div class=\"mobile-wrapper\">";

        return head;
    }

    public String getBody(String response)
    {
        String html = "";



        return html;
    }

    public String getFooter()
    {

        String footer = "</div></body></html>";

        return footer;
    }

    private void loadData()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                try {
                    String str = new String(response, "UTF-8");

                    //AboutActivity.this.browser.setHTML(str);
                } catch (Exception e)
                {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}
