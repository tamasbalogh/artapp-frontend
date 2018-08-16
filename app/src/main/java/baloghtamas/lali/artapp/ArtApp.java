package baloghtamas.lali.artapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    public static String loadJsonObjectFromFile(Context context) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.moc_games);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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
