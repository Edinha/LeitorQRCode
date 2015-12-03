package com.berteodosio.qrlocation.presenter;

import android.content.Context;

import com.berteodosio.qrlocation.asynctask.LoadLocationAsyncTask;
import com.berteodosio.qrlocation.view.DisplayPlacesView;
import com.berteodosio.qrlocation.model.Local;

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
