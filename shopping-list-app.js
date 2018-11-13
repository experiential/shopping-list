
var app = angular.module('shoppingListApp', []);

app.controller('shoppingListController', function($scope, $http) 
{
    // Function called when 'add new item' button is clicked or return pressed in the 'new item' text field
    $scope.fetchList = function() 
    {
        if(!$scope.userName)
            return;

        // Send request to Java servlet to get the shopping list data and user data for the named user.
        $http({
            method : "POST",
            url : "http://localhost:8080//examples/servlets/servlet/ShoppingListServlet",
            data : "{\"request\":\"get-shopping-list\", \"parameters\":{\"userName\":\"" + $scope.userName + "\"} }"
        }).then(function mySuccess(response) {
            // Request succeeded, so update our model in the AngularJS scope object
            $scope.shoppingListItems = response.data.shoppingList.items;
            $scope.userID = response.data.user.userID;
        }, function myError(response) {
            alert(response.statusText);
            $scope.shoppingListItems = [];
        });
    };

    // Function called when 'add new item' button is clicked or return pressed in the 'new item' text field.
    // This sends the name of the new item to the server, along with the user
    $scope.addNewItem = function() 
    {
    	if(!$scope.newItem || !$scope.userID)
    		return;

        // Send request to Java servlet to add the item to the shopping list and return the updated list.
	    $http({
	        method : "POST",
	        url : "http://localhost:8080//examples/servlets/servlet/ShoppingListServlet",
            data : "{\"request\":\"add-item-to-list\", \"parameters\":{\"userID\":" + $scope.userID + ", \"itemName\":\"" + $scope.newItem + "\"} }"
	    }).then(function mySuccess(response) {
            $scope.shoppingListItems = response.data.shoppingList.items;
            $scope.newItem = "";
        }, function myError(response) {
            alert(response.statusText);
	    });
    };

    // For testing/demo purposes only
    $scope.userName = "Bob";
    $scope.fetchList();
});
