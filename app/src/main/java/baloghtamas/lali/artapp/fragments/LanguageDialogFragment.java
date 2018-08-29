package baloghtamas.lali.artapp.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import baloghtamas.lali.artapp.R;

public class LanguageDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.layout.fragment_language_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Select your language!");
        View view = inflater.inflate(R.layout.fragment_language_dialog, container, true);
        return view;
    }
}