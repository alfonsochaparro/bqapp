package com.alfonsochap.bqdropboxapp.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfonsochap.bqdropboxapp.R;
import com.alfonsochap.bqdropboxapp.network.DBApi;
import com.alfonsochap.bqdropboxapp.app.config.Preferences;

public class MainActivity extends AppCompatActivity {

    private DBApi mDBApi;


    ImageView mImgLogo;
    View loginForm;
    TextView mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        Preferences.init(getApplicationContext());

        mDBApi = DBApi.getInstance(this);

        mImgLogo = (ImageView) findViewById(R.id.imgLogo);
        loginForm = findViewById(R.id.loginForm);
        mBtnLogin = (TextView) findViewById(R.id.btnLogin);

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
        mImgLogo.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkDropBoxSession();
            }
        }, 2000);
    }

    void checkDropBoxSession() {
        if(Preferences.hasToken()) {
            mDBApi.api.getSession().setOAuth2AccessToken(Preferences.getToken());
            goForward();
        }
        else {
            mImgLogo.animate().scaleX(0.9f).scaleY(0.9f).setInterpolator(new AccelerateInterpolator()).setDuration(150);
            loginForm.setVisibility(View.VISIBLE);
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
