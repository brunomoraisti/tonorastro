package com.bmorais.tonorastro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bmorais.tonorastro.adapter.AdapterAndamentos;
import com.bmorais.tonorastro.lib.Funcoes;
import com.bmorais.tonorastro.model.AndamentosModel;
import com.bmorais.tonorastro.model.EncomendasModel;
import com.bmorais.tonorastro.dao.AndamentosDao;
import com.bmorais.tonorastro.dao.EncomendasDao;
import com.bmorais.tonorastro.lib.SharedPrefManager;
import com.bmorais.tonorastro.lib.Variaveis;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class AndamentoObjetoActivity extends AppCompatActivity {

    private TextView tvTitulo, tvQtdDias;
    private RecyclerView mRecyclerView;
    private AdapterAndamentos adapter;
    private ArrayList<AndamentosModel> arrayPassosEmergencias = null;
    private ProgressBar pBar;
    public int codencomenda = 0;
    public LinearLayout llAdviewAndamento;
    private AdView mAdView;
    public LinearLayout btnCompartilhar;
    private int mLastDayNightMode;
    private FrameLayout adContainerView;

    public String titulo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLastDayNightMode = Integer.parseInt(SharedPrefManager.getInstance(this).pegarCampo(Variaveis.THEMA_TELA));
        Funcoes.setThemaDayNight(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_andamento_objeto);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_passos_emergencia);

        btnCompartilhar = findViewById(R.id.llCompartilhar);
        tvQtdDias = findViewById(R.id.tvQtdDias);
        adContainerView = findViewById(R.id.adview_container_andamento);
        llAdviewAndamento = this.findViewById(R.id.llAdviewAndamento);

        // PROPAGANDAS
        if (SharedPrefManager.getInstance(this).pegarCampo(Variaveis.REMOVE_ADS).contains("1")) {
            llAdviewAndamento.setVisibility(View.GONE);
        } else {
            mAdView = new AdView(this);
            mAdView.setAdUnitId("ca-app-pub-1598135185151212/3494799366");
            adContainerView.addView(mAdView);
            adContainerView.post(new Runnable() {
                @Override
                public void run() {
                    Funcoes.loadBanner(AndamentoObjetoActivity.this, mAdView);
                }
            });
        }

        pBar = (ProgressBar) this.findViewById(R.id.progressPassosEmergencias);
        pBar.setVisibility(View.VISIBLE);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_passos_emergencia);
        ImageView imgEmergencia = (ImageView) findViewById(R.id.imgEmergenciaPassos);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            /*toolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(getResources().getColor(R.color.preto)));*/
            titulo = bundle.getString("nome");
            codencomenda = bundle.getInt("codencomenda");
            //Picasso.get().load(Variaveis.URL_IMAGEM+bundle.getString("IMAGEM")).into(imgEmergencia);
        }

        buscarEncomenda(codencomenda);

        //getSupportActionBar().setTitle(titulo);
        toolbar.setTitle(titulo);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //LISTA DE GRUPOS
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcvPassosEmergencias);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AdapterAndamentos(this);
        mRecyclerView.setAdapter(adapter);
        arrayPassosEmergencias = new ArrayList<AndamentosModel>();

        carregaInformacoes();

        /*appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //  Vertical offset == 0 indicates appBar is fully expanded.
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false;
                    invalidateOptionsMenu();
                } else {
                    appBarExpanded = true;
                    invalidateOptionsMenu();
                }
            }
        });*/


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //EVENTO ONCLICK
        btnCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = "*APP Tonorastro*\nInformações sobre o objeto *"+titulo+"*\n\n";
                for (AndamentosModel passos: arrayPassosEmergencias){
                    msg += "*"+passos.getDataandamento()+"* - "+passos.getAction()+"\n\n";
                }
                msg += "\nAcompanhe suas encomendas com o *Tonorastro - Rastreamento de Encomendas*, baixe agora: http://bit.ly/app-tonorastro\n";

                final Intent emailIntent = new Intent();
                emailIntent.setAction(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_TEXT, msg);
                emailIntent.setType("text/plain");
                try {
                    AndamentoObjetoActivity.this.startActivity(Intent.createChooser(emailIntent, "Selecione o local"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AndamentoObjetoActivity.this, "Não possui aplicativo", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void carregaInformacoes() {

        AndamentosDao andamentosDao = new AndamentosDao(AndamentoObjetoActivity.this);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    andamentosDao.abrir();
                    arrayPassosEmergencias = andamentosDao.buscarAndamentosEncomenda(codencomenda);
                    andamentosDao.fechar();
                    adapter.addAll(arrayPassosEmergencias);
                    pBar.setVisibility(View.GONE);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();

    }

    private void buscarEncomenda(int codencomenda) {

        EncomendasDao encomendaDao = new EncomendasDao(AndamentoObjetoActivity.this);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    encomendaDao.abrir();
                    EncomendasModel encomendasModel = encomendaDao.buscar(codencomenda);
                    encomendasModel.setLeitura(1);
                    encomendaDao.atualizar(encomendasModel);
                    encomendaDao.fechar();

                    tvQtdDias.setText(encomendasModel.getQtddias()+" Dia(s)");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLastDayNightMode!=Integer.parseInt(SharedPrefManager.getInstance(this).pegarCampo(Variaveis.THEMA_TELA)))
            recreate();
    }
}