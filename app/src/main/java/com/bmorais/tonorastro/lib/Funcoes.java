package com.bmorais.tonorastro.lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bmorais.tonorastro.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Funcoes {

    public boolean mostrarMensagem(Activity activity, String titulo, final String mensagem, final String positivo, final String negativo) {


        final boolean[] click = {false};

        AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
        alerta.setTitle(titulo)
            .setMessage(mensagem)
            .setPositiveButton(positivo,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        click[0] =true;
                    }
                });
        if (negativo!=null) {
            alerta.setNegativeButton(negativo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    click[0] = false;
                }
            });
                }
        alerta.show();

        return click[0];

    }

    public static void showMessageOKCancel(String message, Activity activity, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity).setTitle("ALERTA!").setMessage(message).setPositiveButton("OK", okListener).create().show();
    }


    public static boolean verificaPermissao(final Activity activity, String permissao) {
        int permission = ContextCompat.checkSelfPermission(activity, permissao);
        final List<String> listPermissionsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null && permissao != null) {
            if (permission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permissao);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
                return false;
            }
        }
        return true;
    }

    public static String getDateSQL(){
        SimpleDateFormat formataData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = new Date();
        String dataFormatada = formataData.format(data);
        return dataFormatada;
    }

    public static String formatDataHoraUsuario(Date datahora) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateString = sdf.format(datahora);
        return dateString;
    }

    public static boolean checaFDS(Calendar data)
    {
        boolean retorno = true;
        // se for domingo
        if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            retorno = false;
        } else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            retorno = false;
        } else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            retorno = false;
        } else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            retorno = false;
        } else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            retorno = false;
        } else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            retorno = false;
        } else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

        }

        return retorno;
    }

    public static final boolean verificaConexao(Context context) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    public static AdSize getAdSize(Activity activity) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public static void loadBanner(Activity activity, AdView adView) {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."

        List<String> testDeviceIds = Arrays.asList(activity.getResources().getString(R.string.deviceTest));
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
        AdRequest adRequest = new AdRequest.Builder().build();

        AdSize adSize = getAdSize(activity);
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);
        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    public static void setThemaDayNight(Activity activity) {
        // 1 - DAY; 2 - NIGHT
        if (SharedPrefManager.getInstance(activity).pegarCampo(Variaveis.THEMA_TELA).equals("1")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public static void copyText(Activity activity, String texto){
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(null, texto);
        if (clipboard == null)
            return;
        clipboard.setPrimaryClip(clip);
        Toast.makeText(activity, "Texto copiado", Toast.LENGTH_SHORT).show();
    }

    public static CharSequence colarText(Activity activity){
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        CharSequence textToPaste = "";
        try {
            textToPaste = clipboard.getPrimaryClip().getItemAt(0).getText();
        } catch (Exception e) {
            return textToPaste;
        }
        return textToPaste;
    }




}
