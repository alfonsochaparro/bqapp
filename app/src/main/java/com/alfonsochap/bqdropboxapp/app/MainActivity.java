package com.alfonsochap.bqdropboxapp.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alfonsochap.bqdropboxapp.R;
import com.alfonsochap.bqdropboxapp.network.DBApi;
import com.alfonsochap.bqdropboxapp.preferences.Preferences;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

public class MainActivity extends AppCompatActivity {

    private DBApi mDBApi;

    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Preferences.init(getApplicationContext());

        mDBApi = DBApi.getInstance(this);

        mBtnLogin = (Button) findViewById(R.id.btn_login);

        initAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDBApi.api.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.api.getSession().finishAuthentication();
                String accessToken = mDBApi.api.getSession().getOAuth2AccessToken();



                Preferences.setToken(accessToken);

                goForward();

            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    void initAnimation() {
        // TODO


        checkDropBoxSession();
    }

    void checkDropBoxSession() {
        if(Preferences.hasToken()) {
            mDBApi.api.getSession().setOAuth2AccessToken(Preferences.getToken());
            goForward();
        }
        else {
            mBtnLogin.animate().alpha(1f).setDuration(500);
            mBtnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDBApi.api.getSession().startOAuth2Authentication(MainActivity.this);
                }
            });
        }
    }

    void goForward() {
        startActivity(new Intent(this, ListActivity.class));
        finish();
    }
}
