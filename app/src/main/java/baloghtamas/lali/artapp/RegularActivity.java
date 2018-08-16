package baloghtamas.lali.artapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RegularActivity extends AppCompatActivity{

    Spinner city, lesson, level;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular);

        String cityArray[] = new String[]{"CITY","Paris","Vienna","Turku"};
        ArrayAdapter<String> cityArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, cityArray);
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city =findViewById(R.id.city_spinner);
        city.setAdapter(cityArrayAdapter);


        String lessonArray[] = new String[]{"LESSON","Lesson 1","Lesson 2","Lesson 3","Lesson 4"};
        ArrayAdapter<String> lessonArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, lessonArray);
        lessonArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lesson = findViewById(R.id.lesson_spinner);
        lesson.setAdapter(lessonArrayAdapter);

        String levelArray[] = new String[]{"LEVEL","Basic","Advance"};
        ArrayAdapter<String> levelArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, levelArray);
        levelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level = findViewById(R.id.level_spinner);
        level.setAdapter(levelArrayAdapter);
    }

    public void startButton(View v){
        String cityValue = city.getSelectedItem().toString();
        String lessonValue = lesson.getSelectedItem().toString();
        String levelValue = level.getSelectedItem().toString();
        if (cityValue.equals("CITY")){
            ArtApp.showSnackBar(findViewById(R.id.regularActivityConstraintLayout),"Select a city!");
            return;
        }

        if (lessonValue.equals("LESSON")){
            ArtApp.showSnackBar(findViewById(R.id.regularActivityConstraintLayout),"Select a lesson!");
            return;
        }

        if (levelValue.equals("LEVEL")){
            ArtApp.showSnackBar(findViewById(R.id.regularActivityConstraintLayout),"Select a level!");
            return;
        }

        String url = "http://172.20.16.133:9080/lali/regular/" + cityValue + "/" + lessonValue + "/" + levelValue;
        ArtApp.showSnackBar(findViewById(R.id.regularActivityConstraintLayout),url.toLowerCase().replace(" ", "_"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
