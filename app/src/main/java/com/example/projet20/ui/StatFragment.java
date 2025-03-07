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
        sql = new SQLHelper();//accès à la base de donnée MYSQL

        GraphView graph = (GraphView) view.findViewById(R.id.graphView);//graph technique
        GraphView graph2 = (GraphView) view.findViewById(R.id.graphView2);//graph force


        view.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new History()); //changement pour le fragment history
            }
        });


        new Thread(new Runnable() {//dans une nouvelle thread

            public void run() {

                listStrength = sql.getallStrength(StatFragment.this.getContext());//Tableau remplis de toutes les forces
                listScore = sql.getallScore(StatFragment.this.getContext());//tableau remplis de toutes les technques
                //les 2 sont récupérés à partir la BDD externe MYSQL

                int taille = listStrength.size();
                int tailleS = listScore.size();

                DataPoint [] test  = new DataPoint [taille];//création de 2 tableaux de datapoints
                DataPoint [] testS  = new DataPoint [tailleS];

                        for(int i=0;i<listStrength.size();i++)//remplissage des datapoints à l'aide des données
                        {
                           DataPoint ho= new DataPoint(i,listStrength.get(i));
                           test[i]=ho;
                        }

                        for(int i=0;i<listScore.size();i++)
                        {
                            DataPoint ho= new DataPoint(i,listScore.get(i));
                            testS[i]=ho;
                        }

               StatFragment.this.getActivity().runOnUiThread(new Runnable() //dans la thread principale
               {

                    @Override
                    public void run() {
                        //2 Graph series remplis de nos tableaux de datapoints
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(test);
                        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(testS);

                        //Ajout des series dans les graphs
                        graph.addSeries(series1);
                        graph2.addSeries(series);


                    }
                });
                };


        }).start();

    }
    private boolean loadFragment(Fragment fragment)//fonction de changement de fragment
    {
        //switching fragment
        if (fragment != null) {
            StatFragment.this.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    }