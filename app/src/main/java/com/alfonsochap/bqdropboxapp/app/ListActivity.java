package com.alfonsochap.bqdropboxapp.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alfonsochap.bqdropboxapp.R;
import com.alfonsochap.bqdropboxapp.app.adapter.EpubsAdapter;
import com.alfonsochap.bqdropboxapp.network.DBApi;
import com.alfonsochap.bqdropboxapp.preferences.Preferences;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    DBApi mDBApi;
    DropboxAPI.Account mUserAccount;

    ImageView mImgUserAvatar;
    TextView mTxtUserName;
    TextView mTxtUserEmail;

    ListView mListView;
    EpubsAdapter mAdapter;

    ProgressBar mPrb;

    List<String> path = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mDBApi = DBApi.getInstance(this);

        setUpViews();
        setUpUserInterface();

        navigateTo("");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(path.size() > 1){
            navigateTo(path.size() - 2);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            logoutDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    // Item click listener

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Entry entry = (Entry)adapterView.getItemAtPosition(i);
        if(entry.isDir) navigateTo(entry.path);
    }



    // Navigation methods

    void navigateTo(int index) {
        String folder = path.get(index);
        while(path.size() > index) {
            path.remove(index);
        }
        navigateTo(folder);
    }

    void navigateTo(String folder) {
        path.add(folder);

        new LoadFiles().execute(folder);
    }



    // Session methods

    void logoutDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.logout_alert)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logout();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    void logout() {
        Preferences.removeToken();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }



    // Views initialization

    void setUpViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mImgUserAvatar = (ImageView) findViewById(R.id.img_user_avatar);
        mTxtUserName = (TextView) findViewById(R.id.txt_user_name);
        mTxtUserEmail = (TextView) findViewById(R.id.txt_user_email);

        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new EpubsAdapter(ListActivity.this, new ArrayList<Entry>());
        mPrb = (ProgressBar) findViewById(R.id.prb);

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    void setUpUserInterface() {
        new LoadAccountInfo().execute();
    }



    // AsyncTasks

    class LoadAccountInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mTxtUserName.setText(getString(R.string.loading));
            mTxtUserEmail.setText("");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mUserAccount = mDBApi.api.accountInfo();
            } catch(DropboxException e) {
                Log.v("tag", "Error: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void arg0) {
            if(mUserAccount != null) {
                mTxtUserName.setText(mUserAccount.displayName);
                mTxtUserEmail.setText(mUserAccount.email);
            }
        }
    }

    class LoadFiles extends AsyncTask<String, Void, Void> {

        Entry entry = null;

        @Override
        protected void onPreExecute() {
            mPrb.setVisibility(View.VISIBLE);

            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                entry = mDBApi.api.metadata(params[0], 0, "", true, "");
            } catch(Exception e) {
                Log.v("tag", "Error: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void arg0) {
            mPrb.setVisibility(View.GONE);

            if(entry != null) {
                mAdapter.setItems(entry.contents);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
