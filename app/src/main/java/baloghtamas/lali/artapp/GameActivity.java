package baloghtamas.lali.artapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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

public class GameActivity extends AppCompatActivity {

    JSONArray games;
    int fragmentCounter = 0;
    int correctAnswerCounter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        if(intent != null){
            String jsonArray = intent.getStringExtra("games");
            try {
                games = new JSONArray(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                JSONObject file = new JSONObject(ArtApp.loadJsonObjectFromFile(this));
                games = file.getJSONArray("games");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            showTheProperGameFragment(games.getJSONObject(fragmentCounter).getInt("gametype"), games.getJSONObject(fragmentCounter));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}
