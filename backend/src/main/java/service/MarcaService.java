package service;

import bd.ConexaoBD;
import dao.MarcaDAO;
import model.Marca;

import java.sql.Connection;
import java.util.List;

public class MarcaService {
    private final ConexaoBD conexaoBD;

    public MarcaService() {
        this.conexaoBD = new ConexaoBD();
    }

    public List<Marca> obterMarcas() throws Exception{
        List<Marca> marcas;
        try(Connection conexao = conexaoBD.getConexaoBD()){
            MarcaDAO marcaDAO = new MarcaDAO(conexao);
            conexao.setAutoCommit(false);

            try{
                marcas = marcaDAO.obterMarcas();
                conexao.commit();
            } catch(Exception e){
                throw e;
            }
        }
        return marcas;
    }

    public Marca obterMarcaPorId(Long id) throws Exception{
        Marca marca;
        try(Connection conexao = conexaoBD.getConexaoBD()){
            MarcaDAO marcaDAO = new MarcaDAO(conexao);
            conexao.setAutoCommit(false);
            try{
                marca = marcaDAO.obterMarcaPorId(id);
                conexao.commit();
                if (marca == null){
                    throw new RuntimeException("Não foi possível encontrar uma marca com o id: " + id);
                }
            } catch(Exception e){
                throw e;
            }
        }
        return marca;
    }

    public Marca cadastraMarca(Marca marca) throws Exception{
        validarMarca(marca);
        try(Connection conexao = conexaoBD.getConexaoBD()){
            MarcaDAO marcaDAO = new MarcaDAO(conexao);
            conexao.setAutoCommit(false);
            try{
                marcaDAO.inserirMarca(marca);
                conexao.commit();
            } catch(Exception e){
                conexao.rollback();
                throw e;
            }
        }
        return marca;
    }

    public void validarMarca(Marca marca) throws Exception {
        if(marca.getNome() == null || marca.getNome() == "") throw new Exception("Marca invalida");
    }
}
