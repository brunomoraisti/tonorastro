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
public class EncomendasDao implements InterfaceDAO {
    public static final String codencomenda = "codencomenda";
    public static final String nome = "nome";
    public static final String datacadastro = "datacadastro";
    public static final String objeto = "objeto";
    public static final String qtddias = "qtddias";
    public static final String situacao = "situacao";
    public static final String excluido = "excluido";
    public static final String leitura = "leitura";

    public static final String DATABASE_TABLE = "encomendas";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    /**
     * Método construtor
     * @param context
     */
    public EncomendasDao(Context context) {
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
        if(((EncomendasModel)obj).getCodencomenda() > 0) {
            values.put(codencomenda, ((EncomendasModel)obj).getCodencomenda());
        }
        values.put(nome, ((EncomendasModel)obj).getNome());
        values.put(datacadastro, ((EncomendasModel)obj).getDatacadastro());
        values.put(objeto, ((EncomendasModel)obj).getObjeto());
        values.put(qtddias, ((EncomendasModel)obj).getQtddias());
        values.put(situacao, ((EncomendasModel)obj).getSituacao());
        values.put(leitura, ((EncomendasModel)obj).getLeitura());

        return db.insert(DATABASE_TABLE, null, values);
    }

    /**
     *
     * @param obj
     * @return boolean
     */
    @Override
    public boolean excluir(Object obj) {
        return db.delete(DATABASE_TABLE, codencomenda + "=" + ((EncomendasModel)obj).getCodencomenda(), null) > 0;
    }

    /**
     *
     * @param obj
     * @return boolean
     */
    @Override
    public boolean atualizar(Object obj) {
        ContentValues values = new ContentValues();
        values.put(nome, ((EncomendasModel)obj).getNome());
        values.put(situacao, ((EncomendasModel)obj).getSituacao());
        values.put(leitura, ((EncomendasModel)obj).getLeitura());
        values.put(qtddias, ((EncomendasModel)obj).getQtddias());
        values.put(excluido, ((EncomendasModel)obj).getExcluido());
        return db.update(DATABASE_TABLE, values, codencomenda + "=" + ((EncomendasModel) obj).getCodencomenda(), null) > 0;
    }

    /**
     *
     * @param obj
     * @return boolean
     */
    public boolean excluirEncomenda(Object obj) {
        ContentValues values = new ContentValues();
        values.put(excluido, "1");
        return db.update(DATABASE_TABLE, values, codencomenda + "=" + ((EncomendasModel) obj).getCodencomenda(), null) > 0;
    }

