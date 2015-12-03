package com.example.u13197.leitor;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private TextView txt;
    private android.os.Handler handler = new Handler();

    private List<Local> locations;
    private Location atual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locations = new ArrayList<Local>();
        txt = (TextView) findViewById(R.id.txtScan);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            // quando a posição muda, atualiza o location
            public void onLocationChanged(Location location) {
               MainActivity.this.atual = location;
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void onClickScan(View v) {
        //iniciar o scan de qr code
        new IntentIntegrator(this).initiateScan();
    }

    public void onClickMaps(View v) {
        // mostrar google maps com as parada tudo

        for (Local l : this.locations) {
            LatLng lg = new LatLng(l.getLocation().getLatitude(), l.getLocation().getLongitude());
            this.map.addMarker(new MarkerOptions()
                    .title(l.getText())
                    .position(lg));
        }
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
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //guardar o result referente aquela localização
                            MainActivity.this.locations.add(new Local(atual, result));
                        }
                    });
                }
        }
    }
}
