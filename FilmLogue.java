package main;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.DatabaseManager;

public class FilmLogue extends Application{

    
	private Stage primaryStage;

	DatabaseManager connect = DatabaseManager.getInstace();
	
    public FilmLogue(Stage primaryStage) {
        this.primaryStage = primaryStage;
        start(primaryStage);
    }
	
	 private final ObservableList<Movie> data = FXCollections.observableArrayList();
	    private TableView<Movie> tableView;
	    private ImageView imageView;
	    private Label imageLabel;

	   
	    @Override
	    public void start(Stage primaryStage) {
	    	BorderPane root = new BorderPane();
	        VBox vBox = new VBox();
	        VBox rightBox = new VBox(10);

	        
	        MenuBar menuBar = createMenuBar();
	        root.setTop(menuBar);

	        
	        GridPane formPane = createFormPane();
	        vBox.getChildren().add(formPane);

 
	        tableView = createTableView();
	        root.setCenter(tableView);
	        
	        
	        imageView = new ImageView();
	        imageView.setFitWidth(200);
	        imageView.setFitHeight(300);
         
	             
	        StackPane stackPane = new StackPane();
	        stackPane.getChildren().add(imageView);

	        rightBox.getChildren().add(stackPane);
    
	        vBox.getChildren().add(tableView);

	        root.setLeft(vBox);

	        root.setRight(rightBox);

            getData();
	        primaryStage.setScene(new Scene(root, 1000, 700));
	        primaryStage.setTitle("Filmlogue");
	        primaryStage.show();
	    }


	    
	    private MenuBar createMenuBar() {
	    	 MenuBar menuBar = new MenuBar();

	         Menu movieMenu = new Menu("Movie");
	         MenuItem viewMovieItem = new MenuItem("View Movie");
	         MenuItem deleteMovieItem = new MenuItem("Delete Movie");
	         MenuItem importImageItem = new MenuItem("Import Image");
	         movieMenu.getItems().addAll(viewMovieItem, deleteMovieItem, importImageItem);
	         
	         
	         Menu checkoutMenu = new Menu("Checkout");
	         MenuItem checkoutItem = new MenuItem("Proceed to Checkout");
	         checkoutMenu.getItems().addAll(checkoutItem);

             
	         Menu logoutMenu = new Menu("Log out");
	         MenuItem logoutItem = new MenuItem("Log Out");
	         logoutMenu.getItems().addAll(logoutItem);
	        
	         viewMovieItem.setOnAction(e -> showMovieDetailsPopup());
	         deleteMovieItem.setOnAction(e -> handleDeleteMovie());
	         importImageItem.setOnAction(e -> importImage());
	         checkoutItem.setOnAction(e -> showCheckoutPopup());
	         logoutItem.setOnAction(e -> new Login(primaryStage));
	         
	         menuBar.getMenus().addAll(movieMenu,checkoutMenu, logoutMenu);

	         return menuBar;	
	    }
	    




		private GridPane createFormPane() {
	        GridPane formPane = new GridPane();
	        formPane.setHgap(10);
	        formPane.setVgap(5);
	        formPane.setPadding(new Insets(10));

	        TextField titleField = new TextField();
	        Spinner<Integer> ratingSpinner = new Spinner<>(1, 10, 5);
	        TextArea descriptionArea = new TextArea();
	        descriptionArea.setWrapText(true); // Set wrapText to true for description
	        descriptionArea.setPrefRowCount(5);
	        TextField priceField = new TextField();
	        Spinner<Integer> quantitySpinner = new Spinner<>(1, 20, 1);
	        Button addButton = new Button("Add");
	        
	        

	        formPane.add(new Label("Title:"), 0, 0);
	        formPane.add(titleField, 1, 0);
	        formPane.add(new Label("Rating:"), 0, 1);
	        formPane.add(ratingSpinner, 1, 1);
	        formPane.add(new Label("Description:"), 0, 2);
	        formPane.add(descriptionArea, 1, 2);
	        formPane.add(new Label("Price:"), 0, 3);
	        formPane.add(priceField, 1, 3);
	        formPane.add(new Label("Quantity:"), 0, 4); 
	        formPane.add(quantitySpinner, 1, 4);
	        formPane.add(addButton, 2, 0);

	    
	        addButton.setOnAction(e -> {
	            String title = titleField.getText();
	            int rating = ratingSpinner.getValue();
	            String description = descriptionArea.getText();
	            String price = priceField.getText();
	            int quantity = quantitySpinner.getValue();
	            
	            String query = String.format(
						"INSERT INTO Mainmenu(title, rating, description, price, quantity)" + "VALUES('%s', '%d', '%s', '%s', '%d')", title, rating, description, price, quantity);
				connect.execUpdate(query);
				// %d integer, %f decimal, %s String
//				getData();

	            
	            if (!title.isEmpty()) {
	                data.add(new Movie(title, rating, description, price, quantity));
	                titleField.clear();
	                ratingSpinner.getValueFactory().setValue(5);
	                descriptionArea.clear();
		                priceField.clear();
	            }
	        });
	      
	        return formPane;
	    }

		
	    private void handleDeleteMovie() {
	    	 ObservableList<Movie> selectedMovies = tableView.getSelectionModel().getSelectedItems();

	    	    if (!selectedMovies.isEmpty()) {
	    	        for (Movie movie : selectedMovies) {
	    	            String query = String.format("DELETE FROM Mainmenu WHERE title = '%s'", movie.getTitle());
	    	            connect.execUpdate(query);
	    	        }

	    	        // Remove selected movies from the TableView
	    	        tableView.getItems().removeAll(selectedMovies);
	        } else {
	            Alert alert = new Alert(Alert.AlertType.WARNING);
	            alert.setTitle("No Selection");
	            alert.setHeaderText("No Movie Selected");
	            alert.setContentText("Please select a movie in the table.");
	            alert.showAndWait();
	        }
	    }

