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
import android.widget.ScrollView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.MixedGameActivity;
import baloghtamas.lali.artapp.R;

public class Game11Fragment extends Fragment {

    public static  String TAG = "Game11Fragment";
    private ScrollView scrollView;
    private ImageView image;
    private String[]  images, sentences;
    private Button help, choosePicture;
    private int sentenceCounter = 0;
    private int imageCounter = getRandomNumber();
    private TextView helpTextView;
    private StringBuilder stringBuilder = new StringBuilder();

    private boolean answered = false;
    private float correctAnswer = 0;
    private float wrongAnswer = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_game11,container,false);
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            image = view.findViewById(R.id.fragmentGame11ImageView);
            image.setOnClickListener(imageClickListener);
            scrollView = view.findViewById(R.id.fragmentGame11ScrollView);

            help = view.findViewById(R.id.fragmentGame11HelpButton);
            help.setOnClickListener(helpClickListener);
            choosePicture = view.findViewById(R.id.fragmentGame11ChoosePictureButton);
            choosePicture.setOnClickListener(choosePictureClickListener);

            images = bundle.getStringArray("images");
            sentences = bundle.getStringArray("sentences");

            byte[] decodedString = Base64.decode(images[imageCounter], Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);

            helpTextView= view.findViewById(R.id.fragmentGame11HelpTextView);
            stringBuilder.append(sentences[sentenceCounter]);
            helpTextView.setText(stringBuilder.toString());
        } else {
            ArtApp.log("Bundle is null in the setUp function of Game11Fragment.");
        }
    }


    public static Game11Fragment newInstance(JSONObject game){
        Game11Fragment fragment = new Game11Fragment();
        Bundle args = new Bundle();
        try {
            String[] images = new String[game.getJSONArray("images").length()];
            String[] sentences = new String[game.getJSONArray("sentences").length()];

            for (int i = 0; i < game.getJSONArray("sentences").length(); i++) {
                sentences[i] = game.getJSONArray("sentences").getString(i);
            }

            for (int i = 0; i < game.getJSONArray("images").length(); i++) {
                images[i] = game.getJSONArray("images").getString(i);
            }

            args.putStringArray("images", images);
            args.putStringArray("sentences", sentences);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.setArguments(args);
        return fragment;
    }

    View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            imageCounter++;
            if(imageCounter > 2)
                imageCounter=0;
            byte[] decodedString = Base64.decode(images[imageCounter], Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
        }
    };

    View.OnClickListener helpClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sentenceCounter++;
            if(sentenceCounter >= sentences.length) {
                help.setVisibility(View.GONE);
                ArtApp.showSnackBar(getActivity().findViewById(R.id.fragmentGame11ConstraintLayout),getString(R.string.there_is_no_more_help));
            } else {
                stringBuilder.insert(0,System.getProperty("line.separator"));
                stringBuilder.insert(0,sentences[sentenceCounter].toString());
                helpTextView.setText(stringBuilder.toString());
            }
        }
    };

    View.OnClickListener choosePictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            answered = true;
            getActivity().invalidateOptionsMenu();

            choosePicture.setOnClickListener(null);
            image.setOnClickListener(null);
            help.setVisibility(View.GONE);

            if(imageCounter == 0) {
                choosePicture.setBackground(getResources().getDrawable(R.drawable.button_rounded_25_correct));
                correctAnswer++;
            } else {
                choosePicture.setBackground(getResources().getDrawable(R.drawable.button_rounded_25_wrong));
                wrongAnswer++;
            }


        }
    };

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
                if (correctAnswer == sentences.length){
                    ArtApp.log("Game11Fragment answer is correct.");
                    ((MixedGameActivity) getActivity()).changeFragment(correctAnswer,wrongAnswer);
                } else {
                    ArtApp.log("Game11Fragment answer is bad.");
                    ((MixedGameActivity) getActivity()).changeFragment(correctAnswer,wrongAnswer);
                }
                break;
        }
        return true;
    }

    //random number between 0 and 2
    private int getRandomNumber(){
        Random r = new Random();
        return r.nextInt(3);
    }
}