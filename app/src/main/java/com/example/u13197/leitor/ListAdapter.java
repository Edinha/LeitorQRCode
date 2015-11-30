package com.example.u13197.leitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by u13161 on 30/11/2015.
 */
public class ListAdapter extends ArrayAdapter<Local> {
    private List<Local> mLocalList;
    private ListActivity mListener;

    public ListAdapter(ListActivity listener) {
        super(listener, R.layout.activity_list_listitem);
        mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            int res = R.layout.activity_list_listitem;
            convertView = LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        populateViewFromHolder(holder, position);
        return convertView;
    }

    private void populateViewFromHolder(ViewHolder holder, int position) {
        final Local local = mLocalList.get(position);

        holder.mText.setText(local.getText());
        holder.mLat.setText("Latitude: " + String.valueOf(local.getLocation().getLatitude()));
        holder.mLng.setText("Longitude: " + String.valueOf(local.getLocation().getLongitude()));

        holder.mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLocalClick(v, local);
            }
        });
    }

    public void updateLocals(List<Local> localList) {
        mLocalList = localList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mLocalList != null? mLocalList.size() : 0;
    }

    @Override
    public Local getItem(int position) {
        return mLocalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        private View mRoot;
        private TextView mText;
        private TextView mLat;
        private TextView mLng;

        public ViewHolder(View root) {
            mRoot = root;
            mText = (TextView) mRoot.findViewById(R.id.activity_list_listitem_text);
            mLat = (TextView) mRoot.findViewById(R.id.activity_list_listitem_lat);
            mLng = (TextView) mRoot.findViewById(R.id.activity_list_listitem_lng);
        }
    }
}
