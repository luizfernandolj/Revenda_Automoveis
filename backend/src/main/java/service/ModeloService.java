package service;

import bd.ConexaoBD;
import dao.ModeloDAO;
import model.Marca;
import model.Modelo;

import java.sql.Connection;
import java.util.List;

public class ModeloService {
    private final ConexaoBD conexaoBD;

    public ModeloService() {
        this.conexaoBD = new ConexaoBD();
    }

    public List<Modelo> obterModelos() throws Exception{
        List<Modelo> modelos;
        try(Connection conexao = conexaoBD.getConexaoBD()){
            ModeloDAO modeloDAO = new ModeloDAO(conexao);
            conexao.setAutoCommit(false);

            try{
                modelos = modeloDAO.obterModelos();
                conexao.commit();
            } catch(Exception e){
                throw e;
            }
        }
        return modelos;
    }

    public Modelo obterModeloPorId(Long id) throws Exception{
        Modelo modelo;
        try(Connection conexao = conexaoBD.getConexaoBD()){
            ModeloDAO modeloDAO = new ModeloDAO(conexao);
            conexao.setAutoCommit(false);
            try{
                modelo = modeloDAO.obterModeloPorId(id);
                conexao.commit();
                if (modelo == null){
                    throw new RuntimeException("Não foi possível encontrar uma modelo com o id: " + id);
                }
            } catch(Exception e){
                throw e;
            }
        }
        return modelo;
    }

    public Modelo cadastraModelo(Modelo modelo) throws Exception{
        validarModelo(modelo);
        try(Connection conexao = conexaoBD.getConexaoBD()){
            ModeloDAO modeloDAO = new ModeloDAO(conexao);
            conexao.setAutoCommit(false);
            try{
                modeloDAO.inserirModelo(modelo);
                conexao.commit();
            } catch(Exception e){
                conexao.rollback();
                throw e;
            }
        }
        return modelo;
    }

    public void validarModelo(Modelo modelo) throws Exception {
        if(modelo.getNome() == null || modelo.getNome() == "" || modelo.getMarca() == null || modelo.getMarca().getId() == null || modelo.getMarca().getId() < 0 ) throw new Exception("Modelo invalido");
    }
}
