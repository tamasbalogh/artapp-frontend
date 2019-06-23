package baloghtamas.lali.artapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import baloghtamas.lali.artapp.ArtApp;
import baloghtamas.lali.artapp.GameActivity;
import baloghtamas.lali.artapp.R;

public class ResultFragment extends Fragment implements View.OnClickListener {

    public static  String TAG = "ResultFragment";

    TextView correctAnswerTv, wrongAsnwerTv;
    Button newGame, home;
    PieChart chart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result,container,false);
        setUp(view);
        return view;
    }

    public static ResultFragment newInstance(float correct, float wrong){
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putFloat("correct",correct);
        args.putFloat("wrong",wrong);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragmentResultHomeButton:
                //getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                break;
            case R.id.fragmentResultNewGameButton:
                getActivity().startActivity(new Intent(getActivity(), GameActivity.class));
                getActivity().finish();
                break;
        }
    }

    public void setUp(View view) {
        home = view.findViewById(R.id.fragmentResultHomeButton);
        home.setOnClickListener(this);
        newGame = view.findViewById(R.id.fragmentResultNewGameButton);
        newGame.setOnClickListener(this);
        correctAnswerTv = view.findViewById(R.id.fragmentResultCorrectAnswerTextView);
        wrongAsnwerTv = view.findViewById(R.id.fragmentResultWrongAnswerTextView);

        chart = view.findViewById(R.id.fragmentResultPieChart);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            float correct = bundle.getFloat("correct");
            float wrong = bundle.getFloat("wrong");

            correctAnswerTv.setText(correctAnswerTv.getText().toString()  + " " + correct);
            wrongAsnwerTv.setText(wrongAsnwerTv.getText().toString() + " " + wrong);

            setPieChart(correct,wrong);
        } else {
            ArtApp.log("Bundle is null in the setUp function of ResultFragment.");
        }


    }

    private void setPieChart(float correct, float wrong) {
        List<PieEntry> entries;
        entries = new ArrayList<>();
        entries.add(new PieEntry(wrong, "Wrong answers"));
        entries.add(new PieEntry(correct,"Correct answers"));
        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setDrawValues(false);
        PieData pieData = new PieData(dataSet);
        pieData.setValueTextColor(getResources().getColor(R.color.defaultItem));
        pieData.setValueTextSize(18);
        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorPrimary));
        colors.add(getResources().getColor(R.color.selectedItem));
        dataSet.setColors(colors);
        chart.setData(pieData);
        chart.setHoleColor(getResources().getColor(R.color.defaultItem));
        chart.setTouchEnabled(false);
        chart.animateY(2000);
        chart.getDescription().setEnabled(false);
        chart.setDrawEntryLabels(false);
        chart.getLegend().setEnabled(false);
        chart.setCenterText(Float.toString(calculatePercentage(correct,wrong)) +  " %");
        chart.setCenterTextSizePixels(80);
    }

    private float calculatePercentage(float correct, float wrong){
        float max = correct + wrong;
        return Math.round((correct * 100.0f) / max);
    }
}
