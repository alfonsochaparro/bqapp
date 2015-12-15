package com.alfonsochap.bqdropboxapp.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alfonsochap.bqdropboxapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class DetailsActivity extends AppCompatActivity {

    CollapsingToolbarLayout mToolBarLayout;
    Book mBook;

    TextView mTxtAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initViews();
        readBook();
    }

    void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mToolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mTxtAuthor = (TextView) findViewById(R.id.txtAuthor);
    }

    void readBook() {
        try {
            // find InputStream for book
            InputStream epubInputStream = new FileInputStream(new File(getFilesDir() + "/tmp"));

            // Load Book from inputStream
            mBook = (new EpubReader()).readEpub(epubInputStream);

            // Log the book's title
            mToolBarLayout.setTitle(mBook.getTitle());

            // Log the book's coverimage property
            Bitmap coverImage = BitmapFactory.decodeStream(mBook.getCoverImage().getInputStream());
            mToolBarLayout.setBackgroundDrawable(new BitmapDrawable(coverImage));

            StringBuilder sb = new StringBuilder();
            for(Author author: mBook.getMetadata().getAuthors()) {
                sb.append(author.toString() + ", ");
            }
            if(sb.length() > 0) {
                mTxtAuthor.setText(sb.toString().substring(0, sb.length() - 2));
            }
            // Log the tale of contents
            //mBook.getTableOfContents().getTocReferences()

        } catch (Exception e) {
            Log.e("epublib", e.getMessage());
        }
    }

}
