package com.example.u13197.leitor;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Created by bernardo on 03/12/15.
 */
public class MainAsyncTask extends AsyncTask<Local, Void, Boolean> {
    private MainPresenter mPresenter;
    private Context mContext;

    public MainAsyncTask(MainPresenter presenter, Context context) {
        mPresenter = presenter;
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(Local... params) {
        try {
            XMLManager.writeLocal(params[0], mContext);
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
