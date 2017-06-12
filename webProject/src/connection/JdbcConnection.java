package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcConnection {
	
	public static Connection getConnection() {
		Connection connection = null;
		try {
			String url = "jdbc:mysql://127.0.0.1:3306/computerCooking?user=root&password=password";
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url);
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} 
		
		return connection;
	}
	
	public static void main(String[] args) {
		Connection conn = null;
		try {
			conn = JdbcConnection.getConnection();
			System.out.println(conn);
			String query = "Select * from ingredients where name='white rum';";
			conn = JdbcConnection.getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(query);
			ResultSet rs = prepStmt.executeQuery();
			while(rs.next()) {
				System.out.println(rs.getInt("ingId"));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
