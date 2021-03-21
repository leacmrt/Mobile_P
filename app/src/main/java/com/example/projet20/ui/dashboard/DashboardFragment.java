package com.example.projet20.ui.dashboard;

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

import com.example.projet20.R;

import java.util.ArrayList;
//pas utilisé
public class DashboardFragment extends Fragment {

    ArrayList<String> listtmp;
    int id;
    public DashboardFragment(ArrayList<String> list_rempli, int position) {
        listtmp=list_rempli;
        id=position;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.textView5);
        textView.setText(listtmp.get(id));
        return root;
    }
}