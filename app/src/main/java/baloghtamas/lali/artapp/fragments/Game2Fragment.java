package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.MixedGameActivity;
import baloghtamas.lali.artapp.R;

public class Game2Fragment extends Fragment {

    public static String TAG = "Game2Fragment";

    private ImageView image;
    private ListView numberedListVew, answersListView;
    private ArrayList<String> answeresList, defaultList;
    private ArrayList<NumberedListItem> numberedList;
    private ArrayAdapter<String>  answersAdapter;
    private NumberedListAdapter numberedAdapter;
    private View inflatedView;
    private int numberedListPosition = 0;
    private boolean answered = false;
    private float correctAnswer = 0;
    private float wrongAnswer = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_game2, container, false);
        ((MixedGameActivity)getActivity()).getSupportActionBar().setTitle(R.string.combine_number_and_words);
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        inflatedView = view;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            image = view.findViewById(R.id.fragmentGame2ImageView);
            numberedListVew = view.findViewById(R.id.fragmentGame2NumberedListView);
            answersListView = view.findViewById(R.id.fragmentGame2AnswersListView);

            byte[] decodedString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);

            /*Resources res = view.getResources();
            int imageId = res.getIdentifier(bundle.getString("image"), "drawable", BuildConfig.APPLICATION_ID);
            image.setImageResource(imageId);*/

            defaultList = new ArrayList<>(Arrays.asList(bundle.getStringArray("answers")));
            answeresList = new ArrayList<>(Arrays.asList(ArtApp.mixStringArray(bundle.getStringArray("answers"))));
            numberedList = new ArrayList<>();

            for (int i = 0; i < answeresList.size(); i++) {
                String key = (i+1) + ".";
                numberedList.add(new NumberedListItem(key, null));
            }

            answersAdapter = new ArrayAdapter<String>(view.getContext(),
                    android.R.layout.simple_list_item_1, answeresList);
            answersListView.setAdapter(answersAdapter);

            numberedAdapter = new NumberedListAdapter(view.getContext(), numberedList);
            numberedListVew.setAdapter(numberedAdapter);

            answersListView.setOnItemClickListener(answersOnItemClickListener);
        } else {
            ArtApp.log("Bundle is null in the setUp function of Game2Fragment.");
        }
    }

    public static Game2Fragment newInstance(JSONObject game) {
        Game2Fragment fragment = new Game2Fragment();
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

    AdapterView.OnItemClickListener answersOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String value = answersListView.getItemAtPosition(position).toString();
            numberedList.set(numberedListPosition, new NumberedListItem(numberedList.get(numberedListPosition).getNumber(),value));
            answeresList.remove(position);
            answersAdapter.notifyDataSetChanged();
            numberedAdapter.notifyDataSetChanged();
            numberedListPosition++;

            if(answeresList.isEmpty()){
                answered = true;
                getActivity().invalidateOptionsMenu();
                ArrayList<Integer> colors = new ArrayList<>();

                for (int i = 0; i < defaultList.size(); i++) {

                    if(defaultList.get(i).toString().equals(numberedList.get(i).getValue())){
                        correctAnswer++;
                        //colors.add(R.drawable.button_rounded_10_correct);
                        colors.add(android.R.color.holo_green_dark);
                    } else {
                        wrongAnswer++;
                        //colors.add(R.drawable.button_rounded_10_wrong);
                        colors.add(android.R.color.holo_red_dark);
                    }
                }

                numberedAdapter = new NumberedListAdapter(inflatedView.getContext(),numberedList,colors);
                numberedListVew.setAdapter(numberedAdapter);

                answersAdapter = new ArrayAdapter<String>(view.getContext(),
                        android.R.layout.simple_list_item_1, defaultList);
                answersListView.setAdapter(answersAdapter);

                answersListView.setOnItemClickListener(null);
                numberedListVew.setOnItemClickListener(null);
            }
        }
    };

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
                if (correctAnswer==defaultList.size()){
                    ArtApp.log("Game2Fragment answer is correct.");
                    ((MixedGameActivity) getActivity()).changeFragment(correctAnswer,wrongAnswer);
                } else {
                    ArtApp.log("Game2Fragment answer is bad.");
                    ((MixedGameActivity) getActivity()).changeFragment(correctAnswer,wrongAnswer);
                }
                break;
        }
        return true;
    }

    class NumberedListAdapter extends ArrayAdapter<NumberedListItem> {

        private Context mContext;
        private List<NumberedListItem> list;
        private List<Integer> colors = null;

        public NumberedListAdapter(@NonNull Context context, @LayoutRes ArrayList<NumberedListItem> list) {
            super(context, 0 , list);
            mContext = context;
            this.list = list;
        }

        public NumberedListAdapter(@NonNull Context context, @LayoutRes ArrayList<NumberedListItem> list, ArrayList<Integer> colors) {
            super(context, 0 , list);
            mContext = context;
            this.list = list;
            this.colors = colors;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.fragment_game2_row_item,parent,false);

            NumberedListItem current = list.get(position);

            TextView number = (TextView) listItem.findViewById(R.id.rowItemNumber);
            number.setText(current.getNumber());


            TextView value = (TextView) listItem.findViewById(R.id.rowItemValue);
            value.setText(current.getValue());
            if(colors != null){
                //value.setBackground(mContext.getResources().getDrawable(colors.get(position)));
                value.setBackgroundColor(mContext.getResources().getColor(colors.get(position)));
                //value.setTextColor(mContext.getResources().getColor(R.color.defaultItem));
            }

            return listItem;
        }


    }

    class NumberedListItem {

        private String number;
        private String value;

        public NumberedListItem(String number, String value) {
            this.number = number;
            this.value = value;
        }

        public String getNumber() {
            return number;
        }

        public String getValue() {
            return value;
        }
    }
}
