package com.example.projet20.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.projet20.R;

import java.util.Locale;

public class NotificationsFragment extends Fragment {

    //fragment de paramètre
    Context thiscontext;
    Switch Landscape =null;
    Switch Mode =null;
    Switch Lang = null;
    public NotificationsFragment(){}
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thiscontext = container.getContext();
       return inflater.inflate(R.layout.fragment_notifications, container, false);

    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Landscape =(Switch) getView().findViewById(R.id.switchLandscape); //recupère les 3 switch de changements
        Mode =(Switch) getView().findViewById(R.id.switchDark);
        Lang =(Switch) getView().findViewById(R.id.switchEnglish);

        int la = getResources().getConfiguration().orientation;//orientation de l'application

        if(la==1)//si elle est en mode portrait
        {
            Landscape.setChecked(false);//initalisation couleur + texte
            Landscape.setTextColor(Color.BLUE);
            Landscape.setText("OFF");

        }else if(la==2)//si elle est en mode paysage
        {
            Landscape.setChecked(true);//pareil mais en version VRAIE
            Landscape.setTextColor(Color.GREEN);
            Landscape.setText("ON");
        }


        Landscape.setOnClickListener(new View.OnClickListener()//bouton switch pour changer l'orientation
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(NotificationsFragment.this.getActivity(), String.valueOf(la),Toast.LENGTH_LONG).show();
                if(Landscape.isChecked())//si il passe en mode "ON" donc paysage
                {

                    Landscape.setTextColor(Color.GREEN);
                    Landscape.setText("ON");
                    NotificationsFragment.this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    //on change l'oriangation à l'aide de SetRequestedOrientation

                }else
                {    //situation inverse
                    Landscape.setTextColor(Color.BLUE);
                    Landscape.setText("OFF");
                    NotificationsFragment.this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);}
            }
        });

        Drawable modet = (Drawable) ((ConstraintLayout)getView().findViewById(R.id.test)).getBackground();
        int mode = AppCompatDelegate.getDefaultNightMode();//recupère le mode de couleur utlisé

        if(mode==1)//cas mode jour
        {
            Mode.setChecked(false);
            Mode.setTextColor(Color.BLUE);
            Mode.setText("OFF");

        }else if(mode==2)//cas mode nuit
        {
            Mode.setChecked(true);
            Mode.setTextColor(Color.GREEN);
            Mode.setText("ON");
        }


        Mode.setOnClickListener(new View.OnClickListener()//boutton switch pour changer le mode de couleur
        {

            @Override
            public void onClick(View v)
            {


                if(Mode.isChecked())//si le mode nuit devient "ON"
                {

                    Mode.setTextColor(Color.GREEN);
                    Mode.setText("ON");
                   AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_YES);//Utilisation de APPcompatDelegate.setDefaultNightMode pour activer le mode nuit
                    // FragmentSetting.this.getActivity().recreate();

                }else if(!Mode.isChecked())//Situation contraire
                {
                    Mode.setTextColor(Color.BLUE);
                    Mode.setText("OFF");
                   AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_NO);
                    //  FragmentSetting.this.getActivity().recreate();
                }
            }

        });

        String langt = (String) ((TextView)getView().findViewById(R.id.textView)).getText();
        //Pour savoir dans quelle mode de langue l'activité est en ce moment
        //On prend le texte de changment de langue

        if(langt.equals("French"))//si il est en anglais(=French) cela veut dire que l'application est en anglais
        {
            Lang.setChecked(false);
            Lang.setTextColor(Color.BLUE);
            Lang.setText("OFF");

        }else if(langt.equals("Anglais"))//contraire
        {
            Lang.setChecked(true);
            Lang.setTextColor(Color.GREEN);
            Lang.setText("ON");
        }


        Lang.setOnClickListener(new View.OnClickListener()//boutton switch pour changer la langue
        {

            @Override
            public void onClick(View v)
            {


                if(Lang.isChecked())//si la langue devient anglais
                {


                    Lang.setTextColor(Color.GREEN);
                    Lang.setText("ON");

                    LocaleHelper.setLocale(NotificationsFragment.this.getActivity().getBaseContext(),"fr");
                    //utilisation de la classe LocaleHelper pour avec comme paramètre "fr"
                    NotificationsFragment.this.getActivity().recreate();//on doit recréer l'app pour que les changement soient prit en compte


                }else if(!Lang.isChecked())//cas contraire
                {
                    Lang.setTextColor(Color.BLUE);
                    Lang.setText("OFF");
                    LocaleHelper.setLocale(NotificationsFragment.this.getActivity().getBaseContext(),"en");
                    NotificationsFragment.this.getActivity().recreate();
                }
            }

        });



    }
    public static class LocaleHelper { //classe utilisée pour changer la langue de l'app

        private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

        public static void onCreate(Context context) {

            String lang;
            if(getLanguage(context).isEmpty()){
                lang = getPersistedData(context, Locale.getDefault().getLanguage());
            }else {
                lang = getLanguage(context);
            }

            setLocale(context, lang);
        }

        public static void onCreate(Context context, String defaultLanguage) {
            String lang = getPersistedData(context, defaultLanguage);
            setLocale(context, lang);
        }

        public static String getLanguage(Context context) {
            return getPersistedData(context, Locale.getDefault().getLanguage());
        }

        public static void setLocale(Context context, String language) { //la fonction qu'on utilise
            persist(context, language);
            updateResources(context, language);
        }

        private static String getPersistedData(Context context, String defaultLanguage) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
        }

        private static void persist(Context context, String language) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(SELECTED_LANGUAGE, language);//applique le langage choisis
            editor.apply();
        }

        private static void updateResources(Context context, String language) {
            Locale locale = new Locale(language); //update nos choix
            Locale.setDefault(locale);

            Resources resources = context.getResources();

            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;

            resources.updateConfiguration(configuration, resources.getDisplayMetrics());//jusqu'à la configuration


        }
    }

}