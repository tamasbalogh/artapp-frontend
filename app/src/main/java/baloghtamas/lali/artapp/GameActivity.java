package baloghtamas.lali.artapp;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.TooManyListenersException;
import java.util.jar.Attributes;

import baloghtamas.lali.artapp.fragments.Game10Fragment;
import baloghtamas.lali.artapp.fragments.Game11Fragment;
import baloghtamas.lali.artapp.fragments.Game1Fragment;
import baloghtamas.lali.artapp.fragments.Game2Fragment;
import baloghtamas.lali.artapp.fragments.Game3Fragment;
import baloghtamas.lali.artapp.fragments.Game4Fragment;
import baloghtamas.lali.artapp.fragments.Game5Fragment;
import baloghtamas.lali.artapp.fragments.Game6Fragment;
import baloghtamas.lali.artapp.fragments.Game7Fragment;
import baloghtamas.lali.artapp.fragments.Game8Fragment;
import baloghtamas.lali.artapp.fragments.Game9Fragment;
import baloghtamas.lali.artapp.fragments.ResultFragment;
import cz.msebera.android.httpclient.Header;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameActivity extends AppCompatActivity {

    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    int fragmentCounter = 0;
    int correctAnswerCounter = 0;
    FrameLayout frameLayout;
    ProgressBar progressBar;
    JSONArray games;
    final String MIXED_URL = "http://172.20.16.134:8798/ArtApp/rest/mix";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        frameLayout = findViewById(R.id.gameActivityFrameLayout);
        progressBar = findViewById(R.id.gameActivityProgressBar);
        asyncHttpClient.get(this, MIXED_URL, new JsonHttpResponseHandler(){

            @Override
            public void onStart() {
                super.onStart();
                showProgressBar();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                hideProgressBar();
                frameLayout.setVisibility(View.VISIBLE);

                try {
                    games = response.getJSONArray("games");
                    ArtApp.log("games length: " + games.length());
                    for (int i = 0; i < games.length(); i++) {
                        if(games.isNull(i)) {
                            ArtApp.log(i + " is null.");
                        } else {
                            ArtApp.log(i + " - gametype: " + games.getJSONObject(i).getString("gametype"));
                        }
                    }
                    showTheProperGameFragment(games.getJSONObject(fragmentCounter).getInt("gametype"), games.getJSONObject(fragmentCounter));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideProgressBar();
                finish();
                ArtApp.showSnackBar(findViewById(R.id.mainActivityConstraintLayout), "The backend is not available.");
            }

            @Override
            public void onCancel() {
                hideProgressBar();
                finish();
            }
        });
    }




    private void showTheProperGameFragment(int gametype, JSONObject game) {
        FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        switch (gametype) {
            case 1:
                transaction.replace(R.id.gameActivityFrameLayout, Game1Fragment.newInstance(game), Game1Fragment.TAG);
                break;
            case 2:
                transaction.replace(R.id.gameActivityFrameLayout, Game2Fragment.newInstance(game), Game2Fragment.TAG);
                break;
            case 3:
                transaction.replace(R.id.gameActivityFrameLayout, Game3Fragment.newInstance(game), Game3Fragment.TAG);
                break;
            case 4:
                transaction.replace(R.id.gameActivityFrameLayout, Game4Fragment.newInstance(game), Game4Fragment.TAG);
                break;
            case 5:
                transaction.replace(R.id.gameActivityFrameLayout, Game5Fragment.newInstance(game), Game5Fragment.TAG);
                break;
            case 6:
                transaction.replace(R.id.gameActivityFrameLayout, Game6Fragment.newInstance(game), Game6Fragment.TAG);
                break;
            case 7:
                transaction.replace(R.id.gameActivityFrameLayout, Game7Fragment.newInstance(game), Game7Fragment.TAG);
                break;
            case 8:
                transaction.replace(R.id.gameActivityFrameLayout, Game8Fragment.newInstance(game), Game8Fragment.TAG);
                break;
            case 9:
                transaction.replace(R.id.gameActivityFrameLayout, Game9Fragment.newInstance(game), Game9Fragment.TAG);
                break;
            case 10:
                transaction.replace(R.id.gameActivityFrameLayout, Game10Fragment.newInstance(game), Game10Fragment.TAG);
                break;
            case 11:
                transaction.replace(R.id.gameActivityFrameLayout, Game11Fragment.newInstance(game), Game11Fragment.TAG);
                break;
        }
        fragmentCounter++;
        transaction.commit();
    }

    public void changeFragment(boolean answer) {
        if (answer) {
            correctAnswerCounter++;
        }
        try {
            if (fragmentCounter < games.length()) {
                //ArtApp.log(games.getJSONObject(fragmentCounter).toString());
                showTheProperGameFragment(games.getJSONObject(fragmentCounter).getInt("gametype"), games.getJSONObject(fragmentCounter));
            } else {
                showResultFragment();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showResultFragment() {
        FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.gameActivityFrameLayout, ResultFragment.newInstance(games.length(), correctAnswerCounter), ResultFragment.TAG);
        transaction.commit();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }


}
