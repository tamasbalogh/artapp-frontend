package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
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
import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.GameActivity;
import baloghtamas.lali.artapp.R;

public class Game1Fragment extends Fragment{

    public static  String TAG = "Game1Fragment";

    private ListView colors, definitions;
    private HashMap<String, String> defaultHashMap = new HashMap<>();
    private HashMap<String, String> createdHashMap = new HashMap<>();
    private String selectedColor, selectedDefinition;
    private int selectedColorPosition, selectedDefinitionPosition;
    private List<String> colorsList, definitionsList;
    private  ArrayAdapter<String> colorsAdapter, definitionsAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game1,container,false);
        ((GameActivity)getActivity()).getSupportActionBar().setTitle("Combine color & definition");
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        Bundle bundle = this.getArguments();
        if (bundle != null){
            colors = view.findViewById(R.id.fragmentGame1ColorsListView);
            definitions = view.findViewById(R.id.fragmentGame1DefinitionsListView);

            colorsList = new ArrayList<>(Arrays.asList(bundle.getStringArray("colors")));
            definitionsList = new ArrayList<>(Arrays.asList(bundle.getStringArray("definitions")));

            for(int i=0; i< colorsList.size(); i++){
                defaultHashMap.put(colorsList.get(i),definitionsList.get(i));
            }

            definitionsList = new ArrayList<>(Arrays.asList(ArtApp.mixStringArray(bundle.getStringArray("definitions"))));

            colorsAdapter = new ArrayAdapter<String>(view.getContext(),
                    android.R.layout.simple_list_item_1, colorsList);
            colors.setAdapter(colorsAdapter);

            definitionsAdapter = new ArrayAdapter<String>(view.getContext(),
                    android.R.layout.simple_list_item_1, definitionsList);
            definitions.setAdapter(definitionsAdapter);

            colors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    if(selectedColor != null){
                        ((TextView)colors.getChildAt(selectedColorPosition)).setBackgroundColor(view.getResources().getColor(R.color.defaultItem));
                        ((TextView)colors.getChildAt(selectedColorPosition)).setTextColor(view.getResources().getColor(android.R.color.black));
                        ((TextView)colors.getChildAt(position)).setBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                        ((TextView)colors.getChildAt(position)).setTextColor(view.getResources().getColor(R.color.defaultItem));
                        selectedColorPosition = position;
                        selectedColor = colors.getItemAtPosition(position).toString();
                    } else {
                        selectedColorPosition = position;
                        selectedColor = colors.getItemAtPosition(position).toString();
                        ((TextView)colors.getChildAt(position)).setBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                        ((TextView)colors.getChildAt(position)).setTextColor(view.getResources().getColor(R.color.defaultItem));
                    }

                    if(selectedDefinition != null){
                        createdHashMap.put(selectedColor,selectedDefinition);
                        cleanUpAfterSelections(view);
                    }
                }
            });

            definitions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    if(selectedDefinition != null){
                        ((TextView)definitions.getChildAt(selectedDefinitionPosition)).setBackgroundColor(view.getResources().getColor(R.color.defaultItem));
                        ((TextView)definitions.getChildAt(selectedDefinitionPosition)).setTextColor(view.getResources().getColor(android.R.color.black));
                        ((TextView)definitions.getChildAt(position)).setBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                        ((TextView)definitions.getChildAt(position)).setTextColor(view.getResources().getColor(R.color.defaultItem));
                        selectedDefinitionPosition = position;
                        selectedDefinition = definitions.getItemAtPosition(position).toString();
                    } else {
                        selectedDefinitionPosition = position;
                        selectedDefinition = definitions.getItemAtPosition(position).toString();
                        ((TextView)definitions.getChildAt(position)).setBackgroundColor(view.getResources().getColor(R.color.selectedItem));
                        ((TextView)definitions.getChildAt(position)).setTextColor(view.getResources().getColor(R.color.defaultItem));
                    }

                    if(selectedColor != null){
                        createdHashMap.put(selectedColor,selectedDefinition);
                        cleanUpAfterSelections(view);
                    }
                }
            });

        } else {
            ArtApp.log("Bundle is null in the setUp function of Game3Fragment.");
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
                //((TextView)colors.getChildAt(selectedColorPosition)).setTextAppearance(android.R.layout.simple_list_item_1);
                colors.getChildAt(selectedColorPosition).setBackgroundColor(view.getResources().getColor(R.color.defaultItem));
                ((TextView)colors.getChildAt(selectedColorPosition)).setTextColor(view.getResources().getColor(android.R.color.black));
                definitions.getChildAt(selectedDefinitionPosition).setBackgroundColor(view.getResources().getColor(R.color.defaultItem));
                ((TextView)definitions.getChildAt(selectedDefinitionPosition)).setTextColor(view.getResources().getColor(android.R.color.black));
                if(colorsList.isEmpty()){
                    checkResult();
                }
            }
        }, 200);
    }

    private void checkResult(){
        boolean result = true;
        for(String item : createdHashMap.keySet()){
            //ArtApp.log("key:" + item + ", default value: " +defaultHashMap.get(item) + ", selected value: " + createdHashMap.get(item));
            if(!defaultHashMap.get(item).equals(createdHashMap.get(item))){
                result = false;
                break;
            }
        }
        if(result)
            ArtApp.log("Game1Fragment answer is correct.");
        else
            ArtApp.log("Game1Fragment answer is bad.");
        ((GameActivity) getActivity()).changeFragment(result);
    }
}
