package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexaoBD {
    public Connection getConexaoBD() throws Exception {
        String url = "jdbc:mysql://localhost:3306/revenda_veiculos";
        String usuario = "root";
        String senha = "28022005lL!";
        String driver = "com.mysql.cj.jdbc.Driver";

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
