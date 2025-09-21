package service;

import bd.ConexaoBD;
import dao.CorDAO;
import model.Cor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CorService {
    private final ConexaoBD conexaoBD;

    public CorService() {
        this.conexaoBD = new ConexaoBD();
    }

    public List<Cor> obterCores() throws Exception{
        List<Cor> cores;
        try(Connection conexao = conexaoBD.getConexaoBD()){
            CorDAO corDAO = new CorDAO(conexao);
            conexao.setAutoCommit(false);

            try{
                cores = corDAO.obterCores();
                conexao.commit();
            } catch(Exception e){
                throw e;
            }
        }
        return cores;
    }

    public Cor obterCorPorId(Long id) throws Exception{
        Cor cor;
        try(Connection conexao = conexaoBD.getConexaoBD()){
            CorDAO corDAO = new CorDAO(conexao);
            conexao.setAutoCommit(false);
            try{
                cor = corDAO.obterCorPorId(id);
                conexao.commit();
                if (cor == null){
                    throw new RuntimeException("Não foi possível encontrar uma cor com o id: " + id);
                }
            } catch(Exception e){
                throw e;
            }
        }
        return cor;
    }

    public Cor cadastraCor(Cor cor) throws Exception{
        validarCor(cor);
        try(Connection conexao = conexaoBD.getConexaoBD()){
            CorDAO corDAO = new CorDAO(conexao);
            conexao.setAutoCommit(false);
            try{
                corDAO.inserirCor(cor);
                conexao.commit();
            } catch(Exception e){
                conexao.rollback();
                throw e;
            }
        }

        return cor;
    }

    public void validarCor(Cor cor) throws Exception {
        if(cor.getNome() == null || cor.getNome() == "") throw new Exception("Cor invalida");
    }
}
