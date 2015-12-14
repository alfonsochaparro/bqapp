package com.alfonsochap.bqdropboxapp.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alfonsochap.bqdropboxapp.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.dropbox.client2.DropboxAPI.Entry;

import java.util.List;

/**
 * Created by Alfonso on 14/12/2015.
 */
public class EpubsAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Entry> mItems;

    public EpubsAdapter(Activity activity, List<Entry> movieItems) {
        this.activity = activity;
        this.mItems = movieItems;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int location) {
        return mItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.layout_item_list, null);

        TextView txt = (TextView) convertView.findViewById(R.id.txt);

        txt.setText(mItems.get(position).fileName());
        return convertView;
    }

}
