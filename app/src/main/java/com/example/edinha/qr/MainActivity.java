package com.example.edinha.qr;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private TextView txt;
    private Handler handler = new Handler();

    //lista das localizações
    private List<Local> locations;

    //posição atual
    private Location atual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locations = new ArrayList<Local>();
        txt = (TextView) findViewById(R.id.txtScan);

        // pegará a geolocalização do celular, latitude e longitude
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

        // faz as requisições para mudar a variável atual sempre que a posição do celular muda
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void onClickScan(View v) {
        //iniciar o scan de qr code
        new IntentIntegrator(this).initiateScan();
    }

    public void onClickMaps(View v) {
        // mostrar google maps activity
        Intent myIntent = new Intent(this, MapsActivity.class);
        startActivity(myIntent);
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

                if (scanResult == null)
                    return;

                // nao podera escanear se a localizaçao estiver nula
                if (MainActivity.this.atual == null)
                    return;

                final String result = scanResult.getContents();

                if (result != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //guardar o result referente aquela localização
                            Local l = new Local(atual, result);
                            MainActivity.this.locations.add(l);

                            // mostrar no TextView o texto do QR Code
                            MainActivity.this.txt.setText(result);

                            // guardar no XML
                            new MainActivity.Task().execute(l);
                        }
                    });
                }
        }
    }

    private class Task extends AsyncTask<Local, Void, Void> {

        /*
            AsyncTask para guardar aquela localidade em um XML
         */

        @Override
        protected Void doInBackground(Local... params) {
            try {
                XMLManager.writeLocal(params[0], MainActivity.this.getApplicationContext());
            } catch (IOException e) {e.printStackTrace();}

            return null;
        }
    }

}
