package com.example.u13197.leitor;

/**
 * Created by bernardo on 03/12/15.
 */
public class MainPresenter {
    private MainActivity mView;

    public MainPresenter(MainActivity view) {
        mView = view;
    }

    public void savePlace(Local local) {
        new MainAsyncTask(this, mView).execute(local);
    }

    public void onPlaceSaved(Boolean saved) {
        if (saved)
            mView.onPlaceSaved();
        else
            mView.onPlaceFailedSave();
    }
}