    /**
     *
     * @param obj
     * @return boolean
     */
    public EncomendasModel buscarEncomendaObjeto(String obj) {
        EncomendasModel result = null;
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{codencomenda, nome, datacadastro, objeto, qtddias, excluido, situacao, leitura}, objeto + "=" + obj, null, null, null, null, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                result = new EncomendasModel(cursor.getInt(cursor.getColumnIndex(codencomenda)), cursor.getString(cursor.getColumnIndex(nome)), cursor.getString(cursor.getColumnIndex(datacadastro)), cursor.getString(cursor.getColumnIndex(objeto)), cursor.getInt(cursor.getColumnIndex(qtddias)), cursor.getInt(cursor.getColumnIndex(situacao)), cursor.getInt(cursor.getColumnIndex(excluido)), cursor.getInt(cursor.getColumnIndex(leitura)));
            }
            cursor.close();
        }
        return result;
    }

    /**
     *
     * @return ArrayList
     */
    @Override
    public ArrayList<EncomendasModel> buscar() {
        ArrayList<EncomendasModel> result = new ArrayList<>();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{codencomenda, nome, datacadastro, objeto, qtddias, situacao, excluido, leitura}, null, null, null, null, datacadastro, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                do {
                    result.add(new EncomendasModel(cursor.getInt(cursor.getColumnIndex(codencomenda)), cursor.getString(cursor.getColumnIndex(nome)), cursor.getString(cursor.getColumnIndex(datacadastro)), cursor.getString(cursor.getColumnIndex(objeto)), cursor.getInt(cursor.getColumnIndex(qtddias)), cursor.getInt(cursor.getColumnIndex(situacao)), cursor.getInt(cursor.getColumnIndex(excluido)), cursor.getInt(cursor.getColumnIndex(leitura))));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        return result;
    }

    public ArrayList<EncomendasModel> buscarEncomendaTodos() {

        ArrayList<EncomendasModel> result = new ArrayList<>();
        AndamentosDao andamentosDao = new AndamentosDao(context);

        Cursor cursor = db.rawQuery("SELECT * FROM encomendas WHERE excluido = 0 ORDER BY datacadastro DESC", null);


        //Cursor cursor = db.query(DATABASE_TABLE, new String[]{codencomenda, nome, datacadastro, objeto, qtddias, situacao, excluido}, situacao + "=" + 0, null, null, null, datacadastro, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                try {
                    andamentosDao.abrir();
                    do {
                        result.add(new EncomendasModel(cursor.getInt(cursor.getColumnIndex(codencomenda)), cursor.getString(cursor.getColumnIndex(nome)), cursor.getString(cursor.getColumnIndex(datacadastro)), cursor.getString(cursor.getColumnIndex(objeto)), cursor.getInt(cursor.getColumnIndex(qtddias)), cursor.getInt(cursor.getColumnIndex(situacao)), cursor.getInt(cursor.getColumnIndex(excluido)), cursor.getInt(cursor.getColumnIndex(leitura)), andamentosDao.buscarAndamentosEncomenda(cursor.getInt(cursor.getColumnIndex(codencomenda)))));
                    }while(cursor.moveToNext());
                    andamentosDao.fechar();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            cursor.close();
        }
        return result;
    }

    public ArrayList<EncomendasModel> buscarEncomendaPendente() {
        ArrayList<EncomendasModel> result = new ArrayList<>();
        AndamentosDao andamentosDao = new AndamentosDao(context);
        Cursor cursor = db.rawQuery("SELECT * FROM encomendas WHERE excluido = 0 AND situacao!=0 ORDER BY datacadastro DESC", null);

        //Cursor cursor = db.query(DATABASE_TABLE, new String[]{codencomenda, nome, datacadastro, objeto, qtddias, situacao, excluido}, situacao + "=" + 0, null, null, null, datacadastro, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                try {
                    andamentosDao.abrir();
                    do {
                        result.add(new EncomendasModel(cursor.getInt(cursor.getColumnIndex(codencomenda)), cursor.getString(cursor.getColumnIndex(nome)), cursor.getString(cursor.getColumnIndex(datacadastro)), cursor.getString(cursor.getColumnIndex(objeto)), cursor.getInt(cursor.getColumnIndex(qtddias)), cursor.getInt(cursor.getColumnIndex(situacao)), cursor.getInt(cursor.getColumnIndex(excluido)), cursor.getInt(cursor.getColumnIndex(leitura)), andamentosDao.buscarAndamentosEncomenda(cursor.getInt(cursor.getColumnIndex(codencomenda)))));
                    }while(cursor.moveToNext());
                    andamentosDao.fechar();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            cursor.close();
        }
        return result;
    }

    public ArrayList<EncomendasModel> buscarEncomendaEntregue() {
        ArrayList<EncomendasModel> result = new ArrayList<>();
        AndamentosDao andamentosDao = new AndamentosDao(context);
        Cursor cursor = db.rawQuery("SELECT * FROM encomendas WHERE excluido = 0 AND situacao=0 ORDER BY datacadastro DESC", null);

        //Cursor cursor = db.query(DATABASE_TABLE, new String[]{codencomenda, nome, datacadastro, objeto, qtddias, situacao, excluido}, situacao + "=" + 1, null, null, null, datacadastro, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                try {
                    andamentosDao.abrir();
                    do {
                        result.add(new EncomendasModel(cursor.getInt(cursor.getColumnIndex(codencomenda)), cursor.getString(cursor.getColumnIndex(nome)), cursor.getString(cursor.getColumnIndex(datacadastro)), cursor.getString(cursor.getColumnIndex(objeto)), cursor.getInt(cursor.getColumnIndex(qtddias)), cursor.getInt(cursor.getColumnIndex(situacao)), cursor.getInt(cursor.getColumnIndex(excluido)), cursor.getInt(cursor.getColumnIndex(leitura)), andamentosDao.buscarAndamentosEncomenda(cursor.getInt(cursor.getColumnIndex(codencomenda)))));
                    }while(cursor.moveToNext());
                    andamentosDao.fechar();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

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
    public EncomendasModel buscar(int id) {
        EncomendasModel result = null;
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{codencomenda, nome, datacadastro, objeto, qtddias, excluido, situacao, leitura}, codencomenda + "=" + id, null, null, null, null, null);
        if(cursor != null)
        {
            if(cursor.moveToFirst())
            {
                result = new EncomendasModel(cursor.getInt(cursor.getColumnIndex(codencomenda)), cursor.getString(cursor.getColumnIndex(nome)), cursor.getString(cursor.getColumnIndex(datacadastro)), cursor.getString(cursor.getColumnIndex(objeto)), cursor.getInt(cursor.getColumnIndex(qtddias)), cursor.getInt(cursor.getColumnIndex(situacao)), cursor.getInt(cursor.getColumnIndex(excluido)), cursor.getInt(cursor.getColumnIndex(leitura)));
            }
            cursor.close();
        }
        return result;
    }
}
