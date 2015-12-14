package com.alfonsochap.bqdropboxapp.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfonsochap.bqdropboxapp.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.dropbox.client2.DropboxAPI.Entry;

import java.util.List;

/**
 * Created by Alfonso on 14/12/2015.
 */
public class EpubsAdapter extends BaseAdapter {;
    private Context mContext;
    private LayoutInflater inflater;
    private List<Entry> mItems;

    public EpubsAdapter(Context context, List<Entry> items) {
        mContext = context;
        mItems = items;
    }

    public void setItems(List<Entry> items) {
        mItems = items;
    }

    public void clear() {
        mItems.clear();
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
            inflater = LayoutInflater.from(mContext);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.layout_item_list, null);

        TextView txt = (TextView) convertView.findViewById(R.id.txt);
        ImageView img = (ImageView) convertView.findViewById(R.id.img);

        txt.setText(mItems.get(position).fileName());
        img.setImageResource(mItems.get(position).isDir ? R.drawable.folder : R.drawable.epub);
        return convertView;
    }

}
