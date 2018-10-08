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
import java.util.ArrayList;
import java.util.Random;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.MixedGameActivity;
import baloghtamas.lali.artapp.R;

public class Game10Fragment extends Fragment {

    public static String TAG = "Game10Fragment";

    private TextView title, description;
    private ImageView image;
    private ArrayList<Game10DataModel> list = new ArrayList<>();
    private ArrayList<Game10DataModel> defaultList = new ArrayList<>();
    private boolean answers[] = new boolean[3];
    private int imagePointer = 0;
    private int titlePointer = 0;
    private int descriptionPointer = 0;
    private int buttonClickCounter = 0;
    private Button button;

    private boolean answered = false;
    private float correctAnswer = 0;
    private float wrongAnswer = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_game10, container, false);
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

            if(bundle.getStringArray("images").length == bundle.getStringArray("titles").length &&
                    bundle.getStringArray("images").length == bundle.getStringArray("descriptions").length){
                for (int i = 0; i < bundle.getStringArray("images").length; i++) {
                    list.add(new Game10DataModel(
                            bundle.getStringArray("titles")[i],
                            bundle.getStringArray("images")[i],
                            bundle.getStringArray("descriptions")[i]));
                    defaultList.add(new Game10DataModel(
                            bundle.getStringArray("titles")[i],
                            bundle.getStringArray("images")[i],
                            bundle.getStringArray("descriptions")[i]));
                }
            }

            titlePointer = getRandomNumber();
            imagePointer = getRandomNumber();
            descriptionPointer = getRandomNumber();

            byte[] decodedString = Base64.decode(list.get(imagePointer).getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);

            title.setText(list.get(titlePointer).getTitle());
            description.setText(list.get(descriptionPointer).getDescription());
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
            imagePointer = getNext(imagePointer);
            while(list.get(imagePointer).getImage().equals(" ")){
                imagePointer = getNext(imagePointer);
            }

            byte[] decodedString = Base64.decode(list.get(imagePointer).getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
            //ArtApp.log("image id: " + imagePointer);
        }
    };

    View.OnClickListener titleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            titlePointer = getNext(titlePointer);
            while(list.get(titlePointer).getTitle().equals(" ")){
                titlePointer = getNext(titlePointer);
            }
            title.setText(list.get(titlePointer).getTitle());
            //ArtApp.log("title id: " + titlePointer);
        }
    };

    View.OnClickListener descriptionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            descriptionPointer =getNext(descriptionPointer);
            while(list.get(descriptionPointer).getDescription().equals(" ")){
                descriptionPointer = getNext(descriptionPointer);
            }
            description.setText(list.get(descriptionPointer).getDescription());
            //ArtApp.log("description id: " + descriptionPointer);
        }
    };

    View.OnClickListener checkResult = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            titlePointer = getNext(titlePointer);
            imagePointer = titlePointer;
            descriptionPointer = titlePointer;

            byte[] decodedString = Base64.decode(defaultList.get(imagePointer).getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);

            title.setText(defaultList.get(titlePointer).getTitle());
            description.setText(defaultList.get(descriptionPointer).getDescription());

            if(answers[titlePointer]){
                title.setBackground(getResources().getDrawable(R.drawable.button_rounded_10_correct));
            } else {
                title.setBackground(getResources().getDrawable(R.drawable.button_rounded_10_wrong));
            }
        }
    };

    private int getNext(int pointer) {
        int p = 0;
        switch (pointer){
            case 0:
                p = 1;
                break;
            case 1:
                p = 2;
                break;
            case 2:
                p = 0;
                break;
        }
        return p;
    }

    View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonClickCounter++;

            if(buttonClickCounter > 2){

                if(titlePointer == imagePointer && titlePointer == descriptionPointer){
                    correctAnswer++;
                    answers[titlePointer]=true;
                } else {
                    wrongAnswer++;
                    answers[titlePointer]=false;
                }

                answered = true;
                getActivity().invalidateOptionsMenu();
                button.setText("Check Result");

                title.setOnClickListener(checkResult);
                description.setOnClickListener(checkResult);
                image.setOnClickListener(checkResult);
                button.setOnClickListener(checkResult);

                titlePointer = getNext(titlePointer);
                imagePointer = titlePointer;
                descriptionPointer = titlePointer;

                byte[] decodedString = Base64.decode(defaultList.get(imagePointer).getImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image.setImageBitmap(decodedByte);

                title.setText(defaultList.get(titlePointer).getTitle());
                description.setText(defaultList.get(descriptionPointer).getDescription());

                if(answers[titlePointer]){
                    title.setBackground(getResources().getDrawable(R.drawable.button_rounded_10_correct));
                } else {
                    title.setBackground(getResources().getDrawable(R.drawable.button_rounded_10_wrong));
                }

            } else {
                if(titlePointer == imagePointer && titlePointer == descriptionPointer){
                    correctAnswer++;
                    answers[titlePointer]=true;
                } else {
                    wrongAnswer++;
                    answers[titlePointer]=false;
                }

                list.get(titlePointer).setTitle(" ");
                list.get(imagePointer).setImage(" ");
                list.get(descriptionPointer).setDescription(" ");

                titlePointer = getRandomNumber();
                while(list.get(titlePointer).getTitle().equals(" ")){
                    titlePointer = getNext(titlePointer);
                }
                imagePointer = getRandomNumber();
                while(list.get(imagePointer).getImage().equals(" ")){
                    imagePointer = getNext(imagePointer);
                }
                descriptionPointer = getRandomNumber();
                while(list.get(descriptionPointer).getDescription().equals(" ")){
                    descriptionPointer = getNext(descriptionPointer);
                }

                title.setText(list.get(titlePointer).getTitle());

                byte[] decodedString = Base64.decode(list.get(imagePointer).getImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image.setImageBitmap(decodedByte);

                description.setText(list.get(descriptionPointer).getDescription());
            }
        }
    };

    //random number between 0 and 2
    private int getRandomNumber(){
        Random r = new Random();
        return r.nextInt(3);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(answered) {
            inflater.inflate(R.menu.next_menu, menu);
        } else {
            inflater.inflate(R.menu.information_reload_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuInformation:
                ArtApp.showSnackBar(getActivity().findViewById(R.id.gameActivityConstraintLayout),TAG);
                break;
            case R.id.menuReload:
                ((MixedGameActivity) getActivity()).reloadFragment();
                break;
            case R.id.menuNext:
                if (correctAnswer== list.size()){
                    ArtApp.log("Game10Fragment answer is correct.");
                    ((MixedGameActivity) getActivity()).changeFragment(correctAnswer,wrongAnswer);
                } else {
                    ArtApp.log("Game10Fragment answer is bad.");
                    ((MixedGameActivity) getActivity()).changeFragment(correctAnswer,wrongAnswer);
                }
                break;
        }
        return true;
    }

    private class Game10DataModel{

        private String title;
        private String image;
        private String description;

        public Game10DataModel(String title, String image, String description) {
            this.title = title;
            this.image = image;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }

        public String getDescription() {
            return description;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}