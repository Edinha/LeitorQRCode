package com.example.u13197.leitor.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.u13197.leitor.model.Local;
import com.example.u13197.leitor.xml.XMLManager;
import com.example.u13197.leitor.presenter.LoadLocationPresenter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * Created by bernardo on 03/12/15.
 */
public class LoadLocationAsyncTask extends AsyncTask<Context, Void, List<Local>> {
    private LoadLocationPresenter mPresenter;

    public LoadLocationAsyncTask(LoadLocationPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected List<Local> doInBackground(Context... contexts) {
        try {
            return XMLManager.readAll(contexts[0]);
        } catch (XmlPullParserException | IOException e) {
            mPresenter.onListFailed(e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Local> locals) {
        if (locals != null)
            mPresenter.onPlacesLoaded(locals);
    }
}
