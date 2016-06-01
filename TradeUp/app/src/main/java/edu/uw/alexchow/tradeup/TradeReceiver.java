package edu.uw.alexchow.tradeup;

/**
 * Created by WillyWu on 2016/6/1.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;


/**
 * Created by WillyWu on 2016/4/25.
 */
public class TradeReceiver extends BroadcastReceiver {
    private final static String MY_MESSAGE = "edu.uw.alexchow.tradeup.newItem";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(MY_MESSAGE.equals(intent.getAction())){
            new AlertDialog.Builder(context)
                    .setMessage("Receiving new item")
                    .show();
        }
    }
    /*private static String TAG = "Receiver";
    private int notifies; // in case I need it in the future
    private static final int NOTIFY_DEMO_CODE = 3;
    private static final int TEST_NOTIFY_ID = 0;
    private static final int SMS_SEND_CODE = 2;

    // when received any intent that is mentioned in the Manifest class
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction() == Intent.){
            Toast.makeText(context, "Battery is low!", Toast.LENGTH_SHORT).show();
        } else if(intent.getAction() == Intent.ACTION_POWER_DISCONNECTED){
            Toast.makeText(context, "Power disconnected!", Toast.LENGTH_SHORT).show();
        } else if(intent.getAction() == Intent.ACTION_POWER_CONNECTED){
            Toast.makeText(context, "Power connected!", Toast.LENGTH_SHORT).show();
        }
    }

    // build notification at the given context and set the destination to the page where you look
    // at the messages.
    public void notify(Context context, String author,  String text){
        notifies++;
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(author)
                .setContentText(text);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setVibrate(new long[]{0, 500, 500, 5000});
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(SendActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(NOTIFY_DEMO_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(TEST_NOTIFY_ID, builder.build()); //post the notification!
    }*/
}

