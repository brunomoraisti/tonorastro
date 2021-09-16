package com.bmorais.tonorastro.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;

import com.bmorais.tonorastro.PrincipalActivity;
import com.bmorais.tonorastro.dao.AndamentosDao;
import com.bmorais.tonorastro.dao.EncomendasDao;
import com.bmorais.tonorastro.firebase.MyNotificationManager;
import com.bmorais.tonorastro.http.RetrofitInicializador;
import com.bmorais.tonorastro.lib.Funcoes;
import com.bmorais.tonorastro.lib.SharedPrefManager;
import com.bmorais.tonorastro.lib.Variaveis;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Bruno Morais2 on 20/09/2017.
 */

public class EncomendasModel {

    private int codencomenda;
    private String nome;
    private String datacadastro;
    private String objeto;
    private int qtddias;
    private int situacao;
    private int excluido;
    private int leitura;

    private ArrayList<AndamentosModel> andamentos;

    public EncomendasModel(int codencomenda, String nome, String datacadastro, String objeto, int qtddias, int situacao, int excluido, int leitura) {
        this.codencomenda = codencomenda;
        this.nome = nome;
        this.datacadastro = datacadastro;
        this.objeto = objeto;
        this.qtddias = qtddias;
        this.situacao = situacao;
        this.excluido = excluido;
        this.leitura = leitura;
    }

    public EncomendasModel(int codencomenda, String nome, String datacadastro, String objeto, int qtddias, int situacao, int excluido, int leitura, ArrayList<AndamentosModel> andamentos) {
        this.codencomenda = codencomenda;
        this.nome = nome;
        this.datacadastro = datacadastro;
        this.objeto = objeto;
        this.qtddias = qtddias;
        this.situacao = situacao;
        this.excluido = excluido;
        this.leitura = leitura;
        this.andamentos = andamentos;
    }

    public EncomendasModel(String nome, String datacadastro, String objeto, int qtddias) {
        this.codencomenda = codencomenda;
        this.nome = nome;
        this.datacadastro = datacadastro;
        this.objeto = objeto;
        this.qtddias = qtddias;
        this.situacao = situacao;
        this.excluido = excluido;
    }

    public EncomendasModel() {
    }

    public EncomendasModel(int codencomenda) {
        this.codencomenda = codencomenda;
    }

    public int getCodencomenda() {
        return codencomenda;
    }

