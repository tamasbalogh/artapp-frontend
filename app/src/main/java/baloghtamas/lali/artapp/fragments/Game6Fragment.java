package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.MixedGameActivity;
import baloghtamas.lali.artapp.R;

public class Game6Fragment extends Fragment implements View.OnClickListener {

    public static  String TAG = "Game6Fragment";

    private TextView phenomenon;
    private Button answer1, answer2, answer3;
    private String correctAnswer = "";
    private boolean answered = false;
    private boolean correct;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_game6,container,false);
        ((MixedGameActivity)getActivity()).getSupportActionBar().setTitle(R.string.find_the_antonym);
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

        answered = true;
        getActivity().invalidateOptionsMenu();

        if(correctAnswer.equals(((Button) view).getText().toString())){
            correct = true;
            view.setBackground(getResources().getDrawable(R.drawable.button_rounded_25_correct));
        } else {
            correct = false;
            view.setBackground(getResources().getDrawable(R.drawable.button_rounded_25_wrong));
            if(answer1.getText().equals(correctAnswer))
                answer1.setBackground(getResources().getDrawable(R.drawable.button_rounded_25_correct));
            if(answer2.getText().equals(correctAnswer))
                answer2.setBackground(getResources().getDrawable(R.drawable.button_rounded_25_correct));
            if(answer3.getText().equals(correctAnswer))
                answer3.setBackground(getResources().getDrawable(R.drawable.button_rounded_25_correct));
        }

        answer1.setClickable(false);
        answer2.setClickable(false);
        answer3.setClickable(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(answered) {
            inflater.inflate(R.menu.next_menu, menu);
        } else {
            inflater.inflate(R.menu.information_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuInformation:
                ArtApp.showSnackBar(getActivity().findViewById(R.id.gameActivityConstraintLayout),TAG);
                break;
            case R.id.menuNext:
                if (correct){
                    ArtApp.log("Game6Fragment answer is correct.");
                    ((MixedGameActivity) getActivity()).changeFragment(1,0);
                } else {
                    ArtApp.log("Game6Fragment answer is bad.");
                    ((MixedGameActivity) getActivity()).changeFragment(0,1);
                }
                break;
        }
        return true;
    }
}