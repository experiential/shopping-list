package shoppinglist;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.json.*;

public class ShoppingListServlet extends HttpServlet
{

    // Database connection parameters
    private static final String USERNAME = "root";
    private static final String PASSWORD = "MXZlwCgF";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/test";

    // Main method called when a request comes to the servlet, as the calls in this shopping list
    // app are made using POST.
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        // Parse request JSON
        String requestType = null;
        JsonObject parameters = null;
        try (InputStream is = request.getInputStream();        
             JsonReader rdr = Json.createReader(is))
        {
            JsonObject obj = rdr.readObject();
            requestType = obj.getString("request");
            parameters = obj.getJsonObject("parameters");            
        }
        catch(JsonException e)
        {
            System.err.println(e);
        }

        // Handle the request by calling the appropriate function for the specified request type
        if(requestType.equals("get-shopping-list"))
        {
            handleGetShoppingListRequest(parameters, out);
        }
        else if(requestType.equals("add-item-to-list"))
        {
            handleAddItemToListRequest(parameters, out);
        }

    }

    // This method takes the username and searches in the DB for that user's shopping list, returning
    // both user information (user ID) and the list of items.
    protected void handleGetShoppingListRequest(JsonObject parameters, PrintWriter out)
    {
        String userName = parameters.getString("userName");

        User thisUser = User.getUserByName(userName);
        ShoppingList shoppingList = thisUser.getShoppingList();

        // Output the JSON representation of the shopping list for the specified user
        System.out.println("Output from handleGetShoppingListRequest: "+shoppingList.getJSON());
        String response = "{ \"user\":" + thisUser.getJSON() + ", \"shoppingList\":" + shoppingList.getJSON() + " }";
        out.println(response);
    }

    // This method takes the name of a new item, adds it to the specified user's shopping list,
    // and returns the full updated list.
    protected void handleAddItemToListRequest(JsonObject parameters, PrintWriter out)
    {
        int userID = parameters.getInt("userID");
        String itemName = parameters.getString("itemName");

        User thisUser = User.getUserByID(userID);
        ShoppingList shoppingList = thisUser.getShoppingList();

        shoppingList.addItemToList(itemName);
        String response = "{ \"shoppingList\":" + shoppingList.getJSON() + " }";
        out.println(response);

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        return;
    }
}