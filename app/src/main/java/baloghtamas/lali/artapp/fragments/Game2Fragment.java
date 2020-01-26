package baloghtamas.lali.artapp.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.GameActivity;
import baloghtamas.lali.artapp.R;

public class Game2Fragment extends Fragment {

    public static String TAG = "Game2Fragment";

    private ImageView image;
    private ListView numberedListVew, answersListView, resultListView;
    private ArrayList<String> defaultList;
    private ArrayList<Value> answeresList, numberesList;
    private CustomAdapter answersAdapter, numberesAdapter;
    private HashMap<String , String> answersHashMap = new HashMap<>();

    private View inflatedView;
    private ArrayList<Integer> colors = new ArrayList<>();
    private boolean answered = false;
    private float correctAnswer = 0;
    private float wrongAnswer = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_game2, container, false);
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
            resultListView = view.findViewById(R.id.fragmentGame2ResultListView);

            byte[] decodedString = Base64.decode(bundle.getString("image"), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);

            defaultList = new ArrayList<>(Arrays.asList(bundle.getStringArray("answers")));
            numberesList = new ArrayList<>();
            answeresList = new ArrayList<>();

            for (String s : Arrays.asList(ArtApp.mixStringArray(bundle.getStringArray("answers")))) {
                answeresList.add(new Value(s,false));
            }

            for (int i = 0; i < answeresList.size(); i++) {
                int key =  i + 1;
                numberesList.add(new Value(Integer.toString(key),false));
            }

            answersAdapter = new CustomAdapter(view.getContext(),R.layout.listitem_value,answeresList);
            answersListView.setAdapter(answersAdapter);

            numberesAdapter = new CustomAdapter(view.getContext(),R.layout.listitem_number, numberesList);
            numberedListVew.setAdapter(numberesAdapter);

            answersListView.setOnItemClickListener(answersOnItemClickListener);
            numberedListVew.setOnItemClickListener(numbersOnItemClickListener);

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

            answeresList.get(position).isSelected = true;

            if(getTrueIndex(numberesList) != -1 ){
                answersHashMap.put(numberesList.get(getTrueIndex(numberesList)).value,answeresList.get(getTrueIndex(answeresList)).value);

                answeresList.remove(getTrueIndex(answeresList));
                answersAdapter.notifyDataSetChanged();

                numberesList.remove(getTrueIndex(numberesList));
                numberesAdapter.notifyDataSetChanged();

                if(numberesList.isEmpty() && answeresList.isEmpty()){
                    checkResult();
                }
            }

            if(getTrueIndex(answeresList) != -1){
                answeresList = allFalse(answeresList);
                answeresList.get(position).isSelected = true;
                answersAdapter.notifyDataSetChanged();
            }
        }
    };

    AdapterView.OnItemClickListener numbersOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            numberesList.get(position).isSelected = true;

            if(getTrueIndex(answeresList) != -1 ){
                answersHashMap.put(numberesList.get(getTrueIndex(numberesList)).value,answeresList.get(getTrueIndex(answeresList)).value);

                answeresList.remove(getTrueIndex(answeresList));
                answersAdapter.notifyDataSetChanged();

                numberesList.remove(getTrueIndex(numberesList));
                numberesAdapter.notifyDataSetChanged();

                if(numberesList.isEmpty() && answeresList.isEmpty()){
                    checkResult();
                }
            }

            if(getTrueIndex(numberesList) != -1){
                numberesList = allFalse(numberesList);
                numberesList.get(position).isSelected = true;
                numberesAdapter.notifyDataSetChanged();
            }

        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (answered) {
            inflater.inflate(R.menu.next_menu, menu);
        } else {
            inflater.inflate(R.menu.information_reload_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuInformation:

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment dialogFragment = InformationDialogFragment.newInstance(getResources().getString(R.string.fragment1description));
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                dialogFragment.show(ft, "information");

                break;
            case R.id.menuReload:
                ((GameActivity) getActivity()).reloadFragment();
                break;
            case R.id.menuNext:
                if (correctAnswer==defaultList.size()){
                    ArtApp.log("Game2Fragment answer is correct.");
                    ((GameActivity) getActivity()).changeFragment(correctAnswer,wrongAnswer);
                } else {
                    ArtApp.log("Game2Fragment answer is bad.");
                    ((GameActivity) getActivity()).changeFragment(correctAnswer,wrongAnswer);
                }
                break;
        }
        return true;
    }

    private void checkResult(){
        answered = true;
        getActivity().invalidateOptionsMenu();

        if(defaultList.size() == answersHashMap.size()) {

            ArrayList<String> result = new ArrayList<>();
            ArrayList<Integer> sortedKeys=new ArrayList();

            for (String key: answersHashMap.keySet()) {
                sortedKeys.add(new Integer(key));
            }

            Collections.sort(sortedKeys);

            for (int i = 0; i < sortedKeys.size(); i++) {

                String key = Integer.toString(i + 1);
                result.add(key + ". " + defaultList.get(i));

                if(answersHashMap.get(sortedKeys.get(i).toString()).equals(defaultList.get(i))){
                    correctAnswer++;
                    colors.add(android.R.color.holo_green_dark);
                } else {
                    wrongAnswer++;
                    colors.add(android.R.color.holo_red_dark);
                }
            }

            Game2Fragment.ColoredListAdapter adapter = new Game2Fragment.ColoredListAdapter(inflatedView.getContext(), result, colors);
            resultListView.setAdapter(adapter);
            resultListView.setVisibility(View.VISIBLE);

            answersListView.setOnItemClickListener(null);
            answersListView.setVisibility(View.INVISIBLE);
            numberedListVew.setOnItemClickListener(null);
            numberedListVew.setVisibility(View.INVISIBLE);

        } else {
            ArtApp.log("Game2Fragment - Something went wrong. Size of the color and definition list are different.");
        }

    }

    class CustomAdapter extends ArrayAdapter<Value>{

        private Context context;
        private List<Value> values;
        private int resource;

        public CustomAdapter(@NonNull Context context, int resource, @LayoutRes ArrayList<Value> list) {
            super(context, 0 , list);
            this.context = context;
            this.values = list;
            this.resource = resource;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = LayoutInflater.from(context).inflate(resource,parent,false);
            Value value = values.get(position);
            TextView name = (TextView) listItem.findViewById(R.id.listItemValue);
            name.setText(value.getValue());

            if(value.isSelected){
                listItem.setBackgroundColor(getResources().getColor(R.color.selectedItem));
                ((TextView) listItem).setTextColor(getResources().getColor(android.R.color.white));
            }

            return listItem;
        }
    }

    class Value {

        String value;
        boolean isSelected;

        public Value(String value, boolean isSelected) {
            this.value = value;
            this.isSelected = isSelected;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    private ArrayList<Value> allFalse(ArrayList<Value> arrayList){
        ArrayList<Value> temp = arrayList;
        for ( Value v: temp ) {
            v.isSelected = false;
        }
        return temp;
    }

    private int getTrueIndex(ArrayList<Value> arrayList){
        int index = -1;
        for (int i = 0; i < arrayList.size(); i++) {
            if(arrayList.get(i).isSelected){
                index = i;
                break;
            }
        }
        return index;
    }

    class ColoredListAdapter extends ArrayAdapter<String> {

        private Context mContext;
        private List<String> list;
        private List<Integer> colors;

        public ColoredListAdapter(@NonNull Context context, @LayoutRes ArrayList<String> list, ArrayList<Integer> colors) {
            super(context, 0 , list);
            mContext = context;
            this.list = list;
            this.colors = colors;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1,parent,false);
            TextView textView = (TextView) listItem.findViewById(android.R.id.text1);
            textView.setText(list.get(position));
            if(colors != null){
                textView.setBackgroundColor(mContext.getResources().getColor(colors.get(position)));
            }
            return listItem;
        }


    }
}
