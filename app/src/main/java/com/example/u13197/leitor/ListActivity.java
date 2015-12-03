package com.example.u13197.leitor;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListPresenter mPresenter;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Lista");

        mPresenter = new ListPresenter(this);

        mAdapter = new ListAdapter(this);

        ListView listView = (ListView) findViewById(R.id.list_activity_listView);
        listView.setAdapter(mAdapter);

        mPresenter.loadLocalsFromXML();
    }

    // será chamado pelo presenter <?>
    public void displayLocals(List<Local> localList) {
        mAdapter.updateLocals(localList);
        Toast.makeText(this, "Coordenadas carregadas", Toast.LENGTH_SHORT).show();
    }

    public void onLocalClick(View v, Local local) {
        // TODO: exibir mapa
    }
}
