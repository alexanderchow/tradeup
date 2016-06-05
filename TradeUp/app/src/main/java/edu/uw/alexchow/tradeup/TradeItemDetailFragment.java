package edu.uw.alexchow.tradeup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.uw.alexchow.tradeup.dummy.DummyContent;

/**
 * A fragment representing a single TradeItem detail screen.
 * This fragment is either contained in a {@link TradeItemListActivity}
 * in two-pane mode (on tablets) or a {@link TradeItemDetailActivity}
 * on handsets.
 */
public class TradeItemDetailFragment extends Fragment implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    private String TAG = "trade item detail fragment";

    // for location
    private int permissionCheck;
    private GoogleApiClient mGoogleApiClient;
    private static final int LOCATION_REQUEST_CODE = 1;
    private double longitude;
    private double latitude;

    private String encodedImage;


    public static final String ARG_ITEM_ID = "item_id";
    public static String SESSION_USER = "";

    /**
     * The dummy content this fragment is presenting.
     */
    private TradeItem mItem;
    private boolean addActivity = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TradeItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SESSION_USER = MainActivity.SESSION_USER;

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            if (getArguments().getString(ARG_ITEM_ID).equals("activityMainAdd")) {
                addActivity = true;
                mItem = new TradeItem();
                mItem.setTitle("Create an Item");
            } else {
                mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout =
                    (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.title);
            }
        }
//        if (getArguments().containsKey(SESSION_USER)) {
//            SESSION_USER = getArguments().getString(SESSION_USER);
//        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    private int PICK_IMAGE_REQUEST = 1;
    private static int REQUEST_PICTURE_CODE = 0;
    public static final int RESULT_OK = -1;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "Result received: "+data);
        Bitmap imageBitmap = null;
        if (requestCode == REQUEST_PICTURE_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            encodedImage = BitMapToString(imageBitmap);

        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                        imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        double curWidth = imageBitmap.getWidth();
        double curHeight = imageBitmap.getHeight();

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double screenWidth = size.x * 3 / 4;  // 3/4 of the screen width

        double newHeight = curHeight * screenWidth / curWidth;

        Bitmap resizedBitMap = Bitmap.createScaledBitmap(imageBitmap, (int) Math.round(screenWidth),
                (int) Math.round(newHeight), true);

        ImageView imageView = (ImageView)getActivity().findViewById(R.id.imageView);
        imageView.setImageBitmap(resizedBitMap);
        encodedImage = BitMapToString(resizedBitMap);

        super.onActivityResult(requestCode, resultCode, data);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView;
        // Show the dummy content as text in a TextView.
        if (addActivity) {
            rootView = inflater.inflate(R.layout.tradeitem_add, container, false);


            Button photo = (Button) rootView.findViewById(R.id.takePicture);
            Button select = (Button) rootView.findViewById(R.id.selectPicture);

            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG, "Camera button pressed");
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getContext().getPackageManager()) != null)
                        startActivityForResult(intent, REQUEST_PICTURE_CODE);
                }
            });

            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                }
            });


            Button btnSubmit = (Button) rootView.findViewById(R.id.add_submit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    EditText addTitle = (EditText)  rootView.findViewById(R.id.add_title);
                    EditText addDescription = (EditText) rootView.findViewById(R.id.add_description);

                    Calendar calendar = Calendar.getInstance();
                    Long currentTime = calendar.getTimeInMillis();

                    SimpleDateFormat sdf = new SimpleDateFormat("h:mm a MM/dd");
                    String timeStamp = sdf.format(currentTime);

                    TradeItem newItem = new TradeItem();
                    newItem.setTitle(addTitle.getText().toString());
                    newItem.setId("100");
                    newItem.setDescription(addDescription.getText().toString());
                    newItem.setPosterName(SESSION_USER);
                    newItem.setTime(timeStamp);
                    newItem.setLatitude(latitude);
                    newItem.setLongitude(longitude);
                    newItem.setImage(encodedImage);


                    if (encodedImage == null) {
                        Toast.makeText(getContext(), "Please post a picture of an item.",
                                Toast.LENGTH_SHORT).show();
                    } else if (newItem.getTitle() == null) {
                        Toast.makeText(getContext(), "Please enter a title for your item.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        new Firebase("https://project-5593274257047173778.firebaseio.com/items")
                                .push().setValue(newItem);
                        Toast.makeText(getContext(), "Item posted successfully!",
                                Toast.LENGTH_SHORT).show();

                        Context context = view.getContext();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra(MainActivity.LIST_TYPE, "2");
                        intent.putExtra(MainActivity.SESSION_USER, SESSION_USER);
                        context.startActivity(intent);
                    }
                }
            });

        } else if (mItem != null) {
            rootView = inflater.inflate(R.layout.tradeitem_detail, container, false);

            ImageView detailImage = (ImageView)rootView.findViewById(R.id.detailImageView);
            TextView detailPoster = (TextView)rootView.findViewById(R.id.tradeitem_posterName);
            TextView detailTime = (TextView) rootView.findViewById(R.id.tradeitem_time);
            TextView detailDescription = (TextView) rootView.findViewById(R.id.trade_item_description);


            detailImage.setImageBitmap(StringToBitMap(mItem.image));
            detailPoster.setText("Posted by: " + mItem.posterName);
            detailTime.setText("Posted on: " + mItem.time);
            detailDescription.setText("Description: " + mItem.description);

        } else {
            rootView = inflater.inflate(R.layout.tradeitem_detail, container, false);

        }
        return rootView;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG,"onConnected");

        LocationRequest request = new LocationRequest();
        request.setInterval(5000); // 4 minutes
        request.setFastestInterval(2000); // 2 minutes
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request,
                    (com.google.android.gms.location.LocationListener) this);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.v(TAG, "Permission declined once inside shouldShowRequest..");
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
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

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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

}
