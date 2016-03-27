package sk.ideacorp.promenu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Browser browser;

    private String url = "";

    private Restaurants restaurants;

    private float lastTranslate = 0.0f;

    private double latitude = 0;

    private double longitude = 0;

    private LocationManager locationManager;

    private LocationListener locationListener;

    private Boolean is_located = false;

    private String provider;

    private String mobile_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Loader loader = Loader((Context)MainActivity.this);
        //loader.show();

        Identification id = new Identification();
        this.mobile_id = id.GetID();

        this.restaurants = new Restaurants(this);

        //html += "<div class=\"restaurants\"><div class=\"search_box\"><form method=\"post\"><input type=\"text\" name=\"search_value\" class=\"search_value\" id=\"search_value\" value=\"\" placeholder=\"Vyhľadať mesto alebo reštauráciu\" data-placeholder=\"Vyhľadať mesto alebo reštauráciu\" autocomplete=\"off\" /><input type=\"submit\" name=\"search\" value=\"\" /></form></div></div>";


        GPSTracker tracker = new GPSTracker(this);

        if (!tracker.canGetLocation()) {
            //tracker.showSettingsAlert();

            MessageBox messageBox = new MessageBox(MainActivity.this, "Hľadaj podľa polohy", "Zapni GPS a hľadaj reštaurácie v okolí alebo pokračuj stlačením ok bez lokácie.");
            messageBox.ShowSettings();


            this.url = "http://mobile.promenu.sk/api/restauracie?mobile_id=" + this.mobile_id;

            /*
            this.browser.set_url(this.url);
            this.browser.load();*/
        } else {
            this.latitude = tracker.getLatitude();
            this.longitude = tracker.getLongitude();

            //MessageBox messageBox = new MessageBox(MainActivity.this, "Moja poloha", this.latitude + ", " + this.longitude);
            //messageBox.Show();

            this.url = "http://mobile.promenu.sk/api/restauracie/nearby?lat=" + this.latitude + "&lon=" + this.longitude + "&dis=20&mobile_id=" + this.mobile_id;

            /*
            this.browser.set_url(this.url);
            this.browser.load();*/
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this.url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {}

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"

                String html = "";
                html += MainActivity.this.restaurants.getHeader();
                html += "<div class=\"restaurants\"><div class=\"search_box\"><form method=\"post\"><input type=\"text\" name=\"search_value\" class=\"search_value\" id=\"search_value\" value=\"\" placeholder=\"Vyhľadať mesto alebo reštauráciu\" data-placeholder=\"Vyhľadať mesto alebo reštauráciu\" autocomplete=\"off\" /><input type=\"submit\" name=\"search\" value=\"\" /></form></div></div>";

                try {
                    String str = new String(response, "UTF-8");

                    JSONArray array = new JSONArray(str);

                    //html += MainActivity.this.restaurants.getHeader();

                    for(int i = 0; i < array.length(); i++){
                        JSONObject jsonObject = array.getJSONObject(i);

                        String restaurant_name = jsonObject.optString("nazov_prevadzky").toString();
                        String slug = jsonObject.optString("slug").toString();
                        String profile_photo_url = jsonObject.optString("profile_photo_url").toString();
                        String address = jsonObject.optString("ulica").toString();
                        //float restaurant_latitude = Float.parseFloat(jsonObject.optString("lat").toString());
                        //float restaurant_longitude = Float.parseFloat(jsonObject.optString("lon").toString());
                        float distance = Float.parseFloat(jsonObject.optString("distance").toString());
                        float rating = Float.parseFloat(jsonObject.optString("rating").toString());

                        JSONObject cityObject = jsonObject.optJSONObject("city");
                        String city_name = cityObject.optString("name").toString();

                        String distance_in_km = String.format("%.2f", (distance / 1000));
                        String rating_string = String.format("%.1f", rating);

                        html += "<div class=\"restaurant-box\" onclick=\"window.location='http://mobile.promenu.sk/restauracia/" + slug + "?mobile_id=" + MainActivity.this.mobile_id + "';\">" +
                                "<div class=\"section group\"><div class=\"col span_1_of_5\"><img src=\"http://www.promenu.sk" + profile_photo_url + "\" />";

                        html += "</div>" +
                                "<div class=\"col span_4_of_5\">" +
                                "<h2>" + restaurant_name + "</h2>" +
                                "<div class=\"address\">\n" + distance_in_km + "km " + address + ", " + city_name + "</div><div class=\"rating-box\"><p>" + rating_string + "</p></div></div></div><div class=\"clear\"></div></div>";
                    }

                    html += MainActivity.this.restaurants.getFooter();

                    Browser browser = new Browser((WebView)MainActivity.this.findViewById(R.id.webViewMain), MainActivity.this, (ProgressBar) MainActivity.this.findViewById(R.id.mainProgressBar));
                    //browser.set_url(MainActivity.this.url);
                    //browser.load();
                    browser.setHTMLWithAssets(html);
                    //browser.setHTMLWithAssets(html);

                } catch (Exception e) {
                    e.printStackTrace();

                    MessageBox messageBox = new MessageBox(MainActivity.this, "Chyba", "Chyba pri načítaní dát");
                    messageBox.Show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                MessageBox messageBox = new MessageBox(MainActivity.this, "Chyba", "Chyba pri spojení zo serverom");
                messageBox.Show();
            }

            @Override
            public void onRetry(int retryNo) {}
        });

        //
        // Otvori Lave menu / Posunie content
        //
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset) {
                NavigationView mDrawerList = (NavigationView) findViewById(R.id.nav_view);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

                RelativeLayout content = (RelativeLayout) findViewById(R.id.main_content);

                float moveFactor = (mDrawerList.getWidth() * slideOffset);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    toolbar.setTranslationX(moveFactor);
                    content.setTranslationX(moveFactor);
                    content.setTranslationY(0);
                } else {
                    TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                    anim.setDuration(0);
                    anim.setFillAfter(true);

                    toolbar.startAnimation(anim);
                    content.startAnimation(anim);

                    lastTranslate = moveFactor;
                }
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView mapImageView = (ImageView) findViewById(R.id.mapIcon);

        mapImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    Intent maps_intent = new Intent(MainActivity.this, MapsActivity.class);
                    maps_intent.putExtra("latitude", MainActivity.this.latitude);
                    maps_intent.putExtra("longitude", MainActivity.this.longitude);

                    startActivity(maps_intent);
                }

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            Intent about_intent = new Intent(MainActivity.this, AboutActivity.class);

            startActivity(about_intent);
        } else if(id == R.id.nav_blog) {
            Intent blog_intent = new Intent(MainActivity.this, BlogActivity.class);

            startActivity(blog_intent);
        } else if(id == R.id.nav_cooperation) {
            Intent blog_intent = new Intent(MainActivity.this, CooperationActivity.class);

            startActivity(blog_intent);
        }

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
