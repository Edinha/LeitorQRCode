package com.example.u13197.leitor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.u13197.leitor.adapter.ListAdapter;
import com.example.u13197.leitor.presenter.LoadLocationPresenter;
import com.example.u13197.leitor.model.Local;
import com.example.u13197.leitor.R;
import com.example.u13197.leitor.view.DisplayPlacesView;

import java.util.List;

public class ListActivity extends AppCompatActivity implements DisplayPlacesView {

    private LoadLocationPresenter mPresenter;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Lista");

        mPresenter = new LoadLocationPresenter(this, this);

        mAdapter = new ListAdapter(this);

        ListView listView = (ListView) findViewById(R.id.list_activity_listView);
        listView.setAdapter(mAdapter);

        mPresenter.loadLocalsFromXML();
    }

    // será chamado pelo presenter <?>
    @Override
    public void displayLocals(List<Local> localList) {
        mAdapter.updateLocals(localList);
        Toast.makeText(this, "Coordenadas carregadas", Toast.LENGTH_SHORT).show();
    }

    public void onLocalClick(View v, Local local) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.EXTRA_LOCAL_LOCATION, local.getLocation());
        intent.putExtra(MapActivity.EXTRA_LOCAL_TEXT, local.getText());
        startActivity(intent);
    }
}
