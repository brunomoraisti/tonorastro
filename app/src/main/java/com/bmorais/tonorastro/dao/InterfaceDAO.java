package com.bmorais.tonorastro.dao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by jp on 22/01/16.
 */
public interface InterfaceDAO {

    /**
     * Habilita escrita no banco de dados
     * @return
     * @throws SQLException
     */
    public Object abrir() throws SQLException;

    /**
     * Fecha conexão com o banco
     */
    public void fechar();

    /**
     *
     * @param obj
     * @return long do ultimo ID cadastrado
     */
    public long inserir(Object obj);

    /**
     *
     * @param obj
     * @return boolean
     */
    public boolean excluir(Object obj);

    /**
     *
     * @param obj
     * @return boolean
     */
    public boolean atualizar(Object obj);

    /**
     *
     * @return ArrayList
     */
    public ArrayList buscar();

    /**
     *
     * @param id
     * @return
     */
    public Object buscar(int id);
}
