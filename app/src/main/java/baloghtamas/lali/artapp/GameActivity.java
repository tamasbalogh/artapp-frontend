package baloghtamas.lali.artapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

public class GameActivity extends AppCompatActivity {


    int fragmentCounter = 0;
    int correctAnswerCounter = 0;
    FrameLayout frameLayout;
    AsyncHttpClient asyncHttpClient;
    ProgressBar progressBar;
    JSONArray games;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        frameLayout = findViewById(R.id.gameActivityFrameLayout);
        progressBar = findViewById(R.id.gameActivityProgressBar);


        final String MIXED_URL = "http://172.20.16.134:8798/ArtApp/rest/mix";
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(this, MIXED_URL, new JsonHttpResponseHandler() {
            public void onStart() {
                showProgressBar();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                hideProgressBar();
                frameLayout.setVisibility(View.VISIBLE);
                try {
                    games = response.getJSONArray("games");
                    for (int i = 0; i < games.length(); i++) {
                        ArtApp.log(games.get(i).toString());
                    }
                    showTheProperGameFragment(games.getJSONObject(fragmentCounter).getInt("gametype"), games.getJSONObject(fragmentCounter));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                hideProgressBar();
                ArtApp.log(responseString.toString());
                ArtApp.showSnackBar(findViewById(R.id.gameActivityConstraintLayout), "The following website is not available." + System.getProperty("line.separator") + MIXED_URL);
                asyncHttpClient = null;
                finish();
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
        switch (gametype){
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
        if(answer){
            correctAnswerCounter++;
        }
        try {
            if(fragmentCounter < games.length()){
                ArtApp.log(games.getJSONObject(fragmentCounter).toString());
                showTheProperGameFragment(games.getJSONObject(fragmentCounter).getInt("gametype"), games.getJSONObject(fragmentCounter));
            } else {
                showResultFragment();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showResultFragment(){
        FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.gameActivityFrameLayout, ResultFragment.newInstance(games.length(), correctAnswerCounter), ResultFragment.TAG);
        transaction.commit();
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        progressBar.setVisibility(View.GONE);
    }
}
