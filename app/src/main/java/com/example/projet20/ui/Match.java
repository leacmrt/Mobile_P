package com.example.projet20.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.projet20.Localisation;
import com.example.projet20.R;
import com.example.projet20.ui.home.HomeFragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;


public class Match extends Fragment {
    private DBHelper mydb ;
    private static int CAMERA_REQUEST =123,LOC_REQUEST=121;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    String server_url = "http://10.0.2.2/connect.php";

    EditText Name1 =null;
    EditText Name2 = null;
    EditText Crtique =null;
    SeekBar Score =null;
    SeekBar Strength = null;
    CalendarView dat =null;
    NumberPicker picker1 = null;
    NumberPicker picker2 =null;

    private  SQLHelper lala;
    ImageView imageView;
    String date1;
    Bitmap photo ;
    Blob image;
    String returnString ="Inconnu";


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

        Name1 =(EditText)view.findViewById(R.id.Name1);
        Name2 = (EditText)view.findViewById(R.id.Name2);
        Score = (SeekBar) view.findViewById(R.id.seekBarScore);
        Strength =(SeekBar) view.findViewById(R.id.seekBarStrength);
        dat  = (CalendarView) view.findViewById(R.id.calendarView2); // get the reference of CalendarView
        imageView = (ImageView) view.findViewById(R.id.imageView);
        Crtique= view.findViewById(R.id.Critique);
        picker1 = view.findViewById(R.id.Score1);
        picker2 = view.findViewById(R.id.Score2);

        picker1.setMinValue(0);
        picker1.setMaxValue(9);

        picker2.setMinValue(0);
        picker2.setMaxValue(9);

        lala = new SQLHelper();


        //show the selected date as a toast
        dat.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                date1= day + "/" + month + "/" + year;
            }
        });
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST);
            }
        });

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new History());
            }
        });
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Match.this.getActivity(), Localisation.class);
                startActivityForResult(intent1, SECOND_ACTIVITY_REQUEST_CODE);        }
        });

        view.findViewById(R.id.NEW_Match).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Match.this.getContext());
                builder.setMessage("Are you sure ?")
                        .setTitle("Confirmation")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.insertmatch(Name1.getText().toString(),Name2.getText().toString(), Integer.toString(Score.getProgress()),Integer.toString(Strength.getProgress()),date1,Crtique.getText().toString(),returnString,photo,picker1.getValue(),picker2.getValue());//locale database


                                new Thread(new Runnable() {

                                    public void run() {

                                        lala.ajout(Match.this.getActivity(),Match.this.getContext(),Name1,Name2,Strength,Score,dat,date1,Crtique,photo,imageView,returnString,picker1,picker2);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            //Get the capture image
            photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bos);

            imageView.setImageBitmap(photo);
        }


        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                returnString = data.getStringExtra("keyName");
                Toast.makeText(Match.this.getActivity(),returnString,Toast.LENGTH_LONG).show();
                Toast.makeText(Match.this.getActivity(),"lala",Toast.LENGTH_LONG).show();
            }
        }
}
}