package com.example.edinha.qr;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

public class MapsActivity extends FragmentActivity {

    // variável do Google maps, onde serão colocadas as markers
    private GoogleMap mMap;

    // lista de localizações onde os QR Codes foram lidos
    private List<Local> locations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // inicia a AsyncTask para ler do XML
        new Task().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        while(this.locations == null){}

        // para cada localidade, coloca uma marker no mapa
        for (Local l : this.locations) {
            LatLng lg = new LatLng(l.getLocation().getLatitude(), l.getLocation().getLongitude());
            this.mMap.addMarker(new MarkerOptions()
                    .title(l.getText())
                    .position(lg));
        }

        // para mover a camera para onde estão as markers
        CameraPosition camPos = new CameraPosition.Builder()
                .target(new LatLng(this.locations.get(0).getLocation().getLatitude(),
                        this.locations.get(0).getLocation().getLongitude()))
                .zoom(18)
                .bearing(this.locations.get(0).getLocation().getBearing())
                .tilt(70)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.moveCamera(camUpd3);
    }

    private class Task extends AsyncTask<Void,Void, Void> {

        /*
            É preciso um AsyncTak para qualquer processamento envolvendo XML,
            seja escrita ou leitura, este é o de leitura
         */

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //seta a variável da classe para controle das localizações
                MapsActivity.this.locations = XMLManager.readAll(MapsActivity.this.getApplicationContext());
            } catch (Exception e) {e.printStackTrace(); }

            MapsActivity.this.setUpMapIfNeeded();
            return null;
        }
    }
}
