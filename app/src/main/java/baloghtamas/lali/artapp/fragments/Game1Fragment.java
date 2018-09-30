package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.List;
import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.MixedGameActivity;
import baloghtamas.lali.artapp.R;

public class Game1Fragment extends Fragment {

    public static  String TAG = "Game1Fragment";

    private ListView colorsListView, definitionsListView;
    private ArrayList<Game1DataModel> defaultList = new ArrayList<>();
    private ArrayList<Game1DataModel> createdList = new ArrayList<>();
    private String selectedColor, selectedDefinition;
    private int selectedColorPosition, selectedDefinitionPosition;
    private List<String> colorsList, definitionsList;
    private  ArrayAdapter<String> colorsAdapter, definitionsAdapter;
    private View inflatedView;
    private boolean answered = false;
    private float correctAnswer = 0;
    private float wrongAnswer = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_game1,container,false);
        ((MixedGameActivity)getActivity()).getSupportActionBar().setTitle(R.string.combine_colors_and_definitions);
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        inflatedView = view;
        Bundle bundle = this.getArguments();
        if (bundle != null){
            colorsListView = view.findViewById(R.id.fragmentGame1ColorsListView);
            definitionsListView = view.findViewById(R.id.fragmentGame1DefinitionsListView);

            colorsList = new ArrayList<>(Arrays.asList(bundle.getStringArray("colors")));
            definitionsList = new ArrayList<>(Arrays.asList(bundle.getStringArray("definitions")));

            if(colorsList.size() == definitionsList.size()){
                for(int i=0; i< colorsList.size(); i++){
                    defaultList.add(new Game1DataModel(colorsList.get(i), definitionsList.get(i)));
                }
            } else {
                ArtApp.log("Game1Fragment - Something went wrong. Size of the color and definition list are different.");
                ((MixedGameActivity)getActivity()).changeFragment(0,0);
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
            if(selectedColor != null){
                ((TextView) colorsListView.getChildAt(selectedColorPosition)).setBackgroundColor(view.getResources().getColor(R.color.defaultItem));
                ((TextView) colorsListView.getChildAt(selectedColorPosition)).setTextColor(view.getResources().getColor(android.R.color.black));
                ((TextView) colorsListView.getChildAt(position)).setBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                ((TextView) colorsListView.getChildAt(position)).setTextColor(view.getResources().getColor(R.color.defaultItem));
                selectedColorPosition = position;
                selectedColor = colorsListView.getItemAtPosition(position).toString();
            } else {
                selectedColorPosition = position;
                selectedColor = colorsListView.getItemAtPosition(position).toString();
                ((TextView) colorsListView.getChildAt(position)).setBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                ((TextView) colorsListView.getChildAt(position)).setTextColor(view.getResources().getColor(R.color.defaultItem));
            }

            if(selectedDefinition != null){
                createdList.add(new Game1DataModel(selectedColor,selectedDefinition));
                cleanUpAfterSelections(view);
            }
        }
    };

    AdapterView.OnItemClickListener definitionsOnItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if(selectedDefinition != null){
                ((TextView) definitionsListView.getChildAt(selectedDefinitionPosition)).setBackgroundColor(view.getResources().getColor(R.color.defaultItem));
                ((TextView) definitionsListView.getChildAt(selectedDefinitionPosition)).setTextColor(view.getResources().getColor(android.R.color.black));
                ((TextView) definitionsListView.getChildAt(position)).setBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                ((TextView) definitionsListView.getChildAt(position)).setTextColor(view.getResources().getColor(R.color.defaultItem));
                selectedDefinitionPosition = position;
                selectedDefinition = definitionsListView.getItemAtPosition(position).toString();
            } else {
                selectedDefinitionPosition = position;
                selectedDefinition = definitionsListView.getItemAtPosition(position).toString();
                ((TextView) definitionsListView.getChildAt(position)).setBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                ((TextView) definitionsListView.getChildAt(position)).setTextColor(view.getResources().getColor(R.color.defaultItem));
            }

            if(selectedColor != null){
                createdList.add(new Game1DataModel(selectedColor,selectedDefinition));
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

        if(defaultList.size() == createdList.size()) {
            for (int i = 0; i < createdList.size(); i++) {
                String color = createdList.get(i).getColor();
                String definition = createdList.get(i).getDefinition();
                for (int j = 0; j < defaultList.size(); j++) {
                    if(defaultList.get(i).getColor().equals(color)){
                        if(defaultList.get(i).getDefinition().equals(definition)){
                            correctAnswer++;
                        } else {
                            wrongAnswer++;
                        }
                        break;
                    }
                }
            }

            colorsList = new ArrayList<>();
            definitionsList = new ArrayList<>();

            for (int i = 0; i < defaultList.size(); i++) {
                colorsList.add(defaultList.get(i).getColor());
                definitionsList.add(defaultList.get(i).getDefinition());
            }

            colorsAdapter = new ArrayAdapter<String>(inflatedView.getContext(),
                    android.R.layout.simple_list_item_1, colorsList);
            colorsListView.setAdapter(colorsAdapter);

            definitionsAdapter = new ArrayAdapter<String>(inflatedView.getContext(),
                    android.R.layout.simple_list_item_1, definitionsList);
            definitionsListView.setAdapter(definitionsAdapter);

            colorsListView.setOnItemClickListener(null);
            definitionsListView.setOnItemClickListener(null);

        } else {
            ArtApp.log("Game1Fragment - Something went wrong. Size of the color and definition list are different.");
            ((MixedGameActivity)getActivity()).changeFragment(0,0);
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
                ArtApp.showSnackBar(getActivity().findViewById(R.id.gameActivityConstraintLayout),TAG);
                break;
            case R.id.menuReload:
                ((MixedGameActivity) getActivity()).reloadFragment();
                break;
            case R.id.menuNext:
                if (correctAnswer==defaultList.size()){
                    ArtApp.log("Game1Fragment answer is correct.");
                    ((MixedGameActivity) getActivity()).changeFragment(correctAnswer,wrongAnswer);
                } else {
                    ArtApp.log("Game1Fragment answer is bad.");
                    ((MixedGameActivity) getActivity()).changeFragment(correctAnswer,wrongAnswer);
                }
                break;
        }
        return true;
    }

    private class Game1DataModel {
        private String color;
        private String definition;

        public Game1DataModel(String color, String definition) {
            this.color = color;
            this.definition = definition;
        }

        public String getColor() {
            return color;
        }

        public String getDefinition() {
            return definition;
        }
    }
}
