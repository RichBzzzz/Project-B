import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

public class LibraryView {
    private Stage stage;

    public TextField usernameField = new TextField();
    public PasswordField passwordField = new PasswordField();
    public TextField regNameField = new TextField();
    public TextField regUserField = new TextField();
    public TextField regPassField = new TextField();
    public Button regMember = new Button ("Member");
    public Button regLibrarian = new Button ("Librarian");
    
    public TextField searchField = new TextField();
    public TextField bookIdField = new TextField();
    public TextField dateRentField = new TextField();
    public TextField dateDueField = new TextField();
    public TextField qtyField = new TextField();
    
    public TextField newTitle = new TextField();
    public TextField newAuthor = new TextField();
    public TextField newGenre = new TextField();
    
    public Button loginBtn = new Button("Login");
    public Button registerBtn = new Button("Register");
    public Button backBtn = new Button("Back");
    public Button actionBtn = new Button("Submit");

    public TableView<Book> table = new TableView<>();

    public LibraryView(Stage stage) {
        this.stage = stage;
    }

    private VBox createBaseLayout(String title) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        Label header = new Label("Library Management System");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        Label subHeader = new Label(title);
        subHeader.setFont(Font.font("Arial", 14));
        layout.getChildren().addAll(header, subHeader);
        return layout;
    }

    private void showScene(VBox layout) {
        Scene scene = new Scene(layout, 400, 500);
        stage.setScene(scene);
        stage.show();
    }

    public void drawLanding(Button memberBtn, Button libBtn, Button regLink) {
        VBox layout = createBaseLayout("Log in");
        layout.getChildren().addAll(memberBtn, libBtn, new Label("Don't have an account yet?"), regLink);
        showScene(layout);
    }

    public void drawLogin(String type) {
        VBox layout = createBaseLayout(type + " Log in");
        usernameField.clear(); passwordField.clear();
        layout.getChildren().addAll(new Label("Username"), usernameField, new Label("Password"), passwordField, loginBtn, backBtn);
        showScene(layout);
    }

    public void drawRegister() {
        VBox layout = createBaseLayout("Register");
        HBox accountType = new HBox(15);
        accountType.setAlignment(Pos.CENTER);
        accountType.getChildren().addAll(regMember, regLibrarian);
        layout.getChildren().addAll(
            new Label("Full Name:"), regNameField,
            new Label("Username:"), regUserField,
            new Label("Password:"), regPassField,
            new Label ("Account type:"), accountType, backBtn
        );
        showScene(layout);
    }

    public void drawMenu(String user, List<Button> buttons) {
        VBox layout = createBaseLayout("Welcome " + user + "! Choose an option");
        for (Button b : buttons) {
            b.setMaxWidth(Double.MAX_VALUE);
            layout.getChildren().add(b);
        }
        showScene(layout);
    }

    public void drawBookTable(List<Book> books) {
        VBox layout = createBaseLayout("All Books");
        
        TableColumn<Book, String> nameCol = new TableColumn<>("Books"); // Title
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        
        TableColumn<Book, String> authCol = new TableColumn<>("Author"); // Author
        authCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));
        
        TableColumn<Book, String> statCol = new TableColumn<>("Status"); // Status
        statCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatusStr()));

        TableColumn<Book, String> qtyCol = new TableColumn<>("Quantity"); // Quantity
        qtyCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));

        TableColumn<Book, String> idCol = new TableColumn<>("ID"); // ID
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookID()));

        // Set columns and items
        table.getColumns().setAll(idCol, nameCol, authCol, statCol, qtyCol);
        table.getItems().setAll(books);
        
        // Layout
        layout.getChildren().addAll(table, backBtn);
        showScene(layout);
    }

    public void drawInputForm(String title, String label, boolean showStock) {
        VBox layout = createBaseLayout(title);
        searchField.clear(); bookIdField.clear(); qtyField.clear();
        layout.getChildren().addAll(new Label(label), searchField);
        if (showStock) layout.getChildren().addAll(new Label("Quantity to remove"), qtyField);
        layout.getChildren().addAll(actionBtn, backBtn);
        showScene(layout);
    }

    public void drawBorrowForm() {
        VBox layout = createBaseLayout("Borrow Book");
        bookIdField.clear(); dateRentField.clear(); dateDueField.clear();
        layout.getChildren().addAll(
            new Label("Enter bookID"), bookIdField,
            new Label("Enter rent date"), dateRentField,
            new Label("Enter due date"), dateDueField,
            actionBtn, backBtn
        );
        showScene(layout);
    }

    public void drawWishlist() {
        VBox layout = createBaseLayout("Reading Wish List");
        newTitle.clear(); bookIdField.clear();
        layout.getChildren().addAll(
            new Label("Enter book title"), newTitle,
            new Label("Enter book ID"), bookIdField,
            actionBtn, backBtn
        );
        showScene(layout);
    }

    public void drawHistory(List<String> records) {
        VBox layout = createBaseLayout("Borrowing History");
        ListView<String> list = new ListView<>();
        list.getItems().setAll(records);
        layout.getChildren().addAll(list, backBtn);
        showScene(layout);
    }

    public void drawAddBook() {
        VBox layout = createBaseLayout("Add Book");
        newTitle.clear(); bookIdField.clear(); newGenre.clear(); newAuthor.clear(); qtyField.clear();
        layout.getChildren().addAll(
            new Label("Enter title"), newTitle,
            new Label("Enter book ID"), bookIdField,
            new Label("Enter Genre (HORROR, FANTASY...)"), newGenre,
            new Label("Enter Author"), newAuthor,
            new Label("Quantity"), qtyField,
            actionBtn, backBtn
        );
        showScene(layout);
    }

    public void drawSearchBy(String type){
        VBox layout = createBaseLayout("Search by " + type);
        layout.getChildren().addAll(new Label ("Enter " + type.toLowerCase()), searchField, actionBtn, backBtn);
        showScene(layout);
    }

    public void drawAddToWishList(){
        VBox layout = createBaseLayout("Add to wish list");
        layout.getChildren().addAll(new Label("Enter BookID to add to your wish list"), bookIdField, actionBtn, backBtn);
        actionBtn.setText("Add");
        showScene(layout);
    }

    public void drawWishList(List<Book> books){
        VBox layout = createBaseLayout("Reading Wish List");
        
        TableColumn<Book, String> nameCol = new TableColumn<>("Books"); // Title
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        
        TableColumn<Book, String> authCol = new TableColumn<>("Author"); // Author
        authCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));
        
        TableColumn<Book, String> statCol = new TableColumn<>("Status"); // Status
        statCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatusStr()));

        TableColumn<Book, String> qtyCol = new TableColumn<>("Quantity"); // Quantity
        qtyCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));

        TableColumn<Book, String> idCol = new TableColumn<>("ID"); // ID
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookID()));

        // Set columns and items
        table.getColumns().setAll(idCol, nameCol, authCol, statCol, qtyCol);
        table.getItems().setAll(books);
        
        // Layout
        layout.getChildren().addAll(table, backBtn);
        showScene(layout);
    }
}
