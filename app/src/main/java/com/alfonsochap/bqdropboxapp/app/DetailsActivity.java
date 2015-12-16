package com.alfonsochap.bqdropboxapp.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfonsochap.bqdropboxapp.R;
import com.alfonsochap.bqdropboxapp.app.config.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.domain.TableOfContents;
import nl.siegmann.epublib.epub.EpubReader;

public class DetailsActivity extends AppCompatActivity {

    Book mBook;

    CollapsingToolbarLayout mToolBarLayout;

    ImageView mImgHeader;
    ImageView mImgContent;

    TextView mTxtTitle;
    TextView mTxtAuthor;
    TextView mTxtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initViews();
        readBook();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();

        return super.onOptionsItemSelected(item);
    }



    void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBookDialog();
            }
        });

        mImgHeader = (ImageView) findViewById(R.id.imgHeader);
        mImgContent = (ImageView) findViewById(R.id.imgContent);

        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        mTxtAuthor = (TextView) findViewById(R.id.txtAuthor);
        mTxtContent = (TextView) findViewById(R.id.txtContent);
    }

    void readBook() {
        try {
            // find InputStream for book
            InputStream epubInputStream = openFileInput(Constants.FILE_TMP);

            // Load Book from inputStream
            mBook = (new EpubReader()).readEpub(epubInputStream);

            // Log the book's title
            mToolBarLayout.setTitle(mBook.getTitle());
            mTxtTitle.setText(mBook.getTitle());

            // Log the book's coverimage property
            if(mBook.getCoverImage() != null) {
                Bitmap coverImage = BitmapFactory.decodeStream(mBook.getCoverImage()
                        .getInputStream());
                mImgHeader.setImageBitmap(coverImage);
                mImgContent.setImageBitmap(coverImage);
            }

            Metadata metaData = mBook.getMetadata();
            if(metaData != null) {
                StringBuilder sb = new StringBuilder();
                for (Author author : metaData.getAuthors()) {
                    sb.append(author.toString() + ", ");
                }
                if (sb.length() > 0) {
                    mTxtAuthor.setText(sb.toString().substring(0, sb.length() - 2));
                } else {
                    mTxtAuthor.setText(R.string.no_info);
                }

                if(metaData.getDescriptions().size() > 0) {
                    mTxtContent.setText(Html.fromHtml(metaData.getDescriptions().get(0)));
                }
                else {
                    mTxtContent.setText(R.string.no_info);
                }
            }
            else {
                mTxtAuthor.setText(R.string.no_info);
                mTxtContent.setText(R.string.no_info);
            }
        } catch (Exception e) {
            Log.e("epublib", e.getMessage());
        }
    }

    void openBookDialog() {
        try {
            File file = getFileStreamPath(Constants.FILE_TMP);

            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), Constants.FILE_MIME);

            startActivity(Intent.createChooser(intent, getString(R.string.open_with)));
        } catch(Exception e) {
            Snackbar.make(mToolBarLayout, R.string.no_apps, Snackbar.LENGTH_LONG)
                    .show();
        }
    }

}
