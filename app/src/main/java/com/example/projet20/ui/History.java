package com.example.projet20.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.projet20.R;

import java.util.ArrayList;

public class History extends Fragment {
//Classe pour afficher l'historique des matchs rentrés

    private SQLHelper lala;//Accès BDD MYSQL

    ArrayList<String> list_rempli = new ArrayList<String>();
    ArrayList<Integer> list_id = new ArrayList<>();


    public History(){}


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
        ListView lv = (ListView) view.findViewById(R.id.list);//utilisation d'une listview => possibilitée de scroll
        Button stat = (Button) view.findViewById(R.id.butSat);

        lala = new SQLHelper();

        final Bundle args = new Bundle();//plus utilisé
        args.putString("TAG", "Second_Frag");
        History.this.setArguments(args);


        new Thread(new Runnable()//nouvelle thread car accès à MYSQL ( action trop conséquente, sinon app crash)
        {

            public void run() {
                //liste remplie pour chaque match de NOM1 VS NOM2 : date
                list_rempli = lala.coucou(History.this.getContext());

                //liste remplie de tout les ID des match
                list_id=lala.getallID(History.this.getContext());

                //les 2 recupérées avec LA BDD MYSQL (voir classe SQLHelper)

                History.this.getActivity().runOnUiThread(new Runnable()//sur la thread principale
                {
                    public void run() {
                        //Pour remplir notre liste view, nous devons utiliser un ArrayAdapter
                        //Il nous permet de convertir une ArrayList d'objets en éléments View chargés dans le conteneur ListView
                        //Ici notre array liste remplie est la liste remplies des noms et dates de match
                        ArrayAdapter adapter1 = new ArrayAdapter<>(History.this.getActivity(), R.layout.listview, R.id.textView, list_rempli);

                        //R.layout.listView correspond au layout listview que nous avons créé pour configurer l'apparence de la liste
                        //R.layout.textView correspond au textview dans le layout "listview"


                        lv.setAdapter(adapter1);}


                });
            }}).start();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() //fonction en cliquant sur un des objet de la liste
        {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Toast.makeText(History.this.getActivity(),list_rempli.get(position),Toast.LENGTH_LONG).show();
                loadFragment(new SelectMatch(list_rempli,list_id,position));//overture du fragment "Select match"
                //On envoit la liste des matchs , des ID et la position du match dans la liste view (voir constructeur dans class SelectMatch)

              // loadFragment(new NotificationsFragment());
            }
        });
        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Match());//ouverture fragment Match
            }
        });

        view.findViewById(R.id.butSat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new StatFragment());//Ouverture fragment Statistique
            }
        });
    }
    private boolean loadFragment(Fragment fragment) //fonction chargment de fragment
     {

        if (fragment != null) {
            History.this.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();

            return true;
        }

        return false;
    }
}