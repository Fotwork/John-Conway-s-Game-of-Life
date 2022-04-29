import Controller.Controller;
import View.JeuDeLaVieView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
/**
 * App
 */
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        
        JeuDeLaVieView root = new JeuDeLaVieView();
        Controller controller = new Controller(root);
        
        primaryStage.setScene(new Scene(controller.getView()));
        primaryStage.show();
    }
}