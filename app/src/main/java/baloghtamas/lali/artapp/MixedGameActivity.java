package baloghtamas.lali.artapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

public class MixedGameActivity extends AppCompatActivity {

    public static final int RESULT_CODE_ON_FAILURE = 1;
    public static final int RESULT_CODE_SAVED_INSTANCE_STATE = 2;

    boolean doubleBackToExitPressedOnce = false;
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient(true,80,9443);
    int fragmentCounter = 0;
    float correctAnswerCounter = 0;
    float wrongAnswerCounter = 0;
    FrameLayout frameLayout;
    ProgressBar progressBar;
    JSONArray games;
    //final String MIXED_URL = "http://172.20.16.134:8798/ArtApp/rest/mix";
    final String MIXED_URL = "https://172.20.16.133:9443/ArtApp/rest/mix";
    ConstraintLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        frameLayout = findViewById(R.id.gameActivityFrameLayout);
        progressBar = findViewById(R.id.gameActivityProgressBar);
        layout = findViewById(R.id.gameActivityConstraintLayout);

        if(savedInstanceState == null) {
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

                        //ResultFragment teszteléshez
                        //showResultFragment();

                        //Egyedi fragment teszteléshez
                        /*for (int i = 0; i < games.length(); i++) {
                            if(games.getJSONObject(i).getInt("gametype") == 1) {
                                showTheProperGameFragment(games.getJSONObject(i).getInt("gametype"), games.getJSONObject(i));
                            }
                        }*/

                        //Rendes működéshez törölni az előzőeket
                        showTheProperGameFragment(games.getJSONObject(fragmentCounter).getInt("gametype"), games.getJSONObject(fragmentCounter));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    hideProgressBar();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("onFailure",getString(R.string.server_is_not_available));
                    setResult(RESULT_CODE_ON_FAILURE,returnIntent);
                    finish();
                }

                @Override
                public void onCancel() {
                    hideProgressBar();
                    finish();
                }
            });

        } else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("onSavedInstanceState",getString(R.string.the_game_was_broken));
            setResult(RESULT_CODE_SAVED_INSTANCE_STATE,returnIntent);
            finish();
        }


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

    public void changeFragment(float correct, float wrong) {
        correctAnswerCounter += correct;
        wrongAnswerCounter +=wrong;
        ArtApp.log("Answers correct: " + correctAnswerCounter + ", wrong: " + wrongAnswerCounter);
        try {
            if (fragmentCounter < games.length()) {
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
        correctAnswerCounter = 8;
        wrongAnswerCounter = 5;
        transaction.replace(R.id.gameActivityFrameLayout,
                ResultFragment.newInstance(correctAnswerCounter, wrongAnswerCounter), ResultFragment.TAG);
        transaction.commit();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        ArtApp.showSnackBar(findViewById(R.id.gameActivityConstraintLayout),getString(R.string.press_back_once_again_to_exit));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void reloadFragment(){
        fragmentCounter--;
        try {
            showTheProperGameFragment(games.getJSONObject(fragmentCounter).getInt("gametype"), games.getJSONObject(fragmentCounter));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
