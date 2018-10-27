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

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.GameActivity;
import baloghtamas.lali.artapp.R;

public class Game9Fragment extends Fragment implements View.OnClickListener
{

    public static  String TAG = "Game9Fragment";

    ImageView image;
    FlexboxLayout words;
    private String correctAnswer = "";
    private String[] answers;
    private boolean answered = false;
    private boolean correct;
    private View inflatedView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_game9,container,false);
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        inflatedView = view;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            image = view.findViewById(R.id.fragmentGame9ImageView);
            words = view.findViewById(R.id.fragmentGame9WordsFlexBox);

            byte[] decodedString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);

            correctAnswer = bundle.getStringArray("answers")[0];
            answers = ArtApp.mixStringArray(bundle.getStringArray("answers"));

            for (int i = 0; i < answers.length; i++) {
                Button button = new Button(getActivity());
                button.setBackground(getResources().getDrawable(R.drawable.button_rounded_10));
                button.setTextColor(getResources().getColor(R.color.defaultItem));
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
                button.setTag("fragmentGame9Button" + i);
                button.setText(answers[i]);
                button.setOnClickListener(this);
                words.addView(button);
            }
        } else {
            ArtApp.log("Bundle is null in the setUp function of Game9Fragment.");
        }
    }


    public static Game9Fragment newInstance(JSONObject game){
        Game9Fragment fragment = new Game9Fragment();
        Bundle args = new Bundle();
        try {
            args.putString("image", game.getString("image"));
            String[] answers = new String[game.getJSONArray("answers").length()];
            for (int i = 0; i < game.getJSONArray("answers").length(); i++) {
                answers[i] = game.getJSONArray("answers").getString(i);
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
        answered = true;
        getActivity().invalidateOptionsMenu();

        if(((Button)view).getText().equals(correctAnswer)){
            correct = true;
            view.setBackground(getResources().getDrawable(R.drawable.button_rounded_10_correct));
            for (int i = 0; i < answers.length; i++) {
                Button button = inflatedView.findViewWithTag("fragmentGame9Button" + i);
                button.setClickable(false);
            }
        } else {
            correct = false;
            view.setBackground(getResources().getDrawable(R.drawable.button_rounded_10_wrong));
            for (int i = 0; i < answers.length; i++) {
                Button button = inflatedView.findViewWithTag("fragmentGame9Button" + i);
                if(button.getText().equals(correctAnswer)){
                    button.setBackground(getResources().getDrawable(R.drawable.button_rounded_10_correct));
                }
                button.setClickable(false);
            }
        }
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
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment = InformationDialogFragment.newInstance(getResources().getString(R.string.fragment9description));
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                dialogFragment.show(ft, "information");
                break;
            case R.id.menuNext:
                if (correct){
                    ArtApp.log("Game9Fragment answer is correct.");
                    ((GameActivity) getActivity()).changeFragment(1,0);
                } else {
                    ArtApp.log("Game9Fragment answer is bad.");
                    ((GameActivity) getActivity()).changeFragment(0,1);
                }
                break;
        }
        return true;
    }
}