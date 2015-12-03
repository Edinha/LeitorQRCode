package com.example.u13197.leitor.activity;

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

import com.example.u13197.leitor.model.Local;
import com.example.u13197.leitor.presenter.MainPresenter;
import com.example.u13197.leitor.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MainPresenter mPresenter;

    private TextView mText;
    private android.os.Handler handler = new Handler();

    private List<Local> locations;
    private Location atual;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this);
        mText = (TextView) findViewById(R.id.text);

        locations = new ArrayList<Local>();

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            // quando a posição muda, atualiza o location
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
        //iniciar o scan de qr code
        if (atual != null)
            new IntentIntegrator(this).initiateScan();
        else
            Toast.makeText(this, "Por favor, aguarde até que sua localização seja encontrada", Toast.LENGTH_LONG)
                .show();
    }

    public void onListClick(View v) {
        // mostrar google maps com as parada tudo
        // TODO: open listview

        /*

        for (Local l : this.locations) {
            LatLng lg = new LatLng(l.getLocation().getLatitude(), l.getLocation().getLongitude());
            this.map.addMarker(new MarkerOptions()
                    .title(l.getText())
                    .position(lg));
        }

        */

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
                    mPresenter.savePlace(new Local(atual, result));
                    mText.setText(result);
                    /*
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //guardar o result referente aquela localização
                            MainActivity.this.locations.add(new Local(atual, result));
                        }
                    });
                    */
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
