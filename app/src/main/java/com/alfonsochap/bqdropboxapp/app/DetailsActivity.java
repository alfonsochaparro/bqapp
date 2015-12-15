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

import com.alfonsochap.bqdropboxapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

public class DetailsActivity extends AppCompatActivity {

    CollapsingToolbarLayout mToolBarLayout;
    Book mBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
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

        readBook();
    }

    void readBook() {
        try {
            // find InputStream for book
            InputStream epubInputStream = new FileInputStream(new File(getFilesDir() + "/tmp"));

            // Load Book from inputStream
            mBook = (new EpubReader()).readEpub(epubInputStream);

            // Log the book's authors
            //Log.i("epublib", "author(s): " + book.getMetadata().getAuthors());

            // Log the book's title
            mToolBarLayout.setTitle(mBook.getTitle());


            // Log the book's coverimage property

            Bitmap coverImage = BitmapFactory.decodeStream(mBook.getCoverImage().getInputStream());
            getSupportActionBar().setBackgroundDrawable(new BitmapDrawable(coverImage));


            // Log the tale of contents

            //logTableOfContents(book.getTableOfContents().getTocReferences(), 0);

        } catch (Exception e) {
            Log.e("epublib", e.getMessage());
        }
    }

}
