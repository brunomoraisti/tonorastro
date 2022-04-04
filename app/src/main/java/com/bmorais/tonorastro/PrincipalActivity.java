package com.bmorais.tonorastro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bmorais.tonorastro.adapter.PageAdapter;
import com.bmorais.tonorastro.model.AndamentosModel;
import com.bmorais.tonorastro.model.EncomendasModel;
import com.bmorais.tonorastro.dao.AndamentosDao;
import com.bmorais.tonorastro.dao.EncomendasDao;
import com.bmorais.tonorastro.http.RetrofitInicializador;
import com.bmorais.tonorastro.lib.Funcoes;
import com.bmorais.tonorastro.pages.TodosPage;
import com.bmorais.tonorastro.service.MyAlarmReceiver;
import com.bmorais.tonorastro.lib.SharedPrefManager;
import com.bmorais.tonorastro.lib.Variaveis;
import com.bmorais.tonorastro.pages.ConfigPage;
import com.bmorais.tonorastro.pages.EntreguesPage;
import com.bmorais.tonorastro.pages.PendentesPage;
import com.bmorais.tonorastro.service.MyWorkerManager;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrincipalActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    public ViewPager viewPager;
    public static PageAdapter pageAdapter;
    BottomNavigationView navigation;
    AppBarLayout.LayoutParams params;
    AppBarLayout appBarLayout;
    public MenuItem buscar;
    public MenuItem atualizar;
    public ProgressBar pBar;

    private AdView mAdView;
    private FrameLayout adContainerView;
    private LinearLayout llAdviewPrincipal;
    private Menu menu;
    private int mLastDayNightMode;

    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLastDayNightMode = Integer.parseInt(SharedPrefManager.getInstance(this).pegarCampo(Variaveis.THEMA_TELA));
        Funcoes.setThemaDayNight(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        view = findViewById(android.R.id.content);

        adContainerView = findViewById(R.id.adview_container_principal);

        pBar = view.findViewById(R.id.progressBarPrincipal);
        //pBar.setVisibility(View.VISIBLE);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBar_Emergencias);
        toolbar = (Toolbar) findViewById(R.id.toolbar_emergencia);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        toolbar.setLayoutParams(params);

        viewPager = (ViewPager) findViewById(R.id.pager);
        ArrayList<Fragment> fr_list = new ArrayList<Fragment>();
        fr_list.add(new TodosPage());
        fr_list.add(new PendentesPage());
        fr_list.add(new EntreguesPage());
        fr_list.add(new ConfigPage());

        pageAdapter = new PageAdapter(getSupportFragmentManager(), fr_list);

        //BUTTON NAVIGATION
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_todos:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_pendentes:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_entregue:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.navigation_config:
                        viewPager.setCurrentItem(3);
                        return true;
                }
                return false;
            }
        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager.setAdapter(pageAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CharSequence textoColado = Funcoes.colarText(PrincipalActivity.this);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PrincipalActivity.this);

                View viewInflated = LayoutInflater.from(PrincipalActivity.this).inflate(R.layout.frame_altera_membro, null, false);
                final TextInputEditText inputObjeto = viewInflated.findViewById(R.id.tvObjeto);
                final TextInputEditText inputNomeObjeto = viewInflated.findViewById(R.id.tvNomeObjeto);
                final TextInputLayout inputObjetoLayout = viewInflated.findViewById(R.id.tvObjetoLayout);
                final TextInputLayout inputNomeObjetoLayout = viewInflated.findViewById(R.id.tvNomeObjetoLayout);
                final MaterialButton btnIncluir = viewInflated.findViewById(R.id.btnFrameIncluir);
                final MaterialButton btnCancelar = viewInflated.findViewById(R.id.btnFrameCancelar);

                if (textoColado.length() == 13) {
                    inputObjeto.setText(textoColado);
                    inputNomeObjeto.requestFocus();
                } else {
                    inputObjeto.requestFocus();
                }

                alertDialog.setView(viewInflated);
                AlertDialog dialog = alertDialog.create();

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                dialog.show();

                btnIncluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String objeto = inputObjeto.getText().toString();
                        String nomeObjeto = inputNomeObjeto.getText().toString();

                        if (inputObjeto.getText().toString().isEmpty()) {
                            inputObjetoLayout.setError("Informe o código de rastreamento");
                            return;
                        } else if (inputObjeto.getText().toString().length() != 13) {
                            inputObjetoLayout.setError("O código precisa ter 13 caractéres");
                            return;
                        } else if (inputNomeObjeto.getText().toString().isEmpty()) {
                            inputNomeObjetoLayout.setError("Informe a descrição da encomenda");
                            return;
                        }

                        insereObjeto(objeto, nomeObjeto);
                        dialog.dismiss();
                    }
                });

                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
            }
        });

        atualizarLista();

    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    buscar.setVisible(true);
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
                    toolbar.setLayoutParams(params);
                    appBarLayout.setExpanded(true, true);
                    navigation.setSelectedItemId(R.id.navigation_todos);
                    break;
                case 1:
                    buscar.setVisible(false);
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
                    toolbar.setLayoutParams(params);
                    appBarLayout.setExpanded(true, true);
                    navigation.setSelectedItemId(R.id.navigation_pendentes);
                    break;
                case 2:
                    buscar.setVisible(false);
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
                    toolbar.setLayoutParams(params);
                    appBarLayout.setExpanded(true, true);
                    navigation.setSelectedItemId(R.id.navigation_entregue);
                    break;
                case 3:
                    buscar.setVisible(false);
                    params.setScrollFlags(0);
                    toolbar.setLayoutParams(params);
                    appBarLayout.setExpanded(true, true);
                    navigation.setSelectedItemId(R.id.navigation_config);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        //finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_emergencias, menu);

        buscar = menu.findItem(R.id.itemMenuBuscar);
        atualizar = menu.findItem(R.id.itemMenuAtualizar);
        viewPager.addOnPageChangeListener(pageChangeListener);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(buscar);
        searchView.setOnQueryTextListener(this);

        buscar.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                ((TodosPage) PrincipalActivity.pageAdapter.getItem(0)).adapter.setSearchResult(((TodosPage) PrincipalActivity.pageAdapter.getItem(0)).arrayEncomendas);


                return true; // Return true to collapse action view

            }
        });

        atualizar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                atualizarLista();

                return false;
            }
        });

        return true;

    }

    public void atualizarLista() {
        Snackbar.make(view, "Aguarde! Buscando andamentos...", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        EncomendasDao encomendasDao = new EncomendasDao(PrincipalActivity.this);
        ArrayList<EncomendasModel> encomendasModelArrayList = null;
        try {
            encomendasDao.abrir();
            encomendasModelArrayList = encomendasDao.buscarEncomendaPendente();
            encomendasDao.fechar();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (encomendasModelArrayList != null) {
            EncomendasModel.buscarAlteracoes(PrincipalActivity.this, encomendasModelArrayList, true);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<EncomendasModel> filteredModelList = filter(((TodosPage) PrincipalActivity.pageAdapter.getItem(0)).arrayEncomendas, newText);
        ((TodosPage) PrincipalActivity.pageAdapter.getItem(0)).adapter.setSearchResult(filteredModelList);
        return true;
    }

    private ArrayList<EncomendasModel> filter(ArrayList<EncomendasModel> models, String query) {
        query = query.toLowerCase();
        final ArrayList<EncomendasModel> filteredModelList = new ArrayList<>();
        for (EncomendasModel emergencia : models) {
            final String text = emergencia.getNome().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(emergencia);
            }
        }

        if (filteredModelList.size() < 1) {
            ((TodosPage) PrincipalActivity.pageAdapter.getItem(0)).mSwipeRefreshLayout.setVisibility(View.GONE);
            ((TodosPage) PrincipalActivity.pageAdapter.getItem(0)).semEncomenda.setVisibility(View.VISIBLE);
        } else {
            ((TodosPage) PrincipalActivity.pageAdapter.getItem(0)).mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            ((TodosPage) PrincipalActivity.pageAdapter.getItem(0)).semEncomenda.setVisibility(View.GONE);
        }
        return filteredModelList;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void insereObjeto(String objeto, String nomeobjeto) {


        if (Funcoes.verificaConexao(PrincipalActivity.this)) {
            //pBar.setVisibility(View.VISIBLE);

            Call<JsonObject> call = new RetrofitInicializador().getRetrofitService().getRastreamento(objeto);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful() && response.body().toString() != null) {

                        if (response.body().get("error").toString().equals("false")) {
                            JSONObject json = null;
                            JSONArray jsonAndamentos = null;
                            EncomendasModel encomenda = null;
                            AndamentosModel andamento = new AndamentosModel();

                            EncomendasDao encomendasDao = new EncomendasDao(PrincipalActivity.this);
                            AndamentosDao andamentosDao = new AndamentosDao(PrincipalActivity.this);

                            try {
                                json = new JSONObject(response.body().toString());
                                jsonAndamentos = json.getJSONArray("progress");

                                encomenda = new EncomendasModel(nomeobjeto, Funcoes.getDateSQL(), objeto, json.getInt("days"));
                                encomenda.setLeitura(1); // OBJETO LIDO
                                encomenda.setSituacao(1); // OBJETO COM ENTREGA PENDENTE

                                encomendasDao.abrir();
                                encomenda.setCodencomenda((int) encomendasDao.inserir(encomenda));
                                encomendasDao.fechar();

                                for (int i = 0; i < jsonAndamentos.length(); i++) {
                                    JSONObject item = jsonAndamentos.getJSONObject(i);

                                    andamento.setDataandamento(item.getString("date"));
                                    andamento.setLocal(item.getString("location"));
                                    andamento.setAction(item.getString("action"));
                                    andamento.setMessage(item.getString("message"));
                                    andamento.setEncomenda(encomenda);

                                    if (item.getString("action").contains("Objeto entregue")) {
                                        encomenda.setSituacao(0); // SETA 0 PARA ENTREGA REALIZADA; 1 PARA PENDENTE
                                        encomendasDao.abrir();
                                        encomendasDao.atualizar(encomenda);
                                        encomendasDao.fechar();
                                    }

                                    andamentosDao.abrir();
                                    andamentosDao.inserir(andamento);
                                    andamentosDao.fechar();

                                        /*if (!item.getString("action").contains("Objeto entregue")) {
                                            andamentosDao.abrir();
                                            andamentosDao.inserir(andamento);
                                            andamentosDao.fechar();
                                        }*/


                                }

                                if (encomenda.getCodencomenda() > 0) {
                                    Snackbar.make(view, "Objeto cadastrado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    onResume();
                                } else {
                                    Snackbar.make(view, "Objeto não cadastrado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            //pBar.setVisibility(View.GONE);
                            buscarEncomendas();

                            //((PendentesPage) PrincipalActivity.pageAdapter.getItem(1)).adapter.add(0,encomenda);

                            //arrayObjetos.add(encomenda);
                            //adapter.addAll(arrayObjetos);

                        } else {

                            EncomendasModel encomenda = null;
                            EncomendasDao encomendasDao = new EncomendasDao(PrincipalActivity.this);

                            encomenda = new EncomendasModel(nomeobjeto, Funcoes.getDateSQL(), objeto, 0);
                            encomenda.setLeitura(1);
                            encomenda.setSituacao(2); // OBJETO SEM ANDAMENTOS

                            try {
                                encomendasDao.abrir();
                                encomenda.setCodencomenda((int) encomendasDao.inserir(encomenda));
                                encomendasDao.fechar();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }

                            buscarEncomendas();
                            //pBar.setVisibility(View.GONE);
                            Snackbar.make(view, "Objeto não encontrado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }

                        Log.i("##VOLLEY##", "REGISTRADO COM SUCESSO " + response.body().get("error"));
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.i("##VOLLEY##", "FALHA AO REGISTRAR");
                }
            });

        } else {
            EncomendasModel encomenda = null;
            EncomendasDao encomendasDao = new EncomendasDao(PrincipalActivity.this);

            encomenda = new EncomendasModel(nomeobjeto, Funcoes.getDateSQL(), objeto, 0);
            encomenda.setLeitura(1);
            encomenda.setSituacao(2); // OBJETO SEM ANDAMENTOS

            try {
                encomendasDao.abrir();
                encomenda.setCodencomenda((int) encomendasDao.inserir(encomenda));
                encomendasDao.fechar();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            buscarEncomendas();
            Snackbar.make(view, "Sem internet!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.v("### JWT: ", RetrofitInicializador.getTokenJWT(""));

        llAdviewPrincipal = findViewById(R.id.llAdviewPrincipal);
        //VARIAVEL DO PROJETO DA PROPAGANDA
        if (SharedPrefManager.getInstance(this).pegarCampo(Variaveis.REMOVE_ADS).equals("1")) {
            llAdviewPrincipal.setVisibility(View.GONE);
        } else {
            mAdView = new AdView(this);
            mAdView.setAdUnitId("ca-app-pub-1598135185151212/3492791757");
            adContainerView.addView(mAdView);
            adContainerView.post(new Runnable() {
                @Override
                public void run() {
                    Funcoes.loadBanner(PrincipalActivity.this, mAdView);
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mLastDayNightMode != Integer.parseInt(SharedPrefManager.getInstance(this).pegarCampo(Variaveis.THEMA_TELA)))
            recreate();
    }

    public void buscarEncomendas() {

        //pBar.setVisibility(View.VISIBLE);
        if (((TodosPage) PrincipalActivity.pageAdapter.getItem(0)).adapter != null)
            ((TodosPage) PrincipalActivity.pageAdapter.getItem(0)).buscarEncomendaTodos(this);

        if (((PendentesPage) PrincipalActivity.pageAdapter.getItem(1)).adapter != null)
            ((PendentesPage) PrincipalActivity.pageAdapter.getItem(1)).buscarEncomendaPendente(this);

        if (((EntreguesPage) PrincipalActivity.pageAdapter.getItem(2)).adapter != null)
            ((EntreguesPage) PrincipalActivity.pageAdapter.getItem(2)).buscarEncomendaEntregue(this);
    }
}