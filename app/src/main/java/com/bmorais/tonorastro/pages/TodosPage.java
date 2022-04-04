package com.bmorais.tonorastro.pages;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.core.os.HandlerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bmorais.tonorastro.PrincipalActivity;
import com.bmorais.tonorastro.R;
import com.bmorais.tonorastro.adapter.AdapterEncomendas;
import com.bmorais.tonorastro.dao.EncomendasDao;
import com.bmorais.tonorastro.model.EncomendasModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodosPage extends Fragment {

    private RecyclerView mRecyclerView;
    public AdapterEncomendas adapter;
    public ArrayList<EncomendasModel> arrayEncomendas = null;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public View view;
    public LinearLayoutManager linearLayoutManager;
    public LinearLayout linearLayout;
    public LinearLayout semEncomenda;
    Button btnConectar;
    Button btnEnviarSugestao;
    ExecutorService executorService;
    Handler handler;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.page_todos, container, false);

        semEncomenda = view.findViewById(R.id.semEncomendaTodos);

        linearLayout = view.findViewById(R.id.linearLayoutEmergencias);

        //LISTA DE GRUPOS
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rcvEmergencias);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AdapterEncomendas(getActivity());
        mRecyclerView.setAdapter(adapter);
        arrayEncomendas = new ArrayList<EncomendasModel>();

        //ATUALIZAR LISTA COM PUSH DOWN
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscarEncomendaTodos(getActivity());
            }
        });


        return view;
    }

    public void buscarEncomendaTodos(Activity activity){

        adapter.clear();
        arrayEncomendas.clear();

        executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                EncomendasDao encomendasDao = new EncomendasDao(activity);
                try {
                    encomendasDao.abrir();
                    arrayEncomendas = encomendasDao.buscarEncomendaTodos();
                    encomendasDao.fechar();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                if (arrayEncomendas.size()>0) {
                    atualizaTela(activity, true);
                } else {
                    atualizaTela(activity, false);
                }
            }
        });

    }

    public void atualizaTela(Activity activity, boolean result) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result) {
                    semEncomenda.setVisibility(View.GONE);
                } else {
                    semEncomenda.setVisibility(View.VISIBLE);
                }

                ((PrincipalActivity) activity).pBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                adapter.addAll(arrayEncomendas);
            }
        });
    };

    @Override
    public void onResume() {
        super.onResume();

        buscarEncomendaTodos(getActivity());
    }
}
