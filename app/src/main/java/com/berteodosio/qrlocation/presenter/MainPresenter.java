package com.berteodosio.qrlocation.presenter;

import com.berteodosio.qrlocation.activity.MainActivity;
import com.berteodosio.qrlocation.asynctask.MainAsyncTask;
import com.berteodosio.qrlocation.model.Place;

/**
 * Created by bernardo on 03/12/15.
 */
public class MainPresenter {
    private MainActivity mView;

    public MainPresenter(MainActivity view) {
        mView = view;
    }

    public void savePlace(Place place) {
        new MainAsyncTask(this, mView).execute(place);
    }

    public void onPlaceSaved(Boolean saved) {
        if (saved)
            mView.onPlaceSaved();
        else
            mView.onPlaceFailedSave();
    }
}
