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

        Landscape =(Switch) getView().findViewById(R.id.switchLandscape);
        Mode =(Switch) getView().findViewById(R.id.switchDark);
        Lang =(Switch) getView().findViewById(R.id.switchEnglish);

        int la = getResources().getConfiguration().orientation;

        if(la==1)
        {
            Landscape.setChecked(false);
            Landscape.setTextColor(Color.BLUE);
            Landscape.setText("OFF");

        }else if(la==2)
        {
            Landscape.setChecked(true);
            Landscape.setTextColor(Color.GREEN);
            Landscape.setText("ON");
        }


        Landscape.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(NotificationsFragment.this.getActivity(), String.valueOf(la),Toast.LENGTH_LONG).show();
                if(Landscape.isChecked())
                {

                    Landscape.setTextColor(Color.GREEN);
                    Landscape.setText("ON");
                    NotificationsFragment.this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                }else
                {
                    Landscape.setTextColor(Color.BLUE);
                    Landscape.setText("OFF");
                    NotificationsFragment.this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);}
            }
        });

        Drawable modet = (Drawable) ((ConstraintLayout)getView().findViewById(R.id.test)).getBackground();
        int mode = AppCompatDelegate.getDefaultNightMode();

        if(mode==1)
        {
            Mode.setChecked(false);
            Mode.setTextColor(Color.BLUE);
            Mode.setText("OFF");

        }else if(mode==2)
        {
            Mode.setChecked(true);
            Mode.setTextColor(Color.GREEN);
            Mode.setText("ON");
        }


        Mode.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {


                if(Mode.isChecked())
                {

                    Mode.setTextColor(Color.GREEN);
                    Mode.setText("ON");
                   AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_YES);
                    // FragmentSetting.this.getActivity().recreate();

                }else if(!Mode.isChecked())
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
        //Toast.makeText(MainActivity.this, blankFragment.getTag(),Toast.LENGTH_LONG).show();
        if(langt.equals("French"))
        {
            Lang.setChecked(false);
            Lang.setTextColor(Color.BLUE);
            Lang.setText("OFF");

        }else if(langt.equals("Anglais"))
        {
            Lang.setChecked(true);
            Lang.setTextColor(Color.GREEN);
            Lang.setText("ON");
        }


        Lang.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {


                if(Lang.isChecked())
                {


                    Lang.setTextColor(Color.GREEN);
                    Lang.setText("ON");
                    // LocaleHelper.setLocale(thiscontext,"fr"); //for french;
                    LocaleHelper.setLocale(NotificationsFragment.this.getActivity().getBaseContext(),"fr");
                    NotificationsFragment.this.getActivity().recreate();
                    // LocaleHelper.setLocale(la.getContext(),"fr");

                }else if(!Lang.isChecked())
                {
                    Lang.setTextColor(Color.BLUE);
                    Lang.setText("OFF");
                    LocaleHelper.setLocale(NotificationsFragment.this.getActivity().getBaseContext(),"en");
                    NotificationsFragment.this.getActivity().recreate();
                }
            }

        });



    }
    public static class LocaleHelper {

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

        public static void setLocale(Context context, String language) {
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

            editor.putString(SELECTED_LANGUAGE, language);
            editor.apply();
        }

        private static void updateResources(Context context, String language) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);

            Resources resources = context.getResources();

            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;

            resources.updateConfiguration(configuration, resources.getDisplayMetrics());


        }
    }

}