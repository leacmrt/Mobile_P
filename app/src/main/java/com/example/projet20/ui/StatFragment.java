package com.example.projet20.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projet20.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;


public class StatFragment extends Fragment {

    private SQLHelper sql;
    ArrayList<Integer> listStrength;
    ArrayList<Integer> listScore;
    public StatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_stat, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        sql = new SQLHelper();
        GraphView graph = (GraphView) view.findViewById(R.id.graphView);
        GraphView graph2 = (GraphView) view.findViewById(R.id.graphView2);





        new Thread(new Runnable() {

            public void run() {
                listStrength = sql.getallStrength(StatFragment.this.getContext());
                listScore=sql.getallScore(StatFragment.this.getContext());
                int taille = listStrength.size();
                int tailleS = listScore.size();
                DataPoint [] test  = new DataPoint [taille];
                DataPoint [] testS  = new DataPoint [tailleS];
                        for(int i=0;i<listStrength.size();i++)
                        {
                           DataPoint ho= new DataPoint(i,listStrength.get(i));
                           test[i]=ho;
                        }

                for(int i=0;i<listScore.size();i++)
                {
                    DataPoint ho= new DataPoint(i,listScore.get(i));
                    testS[i]=ho;
                }

               StatFragment.this.getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(test);
                        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(testS);
                        graph.addSeries(series1);
                        graph2.addSeries(series);


                    }
                });
                };


        }).start();

    }
    }