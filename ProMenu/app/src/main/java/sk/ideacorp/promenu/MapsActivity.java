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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private double latitude = 0;

    private double longitude = 0;

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
        } else {
            LatLng slovakia = new LatLng(48.708746, 19.507936);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(slovakia));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(6.0f));
        }

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
