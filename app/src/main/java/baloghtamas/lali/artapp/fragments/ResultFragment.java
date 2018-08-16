package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.BuildConfig;
import baloghtamas.lali.artapp.GameActivity;
import baloghtamas.lali.artapp.MainActivity;
import baloghtamas.lali.artapp.R;

public class ResultFragment extends Fragment implements View.OnClickListener {

    public static  String TAG = "ResultFragment";

    TextView result;
    Button newGame, home;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result,container,false);
        setUp(view);
        return view;
    }

    public static ResultFragment newInstance(int length, int correctAnswer){
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putInt("length",length);
        args.putInt("correctAnswer",correctAnswer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragmentResultHomeButton:
                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                break;
            case R.id.fragmentResultNewGameButton:
                getActivity().startActivity(new Intent(getActivity(), GameActivity.class));
                getActivity().finish();
                break;
        }
    }

    public void setUp(View view) {
        home = view.findViewById(R.id.fragmentResultHomeButton);
        home.setOnClickListener(this);
        newGame = view.findViewById(R.id.fragmentResultNewGameButton);
        newGame.setOnClickListener(this);
        result = view.findViewById(R.id.fragmentResultTextView);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            result.setText(bundle.getInt("length") + " / " + bundle.getInt("correctAnswer"));
        } else {
            ArtApp.log("Bundle is null in the setUp function of ResultFragment.");
        }
    }
}
