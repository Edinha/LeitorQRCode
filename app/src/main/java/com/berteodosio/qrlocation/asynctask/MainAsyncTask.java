package com.berteodosio.qrlocation.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.berteodosio.qrlocation.model.Place;
import com.berteodosio.qrlocation.xml.FileManager;
import com.berteodosio.qrlocation.presenter.MainPresenter;

import java.io.IOException;

/**
 * Created by bernardo on 03/12/15.
 */
public class MainAsyncTask extends AsyncTask<Place, Void, Boolean> {
    private MainPresenter mPresenter;
    private Context mContext;

    public MainAsyncTask(MainPresenter presenter, Context context) {
        mPresenter = presenter;
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(Place... params) {
        try {
            FileManager.write(params[0], mContext);
        } catch (IOException e) {
            return false; // TODO melhorar tratamento desse erro
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean saved) {
        mPresenter.onPlaceSaved(saved);
    }
}
