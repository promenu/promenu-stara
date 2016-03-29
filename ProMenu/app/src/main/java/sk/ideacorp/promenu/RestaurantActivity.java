package sk.ideacorp.promenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RestaurantActivity extends AppCompatActivity {

    private String url = "";

    private String restaurant_name = "";

    private String slug = "";

    private String mobile_id = "";

    private String latitude = "";

    private String longitude = "";

    private String restaurant_latitude = "";

    private String restaurant_longitude = "";

    private Browser browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Bundle bundle = getIntent().getExtras();
        this.restaurant_name = bundle.getString("restaurant_name");
        this.slug = bundle.getString("slug");
        this.mobile_id = bundle.getString("mobile_id");
        this.latitude = bundle.getString("latitude");
        this.longitude = bundle.getString("longitude");

        TextView page_name = (TextView)findViewById(R.id.page_name);

        String restaurant_name_2 = (restaurant_name.length() > 30) ? restaurant_name.substring(0, 30) + "..." : restaurant_name;

        page_name.setText(restaurant_name_2);

        Toolbar about_toolbar = (Toolbar)findViewById(R.id.toolbar);
        about_toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    RestaurantActivity.this.onBackPressed();
                }

                return true;
            }
        });

        ImageView back_button = (ImageView)findViewById(R.id.back_button);
        back_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    RestaurantActivity.this.onBackPressed();

                    return true;
                }

                return true;
            }
        });

        this.url = "http://mobile.promenu.sk/api/restauracia/" + this.slug;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this.url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {}

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"

                try {
                    Restaurants restaurants = new Restaurants(RestaurantActivity.this);

                    String html = "";
                    html += restaurants.getHeader2();

                    String str = new String(response, "UTF-8");

                    html += "<div class=\"restaurant_page\"><div class=\"day_selector\">";
                    html += "<a href=\"#\" data-href=\"#monday\" class=\"first {if date('N') == 1}active{/if}\">Po</a>";
                    html += "<a href=\"#\" data-href=\"#tuesday\" class=\"{if date('N') == 2}active{/if}\">Ut</a>";
                    html += "<a href=\"#\" data-href=\"#wednesday\" class=\"{if date('N') == 3}active{/if}\">St</a>";
                    html += "<a href=\"#\" data-href=\"#thursday\" class=\"{if date('N') == 4}active{/if}\">Št</a>";
                    html += "<a href=\"#\" data-href=\"#friday\" class=\"{if date('N') == 5}active{/if}\">Pi</a>";
                    html += "<a href=\"#\" data-href=\"#saturday\" class=\"{if date('N') == 6}active{/if}\">So</a>";
                    html += "<a href=\"#\" data-href=\"#sunday\" class=\"last {if date('N') == 7}active{/if}\">Ne</a>";
                    html += "</div>";

                    html += "</div>";

                    html += "<div class=\"desc\"></div>";

                    html += restaurants.getFooter();

                    Browser browser = new Browser((WebView)RestaurantActivity.this.findViewById(R.id.webViewMain), RestaurantActivity.this, (ProgressBar)RestaurantActivity.this.findViewById(R.id.mainProgressBar));
                    browser.setHTMLWithAssets(html);

                } catch (Exception e) {
                    e.printStackTrace();

                    MessageBox messageBox = new MessageBox(RestaurantActivity.this, "Chyba", "Chyba pri načítaní dát");
                    messageBox.Show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                MessageBox messageBox = new MessageBox(RestaurantActivity.this, "Chyba", "Chyba pri spojení so serverom");
                messageBox.Show();
            }

            @Override
            public void onRetry(int retryNo) {}
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
