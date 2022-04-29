package Utils;

import javafx.application.Platform;
import javafx.scene.control.Button;


/**
 * Ressort
 * @author ymouacha
 */
public class Quit extends Button {
    public Quit(){
        super("Quit");
        this.setOnAction(h->{
            Platform.exit(); 
        });
    }
}
