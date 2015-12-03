package com.example.u13197.leitor;

import android.content.Context;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * Created by bernardo on 03/12/15.
 */
public class ListAsyncTask extends AsyncTask<Context, Void, List<Local>> {
    private ListPresenter mPresenter;

    public ListAsyncTask(ListPresenter presenter) {
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
