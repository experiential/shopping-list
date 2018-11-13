package shoppinglist;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.json.*;

// ORM class to represent the user data in the DB
public final class User
{
	private int userID = 0;
	private String name = null; 

	// Factory method used to create User objects representing the data held on this user in the DB, given the
	// user's username.
	public static User getUserByName(String username)
	{
        Connection conn = Repository.getConnection();

        User newInstance = null;

        String selectQuery = "select User_ID, User_name from user where User_name = ?"; // User_name is defined as unique
        //out.println("The SQL query is: " + sqlQuery); // Echo For debugging
        
        try
        {
            // Prepare query, preventing SQL injection attacks
            PreparedStatement statement = conn.prepareStatement(selectQuery);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            
            if(resultSet.next()) // Get the first row, if there is one
            {
            	newInstance = new User();
                newInstance.userID = resultSet.getInt("User_ID");
                newInstance.name = resultSet.getString("User_name");
            }
        }
        catch (SQLException e)
        {
            System.err.println(e);
        }

		return newInstance;
	}

	// Factory method used to create User objects representing the data held on this user in the DB, given the
	// user's user ID. 
	public static User getUserByID(int userID)
	{
        Connection conn = Repository.getConnection();

        User newInstance = null;

        String selectQuery = "select User_ID, User_name from user where User_ID = ?";
        
        try
        {
            // Prepare query, preventing SQL injection attacks
            PreparedStatement statement = conn.prepareStatement(selectQuery);
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            
            if(resultSet.next()) // Get the first row, if there is one
            {
            	newInstance = new User();
                newInstance.userID = resultSet.getInt("User_ID");
                newInstance.name = resultSet.getString("User_name");
            }
        }
        catch (SQLException e)
        {
            System.err.println(e);
        }

		return newInstance;
	}

	// Make sure constructor is private so that only the static method above can construct a User object
	private User()
	{
		super();
	}

	// Return the ShoppingList object for this user, containing this user's shopping list data.
	public ShoppingList getShoppingList()
	{
		return ShoppingList.getShoppingListByUserID(this.userID);
	}

	public int getUserID()
	{
		return userID;
	}

	public String getName()
	{
		return name;
	}

	// Return a JSON representation of this object.
	public String getJSON()
	{
		return "{\"userID\":" + userID + ", \"name\":\"" + name + "\" } ";		
	}
}