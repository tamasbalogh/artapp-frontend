package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.BuildConfig;
import baloghtamas.lali.artapp.GameActivity;
import baloghtamas.lali.artapp.R;

public class Game8Fragment extends Fragment implements View.OnClickListener {

    public static  String TAG = "Game8Fragment";

    private ImageView image;
    private TextView sentence;
    private Button trueButton, falseButton ;
    private int correctAnswer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game8,container,false);
        ((GameActivity)getActivity()).getSupportActionBar().setTitle("True or false");
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            image = view.findViewById(R.id.fragmentGame8ImageView);
            Resources res = view.getResources();
            int imageId = res.getIdentifier(bundle.getString("image") , "drawable", BuildConfig.APPLICATION_ID);
            image.setImageResource(imageId);

            sentence= view.findViewById(R.id.fragmentGame8SentenceTextView);
            sentence.setText(bundle.getString("sentence"));

            trueButton = view.findViewById(R.id.fragmentGame8ButtonTrue);
            trueButton.setOnClickListener(this);
            falseButton = view.findViewById(R.id.fragmentGame8ButtonFalse);
            falseButton.setOnClickListener(this);

            correctAnswer = bundle.getInt("true");
        } else {
            ArtApp.log("Bundle is null in the setUp function of Game8Fragment.");
        }
    }


    public static Game8Fragment newInstance(JSONObject game){
        Game8Fragment fragment = new Game8Fragment();
        Bundle args = new Bundle();
        try {
            args.putString("image", game.getString("image"));
            args.putString("sentence", game.getString("sentence"));
            args.putInt("true", game.getInt("true"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        int pressedValue;
        if(view.getId() == R.id.fragmentGame8ButtonTrue){
            pressedValue = 1;
        } else {
            pressedValue = 0;
        }

        if(pressedValue == correctAnswer){
            ArtApp.log("Game8Fragment answer is correct.");
            ((GameActivity) getActivity()).changeFragment(true);
        } else {
            ArtApp.log("Game8Fragment answer is bad.");
            ((GameActivity) getActivity()).changeFragment(false);
        }
    }
}