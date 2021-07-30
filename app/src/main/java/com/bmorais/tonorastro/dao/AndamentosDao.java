package com.bmorais.tonorastro.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bmorais.tonorastro.model.AndamentosModel;
import com.bmorais.tonorastro.model.EncomendasModel;
import com.bmorais.tonorastro.lib.DatabaseHelper;

import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Created by BM on 26/01/16.
 */
public class AndamentosDao implements InterfaceDAO {
    public static final String codandamento = "codandamento";
    public static final String dataandamento = "dataandamento";
    public static final String local = "local";
    public static final String action = "action";
    public static final String message = "message";
    public static final String codencomenda = "codencomenda";

    public static final String DATABASE_TABLE = "andamentos";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    /**
     * Método construtor
     * @param context
     */
    public AndamentosDao(Context context) {
        this.context = context;
        this.DBHelper = new DatabaseHelper(context);
    }

    /**
     * Habilita escrita no banco de dados
     * @return UsuarioDAO
     * @throws SQLException
     */
    @Override
    public Object abrir() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    /**
     * Fecha a conexão com o banco
     */
    @Override
    public void fechar()
    {
        DBHelper.close();
    }

    /**
     * Inserir registro na Tabela
     * @param obj
     * @return long do ultimo ID cadastrado
     */
    @Override
    public long inserir(Object obj) {
        ContentValues values = new ContentValues();
        if(((AndamentosModel)obj).getCodandamento() > 0) {
            values.put(codandamento, ((AndamentosModel)obj).getCodandamento());
        }
        values.put(dataandamento, ((AndamentosModel)obj).getDataandamento());
        values.put(local, ((AndamentosModel)obj).getLocal());
        values.put(action, ((AndamentosModel)obj).getAction());
        values.put(message, ((AndamentosModel)obj).getMessage());
        values.put(codencomenda, ((AndamentosModel)obj).getEncomenda().getCodencomenda());

        return db.insert(DATABASE_TABLE, null, values);
    }

    /**
     *
     * @param obj
     * @return boolean
     */
    @Override
    public boolean excluir(Object obj) {
        return db.delete(DATABASE_TABLE, codandamento + "=" + ((AndamentosModel)obj).getCodandamento(), null) > 0;
    }

    /**
     *
     * @param obj
     * @return boolean
     */
    @Override
    public boolean atualizar(Object obj) {
        ContentValues values = new ContentValues();
        values.put(dataandamento, ((AndamentosModel)obj).getDataandamento());
        values.put(local, ((AndamentosModel)obj).getDataandamento());
        values.put(action, ((AndamentosModel)obj).getAction());
        values.put(message, ((AndamentosModel)obj).getMessage());
        values.put(codencomenda, ((AndamentosModel)obj).getEncomenda().getCodencomenda());

        return db.update(DATABASE_TABLE, values, codandamento + "=" + ((AndamentosModel) obj).getCodandamento(), null) > 0;
    }

    /**
     *
     * @return ArrayList
     */
    @Override
    public ArrayList<AndamentosModel> buscar() {
        ArrayList<AndamentosModel> result = new ArrayList<>();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{codandamento, dataandamento, local, action, message, codencomenda}, null, null, null, null, dataandamento, null);
        if(cursor != null) {
            if(cursor.moveToFirst())
            {
                do {
                    result.add(new AndamentosModel(cursor.getInt(cursor.getColumnIndex(codandamento)), cursor.getString(cursor.getColumnIndex(dataandamento)), cursor.getString(cursor.getColumnIndex(local)), cursor.getString(cursor.getColumnIndex(action)), cursor.getString(cursor.getColumnIndex(message)), (new EncomendasModel(cursor.getInt(cursor.getColumnIndex(codencomenda))))));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        return result;
    }

    /**
     *
     * @param id
     * @return Grupo
     */
    @Override
    public AndamentosModel buscar(int id) {
        AndamentosModel result = null;
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{codandamento, dataandamento, local, action, message, codencomenda}, codandamento + "=" + id, null, null, null, dataandamento, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                result = new AndamentosModel(cursor.getInt(cursor.getColumnIndex(codandamento)), cursor.getString(cursor.getColumnIndex(dataandamento)), cursor.getString(cursor.getColumnIndex(local)), cursor.getString(cursor.getColumnIndex(action)), cursor.getString(cursor.getColumnIndex(message)), new EncomendasModel(cursor.getInt(cursor.getColumnIndex(codencomenda))));
            }
            cursor.close();
        }
        return result;
    }



    public ArrayList<AndamentosModel> buscarAndamentosEncomenda(int id) {
        ArrayList<AndamentosModel> result = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT *, strftime('%d/%m/%Y %H:%M', dataandamento) AS dandamento FROM andamentos WHERE codencomenda = "+String.valueOf(id)+" ORDER BY dataandamento DESC", null);

        //Cursor cursor = db.query(DATABASE_TABLE, new String[]{codandamento, dataandamento, local, action, message, codencomenda}, codencomenda+'='+id, null, null, null, dataandamento, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    result.add(new AndamentosModel(cursor.getInt(cursor.getColumnIndex(codandamento)), cursor.getString(cursor.getColumnIndex("dandamento")), cursor.getString(cursor.getColumnIndex(local)), cursor.getString(cursor.getColumnIndex(action)), cursor.getString(cursor.getColumnIndex(message)), (new EncomendasModel(cursor.getInt(cursor.getColumnIndex(codencomenda))))));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        return result;
    }

    public AndamentosModel buscarAndamentosActionDataCadastro(String andamento, String dtandamento) {

        AndamentosModel result = null;
        Cursor cursor = db.rawQuery("SELECT * FROM andamentos WHERE action LIKE '"+andamento+"' AND dataandamento LIKE '"+dtandamento+"'", null);

        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                result = new AndamentosModel(cursor.getInt(cursor.getColumnIndex(codandamento)), cursor.getString(cursor.getColumnIndex(dataandamento)), cursor.getString(cursor.getColumnIndex(local)), cursor.getString(cursor.getColumnIndex(action)), cursor.getString(cursor.getColumnIndex(message)), new EncomendasModel(cursor.getInt(cursor.getColumnIndex(codencomenda))));
            }
            cursor.close();
        }
        return result;
    }

    public AndamentosModel buscarAndamentosIdActionDataCadastro(String andamento, String dtandamento, int id) {

        AndamentosModel result = null;
        Cursor cursor = db.rawQuery("SELECT * FROM andamentos WHERE codencomenda='"+id+"' AND action LIKE '"+andamento+"' AND dataandamento LIKE '"+dtandamento+"'", null);

        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                result = new AndamentosModel(cursor.getInt(cursor.getColumnIndex(codandamento)), cursor.getString(cursor.getColumnIndex(dataandamento)), cursor.getString(cursor.getColumnIndex(local)), cursor.getString(cursor.getColumnIndex(action)), cursor.getString(cursor.getColumnIndex(message)), new EncomendasModel(cursor.getInt(cursor.getColumnIndex(codencomenda))));
            }
            cursor.close();
        }
        return result;
    }
}
