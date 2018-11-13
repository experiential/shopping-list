package shoppinglist;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.json.*;

// ORM class to represent the shopping list data in the DB
public final class ShoppingList
{
	private int userID = 0;
	private Vector items = null; 

    // Factory method used to create new ShoppingList objects, complete with data on all their items from the DB.
	public static ShoppingList getShoppingListByUserID(int userID)
	{
        System.out.println("getShoppingListByUserID with userID:" + userID);
        
        Connection connection = Repository.getConnection();

        ShoppingList newInstance = new ShoppingList();
        newInstance.userID = userID;

        String selectQuery = "select User_ID, Item_ID, Name, Date_added from user_shopping_list_item inner join user using(User_ID) where User_ID = ? order by Date_added"; // Join on 'user' table to ensure user entry still exists
        
        try
        {
            // Prepare query, preventing SQL injection attacks
            PreparedStatement statement = connection.prepareStatement(selectQuery);
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()) // Iterate through each shopping list item returned from the DB
            {
                // Get the item values
                int thisUserID = resultSet.getInt("User_ID");
                int thisItemID = resultSet.getInt("Item_ID");
                String thisItemName = resultSet.getString("Name");
                Timestamp thisItemDateAdded = resultSet.getTimestamp("Date_added");

                // Construct a new item object with these values and add it to the list
                ShoppingListItem thisItem = new ShoppingListItem(thisUserID, thisItemID, thisItemName, thisItemDateAdded);
                newInstance.items.add(thisItem);
            }
        }
        catch (SQLException e)
        {
            System.err.println(e);
        }

		return newInstance;
	}

	// Make sure constructor is private so that only the static method above can construct a User object
	private ShoppingList()
	{
		super();

        // Create a Vector object to hold the list of items.
        items = new Vector();
	}

    // This method will add a new item to the shopping list in the database and also update this object's list
    // accordingly.
    public void addItemToList(String itemName)
    {
        Connection connection = Repository.getConnection();

        // Construct query for inserting the new item into the database. Selecting from the user table
        // ensures that there actually is an entry for the current user ID in the user table.
        String sqlQuery = "insert into user_shopping_list_item select User_ID, null, ?, ? from user where user.User_ID = ?";
 
        int generatedKey = 0;
        Timestamp itemTimestamp = new Timestamp(System.currentTimeMillis()); // Create timestamp for the time right now
        try
        {
            // Prepare query, preventing SQL injection attacks
            PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, itemName);
            statement.setTimestamp(2, itemTimestamp);
            statement.setInt(3, this.userID);
            statement.executeUpdate();

            // Get the 'autonumber' ID created when this new row was inserted.
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()) 
            {
                generatedKey = resultSet.getInt(1);
            }
        }
        catch (SQLException e)
        {
            System.err.println(e);
        }

        if(generatedKey > 0) // We should get back an Item_ID from the database for the new item we just added
        {
            // Create new ShoppingListItem object and add it to the list
            ShoppingListItem newItem = new ShoppingListItem(this.userID, generatedKey, itemName, itemTimestamp);
            this.items.add(newItem);
        }
    }

    // Return a JSON representation of this object.
    public String getJSON()
    {
        // Set up a mutable string with the beginning of the JSON representation
        StringBuffer buffer = new StringBuffer("{ \"items\":[");

        // Iterate through the list and add JSON representation from each item to the total
        Iterator listIterator = this.items.iterator();
        while(listIterator.hasNext())
        {
            ShoppingListItem item = (ShoppingListItem) listIterator.next();
            buffer.append(item.getJSON());

            // Add separating comma if there are still more items to come
            if(listIterator.hasNext())
                buffer.append(",");
        }

        // Complete the JSON representation
        buffer.append(" ] }");

        return buffer.toString();
    }
}