package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
	
         private final String USERNAME = "root";
         private final String PASSWORD = "";
         private final String DATABASE = "filmbad";
         private final String HOST = "localhost:3306";
         private final String URL = String .format("jdbc:mysql://%s/%s", HOST, DATABASE);
         
         private Connection con;
         private Statement st;
         private ResultSet rs;
         
         
         public static DatabaseManager connect;
         
         public static DatabaseManager getInstace() {
        	 
        	 if (connect == null) {
				connect = new DatabaseManager();
			}
			return connect;
         }
         
	   public ResultSet execQuery(String query) {
		   
		   try {
			rs = st.executeQuery(query);
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return rs;
		   
	   }
	   
	   public void execUpdate(String query) {
			 try {
				st.executeUpdate(query);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		 }
	   
	   private DatabaseManager() {
		   
		   try {
			 Class.forName("com.mysql.cj.jdbc.Driver");
			 con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			 st = con.createStatement();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	   }

}
