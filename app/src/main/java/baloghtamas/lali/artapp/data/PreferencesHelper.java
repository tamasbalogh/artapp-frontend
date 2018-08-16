package baloghtamas.lali.artapp.data;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper implements IPreferencesHelper {

    public static final String PREF_NAME = "ART_APP_PREFERENCES";
    public static final String PREF_KEY_LANGUAGE = "PREF_KEY_LANGUAGE";

    SharedPreferences sharedPreferences;

    @Inject
    public PreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void setLanguage(Language language){
        sharedPreferences.edit().putInt(PREF_KEY_LANGUAGE,language.getCode()).apply();
    }

    @Override
    public int getLanguage(){
        return sharedPreferences.getInt(PREF_KEY_LANGUAGE, 0);
    }
}
