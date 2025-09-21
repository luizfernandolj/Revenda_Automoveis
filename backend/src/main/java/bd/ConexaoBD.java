package bd;

import java.sql.*;

public class ConexaoBD {
    public Connection getConexaoBD() throws Exception {
        String url = System.getenv("DB_URL");
        String usuario = System.getenv("DB_USER");
        String senha = System.getenv("DB_PASSWORD");
        String driver = "org.postgresql.Driver";

        if (url == null || usuario == null || senha == null) {
            throw new Exception("Variáveis de ambiente para conexão com o banco de dados não estão definidas.");
        }

        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, usuario, senha);
        } catch (ClassNotFoundException | SQLException e) {
            throw new Exception("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }

    public void encerrarConexoes(ResultSet resultSet, PreparedStatement stmt, Connection conexao) throws Exception {
        if(resultSet != null){
            resultSet.close();
        }

        if(stmt != null){
            stmt.close();
        }

        if(conexao != null){
            conexao.close();
        }
    }
}
