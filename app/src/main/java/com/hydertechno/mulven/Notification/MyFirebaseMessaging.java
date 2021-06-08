package com.hydertechno.mulven.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hydertechno.mulven.Activities.AboutActivity;
import com.hydertechno.mulven.Activities.MainActivity;
import com.hydertechno.mulven.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    public static final String TAG = "FirebaseMessaging";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private SharedPreferences sharedPreferences;
    String GROUP_KEY = "com.hydertechno.mulven";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        //Log.d(TAG, "onNewToken: updated token "+s);
//        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
//        editor.putString("newToken", s);
//        editor.apply();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            updateToken(s);
            //Toast.makeText(getApplicationContext(),"Your token updated",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        /*String sent = remoteMessage.getData().get("sent");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && sent.equals(firebaseUser.getUid())) {*/

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getOreoNotification(remoteMessage);
            } else {
                getNotification(remoteMessage);
            }

       // }
    }

    private void updateToken(String refreshToken) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
        userRef.child("token").setValue(refreshToken);

    }

    private void getOreoNotification(RemoteMessage remoteMessage) {
        String userID = remoteMessage.getData().get("sent");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String toActivity = remoteMessage.getData().get("toActivity");
        Bitmap bitmap = null;
        if (remoteMessage.getData().get("image")!=null && remoteMessage.getData().get("image").trim().length() > 2) {
            bitmap = getBitmapFromURL(remoteMessage.getData().get("image"));
        }

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(userID.replaceAll("[\\D]", ""));
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        switch (toActivity) {
            case "notification": {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("fragment","notification");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), j, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                OreoNotification oreoNotification = new OreoNotification(this);
//                NotificationCompat.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent);
                NotificationCompat.Builder builder = oreoNotification.getOreoNotificationImage(title, body, pendingIntent, bitmap);
                oreoNotification.getManager().notify(j, builder.build());
                break;
            }
            case "home": {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("fragment","home");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), j, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                OreoNotification oreoNotification = new OreoNotification(this);
//                NotificationCompat.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent);
                NotificationCompat.Builder builder = oreoNotification.getOreoNotificationImage(title, body, pendingIntent, bitmap);
                oreoNotification.getManager().notify(m, builder.build());
                break;
            }case "campaign": {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("fragment","campaign");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), j, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                OreoNotification oreoNotification = new OreoNotification(this);
//                NotificationCompat.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent);
                NotificationCompat.Builder builder = oreoNotification.getOreoNotificationImage(title, body, pendingIntent, bitmap);
                oreoNotification.getManager().notify(m, builder.build());
                break;
            }
            case "notification_with_image": {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("fragment","notification");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), j, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                OreoNotification oreoNotification = new OreoNotification(this);
                NotificationCompat.Builder builder = oreoNotification.getOreoNotificationImage(title, body, pendingIntent, bitmap);
                oreoNotification.getManager().notify(m, builder.build());
                break;
            }
        }
    }


    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    private void getNotification(RemoteMessage remoteMessage) {
        String userID = remoteMessage.getData().get("sent");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String bookingId = remoteMessage.getData().get("bookingId");
        String toActivity = remoteMessage.getData().get("toActivity");

        Bitmap bitmap = null;
        if (remoteMessage.getData().get("image").trim().length() > 2) {
            bitmap = getBitmapFromURL(remoteMessage.getData().get("image"));
        }
         /*//Assign big picture notification
        NotificationCompat.BigPictureStyle bpStyle = new NotificationCompat.BigPictureStyle();
        bpStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.book_car)).build();
        RemoteMessage.Notification notification = remoteMessage.getNotification();*/
        int j = Integer.parseInt(userID.replaceAll("[\\D]", ""));
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        switch (toActivity) {
            case "home": {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("fragment","home");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), m, intent, PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_applogo)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setShowWhen(true)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null))
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setColor(Color.parseColor("#ED1D24"))
                        .setVibrate(pattern)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                        .setSound(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notification))
                        .setContentIntent(pendingIntent);
                NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                noti.notify(m, builder.build());
                break;
            }
            case "campaign": {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("fragment","campaign");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), j, intent, PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_applogo)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setShowWhen(true)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null))
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setColor(Color.parseColor("#ED1D24"))
                        .setVibrate(pattern)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);
                NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                noti.notify(m, builder.build());
                break;
            }
            case "notification": {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("fragment","notification");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), j, intent, PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_applogo)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setShowWhen(true)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null))
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setColor(Color.parseColor("#ED1D24"))
                        .setVibrate(pattern)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);
                NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                noti.notify(m, builder.build());
                break;
            }
            case "notification_with_image": {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class).putExtra("fragment","notification");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), j, intent, PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_applogo)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setShowWhen(true)
                        .setLargeIcon(bitmap)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null))
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setColor(Color.parseColor("#ED1D24"))
                        .setVibrate(pattern)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);
                NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                noti.notify(m, builder.build());
                break;
            }

        }

    }
}
