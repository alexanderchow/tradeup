package edu.uw.alexchow.tradeup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.uw.alexchow.tradeup.dummy.DummyContent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private final String TAG = "MainActivity";

    private boolean mTwoPane;
    public Firebase mFirebase;
    public SimpleItemRecyclerViewAdapter mAdapter;
    private View mRecylceView;

    // for location
    private int permissionCheck;
    private GoogleApiClient mGoogleApiClient;
    private static final int LOCATION_REQUEST_CODE = 1;

    private double longitude;
    private double latitude;

    public static String SESSION_USER = "";
    public static String LIST_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SESSION_USER = getIntent().getStringExtra(MainActivity.SESSION_USER);
        Log.v(TAG, "username = " + SESSION_USER);
        try {
            LIST_TYPE = getIntent().getStringExtra(MainActivity.LIST_TYPE);
        } catch (Exception e) {
            Log.v(TAG, "No such intent extra.");
        }
        if (LIST_TYPE == null) {
            LIST_TYPE = "1";
        }
        Log.v(TAG, "List type = " + LIST_TYPE);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setImageResource();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, TradeItemDetailActivity.class);
                intent.putExtra(TradeItemDetailFragment.ARG_ITEM_ID, "activityMainAdd");
                intent.putExtra(TradeItemDetailFragment.SESSION_USER, SESSION_USER);

                context.startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Firebase.setAndroidContext(this);


        if (findViewById(R.id.tradeitem_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mFirebase = new Firebase("https://project-5593274257047173778.firebaseio.com/items");

        DummyContent.clearItems();

        mFirebase.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TradeItem item = dataSnapshot.getValue(TradeItem.class);

                if (LIST_TYPE.equals("2")) {
                    if (item.posterName.equals(SESSION_USER)) {
                        Log.v(TAG, item.posterName);
                        DummyContent.addItem(item);
                    }
                } else if (LIST_TYPE.equals("3")){
                    DummyContent.addItem(item);
                } else {
                    //  if it's within 10 miles.
                    double longitudueCalcValue = Math.abs(longitude - item.longitude);
                    double latitudeCalcValue = Math.abs(latitude - item.latitude);
                    // getting the distance from user's location to item by doing a^2 + b^2 = c^2
                    // and also convert into miles:  1 lat or long = 69.1 miles

                    if (item.latitude != 0.0 && item.longitude != 0.0) {
                        if (Math.abs(Math.sqrt(longitudueCalcValue * longitudueCalcValue +
                                latitudeCalcValue * latitudeCalcValue)) * 69.1 < 10) {
                            DummyContent.addItem(item);
                        }
                    }
                }


                mRecylceView = findViewById(R.id.tradeitem_list);
                assert mRecylceView != null;
                setupRecyclerView((RecyclerView) mRecylceView);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }



    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<TradeItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<TradeItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tradeitem_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).title);
            holder.mImageView.setImageBitmap(StringToBitMap(mValues.get(position).image));
            holder.mContentView.setText(mValues.get(position).posterName);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(TradeItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        TradeItemDetailFragment fragment = new TradeItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.tradeitem_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, TradeItemDetailActivity.class);
                        intent.putExtra(TradeItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        intent.putExtra(TradeItemDetailFragment.SESSION_USER, SESSION_USER);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public final TextView mIdView;
            public final TextView mContentView;
            public TradeItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.list_image);
                mIdView = (TextView) view.findViewById(R.id.list_item_title);
                mContentView = (TextView) view.findViewById(R.id.list_item_posterName);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            Intent intent = new Intent(MainActivity.this, TradeItemDetailActivity.class);
            intent.putExtra(TradeItemDetailFragment.ARG_ITEM_ID, "activityMainAdd");
            startActivity(intent);
        } else if (id == R.id.nav_itemList) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra(MainActivity.SESSION_USER, SESSION_USER);
            intent.putExtra(MainActivity.LIST_TYPE, "3");
            finish();
            startActivity(intent);
        } else if (id == R.id.nav_ownList) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra(MainActivity.SESSION_USER, SESSION_USER);
            intent.putExtra(MainActivity.LIST_TYPE, "2");
            finish();
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



     /* Location code
     */

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest request = new LocationRequest();
        request.setInterval(5000);
        request.setFastestInterval(2000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, (com.google.android.gms.location.LocationListener) this);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.v(TAG, "Permission declined once inside shouldShowRequest..");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                Log.v(TAG, "Permission declined");
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // permission check
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){
            case LOCATION_REQUEST_CODE: { //if asked for location
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    onConnected(null); //do whatever we'd do when first connecting (try again)
                }
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void populateMasterList(TradeItem item) {
        // if it's within 10 miles.
        double longitudueCalcValue = Math.abs(longitude - item.longitude);
        double latitudeCalcValue = Math.abs(latitude - item.latitude);
        // getting the distance from user's location to item by doing a^2 + b^2 = c^2
        // and also convert into miles:  1 lat or long = 69.1 miles

        if (item.latitude != 0.0 && item.longitude != 0.0) {
            if (Math.abs(Math.sqrt(longitudueCalcValue * longitudueCalcValue +
                    latitudeCalcValue * latitudeCalcValue)) * 69.1 < 10) {
                DummyContent.addItem(item);
            }
        }
    }

    public void populateOwnList(TradeItem item) {
        // if it's our own item
        if (item.getPosterName() == SESSION_USER) {
            DummyContent.addItem(item);
        }
    }


}
