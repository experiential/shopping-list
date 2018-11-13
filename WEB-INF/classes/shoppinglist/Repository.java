package shoppinglist;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.json.*;

// Simple class to contain database connection information and handle creating DB connections.
public class Repository
{
    // Database connection parameters (ideally these would be set in a config file)
    private static final String USERNAME = "root";
    private static final String PASSWORD = "MXZlwCgF";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/test";

    // Simple method to return a connection to the database
	public static Connection getConnection()
	{
        Connection connection = null;
        try 
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
            System.out.println("Connected");
        }
        catch (SQLException e)
        {
            System.err.println(e);
        }
        catch (ClassNotFoundException e)
        {
            System.err.println(e);
        }

        return connection;
	}
	
}