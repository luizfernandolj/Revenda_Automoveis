package service;

import bd.ConexaoBD;
import dao.VeiculoDAO;
import model.Modelo;
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
        validarVeiculo(veiculo);
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

    public void validarVeiculo(Veiculo veiculo) throws Exception {
        if(veiculo.getQuilometragem() == null || veiculo.getQuilometragem() < 0 || veiculo.getAno() == null || veiculo.getAno() < 0 ||
        veiculo.getPreco() == null || veiculo.getPreco() < 0 || veiculo.getTipoVeiculo() == null || veiculo.getTipoVeiculo().getId() == null || veiculo.getTipoVeiculo().getId() < 0 ||
        veiculo.getCor() == null || veiculo.getCor().getId() == null || veiculo.getCor().getId() < 0 ||
                veiculo.getModelo() == null || veiculo.getModelo().getId() == null || veiculo.getModelo().getId() < 0) throw new Exception("Veiculo invalido");
    }
}
