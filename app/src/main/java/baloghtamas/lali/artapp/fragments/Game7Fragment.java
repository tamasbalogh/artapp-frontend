package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
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
import org.json.JSONException;
import org.json.JSONObject;
import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.GameActivity;
import baloghtamas.lali.artapp.R;

public class Game7Fragment extends Fragment implements View.OnClickListener {

    public static  String TAG = "Game7Fragment";
    String correctAnswer = "";
    ImageView image;
    Button answer1, answer2;
    TextView sentence;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game7,container,false);
        ((GameActivity)getActivity()).getSupportActionBar().setTitle("Choose the correct one");
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            image = view.findViewById(R.id.fragmentGame7ImageView);
            sentence = view.findViewById(R.id.fragmentGame7SentenceTextView);
            answer1 = view.findViewById(R.id.fragmentGame7AnswerButton1);
            answer1.setOnClickListener(this);
            answer2 = view.findViewById(R.id.fragmentGame7AnswerButton2);
            answer2.setOnClickListener(this);

            byte[] decodedString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);

            sentence= view.findViewById(R.id.fragmentGame7SentenceTextView);
            sentence.setText(bundle.getString("sentence"));

            correctAnswer = bundle.getStringArray("answers")[0];
            String [] answers = ArtApp.mixStringArray(bundle.getStringArray("answers"));

            answer1.setText(answers[0]);
            answer2.setText(answers[1]);
        } else {
            ArtApp.log("Bundle is null in the setUp function of Game7Fragment.");
        }
    }


    public static Game7Fragment newInstance(JSONObject game){
        Game7Fragment fragment = new Game7Fragment();
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
            ArtApp.log("Game7Fragment answer is correct.");
            ((GameActivity) getActivity()).changeFragment(true);
        } else {
            ArtApp.log("Game7Fragment answer is bad.");
            ((GameActivity) getActivity()).changeFragment(false);
        }
    }
}