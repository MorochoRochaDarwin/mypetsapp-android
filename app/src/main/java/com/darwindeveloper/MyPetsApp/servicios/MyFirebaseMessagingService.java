package com.darwindeveloper.MyPetsApp.servicios;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.darwindeveloper.MyPetsApp.NotificacionActivity;
import com.darwindeveloper.MyPetsApp.R;
import com.darwindeveloper.MyPetsApp.sqlite.DBHelper;
import com.darwindeveloper.MyPetsApp.sqlite.DBManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by DARWIN MOROCHO on 10/8/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String message = remoteMessage.getFrom();

        Log.i("FirebaseMessage", "mensaje de " + message);


        if (remoteMessage.getNotification() != null) {
            // Log.i("FirebaseNotificacion", remoteMessage.getNotification().getBody());
            mostrarNotificacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());

        }

        if (remoteMessage.getData().size() > 0) {
            //  Log.i("FirebaseData", "" + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            DBManager dbManager = new DBManager(this);
            dbManager.save(data.get("titulo"), data.get("tipo"), data.get("establecimiento_id"), data.get("nombre_establecimiento"), data.get("html"), data.get("publicado"));


        }
    }


    private void mostrarNotificacion(String title, String body, Map<String, String> data) {

        Intent intent = new Intent(this, NotificacionActivity.class);
        intent.putExtra("titulo", data.get("titulo"));
        intent.putExtra("establecimiento_id", data.get("establecimiento_id"));
        intent.putExtra("nombre_establecimiento", data.get("nombre_establecimiento"));
        intent.putExtra("publicado", data.get("publicado"));
        intent.putExtra("html", data.get("html"));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());


    }
}
