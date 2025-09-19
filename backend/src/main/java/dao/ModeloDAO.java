package dao;

import model.Marca;
import model.Modelo;
import model.TipoVeiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModeloDAO {
    private final Connection conexao;
    private final MarcaDAO marcaDAO;
    private final TipoVeiculoDAO tipoVeiculoDAO;

    public ModeloDAO(Connection conexao) {
        this.conexao = conexao;
        this.marcaDAO = new MarcaDAO(conexao);
        this.tipoVeiculoDAO = new TipoVeiculoDAO(conexao);
    }

    private Modelo criarModelo(ResultSet rs) throws Exception {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        Marca marca = marcaDAO.obterMarcaPorId(rs.getLong("marca_id"));
        TipoVeiculo tipoVeiculo = tipoVeiculoDAO.obterTipoVeiculoPorId(rs.getLong("tipo_veiculo_id"));
        return new Modelo(id, nome, marca, tipoVeiculo);
    }

    public Modelo obterModeloPorId(Long id) throws Exception {
        String sql = "SELECT * FROM modelo WHERE id = ?";
        Modelo modelo = null;
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    modelo = criarModelo(rs);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao obter cor com o id " + id + ": " + e.getMessage());
        }
        return modelo;
    }

    public List<Modelo> obterModelos() throws Exception {
        String sql = "SELECT * FROM modelo";
        List<Modelo> modelos = new ArrayList<>();
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    modelos.add(criarModelo(rs));
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao obter modelos: " + e.getMessage());
        }
        return modelos;
    }

    public Long inserirModelo(Modelo modelo) throws Exception {
        String sql = "INSERT INTO modelo (nome, marca_id, tipo_veiculo_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, modelo.getNome());
            pstmt.setLong(2, modelo.getMarca().getId());
            pstmt.setLong(3, modelo.getTipoVeiculo().getId());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new Exception("Erro ao inserir modelo: " + modelo.getNome());
            }
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    modelo.setId(rs.getLong(1));
                }
            }
        } catch (Exception e) {
            throw new Exception("Erro ao inserir modelo: " + modelo.getNome());
        }
        return modelo.getId();
    }
}
