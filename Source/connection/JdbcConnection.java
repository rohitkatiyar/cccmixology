package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {
	
	public static Connection getConnection() throws SQLException {
		Connection connection = null;
		try {
			String url = "jdbc:mysql://127.0.0.1:3306/computerCooking?user=root&password=root";
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url);			
		} catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		} catch(ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} 
		
		return connection;
	}

}
