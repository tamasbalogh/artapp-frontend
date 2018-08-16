package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.BuildConfig;
import baloghtamas.lali.artapp.GameActivity;
import baloghtamas.lali.artapp.R;

public class Game9Fragment extends Fragment {

    public static  String TAG = "Game9Fragment";

    ImageView image;
    FlexboxLayout words;

    private String correctAnswer = "";
    private String[] answers;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game9,container,false);
        ((GameActivity)getActivity()).getSupportActionBar().setTitle("Find the odd one out");
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            image = view.findViewById(R.id.fragmentGame9ImageView);
            words = view.findViewById(R.id.fragmentGame9WordsFlexBox);

            Resources res = view.getResources();
            int imageId = res.getIdentifier(bundle.getString("image") , "drawable", BuildConfig.APPLICATION_ID);
            image.setImageResource(imageId);

            correctAnswer = bundle.getStringArray("answers")[0];
            answers = ArtApp.mixStringArray(bundle.getStringArray("answers"));

            for (int i = 0; i < answers.length; i++) {
                Button button = new Button(getActivity());
                button.setBackground(getResources().getDrawable(R.drawable.button_rounded_10));
                button.setTextColor(getResources().getColor(R.color.defaultItem));
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
                button.setText(answers[i]);
                button.setOnClickListener(click);
                words.addView(button);
            }
        } else {
            ArtApp.log("Bundle is null in the setUp function of Game9Fragment.");
        }
    }


    public static Game9Fragment newInstance(JSONObject game){
        Game9Fragment fragment = new Game9Fragment();
        Bundle args = new Bundle();
        try {
            args.putString("image", game.getString("image"));
            String[] answers = new String[game.getJSONArray("answers").length()];
            for (int i = 0; i < game.getJSONArray("answers").length(); i++) {
                answers[i] = game.getJSONArray("answers").getString(i);
            }
            args.putStringArray("answers", answers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.setArguments(args);
        return fragment;
    }



    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(((Button)view).getText().equals(correctAnswer)){
                ArtApp.log("Game9Fragment answer is correct.");
                ((GameActivity) getActivity()).changeFragment(true);
            } else {
                ArtApp.log("Game9Fragment answer is bad.");
                ((GameActivity) getActivity()).changeFragment(false);
            }
        }
    };


}