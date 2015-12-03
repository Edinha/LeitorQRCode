package com.berteodosio.qrlocation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.berteodosio.qrlocation.R;
import com.berteodosio.qrlocation.activity.ListActivity;
import com.berteodosio.qrlocation.model.Place;

import java.util.List;

/**
 * Created by u13161 on 30/11/2015.
 */
public class ListAdapter extends ArrayAdapter<Place> {
    private List<Place> mPlaceList;
    private ListActivity mListener;

    public ListAdapter(ListActivity listener) {
        super(listener, R.layout.list_activity_listitem);
        mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            int res = R.layout.list_activity_listitem;
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
        final Place place = mPlaceList.get(position);

        holder.mText.setText(place.getText());
        holder.mLat.setText("Latitude: " + String.valueOf(place.getLocation().getLatitude()));
        holder.mLng.setText("Longitude: " + String.valueOf(place.getLocation().getLongitude()));

        holder.mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLocalClick(v, place);
            }
        });
    }

    public void updateLocals(List<Place> placeList) {
        mPlaceList = placeList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPlaceList != null? mPlaceList.size() : 0;
    }

    @Override
    public Place getItem(int position) {
        return mPlaceList.get(position);
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
