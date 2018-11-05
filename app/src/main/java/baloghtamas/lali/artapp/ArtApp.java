package baloghtamas.lali.artapp;

import android.app.Application;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import baloghtamas.lali.artapp.di.ApplicationComponent;
import baloghtamas.lali.artapp.di.ApplicationModule;
import baloghtamas.lali.artapp.di.DaggerApplicationComponent;


public class ArtApp extends Application {

    public static final String SERVER_ADDRESS="https://172.20.16.133:9443/ArtApp/";
    public static final String REGULAR_GAME="REGULAR";
    public static final String MIXED_GAME="MIXED";

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public static String[] mixStringArray(String[] answers) {
        String [] tmpArray = answers;
        Random ran = new Random();
        for(int i = 0; i < tmpArray.length * 2; ++i) {
            int fromIndex = ran.nextInt(tmpArray.length);
            int toIndex = ran.nextInt(tmpArray.length);
            String tmp = tmpArray[fromIndex];
            tmpArray[fromIndex] = tmpArray[toIndex];
            tmpArray[toIndex] = tmp;
        }
        return tmpArray;
    }

    public static void log(String log){
        Log.i("ArtApp-log", log);
    }

    public static void showSnackBar(View layout, String text) {
        Snackbar snackbar = Snackbar.make(layout, text, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        view.setBackgroundColor(layout.getResources().getColor(R.color.colorPrimary));
        snackbar.setActionTextColor(layout.getResources().getColor(R.color.defaultItem))
                .show();
    }

}
