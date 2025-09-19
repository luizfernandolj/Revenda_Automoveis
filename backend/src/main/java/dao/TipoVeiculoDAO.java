package dao;

import model.TipoVeiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoVeiculoDAO {
    private final Connection conexao;

    public TipoVeiculoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    private TipoVeiculo criarTipoVeiculo(ResultSet rs) throws Exception {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        return new TipoVeiculo(id, nome);
    }

    public TipoVeiculo obterTipoVeiculoPorId(Long id) throws Exception {
        String sql = "SELECT * FROM tipo_veiculo WHERE id = ?";
        TipoVeiculo tipoVeiculo = null;
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tipoVeiculo = criarTipoVeiculo(rs);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao obter tipoVeiculo com o id " + id + ": " + e.getMessage());
        }
        return tipoVeiculo;
    }

    public List<TipoVeiculo> obterTipoVeiculos() throws Exception {
        String sql = "SELECT * FROM tipo_veiculo";
        List<TipoVeiculo> tipoVeiculos = new ArrayList<>();
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tipoVeiculos.add(criarTipoVeiculo(rs));
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao obter tipoVeiculos: " + e.getMessage());
        }
        return tipoVeiculos;
    }

    public Long inserirTipoVeiculo(TipoVeiculo tipoVeiculo) throws Exception {
        String sql = "INSERT INTO tipo_veiculo (nome) VALUES (?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, tipoVeiculo.getNome());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new Exception("Erro ao inserir tipoVeiculo: " + tipoVeiculo.getNome());
            }
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    tipoVeiculo.setId(rs.getLong(1));
                }
            }
        } catch (Exception e) {
            throw new Exception("Erro ao inserir tipoVeiculo: " + tipoVeiculo.getNome());
        }
        return tipoVeiculo.getId();
    }
}
