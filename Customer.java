package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.FilmLogue.Movie;
import util.DatabaseManager;

public class Customer extends Application{
	
	 private TableView<Movie> tableView;
	private Stage primaryStage;

	DatabaseManager connect = DatabaseManager.getInstace();
	
    public Customer(Stage primaryStage) {
        this.primaryStage = primaryStage;
        start(primaryStage);
    }
    
	@Override
	public void start(Stage primaryStage){
						
		    VBox vbox = new VBox();
	        vbox.setPadding(new Insets(10));
	        
	        MenuBar menuBar = createMenuBar();
	        vbox.getChildren().add(menuBar);

	        Label titleLabel = new Label("Movie List for Customers");
	        TableView<Movie> tableView = createTableView();

	        vbox.getChildren().addAll(titleLabel, tableView);

	        Scene scene = new Scene(vbox, 600, 400);
	        primaryStage.setScene(scene);
	        primaryStage.setTitle("Customer");
	        primaryStage.show();
	    }

	    private TableView<Movie> createTableView() {
	    	tableView = new TableView<>(); 
	        TableColumn<Movie, String> titleCol = new TableColumn<>("Title");
	        TableColumn<Movie, Integer> ratingCol = new TableColumn<>("Rating");
	        TableColumn<Movie, String> descriptionCol = new TableColumn<>("Description");
	        TableColumn<Movie, String> priceCol = new TableColumn<>("Price");
	        TableColumn<Movie, Integer> quantityCol = new TableColumn<>("Quantity");

	        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
	        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
	        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
	        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
	        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

	        tableView.getColumns().addAll(titleCol, ratingCol, descriptionCol, priceCol, quantityCol);

	        getData(tableView);

	        return tableView;
	    }
	    
	    private MenuBar createMenuBar() {
	        MenuBar menuBar = new MenuBar();

	        Menu checkoutMenu = new Menu("Checkout");
	        MenuItem checkoutItem = new MenuItem("Proceed to Checkout");
	        checkoutMenu.getItems().addAll(checkoutItem);

	        Menu logoutMenu = new Menu("Log Out");
	        MenuItem logoutItem = new MenuItem("Log Out");
	        logoutMenu.getItems().addAll(logoutItem);
	        
	        Menu updateMenu = new Menu("Update");
	        MenuItem updateItem = new MenuItem("Update Quantity");
	        updateMenu.getItems().addAll(updateItem);

	        updateItem.setOnAction(e -> {
	            Movie selectedMovie = tableView.getSelectionModel().getSelectedItem();
	            if (selectedMovie != null) {
	                showUpdatePopup(selectedMovie);
	            } else {
	                // Show an alert or handle the case when no movie is selected
	                System.out.println("No movie selected for update");
	            }
	        });

	        checkoutItem.setOnAction(e -> {
	            Movie selectedMovie = tableView.getSelectionModel().getSelectedItem();
	            if (selectedMovie != null) {
	                showCheckoutPopup(selectedMovie);
	            } else {
	                // Show an alert or handle the case when no movie is selected
	                System.out.println("No movie selected for checkout");
	            }
	        });
	        
	        logoutItem.setOnAction(e -> new Logins(primaryStage));

	        menuBar.getMenus().addAll(checkoutMenu, updateMenu, logoutMenu);

	        return menuBar;
	    }
	    

