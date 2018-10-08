package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.MixedGameActivity;
import baloghtamas.lali.artapp.R;

public class Game5Fragment extends Fragment implements View.OnClickListener {

    public static  String TAG = "Game5Fragment";

    private ImageView image;
    private Button answer1, answer2, answer3, answer4;
    private String correctAnswer = "";
    private boolean answered = false;
    private boolean correct;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_game5,container,false);
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
            if(answer4.getText().equals(correctAnswer))
                answer4.setBackground(getResources().getDrawable(R.drawable.button_rounded_25_correct));
        }

        answer1.setClickable(false);
        answer2.setClickable(false);
        answer3.setClickable(false);
        answer4.setClickable(false);

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
                    ArtApp.log("Game5Fragment answer is correct.");
                    ((MixedGameActivity) getActivity()).changeFragment(1,0);
                } else {
                    ArtApp.log("Game5Fragment answer is bad.");
                    ((MixedGameActivity) getActivity()).changeFragment(0,1);
                }
        }
        return true;
    }
}