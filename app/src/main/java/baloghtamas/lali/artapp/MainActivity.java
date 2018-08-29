package baloghtamas.lali.artapp;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import javax.inject.Inject;
import baloghtamas.lali.artapp.data.Language;
import baloghtamas.lali.artapp.data.PreferencesHelper;
import baloghtamas.lali.artapp.fragments.LanguageDialogFragment;

public class MainActivity extends AppCompatActivity {

    Button mixed,regular;

    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mixed = findViewById(R.id.mainActivityButtonMixed);
        regular = findViewById(R.id.mainActivityButtonRegular);
        ((ArtApp)getApplication()).getApplicationComponent().inject(this);

        preferencesHelper.setLanguage(Language.ENGLISH);
        ArtApp.log("Language code:" + preferencesHelper.getLanguage());
    }

    public void startButtonOnClick(View v){
        if(!isNetworkAvailable()){
            ArtApp.showSnackBar(findViewById(R.id.mainActivityConstraintLayout), "Network is not available.");
            return;
        }

        if(v.getId() == R.id.mainActivityButtonMixed){
            startActivity(new Intent(this,GameActivity.class));
            finish();
        }

        if(v.getId() == R.id.mainActivityButtonRegular){
            startActivity(new Intent(this,RegularActivity.class));
            finish();
        }
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAndRemoveTask();
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
                dialogFragment.show(ft, "dialog");
                break;

            case R.id.menuExit:
                finishAndRemoveTask();
                break;
        }
        return true;
    }
}
