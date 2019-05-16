package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {

	private static Connection conn = null;
	
	public static Connection getConnection() {
		if (conn == null) {
			try {
                                //loadProperties - método que carrega um arquivo de configurações
                                //para acesso ao banco de dados
				Properties props = loadProperties(); 
                                //Do arquivo lido via loadProperties, lê uma linha no aquivo que
                                //se inicia com dburl, o valor após essa palavra é colocada na String
                                //url
				String url = props.getProperty("dburl");
                                
                                //conecta-se a um banco de dados, informando o endereço
                                //e o arquivo onde contém os arquivos com o usuário e senha
				conn = DriverManager.getConnection(url, props);
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return conn;
	}
	
	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	private static Properties loadProperties() {
            /*
            Um arquivo de proprieades é uma ótima opção para passar configurações
            para uma determinada aplicação que necessita de configurações externas
            e a mesma em si não pode ser alterada. Um exemplo de aplicação seria
            para conectar a um banco de dados.
            Um arquivo de propriedade é nada mais do que um arquivo simples com 
            extensão .properties
            */
		try (FileInputStream fs = new FileInputStream("db.properties")) {
			Properties props = new Properties(); //cria um objeto do tipo Properties
			props.load(fs); //carrega o aquivo de propriedades na variável props
			return props;
		}
		catch (IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}

	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}
