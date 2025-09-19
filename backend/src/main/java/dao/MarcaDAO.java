package dao;

import model.Marca;
import model.Marca;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarcaDAO {
    private final Connection conexao;

    public MarcaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    private Marca criarMarca(ResultSet rs) throws Exception {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        return new Marca(id, nome);
    }

    public Marca obterMarcaPorId(Long id) throws Exception {
        String sql = "SELECT * FROM marca WHERE id = ?";
        Marca marca = null;
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    marca = criarMarca(rs);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao obter cor com o id " + id + ": " + e.getMessage());
        }
        return marca;
    }

    public List<Marca> obterMarcas() throws Exception {
        String sql = "SELECT * FROM marca";
        List<Marca> marcas = new ArrayList<>();
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    marcas.add(criarMarca(rs));
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao obter marcas: " + e.getMessage());
        }
        return marcas;
    }

    public Long inserirMarca(Marca marca) throws Exception {
        String sql = "INSERT INTO marca (nome) VALUES (?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, marca.getNome());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new Exception("Erro ao inserir marca: " + marca.getNome());
            }
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    marca.setId(rs.getLong(1));
                }
            }
        } catch (Exception e) {
            throw new Exception("Erro ao inserir marca: " + marca.getNome());
        }
        return marca.getId();
    }
}
