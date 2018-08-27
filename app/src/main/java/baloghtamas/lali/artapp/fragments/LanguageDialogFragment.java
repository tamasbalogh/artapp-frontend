package baloghtamas.lali.artapp.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import baloghtamas.lali.artapp.R;

public class LanguageDialogFragment extends DialogFragment {

    private ListView listview;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_language_dialog, container, false);

        list.add("English");
        list.add("Germany");
        list.add("France");
        list.add("Finland");

        listview = view.findViewById(R.id.fragmentLanguageListView);
        adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
        return view;
    }
}