package com.hydertechno.mulven.Notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.hydertechno.mulven.R;


public class OreoNotification extends ContextWrapper {

    private static final String CHANNEL_ID = "com.hydertechno.mulven";
    private static final String CHANNEL_NAME = "mulven";
    private static final String CHANNEL_ID2 = "Ringtone";
    private static final String CHANNEL_NAME2 = "Mulven";

    private NotificationManager notificationManager;

    public OreoNotification(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            createChannel2();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notification);
        //Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build();

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setSound(sound,audioAttributes);
        channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel2() {
        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notification);
        //Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();

        NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID2, CHANNEL_NAME2, NotificationManager.IMPORTANCE_DEFAULT);
        channel2.enableLights(true);
        channel2.enableVibration(true);
        channel2.setSound(sound,audioAttributes);
        channel2.setImportance(NotificationManager.IMPORTANCE_HIGH);
        channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel2);
    }

    public NotificationManager getManager(){
        if (notificationManager == null){
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return  notificationManager;
    }
    /*Drawable myDrawable = getResources().getDrawable(R.drawable.ic_applogo);
    Bitmap smallImage1      = ((BitmapDrawable) myDrawable).getBitmap();*/
    @TargetApi(Build.VERSION_CODES.O)
    public  NotificationCompat.Builder getOreoNotification(String title, String body, PendingIntent pendingIntent){
        // Assign big picture notification
//        /*NotificationCompat.BigPictureStyle bpStyle = new NotificationCompat.BigPictureStyle();
//        bpStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.customer_care)).build();*/
        //Bitmap smallImage=BitmapFactory.decodeResource(getResources(),R.drawable.customer_care);
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_applogo)
                .setContentTitle(title)
                .setContentText(body)
                .setShowWhen(true)
                //.setLargeIcon(smallImage)
                //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(smallImage).bigLargeIcon(null))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(pendingIntent)
                .setColor(Color.parseColor("#ED1D24"))
                .setAutoCancel(true);

    }

    @TargetApi(Build.VERSION_CODES.O)
    public  NotificationCompat.Builder getOreoNotificationImage(String title, String body, PendingIntent pendingIntent, Bitmap image){

        // Assign big picture notification
//        /*NotificationCompat.BigPictureStyle bpStyle = new NotificationCompat.BigPictureStyle();
//        bpStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.customer_care)).build();*/
        //Bitmap smallImage=BitmapFactory.decodeResource(getResources(),R.drawable.customer_care);
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_applogo)
                .setContentTitle(title)
                .setContentText(body)
                .setShowWhen(true)
                .setLargeIcon(image)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).bigLargeIcon(null))
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(pendingIntent)
                .setColor(Color.parseColor("#ED1D24"))
                .setAutoCancel(true);

    }

    @TargetApi(Build.VERSION_CODES.O)
    public  NotificationCompat.Builder getOreoNotification3(String title, String body, PendingIntent pendingIntent){
        // Assign big picture notification
//        /*NotificationCompat.BigPictureStyle bpStyle = new NotificationCompat.BigPictureStyle();
//        bpStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.customer_care)).build();*/
        //Bitmap smallImage=BitmapFactory.decodeResource(getResources(),R.drawable.customer_care);
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID2)
                .setSmallIcon(R.drawable.ic_applogo)
                .setContentTitle(title)
                .setContentText(body)
                .setShowWhen(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .addAction(R.drawable.applogo,"Show",pendingIntent)
                .setContentIntent(pendingIntent)
                .setColor(Color.parseColor("#ED1D24"))
                .setAutoCancel(true);

    }
}
