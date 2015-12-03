package com.berteodosio.qrlocation.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.berteodosio.qrlocation.R;
import com.berteodosio.qrlocation.presenter.LoadLocationPresenter;
import com.berteodosio.qrlocation.view.DisplayPlacesView;
import com.berteodosio.qrlocation.model.Place;
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

    // para pegar o Place passado pelo intent
    public static final String EXTRA_LOCAL_LOCATION = "extra_local_location";
    public static final String EXTRA_LOCAL_TEXT = "extra_local_text";

    private GoogleMap mMap;
    private boolean mFirstTimeLocationChanged = true;
    private LoadLocationPresenter mPresenter;

    private Place mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
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

        // criado com as informações recebidas do intent
        mPlace = new Place(location, text);
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

        // para dar um "zoom" na localização atual
        if (mFirstTimeLocationChanged) {
            mFirstTimeLocationChanged = false;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 13.5f));
        }
    }

    @Override
    public void displayLocals(List<Place> places) {
        // adiciona as marcadas dos locais carregados do arquivo XML
        for (Place place : places) {
            if (place.getText().equals(mPlace.getText()))
                continue;
            LatLng latLng = new LatLng(place.getLocation().getLatitude(), place.getLocation().getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(place.getText()));
        }

        LatLng latLng = new LatLng(mPlace.getLocation().getLatitude(), mPlace.getLocation().getLongitude());

        // adiciona a marca rosa da localização que foi clicada
        mMap.addMarker(new MarkerOptions().position(latLng)
                .title(mPlace.getText())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
    }
}
