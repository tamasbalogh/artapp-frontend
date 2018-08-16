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

public class Game10Fragment extends Fragment {

    public static String TAG = "Game10Fragment";

    private TextView title, description;
    private ImageView image;
    private String[] titles, images, descriptions;
    private int[] titlePointers, imagePointers, descriptionPointers;
    private int imagePointer = 0;
    private int titlePointer = 0;
    private int descriptionPointer = 0;
    private int buttonClickCounter = 0;
    private Button button;
    private boolean answer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game10, container, false);
        ((GameActivity)getActivity()).getSupportActionBar().setTitle("Mix and match");
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            image = view.findViewById(R.id.fragmentGame10ImageView);
            image.setOnClickListener(imageClickListener);
            title = view.findViewById(R.id.fragmentGame10TitleTextView);
            title.setOnClickListener(titleClickListener);
            description = view.findViewById(R.id.fragmentGame10DescriptionTextView);
            description.setOnClickListener(descriptionClickListener);
            button = view.findViewById(R.id.fragmentGame10Button);
            button.setOnClickListener(buttonClickListener);

            images = bundle.getStringArray("images");
            titles = bundle.getStringArray("titles");
            descriptions = bundle.getStringArray("descriptions");

            titlePointers = new int[titles.length];
            descriptionPointers = new int[descriptions.length];
            imagePointers = new int[images.length];

            for (int i = 0; i < titles.length; i++) {
                titlePointers[i] = i;
                descriptionPointers[i] = i;
                imagePointers[i] = i;
            }

            Resources res = view.getResources();
            int imageId = res.getIdentifier(images[0], "drawable", BuildConfig.APPLICATION_ID);
            image.setImageResource(imageId);
            title.setText(titles[0].toString());
            description.setText(descriptions[0].toString());
        } else {
            ArtApp.log("Bundle is null in the setUp function of Game10Fragment.");
        }
    }


    public static Game10Fragment newInstance(JSONObject game) {
        Game10Fragment fragment = new Game10Fragment();
        Bundle args = new Bundle();
        try {
            String[] images = new String[game.getJSONArray("images").length()];
            String[] titles = new String[game.getJSONArray("titles").length()];
            String[] descriptions = new String[game.getJSONArray("descriptions").length()];
            for (int i = 0; i < game.getJSONArray("images").length(); i++) {
                images[i] = game.getJSONArray("images").getString(i);
                titles[i] = game.getJSONArray("titles").getString(i);
                descriptions[i] = game.getJSONArray("descriptions").getString(i);
            }

            args.putStringArray("images", images);
            args.putStringArray("titles", titles);
            args.putStringArray("descriptions", descriptions);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.setArguments(args);
        return fragment;
    }


    View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            imagePointer = getNextPointer(imagePointers, imagePointer);
            Resources res = view.getResources();
            int imageId = res.getIdentifier(images[imagePointer], "drawable", BuildConfig.APPLICATION_ID);
            image.setImageResource(imageId);
        }
    };

    View.OnClickListener titleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            titlePointer = getNextPointer(titlePointers, titlePointer);
            title.setText(titles[titlePointers[titlePointer]]);
        }
    };

    View.OnClickListener descriptionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            descriptionPointer = getNextPointer(descriptionPointers, descriptionPointer);
            description.setText(descriptions[descriptionPointers[descriptionPointer]]);
        }
    };

    private int getNextPointer(int[] arrayOfPointers, int pointer) {
        pointer++;
        if(pointer > 2)
            pointer=0;
        boolean found = true;
        while (found) {
            if(arrayOfPointers[pointer] != -1)
                found=false;
            else {
                pointer++;
                if(pointer > 2)
                    pointer=0;
            }
        }
        return pointer;
    }

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClickCounter++;
            if(buttonClickCounter > 2){
                ArtApp.log("Game10Fragment answer is correct.");
                ((GameActivity) getActivity()).changeFragment(true);
            } else {
                titlePointers[titlePointer] = -1;
                titlePointer = getNextPointer(titlePointers, titlePointer);
                title.setText(titles[titlePointers[titlePointer]]);

                imagePointers[imagePointer] = -1;
                imagePointer = getNextPointer(imagePointers, imagePointer);
                Resources res = view.getResources();
                int imageId = res.getIdentifier(images[imagePointer], "drawable", BuildConfig.APPLICATION_ID);
                image.setImageResource(imageId);

                descriptionPointers[descriptionPointer] = -1;
                descriptionPointer = getNextPointer(descriptionPointers, descriptionPointer);
                description.setText(descriptions[descriptionPointers[descriptionPointer]]);
            }
        }
    };
}