package main;

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

public class Registers extends Application{
	DatabaseManager connect = DatabaseManager.getInstace();
	
	private Stage primaryStage;

    public Registers(Stage primaryStage) {
        this.primaryStage = primaryStage;
        start(primaryStage);
    }
    
	@Override
	public void start(Stage stage) {
		
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
	        grid.add(userLabel, 0, 3);
	        TextField userTextField = new TextField();
	        grid.add(userTextField, 1, 3);

	        Label passwordLabel = new Label("Password:");
	        grid.add(passwordLabel, 0, 4);
	        PasswordField passwordField = new PasswordField();
	        grid.add(passwordField, 1, 4);

	        
	        Button registerButton = new Button("Register");
	        grid.add(registerButton, 1, 5);
	        

	        registerButton.setOnAction(e -> {
	            String user = userTextField.getText();
	            String password = passwordField.getText();

	            if ( isValiduser(user) && isValidPassword(password)) {
	               
	            	String query = String.format(
	                        "INSERT INTO Userss (username, password) " +
	                        "VALUES ('%s', '%s')",user, password);
	                       connect.execUpdate(query);
	                new Logins(primaryStage);
	            } else {
	    
	                showInvalidRegistrationAlert();
	            }
	        });

	        stage.show();
	    }

	
		    private boolean isValiduser(String user) {
		    	 String[] allowedDomains = {"@gmail.com", "@yahoo.com", "@outlook.com"};

		    	    for (String domain : allowedDomains) {
		    	        if (user.toLowerCase().endsWith(domain)) {
		    	            return true;
		    	        }
		    	    }

		    	    return false;
		    }

		    private boolean isValidPassword(String password) {
		       
		        return password.matches("\\d+");
		    }

	    private void showInvalidRegistrationAlert() {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Invalid Registration");
	        alert.setHeaderText(null);
	        alert.setContentText("Make sure the user must end with 'gmail.com' '@yahoo.com' '@outlook.com' and password must be numeric");
	        alert.showAndWait();
	    }

	    private void insertUserData(String user, String password) {
	        String query = String.format(
	                "INSERT INTO Userss (username, password) " +
	                "VALUES ('%s', '%s')",
	                user, password);
	               connect.execUpdate(query);
	   
	    }
	    public static void main(String[] args) {
	        launch(args);
	    }
		
	}


