package service;

import bd.ConexaoBD;
import dao.TipoVeiculoDAO;
import model.TipoVeiculo;

import java.sql.Connection;
import java.util.List;

public class TipoVeiculoService {
    private final ConexaoBD conexaoBD;

    public TipoVeiculoService() {
        this.conexaoBD = new ConexaoBD();
    }

    public List<TipoVeiculo> obterTipoVeiculos() throws Exception{
        List<TipoVeiculo> tipoVeiculos;
        try(Connection conexao = conexaoBD.getConexaoBD()){
            TipoVeiculoDAO tipoVeiculoDAO = new TipoVeiculoDAO(conexao);
            conexao.setAutoCommit(false);

            try{
                tipoVeiculos = tipoVeiculoDAO.obterTipoVeiculos();
                conexao.commit();
            } catch(Exception e){
                throw e;
            }
        }
        return tipoVeiculos;
    }

    public TipoVeiculo obterTipoVeiculoPorId(Long id) throws Exception{
        TipoVeiculo tipoVeiculo;
        try(Connection conexao = conexaoBD.getConexaoBD()){
            TipoVeiculoDAO tipoVeiculoDAO = new TipoVeiculoDAO(conexao);
            conexao.setAutoCommit(false);
            try{
                tipoVeiculo = tipoVeiculoDAO.obterTipoVeiculoPorId(id);
                conexao.commit();
                if (tipoVeiculo == null){
                    throw new RuntimeException("Não foi possível encontrar uma tipoVeiculo com o id: " + id);
                }
            } catch(Exception e){
                throw e;
            }
        }
        return tipoVeiculo;
    }

    public TipoVeiculo cadastraTipoVeiculo(TipoVeiculo tipoVeiculo) throws Exception{
        try(Connection conexao = conexaoBD.getConexaoBD()){
            TipoVeiculoDAO tipoVeiculoDAO = new TipoVeiculoDAO(conexao);
            conexao.setAutoCommit(false);
            try{
                tipoVeiculoDAO.inserirTipoVeiculo(tipoVeiculo);
                conexao.commit();
            } catch(Exception e){
                conexao.rollback();
                throw e;
            }
        }
        return tipoVeiculo;
    }
}
