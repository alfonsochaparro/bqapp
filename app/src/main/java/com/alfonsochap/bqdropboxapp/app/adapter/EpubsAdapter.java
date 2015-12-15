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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by Alfonso on 14/12/2015.
 */
public class EpubsAdapter extends BaseAdapter {;
    private Context mContext;
    private LayoutInflater inflater;
    private List<EpubModel> mItems;

    public EpubsAdapter(Context context, List<EpubModel> items) {
        mContext = context;
        mItems = items;
    }

    public void setItems(List<EpubModel> items) {
        mItems = items;
    }

    public void sort() {
        Collections.sort(mItems, new Comparator<EpubModel>() {
            @Override
            public int compare(EpubModel lhs, EpubModel rhs) {
                if(Preferences.getSortMode() == Preferences.SORT_NAME) {
                    String name1 = lhs.getBook() == null ? lhs.getEntry().fileName() : lhs.getBook().getTitle();
                    String name2 = rhs.getBook() == null ? rhs.getEntry().fileName() : rhs.getBook().getTitle();

                    return name1.compareTo(name2);
                }

                return lhs.getEntry().modified.compareTo(rhs.getEntry().modified);
            }
        });
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_item_list, null);

            new LoadBook(convertView, mItems.get(position)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        return convertView;
    }

    class LoadBook extends AsyncTask<Void, Void, Void> {
        View view;
        EpubModel item;

        TextView txt;
        ImageView img;

        Bitmap bmp;

        public LoadBook(View view, EpubModel item) {
            this.view = view;
            this.item = item;
        }

        @Override
        protected void onPreExecute() {
            txt = (TextView) view.findViewById(R.id.txt);
            img = (ImageView) view.findViewById(R.id.img);

            if(item.getBook() == null) {
                txt.setText(item.getEntry().fileName());
                img.setImageResource(item.getEntry().isDir ? R.drawable.folder : R.drawable.epub);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(item.getBook() == null && !item.getEntry().isDir) {
                try {
                    Book book = (new EpubReader()).readEpub(DBApi.getInstance(mContext).api
                            .getFileStream(item.getEntry().path, ""));
                    item.setBook(book);

                    bmp = BitmapFactory.decodeStream(item.getBook().getCoverImage().getInputStream());

                } catch (Exception e) {
                    Log.v("tag", "Error: " + e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void arg0) {
            if(item.getBook() != null) {
                txt.setText(item.getBook().getTitle());

                if(bmp != null) {
                    img.setImageBitmap(bmp);
                }
            }
        }
    }

}
