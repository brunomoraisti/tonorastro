package com.bmorais.tonorastro.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

import com.bmorais.tonorastro.firebase.MyNotificationManager;
import com.bmorais.tonorastro.lib.Funcoes;

/**
 * Para essa função funcionar necessita:
 * 1 - colocar o receiver no manifest.xml
 * 2 - Fazer outro serviço para funcionar quando o celular for reiniciado
 *
 */
public class MyAlarmReceiverBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        /*if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            MyAlarmReceiver.startAlertPeriodico(context,8,0);
        }*/
    }
}