    public void setCodencomenda(int codencomenda) {
        this.codencomenda = codencomenda;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDatacadastro() {
        return datacadastro;
    }

    public void setDatacadastro(String datacadastro) {
        this.datacadastro = datacadastro;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public int getQtddias() {
        return qtddias;
    }

    public void setQtddias(int qtddias) {
        this.qtddias = qtddias;
    }

    public int getSituacao() {
        return situacao;
    }

    public void setSituacao(int situacao) {
        this.situacao = situacao;
    }

    public int getExcluido() {
        return excluido;
    }

    public void setExcluido(int excluido) {
        this.excluido = excluido;
    }

    public int getLeitura() {
        return leitura;
    }

    public void setLeitura(int leitura) {
        this.leitura = leitura;
    }

    public ArrayList<AndamentosModel> getAndamentos() {
        return andamentos;
    }

    public void setAndamentos(ArrayList<AndamentosModel> andamentos) {
        this.andamentos = andamentos;
    }

    public static void excluirEncomenda(int id, Activity activity) {

        EncomendasModel encomenda = new EncomendasModel();
        EncomendasDao encomendasDao = new EncomendasDao(activity);
        encomenda.setCodencomenda(id);

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    encomendasDao.abrir();
                    encomendasDao.excluirEncomenda(encomenda);
                    encomendasDao.fechar();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                // ATUALIZAR
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((PrincipalActivity) activity).buscarEncomendas();
                    }
                });
            }

        });

    }

    public static void buscarAlteracoes(Context activity, ArrayList<EncomendasModel> arrayEncomendas, boolean atualizaTela) {

        JSONObject objetos = new JSONObject();
        JSONArray codigos = new JSONArray();

        try {
            for (EncomendasModel encomendasModel : arrayEncomendas) {
                JSONObject objeto = new JSONObject();
                objeto.put("codigo", encomendasModel.getObjeto());
                codigos.put(objeto);
            }

            objetos.put("objetos", codigos);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Funcoes.verificaConexao(activity)) {

            Call<JsonObject> call = new RetrofitInicializador().getRetrofitService().postRastreamento(objetos.toString());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful() && response.body().toString() != null) {


                        JSONObject jsonBody = null;
                        JSONArray jsonAndamentos = null;

                        boolean notification = false;
                        String notificationAction = "";

                        AndamentosModel andamento;

                        EncomendasDao encomendasDao = new EncomendasDao(activity);
                        AndamentosDao andamentosDao = new AndamentosDao(activity);

                        try {
                            jsonBody = new JSONObject(response.body().toString());
                            for (EncomendasModel encomendasModel : arrayEncomendas) {
                                notification = false;
                                // PEGA O OBJETO ENCOMENDA
                                JSONObject itemObjeto = jsonBody.getJSONObject(encomendasModel.getObjeto());

                                if (!itemObjeto.getBoolean("error")) {
                                    // PEGA ANDAMENTOS DA ENCOMENDA
                                    jsonAndamentos = itemObjeto.getJSONArray("progress");

                                    // VERIFICA QUANTIDADE DE DIAS É DIFERENTE DA EXISTENTE
                                    if (encomendasModel.getQtddias() != itemObjeto.getInt("days")) {
                                        encomendasModel.setLeitura(0);
                                        encomendasModel.setQtddias(itemObjeto.getInt("days"));

                                        encomendasDao.abrir();
                                        encomendasDao.atualizar(encomendasModel);
                                        encomendasDao.fechar();
                                    }

                                    for (int i = 0; i < jsonAndamentos.length(); i++) {
                                        JSONObject item = jsonAndamentos.getJSONObject(i);

                                        andamentosDao.abrir();
                                        andamento = andamentosDao.buscarAndamentosIdActionDataCadastro(item.getString("action"), item.getString("date"), encomendasModel.getCodencomenda());
                                        andamentosDao.fechar();

                                        if (andamento == null) {
                                            notification = true;
                                            notificationAction = item.getString("action");

                                            // VERIFICA SE O OBJETO FOI ENTREGUE
                                            if (item.getString("action").contains("Objeto entregue")) {
                                                encomendasModel.setLeitura(0);
                                                encomendasModel.setSituacao(0);
                                            } else {
                                                encomendasModel.setLeitura(0);
                                                encomendasModel.setSituacao(1); // OBJETO COM ENTREGA PENDENTE

                                            }

                                            encomendasDao.abrir();
                                            encomendasDao.atualizar(encomendasModel);
                                            encomendasDao.fechar();

                                            andamento = new AndamentosModel();
                                            andamento.setDataandamento(item.getString("date"));
                                            andamento.setLocal(item.getString("location"));
                                            andamento.setAction(item.getString("action"));
                                            andamento.setMessage(item.getString("message"));
                                            andamento.setEncomenda(encomendasModel);

                                            andamentosDao.abrir();
                                            andamentosDao.inserir(andamento);
                                            andamentosDao.fechar();


                                        }
                                    }

                                    if (notification && SharedPrefManager.getInstance(activity).pegarCampo(Variaveis.NOTIFICACOES).equals("1")) {
                                        Intent intent = new Intent(activity, PrincipalActivity.class);
                                        MyNotificationManager mNotificationManager = new MyNotificationManager(activity);
                                        mNotificationManager.showNotification("O objeto " + encomendasModel.getNome() + " teve novo andamento", notificationAction, "", intent);
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        if (atualizaTela){
                            EncomendasModel.atualizaTela(activity, true);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.i("##VOLLEY##", "FALHA AO REGISTRAR");
                }
            });
        }
    }

    public static void atualizaTela(Context activity, boolean result) {
        Handler resultHandler = HandlerCompat.createAsync(Looper.getMainLooper());
        resultHandler.post(new Runnable() {
            @Override
            public void run() {
                ((PrincipalActivity) activity).buscarEncomendas();
            }
        });
    };
}
