package baloghtamas.lali.artapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class RegularGameActivity extends AppCompatActivity implements View.OnClickListener{


    private Button start;
    private Spinner lesson, level;
    private FrameLayout frameLayout;
    private ProgressBar progressBar;
    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient(true,80,9443);
    boolean doubleBackToExitPressedOnce = false;
    private JSONArray games;
    private int fragmentCounter = 0;
    private float correctAnswerCounter = 0;
    private float wrongAnswerCounter = 0;

    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_game);

        ((ArtApp)getApplication()).getApplicationComponent().inject(this);

        frameLayout = findViewById(R.id.regularGameActivityFrameLayout);
        progressBar = findViewById(R.id.regularGameActivityProgressBar);
        start = findViewById(R.id.regularGameActivityStartButton);
        start.setOnClickListener(this);

        String lessonArray[] = getResources().getStringArray(R.array.lesson);
        ArrayAdapter<String> lessonArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, lessonArray);
        lessonArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lesson = findViewById(R.id.regularGameActivityLessonSpinner);
        lesson.setAdapter(lessonArrayAdapter);

        String levelArray[] = getResources().getStringArray(R.array.level);
        ArrayAdapter<String> levelArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, levelArray);
        levelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level = findViewById(R.id.regularGameActivityLevelSpinner);
        level.setAdapter(levelArrayAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.regularGameActivityStartButton){

            String lessonValue = lesson.getSelectedItem().toString();
            String levelValue = level.getSelectedItem().toString();

            if (lessonValue.equals(getResources().getStringArray(R.array.lesson)[0])){
                ArtApp.showSnackBar(findViewById(R.id.regularActivityConstraintLayout),getString(R.string.select_lesson));
                return;
            }

            if (levelValue.equals(getResources().getStringArray(R.array.level)[0])){
                ArtApp.showSnackBar(findViewById(R.id.regularActivityConstraintLayout),getString(R.string.select_level));
                return;
            }

            String language = "language=" + preferencesHelper.getLanguage().getCode();
            String lesson = "lesson=" + lessonValue.split(" ")[1];
            String level ;
            if(levelValue.equals(getResources().getStringArray(R.array.level)[1])){
                level = "level=1";
            } else {
                level = "level=2";
            }
            final String url = "https://172.20.16.133:9443/ArtApp/custom?" + language + "&" + lesson + "&" + level;

            asyncHttpClient.get(this, url, new JsonHttpResponseHandler(){
                @Override
                public void onStart() {
                    super.onStart();
                    preferencesHelper.setCurrentGameType(ArtApp.REGULAR_GAME);
                    showProgressBar(url);
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

                    /*Intent returnIntent = new Intent();
                    returnIntent.putExtra("onFailure",getString(R.string.server_is_not_available));
                    setResult(RESULT_CODE_ON_FAILURE,returnIntent);
                    finish();*/
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    hideProgressBar();

                    /*Intent returnIntent = new Intent();
                    returnIntent.putExtra("onFailure",getString(R.string.server_is_not_available));
                    setResult(RESULT_CODE_ON_FAILURE,returnIntent);
                    finish();*/
                }



                @Override
                public void onCancel() {
                    hideProgressBar();
                    finish();
                }
            });
        }
    }

    private void showTheProperGameFragment(int gametype, JSONObject game) {
        FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        switch (gametype) {
            case 1:
                transaction.replace(R.id.regularGameActivityFrameLayout, Game1Fragment.newInstance(game), Game1Fragment.TAG);
                getSupportActionBar().setTitle(R.string.combine_colors_and_definitions);
                break;
            case 2:
                transaction.replace(R.id.regularGameActivityFrameLayout, Game2Fragment.newInstance(game), Game2Fragment.TAG);
                getSupportActionBar().setTitle(R.string.combine_number_and_words);
                break;
            case 3:
                transaction.replace(R.id.regularGameActivityFrameLayout, Game3Fragment.newInstance(game), Game3Fragment.TAG);
                getSupportActionBar().setTitle(R.string.construct_the_correct_sentence);
                break;
            case 4:
                transaction.replace(R.id.regularGameActivityFrameLayout, Game4Fragment.newInstance(game), Game4Fragment.TAG);
                getSupportActionBar().setTitle(R.string.syntactic_exercise);
                break;
            case 5:
                transaction.replace(R.id.regularGameActivityFrameLayout, Game5Fragment.newInstance(game), Game5Fragment.TAG);
                getSupportActionBar().setTitle(R.string.find_the_correct_sentence);
                break;
            case 6:
                transaction.replace(R.id.regularGameActivityFrameLayout, Game6Fragment.newInstance(game), Game6Fragment.TAG);
                getSupportActionBar().setTitle(R.string.find_the_antonym);
                break;
            case 7:
                transaction.replace(R.id.regularGameActivityFrameLayout, Game7Fragment.newInstance(game), Game7Fragment.TAG);
                getSupportActionBar().setTitle(R.string.choose_the_correct_sentence);
                break;
            case 8:
                transaction.replace(R.id.regularGameActivityFrameLayout, Game8Fragment.newInstance(game), Game8Fragment.TAG);
                getSupportActionBar().setTitle(R.string.true_or_false);
                break;
            case 9:
                transaction.replace(R.id.regularGameActivityFrameLayout, Game9Fragment.newInstance(game), Game9Fragment.TAG);
                getSupportActionBar().setTitle(R.string.find_the_odd_one_out);
                break;
            case 10:
                transaction.replace(R.id.regularGameActivityFrameLayout, Game10Fragment.newInstance(game), Game10Fragment.TAG);
                getSupportActionBar().setTitle(R.string.pair_the_title_image_and_text);
                break;
            case 11:
                transaction.replace(R.id.regularGameActivityFrameLayout, Game11Fragment.newInstance(game), Game11Fragment.TAG);
                getSupportActionBar().setTitle(R.string.find_the_correct_picture);
                break;
        }
        fragmentCounter++;
        transaction.commit();
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar(String url) {
        start.setVisibility(View.GONE);
        lesson.setVisibility(View.GONE);
        level.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ArtApp.log(url);
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
        transaction.replace(R.id.regularGameActivityFrameLayout,
                ResultFragment.newInstance(correctAnswerCounter, wrongAnswerCounter), ResultFragment.TAG);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        ArtApp.showSnackBar(findViewById(R.id.regularActivityConstraintLayout),getString(R.string.press_back_once_again_to_exit));

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
