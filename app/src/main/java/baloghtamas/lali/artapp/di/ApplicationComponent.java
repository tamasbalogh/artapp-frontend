package baloghtamas.lali.artapp.di;

import javax.inject.Singleton;
import baloghtamas.lali.artapp.MainActivity;
import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
}