import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;

public class LibraryController {
    private LibraryModel model;
    private LibraryView view;

    public LibraryController(LibraryModel model, LibraryView view) {
        this.model = model;
        this.view = view;
        initLanding();
    }

    private void initLanding() {
        Button btnMem = new Button("Member");
        Button btnLib = new Button("Librarian");
        Button btnReg = new Button("Register");

        btnMem.setOnAction(e -> {
            view.drawLogin("Member");
            view.loginBtn.setOnAction(ev -> handleLogin(false));
            view.backBtn.setOnAction(ev -> initLanding());
        });

        btnLib.setOnAction(e -> {
            view.drawLogin("Librarian");
            view.loginBtn.setOnAction(ev -> handleLogin(true));
            view.backBtn.setOnAction(ev -> initLanding());
        });

        btnReg.setOnAction(e -> {
            view.drawRegister();
            view.registerBtn.setOnAction(ev -> handleRegister());
            view.backBtn.setOnAction(ev -> initLanding());
        });

        view.drawLanding(btnMem, btnLib, btnReg);
    }

    private void handleLogin(boolean isLibrarian) {
        User user = model.authenticateUser(view.usernameField.getText(), view.passwordField.getText());
        if (user != null) {
            model.setCurrentUser(user);
            if (isLibrarian && user instanceof Librarian) showLibrarianMenu();
            else if (!isLibrarian && user instanceof Member) showMemberMenu();
            else showAlert("Invalid Role for User");
        } else {
            showAlert("Invalid Credentials");
        }
    }

    private void handleRegister() {
        String res = model.registerMember(view.regNameField.getText(), view.regUserField.getText(), view.regPassField.getText());
        showAlert(res);
        if(res.startsWith("Success")) initLanding();
    }

    private void showLibrarianMenu() {
        List<Button> btns = new ArrayList<>();

        btns.add(createNavBtn("List all books", () -> {
            view.drawBookTable(model.getAllBooks());
            view.backBtn.setOnAction(e -> showLibrarianMenu());
        }));

        btns.add(createNavBtn("Add new book", () -> {
            view.drawAddBook();
            view.actionBtn.setText("Add");
            view.actionBtn.setOnAction(e -> handleAddBook());
            view.backBtn.setOnAction(e -> showLibrarianMenu());
        }));

        btns.add(createNavBtn("Remove book stock", () -> {
            view.drawInputForm("Remove Book", "Enter Book ID", true);
            view.searchField.setPromptText("Book ID");
            view.actionBtn.setText("Remove");
            view.actionBtn.setOnAction(e -> handleRemoveStock());
            view.backBtn.setOnAction(e -> showLibrarianMenu());
        }));

        btns.add(createNavBtn("Logout", this::initLanding));
        view.drawMenu("Librarian", btns);
    }

    private void showMemberMenu() {
        List<Button> btns = new ArrayList<>();

        btns.add(createNavBtn("List all books", () -> {
            view.drawBookTable(model.getAllBooks());
            view.backBtn.setOnAction(e -> showMemberMenu());
        }));

        btns.add(createNavBtn("Borrow book", () -> {
            view.drawBorrowForm();
            view.actionBtn.setText("Borrow");
            view.actionBtn.setOnAction(e -> handleBorrow());
            view.backBtn.setOnAction(e -> showMemberMenu());
        }));

        btns.add(createNavBtn("Return a book", () -> {
            view.drawInputForm("Return Book", "Enter bookID", false);
            view.actionBtn.setText("Return");
            view.actionBtn.setOnAction(e -> {
                showAlert(model.returnBook(view.searchField.getText()));
            });
            view.backBtn.setOnAction(e -> showMemberMenu());
        }));

        btns.add(createNavBtn("Borrowing History", () -> {
            view.drawHistory(model.getMemberHistory());
            view.backBtn.setOnAction(e -> showMemberMenu());
        }));

        btns.add(createNavBtn("Logout", this::initLanding));
        view.drawMenu(((Member)model.getCurrentUser()).getName(), btns);
    }

    private void handleBorrow() {
        String result = model.borrowBook(view.bookIdField.getText(), view.dateRentField.getText(), view.dateDueField.getText());
        showAlert(result);
    }

    private void handleAddBook() {
        try {
            Genre g = Genre.valueOf(view.newGenre.getText().toUpperCase());
            int q = Integer.parseInt(view.qtyField.getText());
            Book b = new Book(view.bookIdField.getText(), view.newTitle.getText(), view.newAuthor.getText(), g, BookStatus.AVAILABLE, q);
            model.addBook(b);
            showAlert("Book Added!");
        } catch (Exception e) { 
            showAlert("Error: Check Genre spelling or Number format."); 
        }
    }

    private void handleRemoveStock() {
        try {
            Book b = model.findBookById(view.searchField.getText());
            if (b != null) {
                int q = Integer.parseInt(view.qtyField.getText());
                b.removeQuantity(q);
                showAlert("Stock Updated. Remaining: " + b.getQuantity());
            } else showAlert("Book not found");
        } catch (Exception e) { 
            showAlert("Invalid number"); 
        }
    }

    private Button createNavBtn(String text, Runnable action) {
        Button b = new Button(text);
        b.setOnAction(e -> action.run());
        return b;
    }
    
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}