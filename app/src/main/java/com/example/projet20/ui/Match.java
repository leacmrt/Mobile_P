package com.example.projet20.ui;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.projet20.R;
import com.example.projet20.ui.home.HomeFragment;


public class Match extends Fragment {
    private DBHelper mydb ;

    String server_url = "http://10.0.2.2/connect.php";
    EditText Name1 =null;
    EditText Name2 = null;
    SeekBar Score =null;
    SeekBar Strength = null;
    CalendarView dat =null;
    private  SQLHelper lala;
    //Mysql d=null;
    public  Match()
    {}
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mydb = new DBHelper(Match.this.getContext());
        // connectMySql = new Mysql(FirstFragment.this.getContext());
        Name1 =(EditText)view.findViewById(R.id.Name1);
        Name2 = (EditText)view.findViewById(R.id.Name2);
        Score = (SeekBar) view.findViewById(R.id.seekBarScore);
        Strength =(SeekBar) view.findViewById(R.id.seekBarScore);
        dat  = (CalendarView) view.findViewById(R.id.calendarView2); // get the reference of CalendarView

        lala = new SQLHelper();


        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new History());
            }
        });

        view.findViewById(R.id.NEW_Match).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Match.this.getContext());
                builder.setMessage("Are you sure ?")
                        .setTitle("Confirmation")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(Match.this.getActivity(),"YOUPI",Toast.LENGTH_LONG).show();
                                mydb.insertmatch(Name1.getText().toString(),Name2.getText().toString(), Integer.toString(Score.getProgress()),Integer.toString(Strength.getProgress()),dat.getDate());//locale database

                                //Sender s=new Sender(FirstFragment.this.getContext(),server_url,Strength,Score,Name1,Name2);
                                //s.execute();
                                new Thread(new Runnable() {

                                    public void run() {

                                        lala.ajout(Match.this.getActivity(),Match.this.getContext(),Name1,Name2,Strength,Score,dat);
                                        // lala.ajout(FirstFragment.this.getActivity(),FirstFragment.this.getContext(),Name1.getText().toString(),Name2.getText().toString(),Score.getProgress(),Strength.getProgress());
                                    }

                                }).start();


                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(Match.this.getActivity(),"Ah ok",Toast.LENGTH_LONG).show();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            Match.this.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}