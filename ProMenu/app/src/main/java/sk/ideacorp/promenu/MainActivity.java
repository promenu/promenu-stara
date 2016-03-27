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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        GPSTracker tracker = new GPSTracker(this);

        if (!tracker.canGetLocation()) {
            //tracker.showSettingsAlert();

            MessageBox messageBox = new MessageBox(MainActivity.this, "Hľadaj podľa polohy", "Zapni GPS a hľadaj reštaurácie v okolí alebo pokračuj stlačením ok bez lokácie.");
            messageBox.ShowSettings();
        } else {
            this.latitude = tracker.getLatitude();
            this.longitude = tracker.getLongitude();
        }

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
