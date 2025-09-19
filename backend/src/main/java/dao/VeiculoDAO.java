package dao;

import model.Cor;
import model.Modelo;
import model.Veiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {
    private final Connection conexao;
    private final CorDAO corDAO;
    private final ModeloDAO modeloDAO;

    public VeiculoDAO(Connection conexao) {
        this.conexao = conexao;
        this.corDAO = new CorDAO(conexao);
        this.modeloDAO = new ModeloDAO(conexao);
    }

    private Veiculo criarVeiculo(ResultSet rs) throws Exception {
        Long id = rs.getLong("id");
        Double preco = rs.getDouble("preco");
        Long quilometragem = rs.getLong("quilometragem");
        boolean disponivel = rs.getBoolean("disponivel");
        Cor cor = corDAO.obterCorPorId(rs.getLong("cor_id"));
        Modelo modelo = modeloDAO.obterModeloPorId(rs.getLong("modelo_id"));

        return new Veiculo(id, preco, quilometragem, disponivel, cor, modelo);
    }

    public Veiculo obterVeiculoPorId(Long id) throws Exception {
        String sql = "SELECT * FROM veiculo WHERE id = ?";
        Veiculo veiculo = null;
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    veiculo = criarVeiculo(rs);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao obter cor com o id " + id + ": " + e.getMessage());
        }
        return veiculo;
    }

    public List<Veiculo> obterVeiculos() throws Exception {
        String sql = "SELECT * FROM veiculo";
        List<Veiculo> veiculos = new ArrayList<>();
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    veiculos.add(criarVeiculo(rs));
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao obter veiculos: " + e.getMessage());
        }
        return veiculos;
    }

    public Long inserirVeiculo(Veiculo veiculo) throws Exception {
        String sql = "INSERT INTO veiculo (preco, quilometragem, disponivel, cor_id, modelo_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDouble(1, veiculo.getPreco());
            pstmt.setLong(2, veiculo.getQuilometragem());
            pstmt.setBoolean(3, veiculo.isDisponivel());
            pstmt.setLong(4, veiculo.getCor().getId());
            pstmt.setLong(5, veiculo.getModelo().getId());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new Exception("Erro ao inserir veiculo");
            }
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    veiculo.setId(rs.getLong(1));
                }
            }
        } catch (Exception e) {
            throw new Exception("Erro ao inserir veiculo");
        }
        return veiculo.getId();
    }
}
