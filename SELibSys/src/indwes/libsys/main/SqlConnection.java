package indwes.libsys.main;
import java.sql.*;
import javax.swing.*;

// Below is the connection class that is used to connect to the database

public class SqlConnection {
	
	public static Connection dbConnect() {
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			
			// The database is located in this file path
			conn = DriverManager.getConnection("jdbc:sqlite:/home/mauleenn/mauleenOS/LibrarySystem/LibSys/SELibSys/LibraryDB2.db");
			return conn;
		} 
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
	}


}
