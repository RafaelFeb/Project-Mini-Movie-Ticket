package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import util.DatabaseManager;

public class Logins extends Application {

	DatabaseManager connect = DatabaseManager.getInstace();
    private Stage primaryStage;

    public Logins(Stage primaryStage) {
        this.primaryStage = primaryStage;
        start(primaryStage);
    }
	
	
	@Override
	public void start(Stage stage){
		 stage.setTitle("Login");


	        MenuBar menuBar = new MenuBar();
	        Menu menuCustomer = new Menu("Customer");


	        MenuItem menuItemBack = new MenuItem("Back to Main");
	        menuItemBack.setOnAction(e -> {
	            new Main().start(primaryStage);
	        });
	        
	        menuCustomer.getItems().add(menuItemBack);
	        menuBar.getMenus().add(menuCustomer);

	        GridPane grid = new GridPane();
	        grid.setAlignment(javafx.geometry.Pos.CENTER);
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setPadding(new Insets(25, 25, 25, 25));

	        Scene scene = new Scene(new javafx.scene.layout.VBox(menuBar, grid), 500, 500);
	        stage.setScene(scene);
	        
	        Label userLabel = new Label("Email:");
	        grid.add(userLabel, 0, 1);
	        TextField userTextField = new TextField();
	        grid.add(userTextField, 1, 1);

	        Label passwordLabel = new Label("Password:");
	        grid.add(passwordLabel, 0, 2);
	        PasswordField passwordField = new PasswordField();
	        grid.add(passwordField, 1, 2);


	        Button loginButton = new Button("Login");
	        grid.add(loginButton, 1, 3);
	        

	        loginButton.setOnAction(e -> {
	            String user= userTextField.getText();
	            String password = passwordField.getText();

	            if (isValidCredentials(user, password)) {
	                new Customer(primaryStage);
	                
	            } else {
	                showInvalidUsernameAlert();
	            }
	        });

	        stage.show();
	    }

	    private boolean isValidUsername(String user) {
	    	 String[] allowedDomains = {"@gmail.com", "@yahoo.com", "@outlook.com", "@gmail.co.id"};

	    	    for (String domain : allowedDomains) {
	    	        if (user.toLowerCase().endsWith(domain)) {
	    	            String query = String.format(
	    	                    "SELECT * FROM Userss WHERE username = '%s'", user);
	    	        }
	    	    }

	    	    return false;
	    }

	    private boolean isValidPassword(String password) {
	    	return password.matches("\\d+");
	    }
	    
	    private boolean isValidCredentials(String user, String password) {
	    	 DatabaseManager connect = DatabaseManager.getInstace();
	    	    String query = String.format(
	    	            "SELECT * FROM Userss WHERE username = '%s' AND password = '%s'", user, password);
	    	    try {
	    	        ResultSet rs = connect.execQuery(query);
	    	        boolean isValid = rs.next();
	    	        System.out.println("Query: " + query);
	    	        System.out.println("isValid: " + isValid);
	    	        return isValid;
	    	    } catch (SQLException e) {
	    	        e.printStackTrace();
	    	        return false;
	    	    }
	    }
	    private void showInvalidUsernameAlert() {
	    	 Alert alert = new Alert(Alert.AlertType.ERROR);
	    	    alert.setTitle("Invalid Username");
	    	    alert.setHeaderText(null);
	    	    alert.setContentText("The username must end with @gmail.com / @yahoo.com / @outlook.com / @gmail.co.id / Password must be numeric.");
	    	    alert.showAndWait();

	    	    System.out.println("Invalid login attempt");
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
		
	}


