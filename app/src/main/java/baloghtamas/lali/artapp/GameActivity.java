package baloghtamas.lali.artapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import baloghtamas.lali.artapp.data.PreferencesHelper;
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

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int RESULT_CODE_ON_FAILURE = 1;
    public static final int RESULT_CODE_SAVED_INSTANCE_STATE = 2;
    public static final int RESULT_CODE_GAMES_LENGTH_NULL = 3;

    @Inject
    PreferencesHelper preferencesHelper;

    private boolean doubleBackToExitPressedOnce = false;
    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient(true,80,9443);
    private int fragmentCounter = 0;
    private float correctAnswerCounter = 0;
    private float wrongAnswerCounter = 0;
    private FrameLayout frameLayout;
    private ProgressBar progressBar;
    private JSONArray games;

    private Button start;
    private Spinner lesson, level;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ArtApp)getApplication()).getApplicationComponent().inject(this);

        Locale locale = new Locale(preferencesHelper.getLanguage().getCode());
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_game);

        frameLayout = findViewById(R.id.gameActivityFrameLayout);
        progressBar = findViewById(R.id.gameActivityProgressBar);
        layout = findViewById(R.id.gameActivityConstraintLayout);


        if(preferencesHelper.getCurrentGameType().equals(ArtApp.MIXED_GAME)) {
            if(savedInstanceState == null) {
                final String url = ArtApp.SERVER_ADDRESS + "mix";

                RequestParams body = new RequestParams();
                body.add("auth","yTd0Eq6YzDDVQZBL");
                body.add("language",preferencesHelper.getLanguage().getCode());

                asyncHttpClient.post(this, url, body ,new JsonHttpResponseHandler(){

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressBar.setVisibility(View.VISIBLE);
                        ArtApp.log(url);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        progressBar.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                        try {
                            games = response.getJSONArray("games");
                            for (int i = 0; i < games.length(); i++) {
                                ArtApp.log(i + " - " +games.getJSONObject(i).getString("gametype"));
                            }

                            if (games.length() == 0 ) {
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("onSuccess",getString(R.string.mixed_no_games));
                                setResult(RESULT_CODE_GAMES_LENGTH_NULL,returnIntent);
                                finish();
                            }

                            ArtApp.log("Mixed - Games length: " + games.length());
                            showTheProperGameFragment(games.getJSONObject(fragmentCounter).getInt("gametype"), games.getJSONObject(fragmentCounter));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressBar.setVisibility(View.GONE);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("onFailure",getString(R.string.server_is_not_available));
                        setResult(RESULT_CODE_ON_FAILURE,returnIntent);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        progressBar.setVisibility(View.GONE);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("onFailure",getString(R.string.server_is_not_available));
                        setResult(RESULT_CODE_ON_FAILURE,returnIntent);
                        finish();
                    }



                    @Override
                    public void onCancel() {
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

        if(preferencesHelper.getCurrentGameType().equals(ArtApp.REGULAR_GAME)) {

            lesson = findViewById(R.id.gameActivityLessonSpinner);
            lesson.setVisibility(View.VISIBLE);
            level = findViewById(R.id.gameActivityLevelSpinner);
            level.setVisibility(View.VISIBLE);
            start = findViewById(R.id.gameActivityStartButton);
            start.setVisibility(View.VISIBLE);


            String lessonArray[] = getResources().getStringArray(R.array.lesson);
            ArrayAdapter<String> lessonArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, lessonArray);
            lessonArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lesson.setAdapter(lessonArrayAdapter);

            String levelArray[] = getResources().getStringArray(R.array.level);
            ArrayAdapter<String> levelArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, levelArray);
            levelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            level.setAdapter(levelArrayAdapter);

            start.setOnClickListener(this);
        }

    }

    private void showTheProperGameFragment(int gametype, JSONObject game) {
        FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        switch (gametype) {
            case 1:
                transaction.replace(R.id.gameActivityFrameLayout, Game1Fragment.newInstance(game), Game1Fragment.TAG);
                getSupportActionBar().setTitle(R.string.combine_colors_and_definitions);
                break;
            case 2:
                transaction.replace(R.id.gameActivityFrameLayout, Game2Fragment.newInstance(game), Game2Fragment.TAG);
                getSupportActionBar().setTitle(R.string.combine_number_and_words);
                break;
            case 3:
                transaction.replace(R.id.gameActivityFrameLayout, Game3Fragment.newInstance(game), Game3Fragment.TAG);
                getSupportActionBar().setTitle(R.string.construct_the_correct_sentence);
                break;
            case 4:
                transaction.replace(R.id.gameActivityFrameLayout, Game4Fragment.newInstance(game), Game4Fragment.TAG);
                getSupportActionBar().setTitle(R.string.syntactic_exercise);
                break;
            case 5:
                transaction.replace(R.id.gameActivityFrameLayout, Game5Fragment.newInstance(game), Game5Fragment.TAG);
                getSupportActionBar().setTitle(R.string.find_the_correct_sentence);
                break;
            case 6:
                transaction.replace(R.id.gameActivityFrameLayout, Game6Fragment.newInstance(game), Game6Fragment.TAG);
                getSupportActionBar().setTitle(R.string.find_the_antonym);
                break;
            case 7:
                transaction.replace(R.id.gameActivityFrameLayout, Game7Fragment.newInstance(game), Game7Fragment.TAG);
                getSupportActionBar().setTitle(R.string.choose_the_correct_sentence);
                break;
            case 8:
                transaction.replace(R.id.gameActivityFrameLayout, Game8Fragment.newInstance(game), Game8Fragment.TAG);
                getSupportActionBar().setTitle(R.string.true_or_false);
                break;
            case 9:
                transaction.replace(R.id.gameActivityFrameLayout, Game9Fragment.newInstance(game), Game9Fragment.TAG);
                getSupportActionBar().setTitle(R.string.find_the_odd_one_out);
                break;
            case 10:
                transaction.replace(R.id.gameActivityFrameLayout, Game10Fragment.newInstance(game), Game10Fragment.TAG);
                getSupportActionBar().setTitle(R.string.pair_the_title_image_and_text);
                break;
            case 11:
                transaction.replace(R.id.gameActivityFrameLayout, Game11Fragment.newInstance(game), Game11Fragment.TAG);
                getSupportActionBar().setTitle(R.string.find_the_correct_picture);
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
        getSupportActionBar().setTitle(R.string.result);
        transaction.replace(R.id.gameActivityFrameLayout,
                ResultFragment.newInstance(correctAnswerCounter, wrongAnswerCounter), ResultFragment.TAG);
        transaction.commit();
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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.gameActivityStartButton){

            String lessonValue = lesson.getSelectedItem().toString();
            String levelValue = level.getSelectedItem().toString();

            if (lessonValue.equals(getResources().getStringArray(R.array.lesson)[0])){
                ArtApp.showSnackBar(findViewById(R.id.gameActivityConstraintLayout),getString(R.string.select_lesson));
                return;
            }

            if (levelValue.equals(getResources().getStringArray(R.array.level)[0])){
                ArtApp.showSnackBar(findViewById(R.id.gameActivityConstraintLayout),getString(R.string.select_level));
                return;
            }

            final String levelString ;
            if(levelValue.equals(getResources().getStringArray(R.array.level)[1])){
                levelString = "1";
            } else {
                levelString = "2";
            }
            final String url = ArtApp.SERVER_ADDRESS + "regular";

            RequestParams body = new RequestParams();
            body.add("auth","yTd0Eq6YzDDVQZBL");
            body.add("language",preferencesHelper.getLanguage().getCode());
            if(preferencesHelper.getLanguage().getCode().equals("en")){
                HashMap<String, Integer> lessons = new HashMap<>();
                for (int i = 0; i < getResources().getStringArray(R.array.lesson).length; i++) {
                    lessons.put(getResources().getStringArray(R.array.lesson)[i],(i));
                }
                body.add("lesson",lessons.get(lessonValue).toString());
            } else {
                body.add("lesson",lessonValue.split(" ")[1]);
            }
            body.add("level",levelString);



            asyncHttpClient.post(this, url, body, new JsonHttpResponseHandler(){
                @Override
                public void onStart() {
                    super.onStart();
                    level.setVisibility(View.GONE);
                    lesson.setVisibility(View.GONE);
                    start.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    ArtApp.log(url);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    progressBar.setVisibility(View.GONE);
                    start.setVisibility(View.GONE);
                    lesson.setVisibility(View.GONE);
                    level.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);

                    try {
                        games = response.getJSONArray("games");
                        if (games.length() == 0 ) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("onSuccess",getString(R.string.regular_no_games));
                            setResult(RESULT_CODE_GAMES_LENGTH_NULL,returnIntent);
                            finish();
                        }

                        ArtApp.log("Regular - Games length: " + games.length());
                        showTheProperGameFragment(games.getJSONObject(fragmentCounter).getInt("gametype"), games.getJSONObject(fragmentCounter));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("onFailure",getString(R.string.server_is_not_available));
                    setResult(RESULT_CODE_ON_FAILURE,returnIntent);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("onFailure",getString(R.string.server_is_not_available));
                    setResult(RESULT_CODE_ON_FAILURE,returnIntent);
                    finish();
                }

                @Override
                public void onCancel() {
                    finish();

                }
            });
        }
    }
}
