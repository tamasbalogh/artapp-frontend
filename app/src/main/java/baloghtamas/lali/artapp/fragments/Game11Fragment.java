package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.BuildConfig;
import baloghtamas.lali.artapp.GameActivity;
import baloghtamas.lali.artapp.R;

public class Game11Fragment extends Fragment {

    public static  String TAG = "Game11Fragment";
    private ScrollView scrollView;
    private ImageView image;
    private String[]  images, sentences;
    private Button help, choosePicture;
    private int sentenceCounter = 0;
    private int imageCounter = 0;
    private TextView helpTextView;
    private StringBuilder stringBuilder = new StringBuilder();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game11,container,false);
        ((GameActivity)getActivity()).getSupportActionBar().setTitle("Picture description");
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

            byte[] decodedString = Base64.decode(bundle.getString(images[imageCounter]), Base64.DEFAULT);
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
                ArtApp.showSnackBar(getActivity().findViewById(R.id.fragmentGame11ConstraintLayout),"There is no more help. Choose a picture!");
            } else {
                ArtApp.log("Counter:" + sentenceCounter + "/" + sentences.length);
                ArtApp.log("Sentence: " + sentences[sentenceCounter].toString());
                stringBuilder.insert(0,System.getProperty("line.separator"));
                stringBuilder.insert(0,sentences[sentenceCounter].toString());
                //stringBuilder.append(System.getProperty("line.separator"));
                //stringBuilder.append(sentences[sentenceCounter].toString());
                helpTextView.setText(stringBuilder.toString());
            }
        }
    };

    View.OnClickListener choosePictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (imageCounter == 0 ) {
                ArtApp.log("Game11Fragment answer is correct.");
                ((GameActivity) getActivity()).changeFragment(true);
            } else {
                ArtApp.log("Game11Fragment answer is bad.");
                ((GameActivity) getActivity()).changeFragment(false);
            }

        }
    };
}