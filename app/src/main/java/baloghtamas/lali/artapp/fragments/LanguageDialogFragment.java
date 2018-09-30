package baloghtamas.lali.artapp.fragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import javax.inject.Inject;
import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.R;
import baloghtamas.lali.artapp.data.Language;
import baloghtamas.lali.artapp.data.PreferencesHelper;

public class LanguageDialogFragment extends DialogFragment {

    @Inject
    PreferencesHelper preferencesHelper;

    RadioGroup radioGroup;
    Button choose;
    Language oldLanguage, newLanguage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_language_dialog, container, false);

        ((ArtApp)getActivity().getApplication()).getApplicationComponent().inject(this);

        radioGroup = view.findViewById(R.id.fragmentLanguageRadioGroup);
        choose = view.findViewById(R.id.fragmentLanguageButtonChoose);

        oldLanguage = preferencesHelper.getLanguage();
        newLanguage = preferencesHelper.getLanguage();

        switch (preferencesHelper.getLanguage()){
            case ENGLISH:
                radioGroup.check(R.id.fragmentLanguageRadioButtonEnglish);
                break;
            case GERMANY:
                radioGroup.check(R.id.fragmentLanguageRadioButtonGermany);
                break;
            case FRANCE:
                radioGroup.check(R.id.fragmentLanguageRadioButtonFrance);
                break;
            case FINNISH:
                radioGroup.check(R.id.fragmentLanguageRadioButtonFinland);
                break;
        }

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                switch (selectedId){
                    case R.id.fragmentLanguageRadioButtonEnglish:
                        newLanguage = Language.ENGLISH;
                        //preferencesHelper.setLanguage(Language.ENGLISH);
                        break;
                    case R.id.fragmentLanguageRadioButtonFrance:
                        newLanguage = Language.FRANCE;
                        break;
                    case R.id.fragmentLanguageRadioButtonFinland:
                        newLanguage = Language.FINNISH;
                        break;
                    case R.id.fragmentLanguageRadioButtonGermany:
                        newLanguage = Language.GERMANY;
                        break;
                }
                preferencesHelper.setLanguage(newLanguage);
                dismiss();
            }
        });


        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if( oldLanguage != newLanguage) {
            getActivity().recreate();
        }
    }

}