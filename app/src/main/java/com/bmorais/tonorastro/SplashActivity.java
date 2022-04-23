package com.bmorais.tonorastro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.bmorais.tonorastro.dao.EncomendasDao;
import com.bmorais.tonorastro.lib.Funcoes;
import com.bmorais.tonorastro.lib.SharedPrefManager;
import com.bmorais.tonorastro.lib.Variaveis;
import com.bmorais.tonorastro.service.MyWorkerManager;

import java.sql.SQLException;

public class SplashActivity extends AppCompatActivity {

    EncomendasDao encomendasDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);;
        setContentView(R.layout.activity_splash);



        //PARA NÃO DAR ERRO A PRIMEIRA VEZ NO BANCO
        encomendasDao = new EncomendasDao(this);
        try {
            encomendasDao.abrir();
            encomendasDao.fechar();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SQLiteReadOnlyDatabaseException e) {
            e.printStackTrace();
        }

        //INICIALIZANDO VARIAVEL
        if (SharedPrefManager.getInstance(this).pegarCampo(Variaveis.REMOVE_ADS)==null)
            SharedPrefManager.getInstance(this).gravarCampo(Variaveis.REMOVE_ADS,"0");

        if (SharedPrefManager.getInstance(this).pegarCampo(Variaveis.NOTIFICACOES)==null) {
            SharedPrefManager.getInstance(this).gravarCampo(Variaveis.NOTIFICACOES, "1");
        }

        if (SharedPrefManager.getInstance(this).pegarCampo(Variaveis.ATUALIZACAO)==null) {
            SharedPrefManager.getInstance(this).gravarCampo(Variaveis.ATUALIZACAO, "1");
            SharedPrefManager.getInstance(this).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS, "30");
            SharedPrefManager.getInstance(this).gravarCampo(Variaveis.ATUALIZACAO_MINUTOS_NOME, "30 minutos");
            MyWorkerManager.ativarWorkManager(this,30,Variaveis.ATUALIZACAO);
        }


        if (SharedPrefManager.getInstance(this).pegarCampo(Variaveis.THEMA_TELA)==null) {
            SharedPrefManager.getInstance(this).gravarCampo(Variaveis.THEMA_TELA, "1");
            Funcoes.setThemaDayNight(this);
        }

        // ATIVAR AGENDADOR DE TAREFAS
        /*if (SharedPrefManager.getInstance(this).pegarCampo(Variaveis.WORKMANAGER)==null) {
            SharedPrefManager.getInstance(this).gravarCampo(Variaveis.WORKMANAGER,"1");
            MyWorkerManager.ativarWorkManager(this,30,"updateEncomendas");
            MyAlarmReceiver.startAlertPeriodico(this,8,0);
        }*/

        // CHAMAR A TELA PRINCIPAL COM O TEMPO ESTIPUILADO
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, PrincipalActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, 1500);

    }
}