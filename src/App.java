import Controleur.Controleur;
import Vue.JeuDeLaVieVue;
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
        
        JeuDeLaVieVue root = new JeuDeLaVieVue();
        Controleur controleur = new Controleur(root);
        
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}