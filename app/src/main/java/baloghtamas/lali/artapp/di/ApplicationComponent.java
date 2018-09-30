package baloghtamas.lali.artapp.di;

import javax.inject.Singleton;
import baloghtamas.lali.artapp.MainActivity;
import baloghtamas.lali.artapp.RegularActivity;
import baloghtamas.lali.artapp.fragments.LanguageDialogFragment;
import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
    void inject(LanguageDialogFragment languageDialogFragment);
    void inject(RegularActivity regularActivity);
}