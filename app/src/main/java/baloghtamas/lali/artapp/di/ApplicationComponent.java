package baloghtamas.lali.artapp.di;

import javax.inject.Singleton;
import baloghtamas.lali.artapp.MainActivity;
import baloghtamas.lali.artapp.MixedGameActivity;
import baloghtamas.lali.artapp.RegularGameActivity;
import baloghtamas.lali.artapp.fragments.Game10Fragment;
import baloghtamas.lali.artapp.fragments.Game11Fragment;
import baloghtamas.lali.artapp.fragments.Game1Fragment;
import baloghtamas.lali.artapp.fragments.Game2Fragment;
import baloghtamas.lali.artapp.fragments.Game3Fragment;
import baloghtamas.lali.artapp.fragments.Game4Fragment;
import baloghtamas.lali.artapp.fragments.Game5Fragment;
import baloghtamas.lali.artapp.fragments.Game6Fragment;
import baloghtamas.lali.artapp.fragments.Game7Fragment;
import baloghtamas.lali.artapp.fragments.Game8Fragment;
import baloghtamas.lali.artapp.fragments.Game9Fragment;
import baloghtamas.lali.artapp.fragments.LanguageDialogFragment;
import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
    void inject(LanguageDialogFragment languageDialogFragment);
    void inject(RegularGameActivity regularGameActivity);
    void inject(MixedGameActivity mixedGameActivity);

    void inject(Game1Fragment fragment);
    void inject(Game2Fragment fragment);
    void inject(Game3Fragment fragment);
    void inject(Game4Fragment fragment);
    void inject(Game5Fragment fragment);
    void inject(Game6Fragment fragment);
    void inject(Game7Fragment fragment);
    void inject(Game8Fragment fragment);
    void inject(Game9Fragment fragment);
    void inject(Game10Fragment fragment);
    void inject(Game11Fragment fragment);
}