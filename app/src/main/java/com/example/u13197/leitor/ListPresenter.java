package com.example.u13197.leitor;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by u13161 on 30/11/2015.
 */
public class ListPresenter {

    private ListActivity mView;

    public ListPresenter(ListActivity view) {
        mView = view;   // prequiça de fazer interface
    }

    public void loadLocalsFromXML() {
        // TODO: carregar do xml

        new ListAsyncTask(this).execute(mView);
        /*
        // teste
        List<Local> localList = new ArrayList<>();

        Location a = new Location("uhata");
        a.setLatitude(-22);
        a.setLongitude(-30);

        Location b = new Location("uhata");
        b.setLatitude(-20);
        b.setLongitude(-32);

        localList.add(new Local(a, "Location a"));
        localList.add(new Local(b, "Location b"));
        mView.displayLocals(localList);
        */
    }

    public void onListFailed(Exception e) {

    }

    public void onPlacesLoaded(List<Local> locals) {
        mView.displayLocals(locals);
    }
}
