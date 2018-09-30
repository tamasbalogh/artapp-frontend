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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.MixedGameActivity;
import baloghtamas.lali.artapp.R;

public class Game8Fragment extends Fragment implements View.OnClickListener {

    public static  String TAG = "Game8Fragment";

    private ImageView image;
    private TextView sentence;
    private Button trueButton, falseButton ;
    private int correctAnswer;
    private boolean answered = false;
    private boolean correct;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_game8,container,false);
        ((MixedGameActivity)getActivity()).getSupportActionBar().setTitle(R.string.true_or_false);
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            image = view.findViewById(R.id.fragmentGame8ImageView);

            byte[] decodedString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);

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
        answered = true;
        getActivity().invalidateOptionsMenu();

        int pressedValue;
        if(view.getId() == R.id.fragmentGame8ButtonTrue){
            pressedValue = 1;
        } else {
            pressedValue = 0;
        }

        if(pressedValue == correctAnswer){
            correct = true;
            view.setBackground(getResources().getDrawable(R.drawable.button_rounded_25_correct));
        } else {
            correct = false;
            view.setBackground(getResources().getDrawable(R.drawable.button_rounded_25_wrong));
            if(view.getId() != R.id.fragmentGame8ButtonTrue)
                trueButton.setBackground(getResources().getDrawable(R.drawable.button_rounded_25_correct));
            if(view.getId() != R.id.fragmentGame8ButtonFalse)
                falseButton.setBackground(getResources().getDrawable(R.drawable.button_rounded_25_correct));
        }

        trueButton.setClickable(false);
        falseButton.setClickable(false);
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
                    ArtApp.log("Game8Fragment answer is correct.");
                    ((MixedGameActivity) getActivity()).changeFragment(1,0);
                } else {
                    ArtApp.log("Game8Fragment answer is bad.");
                    ((MixedGameActivity) getActivity()).changeFragment(0,1);
                }
                break;
        }
        return true;
    }
}