	    private TableView<Movie> createTableView() {
	        TableView<Movie> tableView = new TableView<>();
	        TableColumn<Movie, String> titleCol = new TableColumn<>("Title");
	        TableColumn<Movie, Integer> ratingCol = new TableColumn<>("Rating");
	        TableColumn<Movie, String> descriptionCol = new TableColumn<>("Description");
	        TableColumn<Movie, Integer> quantityCol = new TableColumn<>("Quantity");
	        TableColumn<Movie, String> priceCol = new TableColumn<>("Price");

	        
	        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
	        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
	        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
	        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
	        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
	        


	        tableView.getColumns().addAll(titleCol, ratingCol, descriptionCol,quantityCol, priceCol);
	        tableView.setItems(data);

	        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
         
	        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	            if (newSelection != null) {
	                updateImageView(newSelection);
	            }
	        });
	        
	        return tableView;
	    }

	    private void showAddMovieDialog() {
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Add Movie");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(tableView.getScene().getWindow());

	        GridPane grid = new GridPane();
	        grid.setHgap(10);
	        grid.setVgap(5);
	        grid.setPadding(new Insets(20, 150, 20, 150));

	        TextField titleField = new TextField();
	        Spinner<Integer> ratingSpinner = new Spinner<>(1, 10, 5);
	        Button addButton = new Button("Add");

	        grid.add(new Label("Title:"), 0, 0);
	        grid.add(titleField, 1, 0);
	        grid.add(new Label("Rating:"), 0, 1);
	        grid.add(ratingSpinner, 1, 1);
	        grid.add(addButton, 1, 2);
	        

	        addButton.setOnAction(e -> {
	            String title = titleField.getText();
	            int rating = ratingSpinner.getValue();
	            if (!title.isEmpty()) {
	                titleField.clear();
	                ratingSpinner.getValueFactory().setValue(5);
	                dialogStage.close();
	            }
	        });

	        Scene scene = new Scene(grid);
	        dialogStage.setScene(scene);
	        dialogStage.showAndWait();
	    }

	    private void importImage() {   	
	    	    FileChooser fileChooser = new FileChooser();
	    	    File file = fileChooser.showOpenDialog(null);

	    	    if (file != null) {
	    	        Image image = new Image(file.toURI().toString());
	    	        imageView.setImage(image);
	    	        
	    	        imageView.setFitWidth(400);
	    	        imageView.setFitHeight(700);
	    	    }
	    }
	    
	    private void updateImageView(Movie movie) {
	        String imagePath = "images/" + movie.getTitle().toLowerCase() + ".jpg";

	        try {
	        	 File file = new File(imagePath);
	             Image image = new Image("file:10374.jpg");
	             ImageView iv = new ImageView();
	             iv.setImage(image);
	             iv.setFitHeight(600);
	             iv.setFitWidth(600);
	             
	             String imageDetails = "Title: " + movie.getTitle() + "\nRating: " + movie.getRating();
	             imageLabel.setText(imageDetails);

	             imageLabel.setVisible(true);
	             
	        } catch (Exception e) {
	            imageView.setImage(null);
	            imageLabel.setVisible(false);
	        }
	    }
	    
	    private void showMovieDetailsPopup() {
	        Movie selectedMovie = tableView.getSelectionModel().getSelectedItem();

	        if (selectedMovie != null) {
	            Alert alert = new Alert(Alert.AlertType.INFORMATION);
	            alert.setTitle("Movie Details");
	            alert.setHeaderText(null);
	            
	            String details = "Title: " + selectedMovie.getTitle() + "\nRating: " + selectedMovie.getRating()
	                    + "\nDescription: " + selectedMovie.getDescription() + "\nPrice: " + selectedMovie.getPrice() + "\nQuantity: " +selectedMovie.getQuantity();

	            alert.setContentText(details);

	            alert.showAndWait();
	        } else {
	            Alert alert = new Alert(Alert.AlertType.WARNING);
	            alert.setTitle("No Selection");
	            alert.setHeaderText("No Movie Selected");
	            alert.setContentText("Please select a movie in the table.");
	            alert.showAndWait();
	        }
	    }
	    
	   
	    private void showCheckoutPopup() {
	        if (data.isEmpty()) {
	            Alert alert = new Alert(Alert.AlertType.INFORMATION);
	            alert.setTitle("Checkout");
	            alert.setHeaderText(null);
	            alert.setContentText("No movies in the checkout. Add movies before proceeding to checkout.");
	            alert.showAndWait();
	            return;
	        }

	        TableView<Movie> checkoutTableView = createCheckoutTableView();
	        Label totalLabel = new Label("Total Price:");
	        TextField totalTextField = new TextField();
	        totalTextField.setEditable(false);
	        totalTextField.setText(String.format("%.2f", calculateTotalPrice()));

	        ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
	        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

	        Stage checkoutStage = new Stage();
	        checkoutStage.setTitle("Checkout");
	        checkoutStage.initModality(Modality.APPLICATION_MODAL);

	        Button okButton = new Button("OK");
	        okButton.setDefaultButton(true);

	        okButton.setOnAction(e -> {
	            data.removeAll(checkoutTableView.getSelectionModel().getSelectedItems()); // Remove selected movies
	            checkoutStage.close();
	        });

	        GridPane grid = new GridPane();
	        grid.setHgap(10);
	        grid.setVgap(5);
	        grid.setPadding(new Insets(20, 150, 20, 150));
	        grid.add(checkoutTableView, 0, 0, 2, 1);
	        grid.add(totalLabel, 0, 1);
	        grid.add(totalTextField, 1, 1);
	        grid.add(okButton, 0, 2, 2, 1);
	        
	        totalTextField.setText(String.format("%.3f", calculateTotalPrice()));

	        Scene checkoutScene = new Scene(grid);
	        checkoutStage.setScene(checkoutScene);

	        checkoutStage.showAndWait();
	    }

	    private TableView<Movie> createCheckoutTableView() {
	        TableView<Movie> tableView = new TableView<>();
	        TableColumn<Movie, String> titleCol = new TableColumn<>("Title");
	        TableColumn<Movie, Integer> ratingCol = new TableColumn<>("Rating");
	        TableColumn<Movie, String> descriptionCol = new TableColumn<>("Description");
	        TableColumn<Movie, String> priceCol = new TableColumn<>("Price");
	        TableColumn<Movie, Integer> quantityCheckoutCol = new TableColumn<>("Quantity");
	        
	        
	        quantityCheckoutCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
	        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
	        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
	        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
	        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

	        tableView.getColumns().addAll(titleCol, ratingCol, descriptionCol, priceCol, quantityCheckoutCol);
	        tableView.setItems(data);

	        
	        return tableView;
	    }
	    
	    
	    private double calculateTotalPrice() {
	        double total = 0.0;

	        for (Movie movie : data) {
	            try {
	                double price = Double.parseDouble(movie.getPrice());
	                int quantity = movie.getQuantity();
	                total += (price * quantity);
	            } catch (NumberFormatException e) {
	                e.printStackTrace();
	            }
	        }
	        return total;
	    }
	    
	    public static void main(String[] args) {
	        launch(args);
	    }

	    public class Movie {
	        private SimpleStringProperty title;
	        private SimpleIntegerProperty rating;
	        private SimpleStringProperty description;
	        private SimpleStringProperty price;
	        private SimpleIntegerProperty quantity;



	        public Movie(String title, int rating, String description, String price, int quantity) {
	            this.title = new SimpleStringProperty(title);
	            this.rating = new SimpleIntegerProperty(rating);
	            this.description = new SimpleStringProperty(description);
	            this.price = new SimpleStringProperty(price);
	            this.quantity = new SimpleIntegerProperty(quantity);
	        }

	        public String getTitle() {
	            return title.get();
	        }

	        public SimpleStringProperty titleProperty() {
	            return title;
	        }

	        public void setTitle(String title) {
	            this.title.set(title);
	        }

	        public int getRating() {
	            return rating.get();
	        }

	        public SimpleIntegerProperty ratingProperty() {
	            return rating;
	        }

	        public void setRating(int rating) {
	            this.rating.set(rating);
	        }
	        
	        public void setDescription(String description) {
	            this.description.set(description);
	        }
	        
	        public String getDescription() {
	            return description.get();
	        }
	        
	        public SimpleStringProperty priceProperty() {
	            return price;
	        }

	        public String getPrice() {
	            return price.get();
	        }

	        public void setPrice(String price) {
	            this.price.set(price);
	        }
	        
	        public int getQuantity() {
	            return quantity.get();
	        }

	        public SimpleIntegerProperty quantityProperty() {
	            return quantity;
	        }

	        public void setQuantity(int quantity) {
	            this.quantity.set(quantity);
	        }
	        
	    }
	    
	    
	    public void getData() {
	    	
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
			// TODO: handle exception
			e.printStackTrace();
		}
	    }
	    

//		public void insertData(String title, int rating, String description, String price, int quantity) {		
//			String query = String.format(
//					"INSERT INTO Mainmenu(title, rating, description, price, quantity)" + "VALUES('%s', '%d', '%s', '%s', '%d')", title, rating, description, price, quantity);
//			connect.execUpdate(query);
//			 System.out.println("Query: " + query);
//			// %d integer, %f decimal, %s String
//			getData();
//		}
	    
}



