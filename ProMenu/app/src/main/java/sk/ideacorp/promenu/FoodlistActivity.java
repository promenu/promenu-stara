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

public class FoodlistActivity extends AppCompatActivity {

    private String url = "";

    private String restaurant_name = "";

    private String restaurant_name_2 = "";

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
        setContentView(R.layout.activity_foodlist);

        Bundle bundle = getIntent().getExtras();
        this.restaurant_name = bundle.getString("restaurant_name");
        this.slug = bundle.getString("slug");
        this.mobile_id = bundle.getString("mobile_id");
        this.latitude = bundle.getString("latitude");
        this.longitude = bundle.getString("longitude");

        TextView page_name = (TextView)findViewById(R.id.page_name);

        restaurant_name_2 = (restaurant_name.length() > 30) ? restaurant_name.substring(0, 30) + "..." : restaurant_name;

        page_name.setText(restaurant_name_2);

        WebView web_view = (WebView) findViewById(R.id.webViewMain);

        this.url = "http://mobile.promenu.sk/jedalny-listok/" + this.slug;

        this.browser = new Browser(web_view, this, (ProgressBar)findViewById(R.id.mainProgressBar));
        this.browser.set_url(this.url);
        this.browser.load();

        Toolbar about_toolbar = (Toolbar)findViewById(R.id.toolbar);
        about_toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    FoodlistActivity.this.onBackPressed();
                }

                return true;
            }
        });

        ImageView back_button = (ImageView)findViewById(R.id.back_button);
        back_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    FoodlistActivity.this.onBackPressed();

                    return true;
                }

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
