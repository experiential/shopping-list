package shoppinglist;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.json.*;

// Simple class to represent a single item on a shopping list in the database. Also has a method
// to return a JSON representation of the object.
public final class ShoppingListItem
{
	private int userID = 0;
	private int itemID = 0;
	private String name = null;
	private Timestamp dateAdded = null;

	public ShoppingListItem(int userID, int itemID, String name, Timestamp dateAdded)
	{
		super();

		this.userID = userID;
		this.itemID = itemID;
		this.name = name;
		this.dateAdded = dateAdded;
	}

	public int getUserID()
	{
		return userID;
	}

	public int getItemID()
	{
		return itemID;
	}

	public String getName()
	{
		return name;
	}

	public Timestamp getDateAdded()
	{
		return dateAdded;
	}

	public String getJSON()
	{
		return "{\"userID\":" + userID + ", \"itemID\":" + itemID + ", \"Name\":\"" + name + "\", \"dateAdded\":\"" + dateAdded + "\" } ";
	}
}