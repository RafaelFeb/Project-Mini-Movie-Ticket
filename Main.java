package main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class Main extends Application {
    private Scene scene;

    private VBox vbox;
    private Button btn_page1;
    private Button btn_page2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome");

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Admin");
        Menu MenuCustomer = new Menu("Customer");
        
        
        MenuItem menuItemlogins = new MenuItem("Logins");
        menuItemlogins.setOnAction(e -> new Logins(primaryStage));
        
        MenuItem menuItemRegisters = new MenuItem("Registers");
        menuItemRegisters.setOnAction(e -> new Registers(primaryStage));
        

        MenuItem menuItemLogin = new MenuItem("Login");
        menuItemLogin.setOnAction(e -> new Login(primaryStage));

      
        MenuItem menuItemRegister = new MenuItem("Register");
        menuItemRegister.setOnAction(e -> new Register(primaryStage));
        
        MenuCustomer.getItems().addAll(menuItemlogins, menuItemRegisters);
        menuBar.getMenus().add(MenuCustomer);
        
        
        menuFile.getItems().addAll(menuItemLogin, menuItemRegister);
        menuBar.getMenus().add(menuFile);
       
        vbox = new VBox();
        
        Label titleLabel = new Label("Welcome to FilmLogue");
        titleLabel.setStyle("-fx-font-size: 24px;");  
        
        Label subtitleLabel = new Label("2602061605 - Rafael Febrian Kurniawan " + "\n2602056643-Dylan Nathanael" + "\n2602137786 - Ernest Tan"); 
        subtitleLabel.setStyle("-fx-font-size: 16px;");
        
        
        
        
        vbox.getChildren().addAll(titleLabel, subtitleLabel);

        vbox.setAlignment(Pos.CENTER);
       
        Scene scene = new Scene(new javafx.scene.layout.VBox(menuBar, vbox), 500, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
    

}
