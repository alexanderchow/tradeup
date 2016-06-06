package edu.uw.alexchow.tradeup;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;


public class SettingsFragment extends PreferenceFragment {

    public static String SESSION_USER = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent intent = new Intent(MainActivity.class, MainActivity.class);
            intent.putExtra(MainActivity.LIST_TYPE, "2");
            intent.putExtra(MainActivity.SESSION_USER, SESSION_USER);
            context.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
