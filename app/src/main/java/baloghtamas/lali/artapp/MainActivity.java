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

import javax.inject.Inject;
import baloghtamas.lali.artapp.data.Language;
import baloghtamas.lali.artapp.data.PreferencesHelper;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button first,second,third;
    AsyncHttpClient asyncHttpClient;


    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("ArtApp");

        progressBar = findViewById(R.id.mainActivityProgressBar);
        first = findViewById(R.id.mainActivityButtonLocalData);
        second = findViewById(R.id.mainActivityButtonMixedBackend);
        third = findViewById(R.id.mainActivityButtonRegularBackend);
        ((ArtApp)getApplication()).getApplicationComponent().inject(this);

        preferencesHelper.setLanguage(Language.ENGLISH);
        ArtApp.log("Language code:" + preferencesHelper.getLanguage());
    }

    public void startButtonOnClick(View v){
        if(v.getId() == R.id.mainActivityButtonLocalData){
            startActivity(new Intent(this,GameActivity.class));
        }

        if(v.getId() == R.id.mainActivityButtonMixedBackend){
            if(!isNetworkAvailable()){
                ArtApp.showSnackBar(findViewById(R.id.mainActivityConstraintLayout), "Network is not available.");
                return;
            }
            final String MIXED_URL = "http://172.20.16.134:8798/ArtApp/rest/mix";
            asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.setMaxRetriesAndTimeout(3,500);
            asyncHttpClient.get(this, MIXED_URL, new JsonHttpResponseHandler() {
                public void onStart() {
                    showProgressBar();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    hideProgressBar();
                    ArtApp.showSnackBar(findViewById(R.id.mainActivityConstraintLayout), "Success");
                    try {
                        JSONArray games = response.getJSONArray("games");
                        for (int i = 0; i < games.length(); i++) {
                            ArtApp.log(games.getJSONObject(i).toString());
                        }
                        Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                        intent.putExtra("games",games.toString());
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    hideProgressBar();
                    ArtApp.log(responseString.toString());
                    ArtApp.showSnackBar(findViewById(R.id.mainActivityConstraintLayout), "The following website is not available." + System.getProperty("line.separator") + MIXED_URL);
                    asyncHttpClient = null;
                }

                @Override
                public void onCancel() {
                    hideProgressBar();
                }
            });
        }

        if(v.getId() == R.id.mainActivityButtonRegularBackend){
            startActivity(new Intent(this,RegularActivity.class));
        }

    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        first.setClickable(false);
        second.setClickable(false);
        third.setClickable(false);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
        first.setClickable(true);
        second.setClickable(true);
        third.setClickable(true);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if( asyncHttpClient != null){
            asyncHttpClient.cancelAllRequests(true);
            asyncHttpClient = null;
            return;
        }

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
