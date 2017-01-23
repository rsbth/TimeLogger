package timelogger.mprtcz.com.timelogger.activities;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Locale;

import lombok.Getter;
import timelogger.mprtcz.com.timelogger.utils.UiUtils;

public class SettingsListActivity extends ListActivity {
    public static final String TAG = "SettingsListActivity";

    Language[] languages = Language.values();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiUtils.loadLanguage(this);
        setArrayAdapter();
    }

    private void setArrayAdapter() {
        ArrayAdapter<Language> listAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                languages
        );
        ListView listView = getListView();
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(itemClickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Language language = languages[position];
            loadChosenLanguage(language.getLocale());
            Log.d(TAG, "language : " + language.getLocale());
            saveLanguage(language.getLocale().getCountry().toLowerCase());
            finish();
        }
    };

    private void loadChosenLanguage(Locale locale) {
        Log.d(TAG, "Setting default locale = " +locale);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void saveLanguage(String locale) {
        SharedPreferences languagepref = getApplicationContext().getSharedPreferences("language", 0);
        SharedPreferences.Editor editor = languagepref.edit();
        Log.d(TAG, "Saving language: " + locale);
        editor.putString("languageToLoad", locale);
        editor.apply();
    }

    @Getter
    private enum Language {
        POLISH(new Locale("pl", "PL")),
        ENGLISH(new Locale("en", "US"));

        private Locale locale;

        Language(Locale locale) {
            this.locale = locale;
        }
    }
}
