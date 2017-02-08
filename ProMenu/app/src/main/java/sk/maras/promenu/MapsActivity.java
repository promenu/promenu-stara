package sk.maras.promenu;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String selected_restaurant_name = "";

    private double latitude = 0;

    private double longitude = 0;

    private String mobile_id = "";

    private String url = "http://mobile.promenu.sk/api/maps";

    private String JSON_Data = "";

    private boolean is_located = false;

    private Map<String, String> slug_array = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle bundle = getIntent().getExtras();
        this.selected_restaurant_name = bundle.getString("restaurant_name");
        this.mobile_id = bundle.getString("mobile_id");
        this.latitude = (bundle.getString("latitude") != null) ? Double.parseDouble(bundle.getString("latitude")) : 0;
        this.longitude = (bundle.getString("longitude") != null) ? Double.parseDouble(bundle.getString("longitude")) : 0;

        if(selected_restaurant_name != null && selected_restaurant_name.length() > 0) {
            TextView page_name = (TextView) findViewById(R.id.page_name);

            String restaurant_name_2 = (selected_restaurant_name.length() > 30) ? selected_restaurant_name.substring(0, 30) + "..." : selected_restaurant_name;

            page_name.setText(restaurant_name_2);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        /*GPSTracker tracker = new GPSTracker(this);

        if (tracker.canGetLocation()) {
            this.latitude = tracker.getLatitude();
            this.longitude = tracker.getLongitude();

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
        }*/

        if(latitude > 0 && longitude > 0) {
            LatLng restaurant = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(restaurant));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

            is_located = true;
        } else {
            LatLng slovakia = new LatLng(48.708746, 19.507936);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(slovakia));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(6.0f));
        }

        mMap.setOnMyLocationChangeListener(this.myLocationChangeListener);
        mMap.setMyLocationEnabled(true);

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

                        String slug = jsonObject.optString("slug").toString();
                        String restaurant_name = jsonObject.optString("nazov_prevadzky").toString();
                        float restaurant_latitude = Float.parseFloat(jsonObject.optString("lat").toString());
                        float restaurant_longitude = Float.parseFloat(jsonObject.optString("lon").toString());
                        float distance = Float.parseFloat(jsonObject.optString("distance").toString());
                        int rating = Integer.parseInt(jsonObject.optString("rating").toString());

                        if(restaurant_latitude > 0 && restaurant_longitude > 0)
                        {
                            LatLng position = new LatLng(restaurant_latitude, restaurant_longitude);

                            Marker marker;

                            if(distance > 0) {
                                String distance_in_km = String.format("%.2f", (distance / 1000));

                                marker = mMap.addMarker(new MarkerOptions().position(position).title(restaurant_name).snippet(distance_in_km + "km  Rating: " + rating)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                            } else {
                                marker = mMap.addMarker(new MarkerOptions().position(position).title(restaurant_name).snippet("Rating: " + rating)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                            }

                            if(selected_restaurant_name != null && selected_restaurant_name.indexOf(restaurant_name) >= 0) {
                                marker.showInfoWindow();
                            }

                            slug_array.put(marker.getId(), slug);
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
                String restaurant_name = marker.getTitle();

                String slug = slug_array.get(marker.getId());
                slug = (slug != null) ? slug : "";

                LatLng position = marker.getPosition();

                String restaurant_latitude = String.valueOf(position.latitude);

                String restaurant_longitude = String.valueOf(position.longitude);

                Intent restaurant_intent = new Intent(MapsActivity.this, RestaurantActivity.class);
                restaurant_intent.putExtra("restaurant_name", restaurant_name);
                restaurant_intent.putExtra("slug", slug);
                restaurant_intent.putExtra("mobile_id", mobile_id);
                restaurant_intent.putExtra("latitude", latitude);
                restaurant_intent.putExtra("longitude", longitude);
                restaurant_intent.putExtra("restaurant_latitude", restaurant_latitude);
                restaurant_intent.putExtra("restaurant_longitude", restaurant_longitude);

                MapsActivity.this.startActivity(restaurant_intent);
            }
        });

        //mMap.addMarker(new MarkerOptions().position(new LatLng(48.681651, 17.366871)).title("Indore"));
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (is_located == false) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

                //mMap.addMarker(new MarkerOptions().position(loc).title("Ja").snippet("Tu sa nachádzam")
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

                if (mMap != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                }

                is_located = true;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
