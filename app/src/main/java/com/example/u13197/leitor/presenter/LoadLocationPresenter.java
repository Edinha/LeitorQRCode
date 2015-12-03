package com.example.u13197.leitor.presenter;

import android.content.Context;

import com.example.u13197.leitor.asynctask.LoadLocationAsyncTask;
import com.example.u13197.leitor.model.Local;
import com.example.u13197.leitor.view.DisplayPlacesView;

import java.util.List;

/**
 * Created by u13161 on 30/11/2015.
 */
public class LoadLocationPresenter {

    private DisplayPlacesView mView;
    private Context mContext;

    public LoadLocationPresenter(DisplayPlacesView view, Context context) {
        mView = view;
        mContext = context;
    }

    public void loadLocalsFromXML() {
        new LoadLocationAsyncTask(this).execute(mContext);
    }

    public void onListFailed(Exception e) {

    }

    public void onPlacesLoaded(List<Local> locals) {
        mView.displayLocals(locals);
    }
}
