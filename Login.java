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

public class Login extends Application {
	DatabaseManager connect = DatabaseManager.getInstace();
    private Stage primaryStage;

    public Login(Stage primaryStage) {
        this.primaryStage = primaryStage;
        start(primaryStage);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Login");


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

 
        Label gmailLabel = new Label("Email:");
        grid.add(gmailLabel, 0, 1);
        TextField gmailTextField = new TextField();
        grid.add(gmailTextField, 1, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);


        Button loginButton = new Button("Login");
        grid.add(loginButton, 1, 3);
     
       
        

        loginButton.setOnAction(e -> {
            String gmail= gmailTextField.getText();
            String password = passwordField.getText();

            if (isValidCredentials(gmail, password)) {
                new FilmLogue(primaryStage);
                
            } else {
                showInvalidUsernameAlert();
            }
        });

        stage.show();
    }

    private boolean isValidUsername(String gmail) {
    	 String[] allowedDomains = {"@gmail.com", "@yahoo.com", "@outlook.com", "@gmail.co.id"};

    	    for (String domain : allowedDomains) {
    	        if (gmail.toLowerCase().endsWith(domain)) {
    	            String query = String.format(
    	                    "SELECT * FROM Userss WHERE username = '%s'", gmail);
    	        }
    	    }

    	    return false;
    }

    private boolean isValidPassword(String password) {
    	return password.matches("\\d+");
    }

    private void showInvalidUsernameAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Username");
        alert.setHeaderText(null);
        alert.setContentText("The username must end with @gmail.com / @yahoo.com / @outlook.com / @gmail.co.id / Password must be numeric.");
        alert.showAndWait();
    }
    
    private boolean isValidCredentials(String gmail, String password) {
        DatabaseManager connect = DatabaseManager.getInstace();
        String query = String.format(
                "SELECT * FROM Users WHERE gmail = '%s' AND password = '%s'", gmail, password);
        try {
            ResultSet rs = connect.execQuery(query);
            return rs.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


