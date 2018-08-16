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

public class Game6Fragment extends Fragment implements View.OnClickListener {

    public static  String TAG = "Game6Fragment";

    private TextView phenomenon;
    private Button answer1, answer2, answer3;
    private String correctAnswer = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game6,container,false);
        ((GameActivity)getActivity()).getSupportActionBar().setTitle("Find the antonym");
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            phenomenon= view.findViewById(R.id.fragmentGame6PhenomenonTextView);
            phenomenon.setText(bundle.getString("phenomenon"));

            answer1 = view.findViewById(R.id.fragmentGame6AnswerButton1);
            answer1.setOnClickListener(this);
            answer2 = view.findViewById(R.id.fragmentGame6AnswerButton2);
            answer2.setOnClickListener(this);
            answer3 = view.findViewById(R.id.fragmentGame6AnswerButton3);
            answer3.setOnClickListener(this);

            correctAnswer = bundle.getStringArray("antonyms")[0];
            String [] answers = ArtApp.mixStringArray(bundle.getStringArray("antonyms"));
            answer1.setText(answers[0]);
            answer2.setText(answers[1]);
            answer3.setText(answers[2]);

        } else {
            ArtApp.log("Bundle is null in the setUp function of Game6Fragment.");
        }
    }


    public static Game6Fragment newInstance(JSONObject game){
        Game6Fragment fragment = new Game6Fragment();
        Bundle args = new Bundle();
        try {
            args.putString("phenomenon", game.getString("phenomenon"));
            String [] antonyms = new String[game.getJSONArray("antonyms").length()];
            for (int i = 0; i < game.getJSONArray("antonyms").length(); i++) {
                antonyms[i]= game.getJSONArray("antonyms").getString(i);
            }
            args.putStringArray("antonyms", antonyms);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        if (correctAnswer.equals(((Button) view).getText().toString())){
            ArtApp.log("Game6Fragment answer is correct.");
            ((GameActivity) getActivity()).changeFragment(true);
        } else {
            ArtApp.log("Game6Fragment answer is bad.");
            ((GameActivity) getActivity()).changeFragment(false);
        }
    }
}