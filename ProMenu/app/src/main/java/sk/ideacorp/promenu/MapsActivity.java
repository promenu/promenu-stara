package sk.ideacorp.promenu;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private double latitude = 0;

    private double longitude = 0;

    private String url = "http://mobile.promenu.sk/api/maps";

    private String JSON_Data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        this.latitude = bundle.getDouble("latitude");
        this.longitude = bundle.getDouble("longitude");

        Toolbar about_toolbar = (Toolbar)findViewById(R.id.toolbar);
        about_toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    MapsActivity.this.onBackPressed();
                }

                return true;
            }
        });

        ImageView back_button = (ImageView)findViewById(R.id.back_button);
        back_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    MapsActivity.this.onBackPressed();

                    return true;
                }

                return true;
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(this.latitude > 0 && this.longitude > 0) {
            // Add a marker in Sydney and move the camera
            LatLng my_position = new LatLng(this.latitude, this.longitude);

            mMap.addMarker(new MarkerOptions().position(my_position).title("Ja").snippet("Tu sa nachádzam")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(my_position));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

            MapsActivity.this.url += "?lat=" + this.latitude + "&lon=" + this.longitude + "&dis=1000";
        } else {
            LatLng slovakia = new LatLng(48.708746, 19.507936);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(slovakia));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(6.0f));
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(MapsActivity.this.url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                try {
                    String str = new String(response, "UTF-8");

                    String data = "";

                    JSONArray array = new JSONArray(str);

                    for(int i=0; i < array.length(); i++){
                        JSONObject jsonObject = array.getJSONObject(i);

                        String restaurant_name = jsonObject.optString("nazov_prevadzky").toString();
                        float restaurant_latitude = Float.parseFloat(jsonObject.optString("lat").toString());
                        float restaurant_longitude = Float.parseFloat(jsonObject.optString("lon").toString());
                        float distance = Float.parseFloat(jsonObject.optString("distance").toString());
                        int rating = Integer.parseInt(jsonObject.optString("rating").toString());

                        if(restaurant_latitude > 0 && restaurant_longitude > 0)
                        {
                            LatLng position = new LatLng(restaurant_latitude, restaurant_longitude);

                            if(distance > 0) {
                                String distance_in_km = String.format("%.2f", (distance / 1000));

                                mMap.addMarker(new MarkerOptions().position(position).title(restaurant_name).snippet(distance_in_km + "km  Rating: " + rating)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                            } else {
                                mMap.addMarker(new MarkerOptions().position(position).title(restaurant_name).snippet("Rating: " + rating)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                            }
                        }
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();

                    MessageBox messageBox = new MessageBox(MapsActivity.this, "Chyba", "Chyba pri načítaní dát");
                    messageBox.Show();
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

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            public void onInfoWindowClick(Marker marker) {
                Intent i = new Intent(MapsActivity.this, AboutActivity.class);
                startActivity(i);
            }
        });

        //mMap.addMarker(new MarkerOptions().position(new LatLng(48.681651, 17.366871)).title("Indore"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
