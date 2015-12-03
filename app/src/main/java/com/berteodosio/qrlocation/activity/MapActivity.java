package com.berteodosio.qrlocation.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.berteodosio.qrlocation.presenter.LoadLocationPresenter;
import com.berteodosio.qrlocation.view.DisplayPlacesView;
import com.berteodosio.u13197.leitor.R;
import com.berteodosio.qrlocation.model.Local;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationChangeListener, DisplayPlacesView {

    public static final String EXTRA_LOCAL_LOCATION = "extra_local_location";
    public static final String EXTRA_LOCAL_TEXT = "extra_local_text";

    private GoogleMap mMap;
    private boolean mFirstTimeLocationChanged = true;
    private LoadLocationPresenter mPresenter;

    private Local mLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Mapa");

        mPresenter = new LoadLocationPresenter(this, this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        Location location = extras.getParcelable(EXTRA_LOCAL_LOCATION);
        String text = extras.getString(EXTRA_LOCAL_TEXT);
        mLocal = new Local(location, text);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(this);

        mPresenter.loadLocalsFromXML();
    }

    @Override
    public void onMyLocationChange(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        if (mFirstTimeLocationChanged) {
            mFirstTimeLocationChanged = false;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 13.5f));
        }
    }

    @Override
    public void displayLocals(List<Local> locals) {
        for (Local local: locals) {
            if (local.getText().equals(mLocal.getText()))
                continue;
            LatLng latLng = new LatLng(local.getLocation().getLatitude(), local.getLocation().getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(local.getText()));
        }

        LatLng latLng = new LatLng(mLocal.getLocation().getLatitude(), mLocal.getLocation().getLongitude());

        mMap.addMarker(new MarkerOptions().position(latLng)
                .title(mLocal.getText())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
    }
}
