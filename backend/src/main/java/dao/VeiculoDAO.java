package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Cor;
import model.Modelo;
import model.TipoVeiculo;
import model.Veiculo;

public class VeiculoDAO {
    private final Connection conexao;
    private final CorDAO corDAO;
    private final ModeloDAO modeloDAO;
    private final TipoVeiculoDAO tipoVeiculoDAO;

    public VeiculoDAO(Connection conexao) {
        this.conexao = conexao;
        this.corDAO = new CorDAO(conexao);
        this.modeloDAO = new ModeloDAO(conexao);
        this.tipoVeiculoDAO = new TipoVeiculoDAO(conexao);
    }

    private Veiculo criarVeiculo(ResultSet rs) throws Exception {
        Long id = rs.getLong("id");
        Double preco = rs.getDouble("preco");
        Long quilometragem = rs.getLong("quilometragem");
        boolean disponivel = rs.getBoolean("disponivel");
        Long ano = rs.getLong("ano");
        Cor cor = corDAO.obterCorPorId(rs.getLong("cor_id"));
        Modelo modelo = modeloDAO.obterModeloPorId(rs.getLong("modelo_id"));
        TipoVeiculo tipoVeiculo = tipoVeiculoDAO.obterTipoVeiculoPorId(rs.getLong("tipo_veiculo_id"));

        return new Veiculo(id, preco, quilometragem, disponivel, ano, cor, modelo, tipoVeiculo);
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
            throw new Exception("Erro ao obter veiculo com o id " + id + ": " + e.getMessage());
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
        String sql = "INSERT INTO veiculo (preco, quilometragem, disponivel, ano, cor_id, modelo_id, tipo_veiculo_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDouble(1, veiculo.getPreco());
            pstmt.setLong(2, veiculo.getQuilometragem());
            pstmt.setBoolean(3, veiculo.isDisponivel());
            pstmt.setLong(4, veiculo.getAno());
            pstmt.setLong(5, veiculo.getCor().getId());
            pstmt.setLong(6, veiculo.getModelo().getId());
            pstmt.setLong(7, veiculo.getTipoVeiculo().getId());
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
