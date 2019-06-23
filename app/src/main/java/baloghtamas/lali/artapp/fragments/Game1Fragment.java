package baloghtamas.lali.artapp.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.GameActivity;
import baloghtamas.lali.artapp.R;

public class Game1Fragment extends Fragment {

    public static  String TAG = "Game1Fragment";

    private ListView colorsListView, definitionsListView, resultListView;
    private HashMap<String, String> defaultList = new HashMap<>();
    private HashMap<String, String> createdList = new HashMap<>();

    private String selectedColor, selectedDefinition;
    private int selectedColorPosition, selectedDefinitionPosition;
    private ArrayList<String> colorsList, definitionsList;
    private  ArrayAdapter<String> colorsAdapter, definitionsAdapter;
    private View inflatedView;

    private ArrayList<Integer> colors = new ArrayList<>();
    private boolean answered = false;
    private float correctAnswer = 0;
    private float wrongAnswer = 0;

    private boolean isColorSelected = false;
    private boolean isDefinitionSelected = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_game1,container,false);
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        inflatedView = view;
        Bundle bundle = this.getArguments();
        if (bundle != null){
            colorsListView = view.findViewById(R.id.fragmentGame1ColorsListView);
            definitionsListView = view.findViewById(R.id.fragmentGame1DefinitionsListView);
            resultListView = view.findViewById(R.id.fragmentGame1ResultListView);

            colorsList = new ArrayList<>(Arrays.asList(bundle.getStringArray("colors")));
            definitionsList = new ArrayList<>(Arrays.asList(bundle.getStringArray("definitions")));

            if(colorsList.size() == definitionsList.size()){
                for(int i=0; i< colorsList.size(); i++){
                    defaultList.put(colorsList.get(i), definitionsList.get(i));
                }
            } else {
                ArtApp.log("Game1Fragment - Something went wrong. Size of the color and definition list are different.");
                ((GameActivity) getActivity()).changeFragment(0,0);
            }


            definitionsList = new ArrayList<>(Arrays.asList(ArtApp.mixStringArray(bundle.getStringArray("definitions"))));

            colorsAdapter = new ArrayAdapter<String>(view.getContext(),
                    android.R.layout.simple_list_item_1, colorsList);
            colorsListView.setAdapter(colorsAdapter);

            definitionsAdapter = new ArrayAdapter<String>(view.getContext(),
                    android.R.layout.simple_list_item_1, definitionsList);
            definitionsListView.setAdapter(definitionsAdapter);

            colorsListView.setOnItemClickListener(colorOnItemListener);
            definitionsListView.setOnItemClickListener(definitionsOnItemListener);
        } else {
            ArtApp.log("Bundle is null in the setUp function of Game1Fragment.");
        }
    }

    public static Game1Fragment newInstance(JSONObject game){
        Game1Fragment fragment = new Game1Fragment();
        Bundle args = new Bundle();
        try {
            String [] colors = new String[game.getJSONArray("colors").length()];
            for (int i = 0; i < game.getJSONArray("colors").length(); i++) {
                colors[i]= game.getJSONArray("colors").getString(i);
            }

            String [] definitions = new String[game.getJSONArray("definitions").length()];
            for (int i = 0; i < game.getJSONArray("definitions").length(); i++) {
                definitions[i]= game.getJSONArray("definitions").getString(i);
            }

            args.putStringArray("colors", colors);
            args.putStringArray("definitions", definitions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.setArguments(args);
        return fragment;
    }

    AdapterView.OnItemClickListener colorOnItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            if(isColorSelected){
                ((TextView) colorsListView.getChildAt(selectedColorPosition)).setBackgroundColor(view.getResources().getColor(R.color.defaultItem));
                ((TextView) colorsListView.getChildAt(selectedColorPosition)).setTextColor(view.getResources().getColor(android.R.color.black));
                ((TextView) colorsListView.getChildAt(position)).setBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                ((TextView) colorsListView.getChildAt(position)).setTextColor(view.getResources().getColor(R.color.defaultItem));
                selectedColorPosition = position;
                selectedColor = colorsListView.getItemAtPosition(position).toString();
            } else {
                isColorSelected = true;
                selectedColorPosition = position;
                selectedColor = colorsListView.getItemAtPosition(position).toString();
                ((TextView) colorsListView.getChildAt(position)).setBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                ((TextView) colorsListView.getChildAt(position)).setTextColor(view.getResources().getColor(R.color.defaultItem));
            }

            if(selectedDefinition != null){

                createdList.put(selectedColor,selectedDefinition);
                cleanUpAfterSelections(view);
            }
        }
    };

    AdapterView.OnItemClickListener definitionsOnItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if(isDefinitionSelected){
                ((TextView) definitionsListView.getChildAt(selectedDefinitionPosition)).setBackgroundColor(view.getResources().getColor(R.color.defaultItem));
                ((TextView) definitionsListView.getChildAt(selectedDefinitionPosition)).setTextColor(view.getResources().getColor(android.R.color.black));
                ((TextView) definitionsListView.getChildAt(position)).setBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                ((TextView) definitionsListView.getChildAt(position)).setTextColor(view.getResources().getColor(R.color.defaultItem));
                selectedDefinitionPosition = position;
                selectedDefinition = definitionsListView.getItemAtPosition(position).toString();
            } else {
                isDefinitionSelected = true;
                selectedDefinitionPosition = position;
                selectedDefinition = definitionsListView.getItemAtPosition(position).toString();
                ((TextView) definitionsListView.getChildAt(position)).setBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                ((TextView) definitionsListView.getChildAt(position)).setTextColor(view.getResources().getColor(R.color.defaultItem));
            }

            if(selectedColor != null){
                createdList.put(selectedColor,selectedDefinition);
                cleanUpAfterSelections(view);
            }
        }
    };

    private void cleanUpAfterSelections(View v){
        final View view = v;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                selectedColor = null;
                selectedDefinition = null;
                isColorSelected = false;
                isDefinitionSelected = false;
                colorsList.remove(selectedColorPosition);
                definitionsList.remove(selectedDefinitionPosition);
                colorsAdapter.notifyDataSetChanged();
                definitionsAdapter.notifyDataSetChanged();
                //((TextView)colorsListView.getChildAt(selectedColorPosition)).setTextAppearance(android.R.layout.simple_list_item_1);
                colorsListView.getChildAt(selectedColorPosition).setBackgroundColor(view.getResources().getColor(R.color.defaultItem));
                ((TextView) colorsListView.getChildAt(selectedColorPosition)).setTextColor(view.getResources().getColor(android.R.color.black));
                definitionsListView.getChildAt(selectedDefinitionPosition).setBackgroundColor(view.getResources().getColor(R.color.defaultItem));
                ((TextView) definitionsListView.getChildAt(selectedDefinitionPosition)).setTextColor(view.getResources().getColor(android.R.color.black));
                if(colorsList.isEmpty()){
                    checkResult();
                }
            }
        }, 200);
    }

    private void checkResult(){
        answered = true;
        getActivity().invalidateOptionsMenu();

        ArrayList<Game1DataModel> result = new ArrayList<>();

        if(defaultList.size() == createdList.size()) {

            for (String color : defaultList.keySet()) {

                result.add(new Game1DataModel(color,defaultList.get(color)));
                Game1DataModel defaultGame1Object = new Game1DataModel(color,defaultList.get(color));
                Game1DataModel createdGame1Object = new Game1DataModel(color,createdList.get(color));

                if(defaultGame1Object.equals(createdGame1Object)){
                    correctAnswer++;
                    colors.add(android.R.color.holo_green_dark);
                } else {
                    wrongAnswer++;
                    colors.add(android.R.color.holo_red_dark);
                }
            }

            colorsList = new ArrayList<>(defaultList.keySet());
            definitionsList = new ArrayList<>(defaultList.values());

            ColoredListAdapter adapter = new ColoredListAdapter(inflatedView.getContext(),result,colors);
            resultListView.setAdapter(adapter);
            resultListView.setVisibility(View.VISIBLE);

            colorsListView.setOnItemClickListener(null);
            colorsListView.setVisibility(View.INVISIBLE);
            definitionsListView.setOnItemClickListener(null);
            definitionsListView.setVisibility(View.INVISIBLE);

        } else {
            ArtApp.log("Game1Fragment - Something went wrong. Size of the color and definition list are different.");
        }

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
                    ArtApp.log("Game1Fragment answer is correct.");
                    ((GameActivity) getActivity()).changeFragment(correctAnswer,wrongAnswer);
                } else {
                    ArtApp.log("Game1Fragment answer is bad.");
                    ((GameActivity) getActivity()).changeFragment(correctAnswer,wrongAnswer);
                }
                break;
        }
        return true;
    }

    class ColoredListAdapter extends ArrayAdapter<Game1DataModel> {

        private Context mContext;
        private List<Game1DataModel> list;
        private List<Integer> colors;

        public ColoredListAdapter(@NonNull Context context, @LayoutRes ArrayList<Game1DataModel> list, ArrayList<Integer> colors) {
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
                listItem = LayoutInflater.from(mContext).inflate(R.layout.fragment_game1_row_item,parent,false);

            Game1DataModel current = list.get(position);

            TextView color = (TextView) listItem.findViewById(R.id.rowItemColor);
            color.setText(current.getColor());


            TextView definition = (TextView) listItem.findViewById(R.id.rowItemDefinition);
            definition.setText(current.getDefinition());

            if(colors != null){
                color.setBackgroundColor(mContext.getResources().getColor(colors.get(position)));
                definition.setBackgroundColor(mContext.getResources().getColor(colors.get(position)));
            }

            return listItem;
        }


    }

    class Game1DataModel {

        private String color;
        private String definition;

        public Game1DataModel(String color, String definition) {
            this.color = color;
            this.definition = definition;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Game1DataModel that = (Game1DataModel) o;
            return Objects.equals(color, that.color) &&
                    Objects.equals(definition, that.definition);
        }

        @Override
        public int hashCode() {
            return Objects.hash(color, definition);
        }
    }
}

