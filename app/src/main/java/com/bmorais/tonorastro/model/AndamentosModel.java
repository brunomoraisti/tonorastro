package com.bmorais.tonorastro.model;

/**
 * Created by Bruno Morais2 on 20/09/2017.
 */

public class AndamentosModel {

    private int codandamento;
    private String dataandamento;
    private String local;
    private String action;
    private String message;
    private EncomendasModel encomendasModel;

    public AndamentosModel() {

    }

    public AndamentosModel(int codandamento, String dataandamento, String local, String action, String message, EncomendasModel codencomenda) {
        this.codandamento = codandamento;
        this.dataandamento = dataandamento;
        this.local = local;
        this.action = action;
        this.message = message;
        this.encomendasModel = codencomenda;
    }

    public int getCodandamento() {
        return codandamento;
    }

    public void setCodandamento(int codandamento) {
        this.codandamento = codandamento;
    }

    public String getDataandamento() {
        return dataandamento;
    }

    public void setDataandamento(String dataandamento) {
        this.dataandamento = dataandamento;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EncomendasModel getEncomenda() {
        return encomendasModel;
    }

    public void setEncomenda(EncomendasModel codencomenda) {
        this.encomendasModel = codencomenda;
    }
}
