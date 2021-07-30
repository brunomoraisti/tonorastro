package com.bmorais.tonorastro.service;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.bmorais.tonorastro.model.EncomendasModel;
import com.bmorais.tonorastro.dao.EncomendasDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MyWorkerManager extends Worker {

    private Context context;
    private static final String TAG = MyWorkerManager.class.getSimpleName();


    public MyWorkerManager(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @Override
    public Result doWork() {

        // Do the work here--in this case, upload the images.
        //uploadImages();
        Log.v("### BACKGROUND","TESTE DE EXECUAÇÃO EM BACKGROUND");

        try {

            EncomendasDao encomendasDao = new EncomendasDao(context);
            ArrayList<EncomendasModel> encomendasModelArrayList = null;
            try {
                encomendasDao.abrir();
                encomendasModelArrayList = encomendasDao.buscarEncomendaPendente();
                encomendasDao.fechar();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            if (encomendasModelArrayList !=null)
                EncomendasModel.buscarAlteracoes(context, encomendasModelArrayList, false);

            //Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
            //MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
           // mNotificationManager.showNotification("Teste", "Mensagem de teste", "", intent);

            Log.v(TAG, "### SUCESSO WORK MANAGER");
            return Result.success();
        } catch (Throwable throwable) {
            // clean up and log
            Log.e(TAG, "### ERROR WORK MANAGER: ", throwable);
            return Result.failure();
        }
    }

    public static void ativarWorkManager(Activity activity, int minutos, String tag) {

        cancelarWorkManager(activity,tag);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest saveRequest = new PeriodicWorkRequest.Builder(MyWorkerManager.class, minutos, TimeUnit.MINUTES)
                .setConstraints(Constraints.NONE)
                .addTag(tag)
                .build();

        WorkManager.getInstance(activity).enqueue(saveRequest);
    }

    public static void cancelarWorkManager(Activity activity, String tag) {

        WorkManager.getInstance(activity).cancelAllWorkByTag(tag);
    }
}