package View;

import Utils.LabelComboInteger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Parameteres
 */
public class Settings extends VBox {

    LabelComboInteger mortIsolation;
    LabelComboInteger mortAsphyxie;
    LabelComboInteger vieMin;
    LabelComboInteger vieMax;

    public Settings(){
        super(2);
        this.setPadding(new Insets(3));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-border-color : black;-fx-background-color: mediumseagreen;");
        Label title = new Label("Paramètres du jeu");
        title.setStyle("-fx-font-weight: bold");

        this.mortIsolation = new LabelComboInteger("mortSolitude: ");
        this.mortIsolation.getCombobox().getItems().addAll(1,2,3,4,5);
        this.mortIsolation.getCombobox().getSelectionModel().select(0);

        this.mortAsphyxie = new LabelComboInteger("mortAsphyxie: ");
        this.mortAsphyxie.getCombobox().getItems().addAll(1,2,3,4,5);
        this.mortAsphyxie.getCombobox().getSelectionModel().select(3);
        
        this.vieMin = new LabelComboInteger("vieMin: ");
        this.vieMin.getCombobox().getItems().addAll(1,2,3,4,5);
        this.vieMin.getCombobox().getSelectionModel().select(2);

        this.vieMax = new LabelComboInteger("vieMax: ");
        this.vieMax.getCombobox().getItems().addAll(1,2,3,4,5);
        this.vieMax.getCombobox().getSelectionModel().select(2);

        this.getChildren().addAll(title,this.mortIsolation, this.mortAsphyxie, this.vieMin ,this.vieMax);
    }

    public LabelComboInteger getMortIsolation(){
        return this.mortIsolation;
    }

    public LabelComboInteger getMortAsphyxie(){
        return this.mortAsphyxie;
    }

    public LabelComboInteger getVieMin(){
        return this.vieMin;
    }

    public LabelComboInteger getVieMax(){
        return this.vieMax;
    }
}