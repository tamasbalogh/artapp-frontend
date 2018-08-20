package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.BuildConfig;
import baloghtamas.lali.artapp.GameActivity;
import baloghtamas.lali.artapp.R;

public class Game2Fragment extends Fragment {

    public static String TAG = "Game2Fragment";

    private ImageView image;
    private ListView numberedListVew, answersListView;
    private ArrayList<String> numberedList, answeresList, defaultList;
    private HashMap<String, String> numberedHashMap = new HashMap<>();
    private ArrayAdapter<String> numberedAdapter, answersAdapter;
    private int numberedListPosition = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game2, container, false);
        ((GameActivity)getActivity()).getSupportActionBar().setTitle("Combine number & word");
        setUp(view);
        return view;
    }

    private void setUp(View view) {
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
                numberedList.add(i + 1 + ".");
                numberedHashMap.put(i + 1 + ". ", null);
            }

            answersAdapter = new ArrayAdapter<String>(view.getContext(),
                    android.R.layout.simple_list_item_1, answeresList);
            answersListView.setAdapter(answersAdapter);

            numberedAdapter = new ArrayAdapter<String>(view.getContext(),
                    android.R.layout.simple_list_item_1, numberedList);
            numberedListVew.setAdapter(numberedAdapter);

            answersListView.setOnItemClickListener(answersOnItemClickListener);
            numberedListVew.setOnItemClickListener(numberedOnItemClickListener);
        } else {
            ArtApp.log("Bundle is null in the setUp function of Game3Fragment.");
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
            numberedListPosition = getNextNullPosition();
            numberedList.set(numberedListPosition, (numberedListPosition + 1) + ". " + value);
            numberedHashMap.put(numberedListPosition + 1 + ". ", value);
            answeresList.remove(position);
            answersAdapter.notifyDataSetChanged();
            numberedAdapter.notifyDataSetChanged();
            if(answeresList.isEmpty()){
                boolean result = true;
                for (int i = 0; i < defaultList.size(); i++) {
                    String key = (i+1) + ". ";
                    ArtApp.log("default: " + defaultList.get(i) + ", answer: " + numberedHashMap.get(key).toString());
                    if(!defaultList.get(i).toString().equals(numberedHashMap.get(key).toString())){
                        result = false;
                        break;
                    }
                }
                ((GameActivity)getActivity()).changeFragment(result);
            }
        }
    };

    AdapterView.OnItemClickListener numberedOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String value = numberedListVew.getItemAtPosition(position).toString();
            if(value.length()>3){
                String key = (position+1) + ". ";
                numberedList.set(position,key);
                numberedHashMap.put(key,null);
                answeresList.add(value.substring(3,value.length()));
                numberedAdapter.notifyDataSetChanged();
                answersAdapter.notifyDataSetChanged();
            }
        }
    };

    private int getNextNullPosition(){
        int position = 0;
        for (int i = 0; i < numberedHashMap.size(); i++) {
            if(numberedHashMap.get((i+1) +". ") == null){
                position = i;
                break;
            }
        }
        return position;
    }
}