	    private void getData(TableView<Movie> tableView) {
	        String query = "SELECT * FROM Mainmenu";
	        ResultSet rs = connect.execQuery(query);

	        tableView.getItems().clear();

	        try {
	            while (rs.next()) {
	                String title = rs.getString("Title");
	                int rating = rs.getInt("Rating");
	                String description = rs.getString("Description");
	                String price = rs.getString("Price");
	                int quantity = rs.getInt("Quantity");
	                tableView.getItems().add(new Movie(title, rating, description, price, quantity));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    private void showCheckoutPopup(Movie selectedMovie) {
	    	Stage popupStage = new Stage();
	        popupStage.setTitle("Checkout");

	        Label titleLabel = new Label("Selected Movie:");
	        Label movieLabel = new Label(selectedMovie.getTitle());

	        Label ratingLabel = new Label("Rating: " + selectedMovie.getRating());
	        Label descriptionLabel = new Label("Description: " + selectedMovie.getDescription());
	        Label priceLabel = new Label("Price: " + selectedMovie.getPrice());
	        Label quantityLabel = new Label("Quantity: " + selectedMovie.getQuantity());
	        
	        double totalPrice = selectedMovie.getQuantity() * Double.parseDouble(selectedMovie.getPrice());
	        Label totalPriceLabel = new Label("Total Price: Rp. " + String.format("%.3f", totalPrice));

	        Button okButton = new Button("OK");
	        okButton.setOnAction(e -> {
	            tableView.getItems().remove(selectedMovie);
	            deleteData(selectedMovie.getTitle());
	            popupStage.close();
	        });

	        Button cancelButton = new Button("Cancel");
	        cancelButton.setOnAction(e -> popupStage.close());

	        VBox popupVBox = new VBox(10);
	        popupVBox.getChildren().addAll(
	                titleLabel, movieLabel, ratingLabel, descriptionLabel,
	                priceLabel, quantityLabel, totalPriceLabel, okButton, cancelButton
	        );
	        popupVBox.setAlignment(javafx.geometry.Pos.CENTER);

	        Scene popupScene = new Scene(popupVBox, 400, 250);
	        popupStage.setScene(popupScene);

	        popupStage.show();
	    }
	    
	    private void showUpdatePopup(Movie selectedMovie) {
	        Stage updatePopupStage = new Stage();
	        updatePopupStage.setTitle("Update Quantity");

	        Label titleLabel = new Label("Selected Movie:");
	        Label movieLabel = new Label(selectedMovie.getTitle());

	        // Add a TextField for the user to input the updated quantity
	        TextField quantityField = new TextField();
	        quantityField.setPromptText("Enter updated quantity");

	        Button updateButton = new Button("Update");
	        updateButton.setOnAction(e -> {
	            String updatedQuantityText = quantityField.getText();
	            if (!updatedQuantityText.isEmpty()) {
	                int updatedQuantity = Integer.parseInt(updatedQuantityText);

	                // Update the quantity in TableView
	                selectedMovie.setQuantity(updatedQuantity);
	                tableView.refresh();

	                // Update the quantity in the database
	                updateData(selectedMovie.getTitle(), updatedQuantity);

	                updatePopupStage.close();
	            } else {
	                // Show an alert or handle the case when the quantity field is empty
	                System.out.println("Please enter a valid quantity");
	            }
	        });

	        VBox updatePopupVBox = new VBox(10);
	        updatePopupVBox.getChildren().addAll(titleLabel, movieLabel, quantityField, updateButton);
	        updatePopupVBox.setAlignment(javafx.geometry.Pos.CENTER);

	        Scene updatePopupScene = new Scene(updatePopupVBox, 400, 200);
	        updatePopupStage.setScene(updatePopupScene);

	        updatePopupStage.show();
	    }
	    
	    private void updateData(String title, int updatedQuantity) {
	        String query = "UPDATE Mainmenu SET Quantity = " + updatedQuantity + " WHERE Title = '" + title + "'";
	        connect.execUpdate(query);
	    }
	    
	    private void deleteData(String title) {
	        String query = "DELETE FROM Mainmenu WHERE Title = '" + title + "'";
	        connect.execUpdate(query);
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
	    
	    public class Movie {
	        private String title;
	        private int rating;
	        private String description;
	        private String price;
	        private int quantity;

	        // Constructor
	        public Movie(String title, int rating, String description, String price, int quantity) {
	            this.title = title;
	            this.rating = rating;
	            this.description = description;
	            this.price = price;
	            this.quantity = quantity;
	        }
	        
	        public void setQuantity(int quantity) {
	            this.quantity = quantity;
	        }

	        // Getters
	        public String getTitle() {
	            return title;
	        }

	        public int getRating() {
	            return rating;
	        }

	        public String getDescription() {
	            return description;
	        }

	        public String getPrice() {
	            return price;
	        }

	        public int getQuantity() {
	            return quantity;
	        }
		
	}
}
