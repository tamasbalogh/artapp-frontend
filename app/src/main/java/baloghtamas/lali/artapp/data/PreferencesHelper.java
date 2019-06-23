package baloghtamas.lali.artapp.data;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import baloghtamas.lali.artapp.ArtApp;

@Singleton
public class PreferencesHelper implements IPreferencesHelper {

    public static final String PREF_NAME = "ART_APP_PREFERENCES";
    public static final String PREF_KEY_LANGUAGE = "PREF_KEY_LANGUAGE_STRING";
    public static final String PREF_KEY_ONBOARDING = "PREF_KEY_ONBOARDING";
    public static final String PREF_KEY_GAMETYPE = "PREF_KEY_GAMETYPE";
    public static final String PREF_KEY_SELECTED_LESSON = "PREF_KEY_SELECTED_LESSON";
    public static final String PREF_KEY_SELECTED_LEVEL = "PREF_KEY_SELECTED_LEVEL";

    SharedPreferences sharedPreferences;

    @Inject
    public PreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void setLanguage(Language language){
        sharedPreferences.edit().putString(PREF_KEY_LANGUAGE,language.getCode()).apply();
    }

    @Override
    public Language getLanguage(){
        return Language.lookupByCode(sharedPreferences.getString(PREF_KEY_LANGUAGE, "en"));
    }

    @Override
    public boolean getAlreadyOnBoardStatus() {
        return sharedPreferences.getBoolean(PREF_KEY_ONBOARDING,false);
    }

    @Override
    public void setAlreadyOnBoardStatusToTrue() {
        sharedPreferences.edit().putBoolean(PREF_KEY_ONBOARDING,true).apply();
    }

    @Override
    public String getCurrentGameType() {
        return sharedPreferences.getString(PREF_KEY_GAMETYPE,"");
    }

    @Override
    public void setCurrentGameType(String gameType) {
        sharedPreferences.edit().putString(PREF_KEY_GAMETYPE,gameType).apply();
    }

    @Override
    public int getSelectedLesson() {
        return sharedPreferences.getInt(PREF_KEY_SELECTED_LESSON,0);
    }

    @Override
    public void setSelectedLesson(int lesson) {
        sharedPreferences.edit().putInt(PREF_KEY_SELECTED_LESSON,lesson).apply();
    }

    @Override
    public int getSelectedLevel() {
        return sharedPreferences.getInt(PREF_KEY_SELECTED_LEVEL,0);
    }

    @Override
    public void setSelectedLevel(int level) {
        sharedPreferences.edit().putInt(PREF_KEY_SELECTED_LEVEL,level).apply();
    }
}
