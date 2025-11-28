import javafx.application.Application;
import javafx.stage.Stage;

public class LibraryApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        LibraryModel model = new LibraryModel();
        LibraryView view = new LibraryView(primaryStage);
        new LibraryController(model, view);
    }
    public static void main(String[] args) {
        launch(args);
    }
}