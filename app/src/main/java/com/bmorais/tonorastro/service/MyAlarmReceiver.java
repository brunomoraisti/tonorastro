package com.bmorais.tonorastro.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.widget.Toast;

import com.bmorais.tonorastro.PrincipalActivity;
import com.bmorais.tonorastro.firebase.MyNotificationManager;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class MyAlarmReceiver extends BroadcastReceiver {

    private static int alarmePeriodico = 280192;
    private static int alarmeUnico = 280193;

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Notificação em background", Toast.LENGTH_LONG).show();
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        MyNotificationManager mNotificationManager = new MyNotificationManager(context);
        mNotificationManager.showNotification("Broadcast", "broadcas receiver", "", intent);

    }

    public static void startAlertPeriodico(Context activity, int hora, int minuto) {

        // alarm first vibrate at 14 hrs and 40 min and repeat itself at ONE_HOUR interval

        Intent intent = new Intent(activity, MyAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity.getApplicationContext(), alarmePeriodico, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, 0);

        //2 minutos
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(activity, "Alarm will vibrate at time specified",
                Toast.LENGTH_SHORT).show();

        ComponentName receiver = new ComponentName(activity, MyAlarmReceiverBoot.class);
        PackageManager pm = activity.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }

    public void startAlertUnico(Context activity, int minutos) {
        Intent intent = new Intent(activity, MyAlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity.getApplicationContext(), alarmeUnico, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + ((minutos*60) * 1000), 10000, pendingIntent);

        Toast.makeText(activity, "Alarm will set in " + minutos + " seconds",
                Toast.LENGTH_LONG).show();

    }

    public static void stopAlertUnico(Context activity){

        Intent intent = new Intent(activity, MyAlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity.getApplicationContext(), alarmeUnico, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);

            ComponentName receiver = new ComponentName(activity, MyAlarmReceiverBoot.class);
            PackageManager pm = activity.getPackageManager();
            pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
    }

    public static void stopAlertPeriodico(Context activity){

        Intent intent = new Intent(activity, MyAlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity.getApplicationContext(), alarmePeriodico, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);

            ComponentName receiver = new ComponentName(activity, MyAlarmReceiverBoot.class);
            PackageManager pm = activity.getPackageManager();
            pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        }
    }
}