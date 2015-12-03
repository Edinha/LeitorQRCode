package com.berteodosio.qrlocation.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.berteodosio.qrlocation.R;
import com.berteodosio.qrlocation.model.Place;
import com.berteodosio.qrlocation.presenter.MainPresenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // faz a interação entre a activity e o arquivo
    private MainPresenter mPresenter;

    private TextView mText;
    private android.os.Handler handler = new Handler();

    private List<Place> locations;
    private Location atual;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mPresenter = new MainPresenter(this);
        mText = (TextView) findViewById(R.id.text);

        locations = new ArrayList<Place>();

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            // pega a posição atual
            public void onLocationChanged(Location location) {
                if (MainActivity.this.atual == null)    // primeira vez
                {
                    CheckBox box = (CheckBox) findViewById(R.id.checkBox);
                    box.setText("Localização encontrada");
                    box.setChecked(true);

                    Button scan = (Button) findViewById(R.id.scan);
                    scan.setEnabled(true);

                    ProgressBar bar = (ProgressBar) findViewById(R.id.progressbar);
                    bar.setVisibility(View.GONE);
                }

                MainActivity.this.atual = location;
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void onScanClick(View v) {
        if (atual != null)
            new IntentIntegrator(this).initiateScan();
        else
            Toast.makeText(this, "Por favor, aguarde até que sua localização seja encontrada", Toast.LENGTH_LONG)
                .show();
    }

    public void onListClick(View v) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(
                requestCode, resultCode, data);

        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                IntentResult scanResult =
                        IntentIntegrator.parseActivityResult(
                                requestCode, resultCode,data);

                if (scanResult == null) {
                    return;
                }

                final String result = scanResult.getContents();

                if (result != null) {
                    // salva a localização
                    mPresenter.savePlace(new Place(atual, result));
                    mText.setText(result);
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setMyLocationEnabled(true);
    }

    public void onPlaceSaved() {
        Toast.makeText(this, "Localização salva", Toast.LENGTH_SHORT).show();
    }

    public void onPlaceFailedSave() {
        Toast.makeText(this, "Falha ao salvar localização", Toast.LENGTH_SHORT).show();
    }
}
