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
 //Classe pour ajouter un match
    private DBHelper mydb ;
    
    // Variable choisie arbitrairement permettant d'identifier l'intent que l'on va retoruner lors de l'appel de la fonction.
    private static int CAMERA_REQUEST =123,LOC_REQUEST=121;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    String server_url = "http://10.0.2.2/connect.php";


    //inputs
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

        picker1.setMinValue(0);//Caracteristiques des NumberPickers
        picker1.setMaxValue(9);

        picker2.setMinValue(0);
        picker2.setMaxValue(9);

        lala = new SQLHelper();//Acces à la BDD Mysql



        dat.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                //String de la date choisie dans le calendrier si changement est fait
                date1= day + "/" + month + "/" + year;
            }
        });
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent permet de start la caméra et capturer les images
                MediaStore.ACTION_IMAGE_CAPTURE est utilisé pour lancer l’application
                de caméra existante sur notre téléphone. Ici, on a un intent qui permet
                de renvoyer l’image capturé par la caméra.*/
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                /*lance une activité pour laquelle on veut récupérer le résultat. 
                Si cette activité existe, la méthode onActivityResult() sera appelé 
                grâce au requestCode (CAMERA_REQUEST ici) */
                startActivityForResult(intent,CAMERA_REQUEST);
            }
        });

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new History());//passe au fragment history
            }
        });
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Match.this.getActivity(), Localisation.class); //Ouvre l'activité localisation
                startActivityForResult(intent1, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });

        view.findViewById(R.id.NEW_Match).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Match.this.getContext());//Création d'une fenètre d'alèrte
                builder.setMessage("Are you sure ?")
                        .setTitle("Confirmation")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener()//Si choix = oui
                        {
                            public void onClick(DialogInterface dialog, int id) {

                                //Insertion dans la BDD SQLite du match (voir classe DBHelper pour la fonction)
                                mydb.insertmatch(Name1.getText().toString(),Name2.getText().toString(), Integer.toString(Score.getProgress()),
                                        Integer.toString(Strength.getProgress()),date1,
                                        Crtique.getText().toString(),returnString,photo,picker1.getValue(),picker2.getValue());//locale database


                                new Thread(new Runnable() //dans une nouvelle thread (obligée pour MYSQL )
                                {

                                    public void run() {
                                    //Insertion dans la BDD MYSQL du match ( voir classe SQLHelper)
                                        lala.ajout(Match.this.getActivity(),Match.this.getContext(),Name1,Name2,Strength,Score,dat,date1,Crtique,photo,imageView,returnString,picker1,picker2);
                                         }

                                }).start();//lancement de la thread


                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() //si choix = NON
                        {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(Match.this.getActivity(),"Ah ok",Toast.LENGTH_LONG).show();//Simple toast
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    private boolean loadFragment(Fragment fragment) //Fonction changement de fragment
    {
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
    
    /*Cette méthode est appelée lorsque l’activité qu’on a lance précédement existe.
    Elle nous rend le requestCode (CAMERA_REQUEST, l’integer fourni au debut permettant
    d’identifier de qui provient le résultat )
    , le resultCode (RESULT_OK) et quelconque data additionnel. */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Cette ligne check si le résultat est successful.
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            //Get the capture image
            //on recupere l’image capturé sous format Bitmap
            photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            //affiche la photo dans l’objet imageView
            imageView.setImageBitmap(photo);
        }


        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                returnString = data.getStringExtra("keyName");
                Toast.makeText(Match.this.getActivity(),returnString,Toast.LENGTH_LONG).show();
             //   Toast.makeText(Match.this.getActivity(),"lala",Toast.LENGTH_LONG).show();
            }
        }
}
}
