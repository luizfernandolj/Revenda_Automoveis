package dao;

import model.Cor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CorDAO {
    private final Connection conexao;

    public CorDAO(Connection conexao) {
        this.conexao = conexao;
    }

    private Cor criarCor(ResultSet rs) throws Exception {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        return new Cor(id, nome);
    }

    public Cor obterCorPorId(Long id) throws Exception {
            String sql = "SELECT * FROM cor WHERE id = ?";
            Cor cor = null;
            try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
                pstmt.setLong(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        cor = criarCor(rs);
                    }
                }
            } catch (SQLException e) {
                throw new Exception("Erro ao obter cor com o id " + id + ": " + e.getMessage());
            }
            return cor;
    }

    public List<Cor> obterCores() throws Exception {
        String sql = "SELECT * FROM cor";
        List<Cor> cores = new ArrayList<>();
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cores.add(criarCor(rs));
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao obter cores: " + e.getMessage());
        }
        return cores;
    }

    public Long inserirCor(Cor cor) throws Exception {
        String sql = "INSERT INTO cor (nome) VALUES (?)";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, cor.getNome());
            int resultado = pstmt.executeUpdate();
            if (resultado == 0) {
                throw new Exception("Erro ao inserir cor: " + cor.getNome());
            }
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cor.setId(rs.getLong(1));
                }
            }
        } catch (Exception e) {
            throw new Exception("Erro ao inserir cor: " + cor.getNome());
        }
        return cor.getId();
    }

}
