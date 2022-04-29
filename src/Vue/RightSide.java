package Vue;

import Utils.EtiquetteComboString;
import Utils.Quit;
import Utils.Ressort;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * RightSide
 */
public class RightSide extends VBox {
    Board zoneTampon;
    EtiquetteComboString jeuxPredefinis;
    Button load;

    public RightSide(){
        super(5);
        this.setPadding(new Insets(5));
        this.setAlignment(Pos.TOP_RIGHT);
        this.setStyle("-fx-background-color:gold;-fx-border-color: black;");

        HBox myHbox = new HBox();
        Ressort ressort1 = new Ressort();
        this.zoneTampon = new Board(10,5);
        myHbox.getChildren().addAll(ressort1,zoneTampon);
        this.jeuxPredefinis = new EtiquetteComboString("Jeux prédéfinis");
        this.jeuxPredefinis.getCombobox().getItems().addAll("Motif 1", "Motif 2");

        this.load = new Button("Charger");

        Ressort ressort2 = new Ressort();

        Quit quit = new Quit();

        this.getChildren().addAll(myHbox,this.jeuxPredefinis,this.load,ressort2,quit);
    }

    public Button getLoad(){
        return this.load;
    }

    public EtiquetteComboString getPredefCombo(){
        return this.jeuxPredefinis;
    }

    public Board getZoneTompon(){
        return this.zoneTampon;
    }
}