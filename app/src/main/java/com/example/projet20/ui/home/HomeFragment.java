package com.example.projet20.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projet20.R;
import com.example.projet20.ui.History;
import com.example.projet20.ui.Match;

public class HomeFragment extends Fragment {

    public HomeFragment(){

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



          return inflater.inflate(R.layout.fragment_home, container, false);


    };



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       view.findViewById(R.id.New).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             loadFragment(new Match());

            }
        });

        view.findViewById(R.id.History).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 loadFragment(new History());

            }
        });

    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            HomeFragment.this.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    }
