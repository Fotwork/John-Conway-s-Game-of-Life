package View;

import Utils.Ressort;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * LeftSide
 */
public class LeftSide extends VBox {
    
    Edition edition;
    Settings settings;
    Button play;
    Label nbTours;

    public LeftSide(){
        super(10);
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color:seagreen");

        this.edition = new Edition();
        this.settings = new Settings();

        this.play = new Button("Play");
        this.play.setPrefWidth(150);
        this.play.setPrefHeight(150);
        
        this.nbTours = new Label("Nombre de générations : 0");
        this.nbTours.setStyle("-fx-border-color: black;-fx-background-color:mediumseagreen;");


        Ressort ressort1 = new Ressort();
        Ressort ressort2 = new Ressort();

        this.getChildren().addAll(this.edition, this.settings, ressort1, this.nbTours,this.play, ressort2);
    }

    public Label getLabelTour(){
        return this.nbTours;
    }

    public Edition getEdition(){
        return this.edition;
    }

    public Settings getSettings(){
        return this.settings;
    }

    public Button getPlay(){
        return this.play;
    }
}
