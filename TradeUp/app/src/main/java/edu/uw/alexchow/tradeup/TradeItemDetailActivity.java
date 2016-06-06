package edu.uw.alexchow.tradeup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

/**
 * An activity representing a single TradeItem detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link TradeItemListActivity}.
 */
public class TradeItemDetailActivity extends AppCompatActivity{

    private String TAG = "TradeItemDetailActivity";
    public static String SESSION_USER = "";

    public static double CURRENT_LONGITUDE;
    public static double CURRENT_LATITUDE;

    private double longitude;
    private double latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SESSION_USER = getIntent().getStringExtra(TradeItemDetailActivity.SESSION_USER);

        try {
            CURRENT_LONGITUDE = getIntent().getDoubleExtra("longitude", MainActivity.CURRENT_LONGITUDE);
            CURRENT_LATITUDE = getIntent().getDoubleExtra("latitude", MainActivity.CURRENT_LATITUDE);
            longitude = CURRENT_LONGITUDE;
            latitude = CURRENT_LATITUDE;
        } catch (Exception e) {
            Log.v(TAG, "No such intent extra current location");
        }

        setContentView(R.layout.activity_tradeitem_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(TradeItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(TradeItemDetailFragment.ARG_ITEM_ID));
            TradeItemDetailFragment fragment = new TradeItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.tradeitem_detail_container, fragment)
                    .commit();
        }
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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(MainActivity.LIST_TYPE, "3");
            intent.putExtra(MainActivity.SESSION_USER, SESSION_USER);
            intent.putExtra("longitude", longitude);
            intent.putExtra("latitude", latitude);
            NavUtils.navigateUpTo(this, intent );
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
