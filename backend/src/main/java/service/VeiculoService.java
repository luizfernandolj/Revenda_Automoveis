package service;

import bd.ConexaoBD;
import dao.VeiculoDAO;
import model.Veiculo;

import java.sql.Connection;
import java.util.List;

public class VeiculoService {
    private final ConexaoBD conexaoBD;

    public VeiculoService() {
        this.conexaoBD = new ConexaoBD();
    }

    public List<Veiculo> obterVeiculos() throws Exception{
        List<Veiculo> veiculos;
        try(Connection conexao = conexaoBD.getConexaoBD()){
            VeiculoDAO veiculoDAO = new VeiculoDAO(conexao);
            conexao.setAutoCommit(false);

            try{
                veiculos = veiculoDAO.obterVeiculos();
                conexao.commit();
            } catch(Exception e){
                throw e;
            }
        }
        return veiculos;
    }

    public Veiculo obterVeiculoPorId(Long id) throws Exception{
        Veiculo veiculo;
        try(Connection conexao = conexaoBD.getConexaoBD()){
            VeiculoDAO veiculoDAO = new VeiculoDAO(conexao);
            conexao.setAutoCommit(false);
            try{
                veiculo = veiculoDAO.obterVeiculoPorId(id);
                conexao.commit();
                if (veiculo == null){
                    throw new RuntimeException("Não foi possível encontrar uma veiculo com o id: " + id);
                }
            } catch(Exception e){
                throw e;
            }
        }
        return veiculo;
    }

    public Veiculo cadastraVeiculo(Veiculo veiculo) throws Exception{
        try(Connection conexao = conexaoBD.getConexaoBD()){
            VeiculoDAO veiculoDAO = new VeiculoDAO(conexao);
            conexao.setAutoCommit(false);
            try{
                veiculoDAO.inserirVeiculo(veiculo);
                conexao.commit();
            } catch(Exception e){
                conexao.rollback();
                throw e;
            }
        }
        return veiculo;
    }
}
