package baloghtamas.lali.artapp.di;

import android.app.Application;
import android.content.SharedPreferences;
import javax.inject.Singleton;

import baloghtamas.lali.artapp.data.PreferencesHelper;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return application.getSharedPreferences(PreferencesHelper.PREF_NAME,application.MODE_PRIVATE);
    }

}
