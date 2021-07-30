package com.bmorais.tonorastro.pages;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bmorais.tonorastro.PrincipalActivity;
import com.bmorais.tonorastro.R;
import com.bmorais.tonorastro.adapter.AdapterEncomendas;
import com.bmorais.tonorastro.model.EncomendasModel;
import com.bmorais.tonorastro.dao.EncomendasDao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PendentesPage extends Fragment {

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
        view =  inflater.inflate(R.layout.page_pendentes, container, false);

        semEncomenda = view.findViewById(R.id.semEncomendaPendente);

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
                buscarEncomendaPendente(getActivity());
            }
        });

        return view;
    }

    public void buscarEncomendaPendente(Activity activity){

        adapter.clear();
        arrayEncomendas.clear();

        executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                EncomendasDao encomendasDao = new EncomendasDao(activity);
                try {
                    encomendasDao.abrir();
                    arrayEncomendas = encomendasDao.buscarEncomendaPendente();
                    encomendasDao.fechar();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                if (arrayEncomendas.size()>0) {
                    // ATUALIZAR
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addAll(arrayEncomendas);
                            ((PrincipalActivity) activity).pBar.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                            semEncomenda.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addAll(arrayEncomendas);
                            ((PrincipalActivity) activity).pBar.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                            semEncomenda.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        buscarEncomendaPendente(getActivity());
    }

}
