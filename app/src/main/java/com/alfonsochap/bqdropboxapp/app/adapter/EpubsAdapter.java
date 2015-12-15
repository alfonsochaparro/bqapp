package com.alfonsochap.bqdropboxapp.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfonsochap.bqdropboxapp.R;
import com.alfonsochap.bqdropboxapp.app.model.EpubModel;
import com.alfonsochap.bqdropboxapp.network.DBApi;
import com.alfonsochap.bqdropboxapp.preferences.Preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by Alfonso on 14/12/2015.
 */
public class EpubsAdapter extends BaseAdapter {;
    private Context mContext;
    private LayoutInflater inflater;
    private List<EpubModel> mItems;

    private int mViewMode;
    private int mFolderIcon;
    private int mEpubIcon;

    private SimpleDateFormat mDateFormatInput = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
    private SimpleDateFormat mDateFormatOutput = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public EpubsAdapter(Context context, List<EpubModel> items) {
        mContext = context;
        mItems = items;

        updateViewMode();
    }

    public void setItems(List<EpubModel> items) {
        mItems.clear();
        mItems.addAll(items);
    }

    public List<EpubModel> getItems() {
        return mItems;
    }

    public void clear() {
        mItems.clear();
    }

    public void updateViewMode() {
        mViewMode = Preferences.getViewMode();

        mFolderIcon = mViewMode == Preferences.VIEW_LIST ?
                R.drawable.folder : R.drawable.folder_big;

        mEpubIcon = mViewMode == Preferences.VIEW_LIST ?
                R.drawable.epub : R.drawable.epub_big;
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
        if (convertView == null || ((Integer)convertView.getTag()) != mViewMode) {
            convertView = inflater.inflate(mViewMode == Preferences.VIEW_LIST ?
                    R.layout.layout_item_list : R.layout.layout_item_grid, parent, false);

            convertView.setTag(mViewMode);

            new LoadBook(convertView, mItems.get(position))
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            fillView(convertView, mItems.get(position));
        }
        return convertView;
    }

    private void fillView(View view, EpubModel item) {
        TextView txt = (TextView) view.findViewById(R.id.txt);
        TextView txt2 = (TextView) view.findViewById(R.id.txt2);
        ImageView img = (ImageView) view.findViewById(R.id.img);

        if(item.getBook() == null) {
            // Showing metadata info if book data has not been downloaded

            txt.setText(item.getEntry().fileName());
            img.setImageResource(item.getEntry().isDir ? mFolderIcon : mEpubIcon);
        }
        else {
            txt.setText(item.getBook().getTitle());
            try {
                Bitmap bmp = BitmapFactory.decodeStream(item.getBook().getCoverImage().getInputStream());
                img.setImageBitmap(bmp);
            } catch (Exception e) {
                img.setImageResource(mEpubIcon);
            }
        }

        try {
            txt2.setText(mDateFormatOutput.format(mDateFormatInput.parse(item.getEntry().modified)));
        } catch (ParseException e) {
            Log.v("tag", "Error: " + e.getMessage());
        }
    }

    class LoadBook extends AsyncTask<Void, Void, Void> {
        View view;
        EpubModel item;

        public LoadBook(View view, EpubModel item) {
            this.view = view;
            this.item = item;
        }

        @Override
        protected void onPreExecute() {
            fillView(view, item);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if(item.getBook() == null && !item.getEntry().isDir) {
                    Book book = (new EpubReader()).readEpub(DBApi.getInstance(mContext).api
                            .getFileStream(item.getEntry().path, ""));
                    item.setBook(book);
                }
            } catch (Exception e) {
                Log.v("tag", "Error: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void arg0) {
            if(item.getBook() != null) {
                fillView(view, item);
            }
        }
    }

}
