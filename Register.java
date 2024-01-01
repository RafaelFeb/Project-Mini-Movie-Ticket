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

public class Register extends Application{
	DatabaseManager connect = DatabaseManager.getInstace();
	private Stage primaryStage;

    public Register(Stage primaryStage) {
        this.primaryStage = primaryStage;
        start(primaryStage);
    }

	@Override
    public void start(Stage stage) {
        stage.setTitle("Register");

        
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");

        
        MenuItem menuItemBack = new MenuItem("Back to Main");
        menuItemBack.setOnAction(e -> {
            new Main().start(primaryStage);
        });

        menuFile.getItems().add(menuItemBack);
        menuBar.getMenus().add(menuFile);

        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(new javafx.scene.layout.VBox(menuBar, grid), 500, 500);
        stage.setScene(scene);

       
        Label firstNameLabel = new Label("First Name:");
        grid.add(firstNameLabel, 0, 1);
        TextField firstNameTextField = new TextField();
        grid.add(firstNameTextField, 1, 1);

        Label lastNameLabel = new Label("Last Name:");
        grid.add(lastNameLabel, 0, 2);
        TextField lastNameTextField = new TextField();
        grid.add(lastNameTextField, 1, 2);

        Label gmailLabel = new Label("Email:");
        grid.add(gmailLabel, 0, 3);
        TextField gmailTextField = new TextField();
        grid.add(gmailTextField, 1, 3);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 4);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 4);

        
        Button registerButton = new Button("Register");
        grid.add(registerButton, 1, 5);

      
        registerButton.setOnAction(e -> {
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String gmail = gmailTextField.getText();
            String password = passwordField.getText();

            if (isValidFirstName(firstName) && isValidLastName(lastName) && isValidGmail(firstName, lastName, gmail) && isValidPassword(password)) {
               
            	String query = String.format(
                        "INSERT INTO Users (first_name, last_name, gmail, password) " +
                        "VALUES ('%s', '%s', '%s', '%s')",
                        firstName, lastName, gmail, password);
                       connect.execUpdate(query);
                new Login(primaryStage);
            } else {
                
                showInvalidRegistrationAlert();
            }
        });
	
        
        stage.show();
    }

	 private boolean isValidFirstName(String firstName) {
	        return !firstName.isEmpty() && Character.isUpperCase(firstName.charAt(0));
	    }

	    private boolean isValidLastName(String lastName) {
	        return !lastName.isEmpty() && Character.isUpperCase(lastName.charAt(0));
	    }

	    private boolean isValidGmail(String firstName, String lastName, String gmail) {
	    	 String[] allowedDomains = {"@gmail.com", "@yahoo.com", "@outlook.com", "@gmail.co.id"};

	    	    String expectedEmail = (firstName + lastName + allowedDomains[0]).toLowerCase(); // Assuming Gmail as default

	    	    for (String domain : allowedDomains) {
	    	        if (gmail.toLowerCase().endsWith(domain)) {
	    	            expectedEmail = (firstName + lastName + domain).toLowerCase();
	    	            return gmail.equalsIgnoreCase(expectedEmail);
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
        alert.setContentText("Make sure the First Letter are capitalized,Password must numeric and Gmail must end with 'gmail.com' '@yahoo.com' '@outlook.com'.");
        alert.showAndWait();
    }

    private void insertUserData(String firstName, String lastName, String gmail, String password) {
        String query = String.format(
                "INSERT INTO Users (first_name, last_name, gmail, password) " +
                "VALUES ('%s', '%s', '%s', '%s')",
                firstName, lastName, gmail, password);
               connect.execUpdate(query);
   
    }
    public static void main(String[] args) {
        launch(args);
    }
}

