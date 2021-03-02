package com.example.projet20.ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet20.R;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;


public class SelectMatch extends Fragment{
    ArrayList<String> listtmp;
    ArrayList<Integer> listid;
    int position,id;
    String Name1,Name2,Critique,Local;
    Blob im;
    Bitmap pho;

    int Score,Strength;
    private SQLHelper lala;

        public  SelectMatch(ArrayList<String> list_rempli,ArrayList<Integer>ListID, int position) {
            listtmp=list_rempli;
            listid=ListID;
            id=listid.get(position);

        }
        @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState
        ) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_select_match, container, false);
        }

        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

            super.onViewCreated(view, savedInstanceState);

            //Toast.makeText(SelectMatch.this.getContext(),Integer.toString(id),Toast.LENGTH_LONG).show();

            final TextView textView = view.findViewById(R.id.textview_second);
            TextView textName1 = view.findViewById(R.id.textName);
            TextView textName2 = view.findViewById(R.id.textName2);
            TextView textScore = view.findViewById(R.id.textScore);
            TextView textStrength = view.findViewById(R.id.textStrength);
            TextView textCritique = view.findViewById(R.id.textView13);
            TextView textLocal = view.findViewById(R.id.textView14);
            ImageView photo =view.findViewById(R.id.imageView2);
            lala = new SQLHelper();

            new Thread(new Runnable() {

                public void run() {

                    try {
                        Name1 = lala.getName1(id);
                        Name2 = lala.getName2(id);
                        Score = lala.getScore(id);
                        Strength = lala.getStrength(id);
                        Critique=lala.getCritique(id);
                        Local=lala.getLocalisation(id);
                        im =lala.getPicture(id);

                        SelectMatch.this.getActivity().runOnUiThread(
                                new Runnable() {
                                 public void run() {


                                     byte[] blobAsBytes = new byte[0];
                                     try {
                                         blobAsBytes = im.getBytes(1, (int) im.length());
                                         pho = BitmapFactory.decodeByteArray(blobAsBytes,0,blobAsBytes.length);
                                     } catch (SQLException throwables) {
                                         throwables.printStackTrace();
                                     }

                                textName1.setText(Name1);
                                textName2.setText(Name2);
                                textScore.setText(Integer.toString(Strength));
                                textStrength.setText(Integer.toString(Score  ));
                                textCritique.setText(Critique);
                                textLocal.setText(Local);
                                photo.setImageBitmap(pho);
                            }
                        });
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


                }}).start();


            view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadFragment(new History());
                }
            });
        }
        private boolean loadFragment(Fragment fragment) {
            //switching fragment
            if (fragment != null) {
                SelectMatch.this.getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                return true;
            }
            return false;
        }
}