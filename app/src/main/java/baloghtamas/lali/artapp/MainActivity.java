package baloghtamas.lali.artapp;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import javax.inject.Inject;

import baloghtamas.lali.artapp.data.PreferencesHelper;
import baloghtamas.lali.artapp.fragments.LanguageDialogFragment;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private Button mixed,regular;

    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ArtApp)getApplication()).getApplicationComponent().inject(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if( preferencesHelper.getAlreadyOnBoardStatus() == false) {
            preferencesHelper.setAlreadyOnBoardStatusToTrue();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            DialogFragment newFragment = new LanguageDialogFragment();
            newFragment.show(ft, "dialog");
        }
        
        Locale locale = new Locale(preferencesHelper.getLanguage().getCode());
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());


        setContentView(R.layout.activity_main);

        TextView terms = findViewById(R.id.terms);

        if( preferencesHelper.getLanguage().getCode().equals("en") ||
                preferencesHelper.getLanguage().getCode().equals("fi")){
            terms.setVisibility(View.VISIBLE);
        } else {
            terms.setVisibility(View.GONE);
        }

        mixed = findViewById(R.id.mainActivityButtonMixed);
        regular = findViewById(R.id.mainActivityButtonRegular);
    }

    public void startButtonOnClick(View v){
        if(!isNetworkAvailable()){
            ArtApp.showSnackBarLong(findViewById(R.id.mainActivityConstraintLayout), getString(R.string.network_problem));
            return;
        }

        if(v.getId() == R.id.mainActivityButtonMixed){
            preferencesHelper.setCurrentGameType(ArtApp.MIXED_GAME);
            startActivityForResult(new Intent(this,GameActivity.class),1);
            //finish();
        }

        if(v.getId() == R.id.mainActivityButtonRegular){
            preferencesHelper.setCurrentGameType(ArtApp.REGULAR_GAME);
            preferencesHelper.setSelectedLevel(0);
            preferencesHelper.setSelectedLesson(0);
            startActivityForResult(new Intent(this,GameActivity.class),1);
            //finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //show onfailure message when call onfailure in GameActivity.
        if (requestCode == 1) {
            if(resultCode == GameActivity.RESULT_CODE_ON_FAILURE){
                ArtApp.showSnackBarLong(findViewById(R.id.mainActivityConstraintLayout),data.getStringExtra("onFailure"));
            }
            if(resultCode == GameActivity.RESULT_CODE_SAVED_INSTANCE_STATE){
                ArtApp.showSnackBarLong(findViewById(R.id.mainActivityConstraintLayout),data.getStringExtra("onSavedInstanceState"));
            }
            if(resultCode == GameActivity.RESULT_CODE_GAMES_LENGTH_NULL){
                ArtApp.showSnackBarLong(findViewById(R.id.mainActivityConstraintLayout),data.getStringExtra("onSuccess"));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAndRemoveTask();
        }

        this.doubleBackToExitPressedOnce = true;
        ArtApp.showSnackBar(findViewById(R.id.mainActivityConstraintLayout),getString(R.string.press_back_once_again_to_exit));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuLanguage:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new LanguageDialogFragment();
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                dialogFragment.show(ft, "dialog");
                break;
            case R.id.menuExit:
                finishAndRemoveTask();
                break;
        }
        return true;
    }
}
