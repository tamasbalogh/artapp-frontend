package baloghtamas.lali.artapp.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.GameActivity;
import baloghtamas.lali.artapp.R;

public class Game4Fragment  extends Fragment {

    public static  String TAG = "Game4Fragment";

    ImageView image;
    FlexboxLayout allWords, selectedWords;

    private String correctAnswer[];
    private String sentence;
    private ArrayList<String> answers = new ArrayList<>();
    private int appendCounter = 0;
    private View inflatedView;
    private boolean answered = false;
    private boolean correct;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_game4,container,false);
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        inflatedView = view;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            image = view.findViewById(R.id.fragmentGame4ImageView);
            allWords = view.findViewById(R.id.fragmentGame4AllWordsLinearLayout);
            selectedWords = view.findViewById(R.id.fragmentGame4SelectedWordLinearLayout);

            byte[] decodedString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);

            correctAnswer=bundle.getString("sentence").split("\\s+");
            sentence = bundle.getString("sentence");
            showAllWords(ArtApp.mixStringArray(sentence.split("\\s+")));
        } else {
            ArtApp.log("Bundle is null in the setUp function of Game4Fragment.");
        }
    }


    public static Game4Fragment newInstance(JSONObject game){
        Game4Fragment fragment = new Game4Fragment();
        Bundle args = new Bundle();
        try {
            args.putString("image", game.getString("image"));
            args.putString("sentence", game.getString("sentence"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.setArguments(args);
        return fragment;
    }

    private void showAllWords(String[] array){
        for (int i = 0; i < array.length; i++) {
            Button b = new Button(getActivity());
            b.setTag(array[i]);
            b.setText(array[i]);
            b.setBackground(getResources().getDrawable(R.drawable.button_rounded_10_bright));
            b.setTextColor(getResources().getColor(R.color.defaultItem));
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
            b.setOnClickListener(put);
            allWords.addView(b);
        }
    }

    private void showAllWordsCorrect(String[] array){
        for (int i = 0; i < array.length; i++) {
            Button b = new Button(getActivity());
            b.setTag(array[i]);
            b.setText(array[i]);
            b.setBackground(getResources().getDrawable(R.drawable.button_rounded_10));
            b.setTextColor(getResources().getColor(R.color.defaultItem));
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
            b.setClickable(false);
            allWords.addView(b);
        }
    }


    View.OnClickListener put = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            answers.add(appendCounter,((Button)view).getText().toString());
            allWords.removeView(view);

            Button b = (Button)view;
            b.setBackground(getResources().getDrawable(R.drawable.button_rounded_10));
            b.setTextColor(getResources().getColor(R.color.defaultItem));
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
            selectedWords.addView(b);
            b.setOnClickListener(delete);
            appendCounter++;

            if(allWords.getChildCount() == 0){
                answered = true;
                getActivity().invalidateOptionsMenu();

                correct = true;
                int correctCounter = 0;
                for(int i=0; i < correctAnswer.length; i++){
                    if(!answers.get(i).equals(correctAnswer[i])){
                        correct = false;
                        break;
                    }
                    correctCounter++;
                }

                allWords.removeAllViews();
                showAllWordsCorrect(correctAnswer);

                for(int i = 0 ; i < selectedWords.getChildCount(); i++){
                    if(i < correctCounter){
                        ((Button)selectedWords.getFlexItemAt(i)).setBackground(getResources().getDrawable(R.drawable.button_rounded_10_correct));
                    } else {
                        ((Button)selectedWords.getFlexItemAt(i)).setBackground(getResources().getDrawable(R.drawable.button_rounded_10_wrong));
                    }
                    selectedWords.getFlexItemAt(i).setClickable(false);
                }
            }
        }
    };

    View.OnClickListener delete = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            appendCounter--;
            answers.remove(appendCounter);
            selectedWords.removeView(view);
            Button b = (Button)view;
            b.setBackground(getResources().getDrawable(R.drawable.button_rounded_10_bright));
            b.setTextColor(getResources().getColor(R.color.defaultItem));
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
            allWords.addView(b);
            b.setOnClickListener(put);
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
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment = InformationDialogFragment.newInstance(getResources().getString(R.string.fragment4description));
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                dialogFragment.show(ft, "information");
                break;
            case R.id.menuNext:
                if (correct){
                    ArtApp.log("Game4Fragment answer is correct.");
                    ((GameActivity) getActivity()).changeFragment(1,0);
                } else {
                    ArtApp.log("Game4Fragment answer is bad.");
                    ((GameActivity) getActivity()).changeFragment(0,1);
                }
                break;
        }
        return true;
    }

}