package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.BuildConfig;
import baloghtamas.lali.artapp.GameActivity;
import baloghtamas.lali.artapp.R;

public class Game3Fragment extends Fragment implements View.OnClickListener{

    public static  String TAG = "Game3Fragment";



    private ImageView image;
    private TextView sentence;
    private Button answer1, answer2, answer3, answer4;

    private String correctAnswer = "";
    private String [] answers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game3,container,false);
        ((GameActivity)getActivity()).getSupportActionBar().setTitle("Construct the correct sentence");
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        Bundle bundle = this.getArguments();
        if (bundle != null){
            image = view.findViewById(R.id.fragmentGame3ImageView);
            sentence = view.findViewById(R.id.fragmentGame3SentenceTextView);
            answer1 = view.findViewById(R.id.fragmentGame3AnswerButton1);
            answer1.setOnClickListener(this);
            answer2 = view.findViewById(R.id.fragmentGame3AnswerButton2);
            answer2.setOnClickListener(this);
            answer3 = view.findViewById(R.id.fragmentGame3AnswerButton3);
            answer3.setOnClickListener(this);
            answer4 = view.findViewById(R.id.fragmentGame3AnswerButton4);
            answer4.setOnClickListener(this);

            byte[] decodedString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);

            sentence.setText(bundle.getString("sentence"));

            correctAnswer = bundle.getStringArray("answers")[0];

            answers = ArtApp.mixStringArray(bundle.getStringArray("answers"));
            answer1.setText(answers[0]);
            answer2.setText(answers[1]);
            answer3.setText(answers[2]);
            answer4.setText(answers[3]);
        } else {
            ArtApp.log("Bundle is null in the setUp function of Game3Fragment.");
        }
    }

    public static Game3Fragment newInstance(JSONObject game){
        Game3Fragment fragment = new Game3Fragment();
        Bundle args = new Bundle();
        try {
            args.putString("image", game.getString("image"));
            args.putString("sentence", game.getString("sentence"));

            String [] answers = new String[game.getJSONArray("answers").length()];
            for (int i = 0; i < game.getJSONArray("answers").length(); i++) {
                answers[i]= game.getJSONArray("answers").getString(i);
            }

            args.putStringArray("answers", answers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        if (correctAnswer.equals(((Button) view).getText().toString())){
            ArtApp.log("Game3Fragment answer is correct.");
            ((GameActivity) getActivity()).changeFragment(true);
        } else {
            ArtApp.log("Game3Fragment answer is bad.");
            ((GameActivity) getActivity()).changeFragment(false);
        }
    }

}
