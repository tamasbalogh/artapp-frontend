package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class Game5Fragment extends Fragment implements View.OnClickListener {

    public static  String TAG = "Game5Fragment";

    private ImageView image;
    private Button answer1, answer2, answer3, answer4;
    private String correctAnswer = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game5,container,false);
        ((GameActivity)getActivity()).getSupportActionBar().setTitle("Find the correct sentence");
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            image = view.findViewById(R.id.fragmentGame5ImageView);
            answer1 = view.findViewById(R.id.fragmentGame5AnswerButton1);
            answer1.setOnClickListener(this);
            answer2 = view.findViewById(R.id.fragmentGame5AnswerButton2);
            answer2.setOnClickListener(this);
            answer3 = view.findViewById(R.id.fragmentGame5AnswerButton3);
            answer3.setOnClickListener(this);
            answer4 = view.findViewById(R.id.fragmentGame5AnswerButton4);
            answer4.setOnClickListener(this);

            byte[] decodedString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);

            correctAnswer = bundle.getStringArray("sentences")[0];
            String [] answers = ArtApp.mixStringArray(bundle.getStringArray("sentences"));
            answer1.setText(answers[0]);
            answer2.setText(answers[1]);
            answer3.setText(answers[2]);
            answer4.setText(answers[3]);

        } else {
            ArtApp.log("Bundle is null in the setUp function of Game5Fragment.");
        }
    }


    public static Game5Fragment newInstance(JSONObject game){
        Game5Fragment fragment = new Game5Fragment();
        Bundle args = new Bundle();
        try {
            args.putString("image", game.getString("image"));
            String [] sentences = new String[game.getJSONArray("sentences").length()];
            for (int i = 0; i < game.getJSONArray("sentences").length(); i++) {
                sentences[i]= game.getJSONArray("sentences").getString(i);
            }
            args.putStringArray("sentences", sentences);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        if (correctAnswer.equals(((Button) view).getText().toString())){
            ArtApp.log("Game5Fragment answer is correct.");
            ((GameActivity) getActivity()).changeFragment(true);
        } else {
            ArtApp.log("Game5Fragment answer is bad.");
            ((GameActivity) getActivity()).changeFragment(false);
        }
    }
}