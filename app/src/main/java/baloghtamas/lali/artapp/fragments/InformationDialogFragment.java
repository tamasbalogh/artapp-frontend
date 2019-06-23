package baloghtamas.lali.artapp.fragments;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import javax.inject.Inject;
import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.R;
import baloghtamas.lali.artapp.data.PreferencesHelper;

public class InformationDialogFragment extends DialogFragment {

    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_dialog, container, false);

        TextView inf = view.findViewById(R.id.fragmentInformationTextView);

        Bundle bundle = getArguments();
        if(bundle != null) {
            inf.setText(bundle.getString("information"));
        } else {
            dismiss();
            ArtApp.showSnackBarLong(getActivity().findViewById(R.id.gameActivityConstraintLayout), getString(R.string.information_is_not_available));
        }
        return view;
    }

    public static InformationDialogFragment newInstance(String information){
        InformationDialogFragment fragment = new InformationDialogFragment();
        Bundle args = new Bundle();
        args.putString("information", information);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

}