package edu.uw.alexchow.tradeup;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;

/**
 * Created by Arnav on 5/31/2016.
 */
public class LoginApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);
    }
}
