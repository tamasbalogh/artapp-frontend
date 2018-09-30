package baloghtamas.lali.artapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import javax.inject.Inject;

import baloghtamas.lali.artapp.data.PreferencesHelper;

public class RegularActivity extends AppCompatActivity{

    Spinner city, lesson, level;
    boolean doubleBackToExitPressedOnce = false;

    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular);

        ((ArtApp)getApplication()).getApplicationComponent().inject(this);

        /*String cityArray[] = new String[]{"CITY","Paris","Vienna","Turku"};
        ArrayAdapter<String> cityArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, cityArray);
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city =findViewById(R.id.city_spinner);
        city.setAdapter(cityArrayAdapter);*/


        String lessonArray[] = getResources().getStringArray(R.array.lesson);
        ArrayAdapter<String> lessonArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, lessonArray);
        lessonArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lesson = findViewById(R.id.lesson_spinner);
        lesson.setAdapter(lessonArrayAdapter);

        String levelArray[] = getResources().getStringArray(R.array.level);
        ArrayAdapter<String> levelArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, levelArray);
        levelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level = findViewById(R.id.level_spinner);
        level.setAdapter(levelArrayAdapter);
    }

    public void startButton(View v){
        //String cityValue = city.getSelectedItem().toString();
        String lessonValue = lesson.getSelectedItem().toString();
        String levelValue = level.getSelectedItem().toString();

        /*if (cityValue.equals("CITY")){
            ArtApp.showSnackBar(findViewById(R.id.regularActivityConstraintLayout),"Select a city!");
            return;
        }*/

        if (lessonValue.equals(getResources().getStringArray(R.array.lesson)[0])){
            ArtApp.showSnackBar(findViewById(R.id.regularActivityConstraintLayout),getString(R.string.select_lesson));
            return;
        }

        if (levelValue.equals(getResources().getStringArray(R.array.level)[0])){
            ArtApp.showSnackBar(findViewById(R.id.regularActivityConstraintLayout),getString(R.string.select_level));
            return;
        }

        String url = "http://172.20.16.133:9080/lali/regular/" + preferencesHelper.getLanguage().toString()  + "/" + lessonValue + "/" + levelValue;
        ArtApp.showSnackBar(findViewById(R.id.regularActivityConstraintLayout),url.toLowerCase().replace(" ", "_"));
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

}
