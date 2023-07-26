package project.doa;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {
	private  static final String dbUrl="jdbc:mysql://localhost:3306/advance_programming_cw";
	private   static final String dbUsername="root";
	private   static final String dbPassword="root";
	private static Connection connection;
	
	private DbConnect()
	{
		
	}
	public static  Connection getDatabaseConeection()
	{ 
		
		
				try {
					
					if(connection==null)
					{
						synchronized (DbConnect.class) {
							if(connection==null)
							{
							Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
							connection=DriverManager.getConnection(dbUrl,dbPassword,dbPassword);
							}
							
						}
					}
					return connection;
					
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException
						| ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
		return connection;
	}

}
