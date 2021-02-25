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

    private SQLHelper lala;
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
        ListView lv = (ListView) view.findViewById(R.id.list);
        Button stat = (Button) view.findViewById(R.id.butSat);
        lala = new SQLHelper();
        final Bundle args = new Bundle();
        args.putString("TAG", "Second_Frag");
        History.this.setArguments(args);


        new Thread(new Runnable() {

            public void run() {

                list_rempli = lala.coucou(History.this.getContext());
                list_id=lala.getallID(History.this.getContext());

                History.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ArrayAdapter adapter1 = new ArrayAdapter<>(History.this.getActivity(), R.layout.listview, R.id.textView, list_rempli);
                        lv.setAdapter(adapter1);}


                });
            }}).start();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Toast.makeText(History.this.getActivity(),list_rempli.get(position),Toast.LENGTH_LONG).show();
                loadFragment(new SelectMatch(list_rempli,list_id,position));
              // loadFragment(new NotificationsFragment());
            }
        });
        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new Match());
            }
        });

        view.findViewById(R.id.butSat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new StatFragment());
            }
        });
    }
    private boolean loadFragment(Fragment fragment) {

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