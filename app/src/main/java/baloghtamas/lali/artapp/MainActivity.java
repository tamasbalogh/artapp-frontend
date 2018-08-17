package baloghtamas.lali.artapp;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import javax.inject.Inject;
import baloghtamas.lali.artapp.data.Language;
import baloghtamas.lali.artapp.data.PreferencesHelper;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    Button mixed,regular;

    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("ArtApp");

        mixed = findViewById(R.id.mainActivityButtonMixed);
        regular = findViewById(R.id.mainActivityButtonRegular);
        ((ArtApp)getApplication()).getApplicationComponent().inject(this);

        preferencesHelper.setLanguage(Language.ENGLISH);
        ArtApp.log("Language code:" + preferencesHelper.getLanguage());
    }

    public void startButtonOnClick(View v){
        if(!isNetworkAvailable()){
            ArtApp.showSnackBar(findViewById(R.id.mainActivityConstraintLayout), "Network is not available.");
            return;
        }

        if(v.getId() == R.id.mainActivityButtonMixed){
            startActivity(new Intent(this,GameActivity.class));
        }

        if(v.getId() == R.id.mainActivityButtonRegular){
            startActivity(new Intent(this,RegularActivity.class));
        }
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        ArtApp.showSnackBar(findViewById(R.id.mainActivityConstraintLayout),"Click back once again to exit!");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}