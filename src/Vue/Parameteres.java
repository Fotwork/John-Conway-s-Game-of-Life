package Vue;

import Utils.EtiquetteComboInteger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Parameteres
 */
public class Parameteres extends VBox {

    EtiquetteComboInteger mortSolitude;
    EtiquetteComboInteger mortAsphyxie;
    EtiquetteComboInteger vieMin;
    EtiquetteComboInteger vieMax;

    public Parameteres(){
        super(2);
        this.setPadding(new Insets(3));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-border-color : black;-fx-background-color: mediumseagreen;");
        Label title = new Label("Paramètres du jeu");
        title.setStyle("-fx-font-weight: bold");

        this.mortSolitude = new EtiquetteComboInteger("mortSolitude: ");
        this.mortSolitude.getCombobox().getItems().addAll(1,2,3,4,5);
        this.mortSolitude.getCombobox().getSelectionModel().select(0);

        this.mortAsphyxie = new EtiquetteComboInteger("mortAsphyxie: ");
        this.mortAsphyxie.getCombobox().getItems().addAll(1,2,3,4,5);
        this.mortAsphyxie.getCombobox().getSelectionModel().select(3);
        
        this.vieMin = new EtiquetteComboInteger("vieMin: ");
        this.vieMin.getCombobox().getItems().addAll(1,2,3,4,5);
        this.vieMin.getCombobox().getSelectionModel().select(2);

        this.vieMax = new EtiquetteComboInteger("vieMax: ");
        this.vieMax.getCombobox().getItems().addAll(1,2,3,4,5);
        this.vieMax.getCombobox().getSelectionModel().select(2);

        this.getChildren().addAll(title,this.mortSolitude, this.mortAsphyxie, this.vieMin ,this.vieMax);
    }

    public EtiquetteComboInteger getMortSolitude(){
        return this.mortSolitude;
    }

    public EtiquetteComboInteger getMortAsphyxie(){
        return this.mortAsphyxie;
    }

    public EtiquetteComboInteger getVieMin(){
        return this.vieMin;
    }

    public EtiquetteComboInteger getVieMax(){
        return this.vieMax;
    }
}